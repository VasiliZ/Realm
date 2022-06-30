package com.github.rtyvz.realm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.realm.model.BookDto
import com.github.rtyvz.realm.model.BookPresentation

class BookAdapter(
    private val onClickListener: ((BookPresentation) -> (Unit))
) : ListAdapter<BookPresentation, BookAdapter.BookViewHolder>(BookDiffer()) {

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

        fun bind(bookDto: BookPresentation) {
            itemTitle.text = bookDto.title
            itemDescription.text = bookDto.description
        }
    }
}

class BookDiffer : DiffUtil.ItemCallback<BookPresentation>() {
    override fun areItemsTheSame(oldItem: BookPresentation, newItem: BookPresentation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookPresentation, newItem: BookPresentation): Boolean {
        return oldItem.equals(newItem)
    }
}