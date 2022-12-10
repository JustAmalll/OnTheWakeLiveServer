package com.onthewake.requests

import com.onthewake.data.model.trick_list.TrickItem

@kotlinx.serialization.Serializable
data class TrickItemRequest(
    val name: String,
    val description: String
)

fun TrickItemRequest.toTrickItem(): TrickItem = TrickItem(
    name = name, description = description
)