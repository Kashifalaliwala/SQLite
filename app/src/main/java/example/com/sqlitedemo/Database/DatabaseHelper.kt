package example.com.sqlitedemo.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        val CREATE_TABLE = ("CREATE TABLE " + Config.TABLE_PERSON + "("
                + Config.COLUMN_PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_PERSON_NAME + " TEXT NOT NULL, "
                + Config.COLUMN_PERSON_PHONE + " TEXT, " //nullable
                + Config.COLUMN_PERSON_EMAIL + " TEXT, " //nullable
                + Config.COLUMN_PERSON_OCCUPATION + " TEXT "
                + ")")

        db.execSQL(CREATE_TABLE)

        Log.d(TAG, "DB created!")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_PERSON)

        onCreate(db)
    }

    companion object {

        private val TAG = DatabaseHelper::class.java.simpleName
        private var databaseHelper: DatabaseHelper? = null

        private val DATABASE_VERSION = 1

        private val DATABASE_NAME = Config.DATABASE_NAME

        @Synchronized
        fun getInstance(context: Context): DatabaseHelper {
            if (databaseHelper == null) {
                databaseHelper = DatabaseHelper(context)
            }
            return databaseHelper!!
        }
    }

}
