package example.com.sqlitedemo.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.util.Log
import android.widget.Toast
import example.com.sqlitedemo.Model.Person

import java.util.ArrayList

class DatabaseQueryClass(private val context: Context) {
    private val TAG = DatabaseQueryClass::class.java.simpleName

    val allPersonList: ArrayList<Person>
        get() {

            val databaseHelper = DatabaseHelper.getInstance(context)
            val sqLiteDatabase = databaseHelper.readableDatabase

            var cursor: Cursor? = null

            try {
                cursor = sqLiteDatabase.query(Config.TABLE_PERSON, null, null, null, null, null, null, null)

                if (cursor != null)
                    if (cursor.moveToFirst()) {
                        val personList = ArrayList<Person>()
                        do {
                            val id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PERSON_ID))
                            val name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_NAME))
                            val email = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_EMAIL))
                            val phone = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_PHONE))
                            val occupation = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_OCCUPATION))

                            personList.add(Person(id, name, phone, email, occupation))
                        } while (cursor.moveToNext())

                        return personList
                    }
            } catch (e: Exception) {
                Log.d(TAG, "Exception: " + e.message)
                Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show()
            } finally {
                cursor?.close()
                sqLiteDatabase.close()
            }

            return ArrayList()
        }

    fun insertPerson(person: Person): Long {

        var id: Long = -1
        val databaseHelper = DatabaseHelper.getInstance(context)
        val sqLiteDatabase = databaseHelper.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Config.COLUMN_PERSON_NAME, person.name)
        contentValues.put(Config.COLUMN_PERSON_PHONE, person.phoneNumber)
        contentValues.put(Config.COLUMN_PERSON_EMAIL, person.email)
        contentValues.put(Config.COLUMN_PERSON_OCCUPATION, person.occupation)

        try {
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_PERSON, null, contentValues)
        } catch (e: SQLiteException) {
            Log.d(TAG, "Exception: " + e.message)
            Toast.makeText(context, "Operation failed: " + e.message, Toast.LENGTH_LONG).show()
        } finally {
            sqLiteDatabase.close()
        }

        return id
    }

    fun getPersonById(idOfPerson: Long): Person? {

        val databaseHelper = DatabaseHelper.getInstance(context)
        val sqLiteDatabase = databaseHelper.readableDatabase

        var cursor: Cursor? = null
        var person: Person? = null
        try {

            cursor = sqLiteDatabase.query(
                Config.TABLE_PERSON, null,
                Config.COLUMN_PERSON_ID + " = ? ", arrayOf(idOfPerson.toString()), null, null, null
            )

            if (cursor!!.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PERSON_ID))
                val name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_NAME))
                val phone = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_PHONE))
                val email = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_EMAIL))
                val occupation = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERSON_OCCUPATION))

                person = Person(id, name, phone, email, occupation)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Exception: " + e.message)
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show()
        } finally {
            cursor?.close()
            sqLiteDatabase.close()
        }

        return person
    }

    fun updatePersonInfo(person: Person): Long {

        var rowCount: Long = 0
        val databaseHelper = DatabaseHelper.getInstance(context)
        val sqLiteDatabase = databaseHelper.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Config.COLUMN_PERSON_NAME, person.name)
        contentValues.put(Config.COLUMN_PERSON_PHONE, person.phoneNumber)
        contentValues.put(Config.COLUMN_PERSON_EMAIL, person.email)
        contentValues.put(Config.COLUMN_PERSON_OCCUPATION, person.occupation)

        try {
            rowCount = sqLiteDatabase.update(
                Config.TABLE_PERSON, contentValues,
                Config.COLUMN_PERSON_ID + " = ? ",
                arrayOf(person.id.toString())
            ).toLong()
        } catch (e: SQLiteException) {
            Log.d(TAG, "Exception: " + e.message)
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        } finally {
            sqLiteDatabase.close()
        }

        return rowCount
    }

    fun deletePersonById(idOfPerson: Long): Long {
        var deletedRowCount: Long = -1
        val databaseHelper = DatabaseHelper.getInstance(context)
        val sqLiteDatabase = databaseHelper.writableDatabase

        try {
            deletedRowCount = sqLiteDatabase.delete(
                Config.TABLE_PERSON,
                Config.COLUMN_PERSON_ID + " = ? ",
                arrayOf(idOfPerson.toString())
            ).toLong()
        } catch (e: SQLiteException) {
            Log.d(TAG, "Exception: " + e.message)
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        } finally {
            sqLiteDatabase.close()
        }

        return deletedRowCount
    }



}