package com.example.aplicacionmarzo.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacionmarzo.databinding.FragmentCommentsBinding
import com.example.aplicacionmarzo.ui.adapters.CommentAdapter
import com.example.aplicacionmarzo.ui.viewmodel.CommentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentComments : Fragment() {
    private lateinit var binding: FragmentCommentsBinding
    private lateinit var commentAdapter: CommentAdapter
    private val commentViewModel: CommentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView
        binding.recyclerViewComments.layoutManager = LinearLayoutManager(requireContext())
        // Iniciamos el adapter sin datos o con una lista vacía
        commentAdapter = CommentAdapter(emptyList()) { comment ->
            commentViewModel.eliminarComentario(comment)
        }
        binding.recyclerViewComments.adapter = commentAdapter

        // Observamos los cambios en el ViewModel
        commentViewModel.comments.observe(viewLifecycleOwner) { comments ->
            // Actualiza el adapter cuando cambie la lista de comentarios
            if (comments != null) {
                // método que actualiza la lista
                commentAdapter.updateData(comments)
            }
        }

    }
}
