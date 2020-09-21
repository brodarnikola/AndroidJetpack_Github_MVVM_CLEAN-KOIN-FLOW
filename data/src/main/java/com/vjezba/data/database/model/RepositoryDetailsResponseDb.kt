package com.vjezba.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "github_repositories")
data class RepositoryDetailsResponseDb(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long = 0,
    val avatarUrl: String = "",
    val name: String? = "",
    val description: String? = "",
    val html_url: String? = ""
)