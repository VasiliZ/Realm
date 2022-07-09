package com.github.rtyvz.realm.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.realm.R
import com.github.rtyvz.realm.RealmApp.Companion.realmConfig
import com.github.rtyvz.realm.RealmApp.Companion.realmCoroutineDispatcher
import com.github.rtyvz.realm.model.AuthorDto
import com.google.android.material.textfield.TextInputEditText
import io.realm.Realm
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.*


class AddAuthorActivity : AppCompatActivity() {

    private lateinit var addAuthorButton: Button
    private lateinit var authorNameTextField: TextInputEditText
    private val coroutineContext = CoroutineScope(realmCoroutineDispatcher + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_author)

        addAuthorButton = findViewById(R.id.addAuthorButton)
        authorNameTextField = findViewById(R.id.authorName)

        addAuthorButton.setOnClickListener {
            addAuthor()
        }
    }

    private fun addAuthor() {
        coroutineContext.launch {
            Realm.getInstance(realmConfig).executeTransactionAwait {
                it.insert(AuthorDto(author = authorNameTextField.text.toString()))
            }

            withContext(Dispatchers.Main) {
                onBackPressed()
            }
        }
    }
}