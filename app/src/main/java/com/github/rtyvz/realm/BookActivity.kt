package com.github.rtyvz.realm

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.github.rtyvz.realm.model.BookDto
import com.github.rtyvz.realm.model.BookPresentation
import com.google.android.material.textfield.TextInputEditText
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.*

const val EDIT_BOOK_EXTRA = "EDIT_BOOK_EXTRA"
private const val BOOK_TITLE_FIELD = "title"

class AddBookActivity : AppCompatActivity() {

    private val coroutineContext = CoroutineScope(Dispatchers.IO + Job())
    private val realmConfig = RealmConfiguration.Builder().schemaVersion(realmVersion).build()

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
                changeBook(book)
            }
        }
    }

    private fun addNewBook() {
        coroutineContext.launch {
            Realm.getInstance(realmConfig).executeTransactionAwait(Dispatchers.IO) {
                it.insert(BookDto().apply {
                    title = titleBook.text.toString()
                    description = descriptionBook.text.toString()
                })
            }
            withContext(Dispatchers.Main) {
                onBackPressed()
            }
        }
    }

    private fun changeBook(book: BookPresentation) {
        coroutineContext.launch {
            Realm.getInstance(realmConfig).executeTransactionAwait { realm ->
                realm.where(BookDto::class.java)
                    .equalTo(BOOK_TITLE_FIELD, book.title)
                    .findFirst()?.let {
                        realm.insertOrUpdate(it.apply {
                            title = titleBook.text.toString()
                            description = descriptionBook.text.toString()
                        })
                    }
            }
            withContext(Dispatchers.Main) {
                onBackPressed()
            }
        }
    }
}