package com.github.rtyvz.realm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.realm.model.AuthorPresentation

class AuthorListAdapter(
    private val onAuthorClickListener: (AuthorPresentation) -> Unit
) : ListAdapter<AuthorPresentation, AuthorListAdapter.AuthorViewHolder>(AuthorItemsDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {
        return AuthorViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.author_item, parent, false)
        ).apply {
            this.itemView.setOnClickListener {
                onAuthorClickListener(currentList[adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class AuthorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val authorNameTextView = view.findViewById<TextView>(R.id.authorNameTextView)

        fun bind(author: AuthorPresentation) {
            authorNameTextView.text = author.name
        }
    }

    class AuthorItemsDiffer : DiffUtil.ItemCallback<AuthorPresentation>() {
        override fun areItemsTheSame(
            oldItem: AuthorPresentation,
            newItem: AuthorPresentation
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AuthorPresentation,
            newItem: AuthorPresentation
        ): Boolean {
            return oldItem == newItem
        }
    }
}