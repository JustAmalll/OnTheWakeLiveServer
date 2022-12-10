package com.onthewake.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@kotlinx.serialization.Serializable
data class Queue(
    @BsonId val id: String = ObjectId().toString(),
    val userId: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUri: String,
    val isLeftQueue: Boolean,
    val timestamp: Long
)