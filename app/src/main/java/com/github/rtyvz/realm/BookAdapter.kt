package com.github.rtyvz.realm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.realm.model.BookPresentation

class BookAdapter(
    private val onEditBookCallback: ((BookPresentation) -> (Unit)),
    private val onAddAuthorCallback: ((String) -> (Unit)),
    private val onLongClickCallback: (String) -> Unit
) : ListAdapter<BookPresentation, BookAdapter.BookViewHolder>(BookDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        ).apply {
            this.itemView.findViewById<Button>(R.id.editBookInfoButton).setOnClickListener {
                onEditBookCallback(currentList[adapterPosition])
            }

            this.itemView.findViewById<Button>(R.id.addAuthorButton).setOnClickListener {
                onAddAuthorCallback(currentList[adapterPosition].id)
            }
            this.itemView.setOnLongClickListener {
                onLongClickCallback(currentList[adapterPosition].id)
                true
            }
        }
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemTitle = view.findViewById<TextView>(R.id.itemTitle)
        private val itemDescription = view.findViewById<TextView>(R.id.itemDescription)
        private val itemAuthorName = view.findViewById<TextView>(R.id.itemAuthorName)

        fun bind(bookDto: BookPresentation) {
            itemTitle.text = bookDto.title
            itemDescription.text = bookDto.description
            itemAuthorName.text = bookDto.authorName
        }
    }
}

class BookDiffer : DiffUtil.ItemCallback<BookPresentation>() {
    override fun areItemsTheSame(oldItem: BookPresentation, newItem: BookPresentation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookPresentation, newItem: BookPresentation): Boolean {
        return oldItem == newItem
    }
}