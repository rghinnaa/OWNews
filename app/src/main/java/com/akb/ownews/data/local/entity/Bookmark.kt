package com.akb.ownews.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akb.ownews.utils.Const.Entity.BOOKMARK

@Entity(tableName = BOOKMARK)
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var source: String? = null,
    var author: String? = null,
    var title: String? = null,
    var description: String? = null,
    var url: String? = null,
    var urlToImage: String? = null,
    var publishedAt: String? = null,
    var content: String? = null
)