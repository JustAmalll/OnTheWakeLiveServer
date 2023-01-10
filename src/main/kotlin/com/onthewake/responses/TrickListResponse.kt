package com.onthewake.responses

import com.onthewake.data.model.trick_list.TrickItem

@kotlinx.serialization.Serializable
data class TrickListResponse(
    val spins: List<TrickItem> = emptyList(),
    val raileyTricks: List<TrickItem> = emptyList(),
    val backRollTricks: List<TrickItem> = emptyList(),
    val frontFlipTricks: List<TrickItem> = emptyList(),
    val frontRollTricks: List<TrickItem> = emptyList(),
    val tantrumTricks: List<TrickItem> = emptyList(),
    val whipTricks: List<TrickItem> = emptyList(),
    val grabs: List<TrickItem> = emptyList(),
    val rails: List<TrickItem> = emptyList()
)
