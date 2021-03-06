package com.github.rtyvz.realm.model

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class BookDto(
    @PrimaryKey
    var id: String = ObjectId().toHexString(),
    @Required
    var title: String = "",
    @Required
    var description: String = "",
    @LinkingObjects("books")
    val author: RealmResults<AuthorDto>? = null
) : RealmObject()