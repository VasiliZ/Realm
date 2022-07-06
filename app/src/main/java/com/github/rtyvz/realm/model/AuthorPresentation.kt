package com.github.rtyvz.realm.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthorPresentation(val id: String, val name: String) : Parcelable