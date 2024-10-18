package com.example.datamanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "datamanager.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(data: Data): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, data.title)
            put(COLUMN_DESCRIPTION, data.description)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllData(): ArrayList<Data> {
        val dataList = ArrayList<Data>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"

        db.rawQuery(selectQuery, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                    val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))

                    dataList.add(Data(id, title, description))
                } while (cursor.moveToNext())
            }
        }
        return dataList
    }
}