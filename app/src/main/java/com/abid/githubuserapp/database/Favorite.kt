package com.abid.githubuserapp.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Favorite (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "avatarId")
    var avatarUrl: String? = null
):Parcelable