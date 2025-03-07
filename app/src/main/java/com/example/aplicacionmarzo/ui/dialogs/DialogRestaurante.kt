package com.example.aplicacionmarzo.ui.dialogs

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.aplicacionmarzo.databinding.DialogRestauranteBinding
import com.example.aplicacionmarzo.domain.models.Restaurante
import java.io.ByteArrayOutputStream

class DialogRestaurante(
    private val restaurante: Restaurante? = null,
    private val onAction: (Restaurante) -> Unit
) : DialogFragment() {

    private var _binding: DialogRestauranteBinding? = null
    private val binding get() = _binding!!
    private var selectedBitmap: Bitmap? = null
    private val MAX_IMAGE_DIMENSION = 100
    // Tamaño máximo para la imagen codificada en Base64 (caracteres)
    private val MAX_BASE64_LENGTH = 250

    private var procesando = false

    // Registros para manejar resultados
    private val galeriaLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { cargarImagenEscalada(it) }
    }

    private val camaraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            selectedBitmap = escalarBitmap(it, MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION)
            binding.imgPreview.setImageBitmap(selectedBitmap)
        }
    }

    private val permisosCamaraLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) camaraLauncher.launch(null)
        else Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show()
    }

    private fun cargarImagenEscalada(uri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            options.inSampleSize = calcularFactorEscalado(
                options.outWidth,
                options.outHeight,
                MAX_IMAGE_DIMENSION,
                MAX_IMAGE_DIMENSION
            )

            options.inJustDecodeBounds = false
            options.inPreferredConfig = Bitmap.Config.RGB_565 // Menor consumo de memoria

            val newInputStream = requireContext().contentResolver.openInputStream(uri)
            selectedBitmap = BitmapFactory.decodeStream(newInputStream, null, options)
            newInputStream?.close()

            binding.imgPreview.setImageBitmap(selectedBitmap)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error al cargar imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calcularFactorEscalado(
        anchoReal: Int,
        altoReal: Int,
        anchoDeseado: Int,
        altoDeseado: Int
    ): Int {
        var inSampleSize = 1

        if (altoReal > altoDeseado || anchoReal > anchoDeseado) {
            val mitadAlto = altoReal / 2
            val mitadAncho = anchoReal / 2

            while (mitadAlto / inSampleSize >= altoDeseado &&
                mitadAncho / inSampleSize >= anchoDeseado) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun escalarBitmap(bitmap: Bitmap, maxAncho: Int, maxAlto: Int): Bitmap {
        val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val nuevoAncho = if (bitmap.width > maxAncho) maxAncho else bitmap.width
        val nuevoAlto = (nuevoAncho / ratio).toInt()

        return Bitmap.createScaledBitmap(
            bitmap,
            nuevoAncho,
            nuevoAlto,
            true
        )
    }

    private fun bitmapABase64(bitmap: Bitmap?): String? {
        if (bitmap == null) return null

        return try {
            // Empezar con un bitmap muy pequeño
            val bitmapPequeno = escalarBitmap(bitmap, MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION)

            // Comprimir con calidad muy baja
            val byteArrayOutputStream = ByteArrayOutputStream()
            var calidad = 10 // Calidad inicial muy baja

            do {
                byteArrayOutputStream.reset()
                bitmapPequeno.compress(Bitmap.CompressFormat.JPEG, calidad, byteArrayOutputStream)

                // Codificar en Base64
                val resultado = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)

                Log.d("DialogRestaurante", "Tamaño Base64: ${resultado.length} caracteres, calidad: $calidad")

                // Si es demasiado grande, reducir la calidad más
                if (resultado.length > MAX_BASE64_LENGTH && calidad > 1) {
                    calidad -= 1
                } else {
                    // Si llegamos a calidad 1 o es suficientemente pequeño, lo usamos
                    if (resultado.length > MAX_BASE64_LENGTH) {
                        Toast.makeText(
                            requireContext(),
                            "La imagen es demasiado grande, se usará una URL predeterminada",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Si aun así es demasiado grande, usar una URL predeterminada
                        return "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/120px-No_image_available.svg.png"
                    }
                    return resultado
                }
            } while (calidad > 1)

            // Si llegamos aquí, algo salió mal
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/120px-No_image_available.svg.png"
        } catch (e: Exception) {
            Log.e("DialogRestaurante", "Error al comprimir imagen", e)
            Toast.makeText(requireContext(), "Error al procesar la imagen", Toast.LENGTH_SHORT).show()
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/120px-No_image_available.svg.png"
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogRestauranteBinding.inflate(layoutInflater)

        binding.btnGaleria.setOnClickListener { galeriaLauncher.launch("image/*") }
        binding.btnCamara.setOnClickListener { verificarPermisoCamara() }

        val builder = AlertDialog.Builder(requireContext())
            .setTitle(if (restaurante == null) "Añadir Restaurante" else "Editar Restaurante")
            .setView(binding.root)
            .setPositiveButton("Guardar", null) // Importante: inicialmente null
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()

        // Configuramos el listener después de crear el diálogo para evitar el cierre automático
        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                if (!procesando) {
                    procesando = true
                    if (validarYCrearRestaurante()) {
                        dialog.dismiss()
                    }
                    procesando = false
                }
            }
        }

        restaurante?.let { cargarDatosExistente(it) }

        return dialog
    }

    private fun validarYCrearRestaurante(): Boolean {
        val nombre = binding.txtNombre.text.toString()
        val comida = binding.txtComida.text.toString()
        val tiempoEntrega = binding.txtTiempoEntrega.text.toString()
        val cantidad = binding.txtCantidad.text.toString().toIntOrNull()
        val precio = binding.txtPrecio.text.toString().toDoubleOrNull()

        // Procesar la imagen si existe
        val imagenBase64 = if (selectedBitmap != null) {
            bitmapABase64(selectedBitmap)
        } else if (restaurante?.imagen?.startsWith("http") == true) {
            // Si es una URL externa y no se cambió la imagen, mantener la URL
            restaurante.imagen
        } else {
            // Imagen por defecto si no hay seleccionada
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/120px-No_image_available.svg.png"
        }

        if (validarCampos(nombre, comida, tiempoEntrega, cantidad, precio)) {
            val nuevoRestaurante = Restaurante(
                id = restaurante?.id,
                nombre = nombre,
                comida = comida,
                tiempoEntrega = tiempoEntrega,
                cantidad = cantidad!!,
                precio = precio!!,
                imagen = imagenBase64
            )

            Log.d("DialogRestaurante", "Creando restaurante: $nuevoRestaurante")
            onAction(nuevoRestaurante)
            return true
        } else {
            Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun validarCampos(
        nombre: String,
        comida: String,
        tiempoEntrega: String,
        cantidad: Int?,
        precio: Double?
    ): Boolean {
        return nombre.isNotBlank() &&
                comida.isNotBlank() &&
                tiempoEntrega.isNotBlank() &&
                cantidad != null &&
                precio != null
    }

    private fun cargarDatosExistente(restaurante: Restaurante) {
        binding.txtNombre.setText(restaurante.nombre)
        binding.txtComida.setText(restaurante.comida)
        binding.txtTiempoEntrega.setText(restaurante.tiempoEntrega)
        binding.txtCantidad.setText(restaurante.cantidad.toString())
        binding.txtPrecio.setText(restaurante.precio.toString())

        restaurante.imagen?.let {
            if (it.startsWith("http")) {
                // Es una URL, mostrar mensaje informativo
                Toast.makeText(requireContext(), "Imagen cargada desde URL", Toast.LENGTH_SHORT).show()
                // La imagen se cargará mediante Glide en el ViewHolder
            } else {
                try {
                    val decodedBytes = Base64.decode(it, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    selectedBitmap = bitmap
                    binding.imgPreview.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    Log.e("DialogRestaurante", "Error al decodificar imagen", e)
                    Toast.makeText(requireContext(), "No se pudo cargar la imagen guardada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun verificarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            camaraLauncher.launch(null)
        } else {
            permisosCamaraLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        selectedBitmap?.recycle()
        _binding = null
    }
}