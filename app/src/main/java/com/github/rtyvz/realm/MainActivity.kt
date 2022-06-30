package com.github.rtyvz.realm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.realm.model.Book
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var addBookButton: FloatingActionButton
    private val coroutineContext = CoroutineScope(Dispatchers.IO + Job())
    private val realmConfig = RealmConfiguration.Builder().schemaVersion(realmVersion).build()
    private lateinit var bookList: RecyclerView
    private lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addBookButton = findViewById(R.id.addBookAction)
        bookList = findViewById(R.id.bookList)

        bookAdapter = BookAdapter {
            startActivity(Intent(this, AddBookActivity::class.java)).apply {

            }
        }

        bookList.adapter = bookAdapter

        coroutineContext.launch {
            retrieveBooks()
        }

        addBookButton.setOnClickListener {
            startActivity(Intent(this, AddBookActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        coroutineContext.launch {
            bookAdapter.submitList(retrieveBooks())
        }
    }


    private suspend fun retrieveBooks(): List<Book> {
        val realm = Realm.getInstance(realmConfig)
        val books = mutableListOf<Book>()
        realm.executeTransactionAwait(Dispatchers.IO) { transaction ->
            books.addAll(
                realm.where(Book::class.java)
                    .findAll()
                    .map {
                        Book().apply {
                            title = it.title
                            description = it.description
                        }
                    }
            )
        }
        return books
    }

    private fun editBook(book: Book) {


        coroutineContext.launch {
            val realm = Realm.getInstance(realmConfig)

        }
    }
}
