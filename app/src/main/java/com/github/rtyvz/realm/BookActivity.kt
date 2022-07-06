package com.github.rtyvz.realm

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.github.rtyvz.realm.RealmApp.Companion.realmConfig
import com.github.rtyvz.realm.RealmApp.Companion.realmCoroutineDispatcher
import com.github.rtyvz.realm.model.BookDto
import com.github.rtyvz.realm.model.BookPresentation
import com.google.android.material.textfield.TextInputEditText
import io.realm.Realm
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.*

const val EDIT_BOOK_EXTRA = "EDIT_BOOK"

class AddBookActivity : AppCompatActivity() {

    private val coroutineContext = CoroutineScope(realmCoroutineDispatcher + Job())

    private lateinit var titleBook: TextInputEditText
    private lateinit var descriptionBook: TextInputEditText
    private lateinit var addBook: Button
    private lateinit var changeBook: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        titleBook = findViewById(R.id.titleBookTextField)
        descriptionBook = findViewById(R.id.descriptionBookTextField)
        addBook = findViewById(R.id.addBookButton)
        changeBook = findViewById(R.id.changeBookButton)

        val book = intent.getParcelableExtra<BookPresentation>(EDIT_BOOK_EXTRA)

        if (book != null) {
            titleBook.setText(book.title)
            descriptionBook.setText(book.description)
            addBook.isVisible = false
            changeBook.isVisible = true
        }

        addBook.setOnClickListener {
            addNewBook()
        }

        changeBook.setOnClickListener {
            if (book != null) {
                changeBook(
                    BookPresentation(
                        book.id,
                        titleBook.text.toString(),
                        descriptionBook.text.toString(),
                        book.authorName
                    )
                )
            }
        }

        if (book != null) {
            titleBook.setText(book.title)
            descriptionBook.setText(book.description)
            addBook.isVisible = false
            changeBook.isVisible = true
        }
    }

    private fun addNewBook() {
        coroutineContext.launch {
            Realm.getInstance(realmConfig).executeTransactionAwait {
                it.insert(
                    BookDto(
                        title = titleBook.text.toString(),
                        description = descriptionBook.text.toString()
                    )
                )
            }
            withContext(Dispatchers.Main) {
                onBackPressed()
            }
        }
    }

    private fun changeBook(book: BookPresentation) {
        coroutineContext.launch {
            Realm.getInstance(realmConfig).executeTransactionAwait { realm ->
                realm.insertOrUpdate(
                    BookDto(
                        id = book.id,
                        title = book.title,
                        description = book.description,
                    )
                )
            }
            withContext(Dispatchers.Main) {
                onBackPressed()
            }
        }
    }
}