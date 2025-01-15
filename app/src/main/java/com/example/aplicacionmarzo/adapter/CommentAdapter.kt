package com.example.aplicacionmarzo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionmarzo.R
import com.example.aplicacionmarzo.model.Comment

class CommentAdapter(private val commentList: List<Comment>, private val onDeleteClick: (Comment) -> Unit) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtUsername: TextView = itemView.findViewById(R.id.txtUsername)
        val txtComment: TextView = itemView.findViewById(R.id.txtComment)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val btnDeleteComment: ImageButton = itemView.findViewById(R.id.btnDeleteComment)

        fun bind(comment: Comment) {
            txtUsername.text = comment.username
            txtComment.text = comment.commentText
            txtDate.text = comment.date

            btnDeleteComment.setOnClickListener {
                onDeleteClick(comment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(commentList[position])
    }

    override fun getItemCount(): Int = commentList.size
}
