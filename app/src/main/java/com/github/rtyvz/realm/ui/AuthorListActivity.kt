package com.github.rtyvz.realm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.realm.R
import com.github.rtyvz.realm.RealmApp.Companion.realmConfig
import com.github.rtyvz.realm.RealmApp.Companion.realmCoroutineDispatcher
import com.github.rtyvz.realm.adapter.AuthorListAdapter
import com.github.rtyvz.realm.model.AuthorDto
import com.github.rtyvz.realm.model.AuthorPresentation
import com.github.rtyvz.realm.model.BookDto
import io.realm.Realm
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.*

const val BOOK_ID_EXTRA = "BOOK_ID"
private const val BOOK_ID_FIELD_NAME = "id"
private const val AUTHOR_ID_FIELD_NAME = "id"
private const val EMPTY_STRING = ""

class AuthorsActivity : AppCompatActivity() {

    private val coroutineContext = CoroutineScope(realmCoroutineDispatcher + Job())
    private lateinit var authorList: RecyclerView
    private var authorAdapter: AuthorListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.author_list_activity)

        val bookId = intent.getStringExtra(BOOK_ID_EXTRA) ?: EMPTY_STRING

        authorAdapter = AuthorListAdapter {
            addAuthorBook(it.id, bookId)
        }

        authorList = findViewById(R.id.authorList)
        authorList.adapter = authorAdapter


        coroutineContext.launch(Dispatchers.Main) {
            authorAdapter?.submitList(retrieveAuthors())
        }
    }

    private suspend fun retrieveAuthors(): List<AuthorPresentation> {

        return withContext(coroutineContext.coroutineContext) {
            val authors = mutableListOf<AuthorPresentation>()
            Realm.getInstance(realmConfig).executeTransactionAwait { transaction ->
                authors.addAll(
                    transaction
                        .where(AuthorDto::class.java)
                        .findAll()
                        .map {
                            AuthorPresentation(
                                id = it.id,
                                name = it.author
                            )
                        })
            }
            authors
        }
    }

    private fun addAuthorBook(authorId: String, bookId: String) {

        coroutineContext.launch {
            Realm.getInstance(realmConfig).executeTransactionAwait {
                val book = it.where(BookDto::class.java)
                    .equalTo(BOOK_ID_FIELD_NAME, bookId)
                    .findFirst()

                val author = it.where(AuthorDto::class.java)
                    .equalTo(AUTHOR_ID_FIELD_NAME, authorId)
                    .findFirst()

                author?.books?.add(book)
            }
            withContext(Dispatchers.Main) {
                onBackPressed()
            }
        }
    }
}