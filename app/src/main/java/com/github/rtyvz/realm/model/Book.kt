package com.github.rtyvz.realm.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Book() : RealmObject() {
    @PrimaryKey
    var id: String = ObjectId().toHexString()
    var title: String = ""
    var description: String = ""
}