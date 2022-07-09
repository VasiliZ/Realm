package com.github.rtyvz.realm.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.realm.R
import com.github.rtyvz.realm.RealmApp
import com.github.rtyvz.realm.RealmApp.Companion.realmCoroutineDispatcher
import com.github.rtyvz.realm.adapter.BookAdapter
import com.github.rtyvz.realm.model.BookDto
import com.github.rtyvz.realm.model.BookPresentation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.*

private const val BOOK_ID_FIELD_NAME = "id"

class MainActivity : AppCompatActivity() {

    private lateinit var addBookButton: FloatingActionButton
    private val coroutineContext = CoroutineScope(realmCoroutineDispatcher + Job())
    private lateinit var bookList: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private lateinit var toolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolBar = findViewById(R.id.toolBar)
        addBookButton = findViewById(R.id.addBookAction)
        bookList = findViewById(R.id.bookList)

        setSupportActionBar(toolBar)

        bookAdapter = BookAdapter({
            startActivity(Intent(this, AddBookActivity::class.java).apply {
                putExtra(EDIT_BOOK_EXTRA, it)
            })
        }, {
            startActivity(Intent(this, AuthorsActivity::class.java).apply {
                putExtra(BOOK_ID_EXTRA, it)
            })
        }) {
            deleteBook(it)

            coroutineContext.launch(Dispatchers.Main) {
                bookAdapter.submitList(retrieveBooks())
            }
        }

        bookList.adapter = bookAdapter

        addBookButton.setOnClickListener {
            startActivity(Intent(this, AddBookActivity::class.java))
        }

        toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.authorItem -> {
                    startActivity(Intent(this, AddAuthorActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()

        coroutineContext.launch(Dispatchers.Main) {
            bookAdapter.submitList(retrieveBooks())
        }
    }

    private suspend fun retrieveBooks(): List<BookPresentation> {
        val books = mutableListOf<BookPresentation>()

        return withContext(coroutineContext.coroutineContext) {
            Realm.getInstance(RealmApp.realmConfig)
                .executeTransactionAwait(Dispatchers.IO) { transaction ->
                    books.addAll(
                        transaction
                            .where(BookDto::class.java)
                            .findAll()
                            .map {
                                BookPresentation(
                                    id = it.id,
                                    title = it.title,
                                    description = it.description,
                                    authorName = it.author?.firstOrNull()?.author
                                )
                            }
                    )
                }
            books
        }
    }

    private fun deleteBook(bookId: String) {
        coroutineContext.launch {
            Realm.getInstance(RealmApp.realmConfig).executeTransactionAwait {
                it.where(BookDto::class.java)
                    .equalTo(BOOK_ID_FIELD_NAME, bookId)
                    .findFirst()
                    ?.deleteFromRealm()
            }
        }
    }
}
