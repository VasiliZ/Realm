package com.github.rtyvz.realm

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.realm.model.Book
import com.google.android.material.textfield.TextInputEditText
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.*

class AddBookActivity : AppCompatActivity() {

    private val coroutineContext = CoroutineScope(Dispatchers.IO + Job())
    private val realmConfig = RealmConfiguration.Builder().schemaVersion(realmVersion).build()

    private lateinit var titleBook: TextInputEditText
    private lateinit var descriptionBook: TextInputEditText
    private lateinit var addBook: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        titleBook = findViewById(R.id.titleBookTextField)
        descriptionBook = findViewById(R.id.descriptionBookTextField)
        addBook = findViewById(R.id.addBookButton)

        addBook.setOnClickListener {
            addNewBook()
        }
    }

    private fun addNewBook() {
        coroutineContext.launch {
            Realm.getInstance(realmConfig).executeTransactionAwait(Dispatchers.IO) {
                it.insert(Book().apply {
                    title = titleBook.text.toString()
                    description = descriptionBook.text.toString()
                })
            }
            withContext(Dispatchers.Main) {
                onBackPressed()
            }
        }
    }
}