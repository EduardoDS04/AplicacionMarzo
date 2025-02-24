package com.example.aplicacionmarzo.ui.dialogs

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
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

    // Registros para manejar resultados
    private val galeriaLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { cargarImagenEscalada(it) }
    }

    private val camaraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            selectedBitmap = escalarBitmap(it, 1024, 1024)
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
                1024,
                1024
            )

            options.inJustDecodeBounds = false
            options.inPreferredConfig = Bitmap.Config.RGB_565

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogRestauranteBinding.inflate(layoutInflater)

        binding.btnGaleria.setOnClickListener { galeriaLauncher.launch("image/*") }
        binding.btnCamara.setOnClickListener { verificarPermisoCamara() }

        val builder = AlertDialog.Builder(requireContext())
            .setTitle(if (restaurante == null) "Añadir Restaurante" else "Editar Restaurante")
            .setView(binding.root)
            .setPositiveButton("Guardar") { _, _ ->
                validarYCrearRestaurante()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }

        restaurante?.let { cargarDatosExistente(it) }

        return builder.create()
    }

    private fun validarYCrearRestaurante() {
        val nombre = binding.txtNombre.text.toString()
        val comida = binding.txtComida.text.toString()
        val tiempoEntrega = binding.txtTiempoEntrega.text.toString()
        val cantidad = binding.txtCantidad.text.toString().toIntOrNull()
        val precio = binding.txtPrecio.text.toString().toDoubleOrNull()
        val imagenBase64 = bitmapABase64(selectedBitmap)

        if (validarCampos(nombre, comida, tiempoEntrega, cantidad, precio)) {
            val nuevoRestaurante = Restaurante(
                nombre = nombre,
                comida = comida,
                tiempoEntrega = tiempoEntrega,
                cantidad = cantidad!!,
                precio = precio!!,
                imagen = imagenBase64
            )
            onAction(nuevoRestaurante)
        } else {
            Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
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

    private fun bitmapABase64(bitmap: Bitmap?): String? {
        if (bitmap == null) return null
        return try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
            Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }

    private fun cargarDatosExistente(restaurante: Restaurante) {
        binding.txtNombre.setText(restaurante.nombre)
        binding.txtComida.setText(restaurante.comida)
        binding.txtTiempoEntrega.setText(restaurante.tiempoEntrega)
        binding.txtCantidad.setText(restaurante.cantidad.toString())
        binding.txtPrecio.setText(restaurante.precio.toString())

        restaurante.imagen?.let {
            try {
                val decodedBytes = Base64.decode(it, Base64.DEFAULT)
                selectedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                binding.imgPreview.setImageBitmap(selectedBitmap)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "No hay fotos guardadas", Toast.LENGTH_SHORT).show()
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