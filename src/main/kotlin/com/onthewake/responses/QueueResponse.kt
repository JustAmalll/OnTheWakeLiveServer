package com.onthewake.responses

import com.onthewake.data.model.Queue

@kotlinx.serialization.Serializable
data class QueueResponse(
    val isDeleteAction: Boolean,
    val queueItem: Queue
)