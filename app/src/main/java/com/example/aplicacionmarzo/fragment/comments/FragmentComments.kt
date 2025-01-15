package com.example.aplicacionmarzo.fragment.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacionmarzo.adapter.CommentAdapter
import com.example.aplicacionmarzo.databinding.FragmentCommentsBinding
import com.example.aplicacionmarzo.model.Comment

class FragmentComments : Fragment() {

    private lateinit var binding: FragmentCommentsBinding
    private lateinit var commentAdapter: CommentAdapter
    private val commentsList = mutableListOf<Comment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        binding.recyclerViewComments.layoutManager = LinearLayoutManager(requireContext())
        commentAdapter = CommentAdapter(commentsList) { comment ->
            removeComment(comment)
        }
        binding.recyclerViewComments.adapter = commentAdapter

        // Agregamos comentarios
        commentsList.add(Comment("Maria", "Este es un comentario de prueba", "12/01/2025"))
        commentsList.add(Comment("Andrea", "Me encanta esta aplicación, es perfecta.", "13/01/2025"))
        commentsList.add(Comment("Victoria", "Magnifica app.", "14/01/2025"))

        commentAdapter.notifyDataSetChanged()
    }

    private fun removeComment(comment: Comment) {
        commentsList.remove(comment)
        commentAdapter.notifyDataSetChanged()
    }
}
