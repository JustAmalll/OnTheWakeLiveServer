package com.onthewake.data.trick_list

import com.onthewake.data.model.trick_list.TrickList

interface TrickListSource {
    suspend fun getUsersTrickList(userId: String): TrickList?
    suspend fun addTrickList(trickList: TrickList): Boolean
    suspend fun updateTrickList(userId: String, trickListItemId: String, trickList: TrickList): Boolean
}