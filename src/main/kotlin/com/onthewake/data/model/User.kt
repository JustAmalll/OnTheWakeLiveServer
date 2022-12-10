package com.onthewake.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    @BsonId val id: String = ObjectId().toString(),
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val instagram: String = "",
    val telegram: String = "",
    val dateOfBirth: String = "",
    val profilePictureUri: String = "",
    val password: String,
    val salt: String
)