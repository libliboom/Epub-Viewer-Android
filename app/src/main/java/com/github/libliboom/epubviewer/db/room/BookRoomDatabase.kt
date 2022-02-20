package com.github.libliboom.epubviewer.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class BookRoomDatabase : RoomDatabase() {

  abstract fun bookDao(): BookDao

  companion object {
    @Volatile
    private var INSTANCE: BookRoomDatabase? = null

    fun getDatabase(
      context: Context,
      scope: CoroutineScope
    ): BookRoomDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          BookRoomDatabase::class.java,
          "book_database"
        )
          .addCallback(ContentsDatabaseCallback(scope))
          .build()
        INSTANCE = instance
        instance
      }
    }
  }

  private class ContentsDatabaseCallback(
    private val scope: CoroutineScope
  ) : RoomDatabase.Callback() {
    override fun onOpen(db: SupportSQLiteDatabase) {
      super.onOpen(db)
      INSTANCE?.let {
        scope.launch(Dispatchers.IO) {
          // Do nothing yet
        }
      }
    }
  }
}
