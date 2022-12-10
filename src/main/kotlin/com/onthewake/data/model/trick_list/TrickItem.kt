package com.onthewake.data.model.trick_list

@kotlinx.serialization.Serializable
data class TrickItem(
    val name: String,
    val description: String
)