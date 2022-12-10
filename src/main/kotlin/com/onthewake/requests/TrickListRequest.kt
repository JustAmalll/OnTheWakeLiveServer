package com.onthewake.requests

@kotlinx.serialization.Serializable
data class TrickListRequest(
    val spins: List<TrickItemRequest> = emptyList(),
    val raileyTricks: List<TrickItemRequest> = emptyList(),
    val backRollTricks: List<TrickItemRequest> = emptyList(),
    val frontFlipTricks: List<TrickItemRequest> = emptyList(),
    val frontRollTricks: List<TrickItemRequest> = emptyList(),
    val tantrumTricks: List<TrickItemRequest> = emptyList(),
    val whipTricks: List<TrickItemRequest> = emptyList(),
    val grabs: List<TrickItemRequest> = emptyList(),
    val rails: List<TrickItemRequest> = emptyList()
)
