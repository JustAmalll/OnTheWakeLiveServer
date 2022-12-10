package com.onthewake.data.trick_list

import com.onthewake.data.model.trick_list.TrickList
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class TrickListSourceImpl(
    db: CoroutineDatabase
) : TrickListSource {

    private val trickListDb = db.getCollection<TrickList>()
    override suspend fun getUsersTrickList(userId: String): TrickList? {
        return trickListDb.findOne(TrickList::userId eq userId)
    }

    override suspend fun addTrickList(trickList: TrickList): Boolean {
        return trickListDb.insertOne(trickList).wasAcknowledged()
    }

    override suspend fun updateTrickList(
        userId: String, trickListItemId: String, trickList: TrickList
    ): Boolean = trickListDb.updateOneById(
        id = trickListItemId,
        update = TrickList(
            id = trickListItemId,
            userId = userId,
            spins = trickList.spins,
            raileyTricks = trickList.raileyTricks,
            backRollTricks = trickList.backRollTricks,
            frontFlipTricks = trickList.frontFlipTricks,
            frontRollTricks = trickList.frontRollTricks,
            tantrumTricks = trickList.tantrumTricks,
            whipTricks = trickList.whipTricks,
            grabs = trickList.grabs,
            rails = trickList.rails
        )
    ).wasAcknowledged()
}