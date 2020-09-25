package com.vjezba.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "github_repositories",
    indices = [Index(value = ["name"], unique = false)])
data class RepositoryDetailsResponseDb(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long = 0,
    val avatarUrl: String = "",
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val name: String? = "",
    val description: String? = "",
    val html_url: String? = ""
) {
    var indexInResponse: Int = -1
}