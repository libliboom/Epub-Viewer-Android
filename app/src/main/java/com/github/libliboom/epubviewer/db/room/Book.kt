package com.github.libliboom.epubviewer.db.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_table")
data class Book(
  @PrimaryKey
  @ColumnInfo(name = "config") val config: String,
  @ColumnInfo(name = "page") val page: String,
  @ColumnInfo(name = "chapters") val chapters: String
)
