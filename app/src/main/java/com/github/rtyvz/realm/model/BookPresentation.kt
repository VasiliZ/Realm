package com.github.rtyvz.realm.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookPresentation(
    val id: String,
    val title: String,
    val description: String,
    val authorName: String?
) : Parcelable