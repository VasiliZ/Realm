package com.github.rtyvz.realm.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class AuthorDto(
    @PrimaryKey
    var id: String = ObjectId().toHexString(),
    @Required
    var author: String = "",
    var books: RealmList<BookDto> = RealmList()
) : RealmObject()