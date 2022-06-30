package com.github.rtyvz.realm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.realm.model.Book

class BookAdapter(
    private val onClickListener: ((Book) -> (Unit))
) : ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        ).apply {
            this.itemView.setOnClickListener {
                onClickListener(getItem(adapterPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemTitle = view.findViewById<TextView>(R.id.itemTitle)
        private val itemDescription = view.findViewById<TextView>(R.id.itemDescription)

        fun bind(book: Book) {
            itemTitle.text = book.title
            itemDescription.text = book.description
        }
    }
}

class BookDiffer : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.equals(newItem)
    }
}