package com.onthewake.data.model.trick_list

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@kotlinx.serialization.Serializable
data class TrickList(
    @BsonId val id: String = ObjectId().toString(),
    val userId: String = "",
    val spins: List<TrickItem>,
    val raileyTricks: List<TrickItem>,
    val backRollTricks: List<TrickItem>,
    val frontFlipTricks: List<TrickItem>,
    val frontRollTricks: List<TrickItem>,
    val tantrumTricks: List<TrickItem>,
    val whipTricks: List<TrickItem>,
    val grabs: List<TrickItem>,
    val rails: List<TrickItem>
)
