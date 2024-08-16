package com.example.suspenso_jose_astudillo


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionSQL(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "cuenta.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "calculos"
        private const val COLUMN_ID = "id"
        private const val COLUMN_LADO_1 = "total_cuenta"
        private const val COLUMN_LADO_2 = "porcentaje"
        private const val COLUMN_LADO_3 = "personas"
        private const val COLUMN_LADO_4 = "total"
        private const val COLUMN_LADO_5 = "media"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_LADO_1 REAL," +
                "$COLUMN_LADO_2 REAL," +
                "$COLUMN_LADO_3 REAL,"+
                "$COLUMN_LADO_4 REAL,"+
                "$COLUMN_LADO_5 REAL)"


        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertLados(total_cuenta: Float, porcentaje: Float, personas: Float): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_LADO_1, total_cuenta)
            put(COLUMN_LADO_2, porcentaje)
            put(COLUMN_LADO_3, personas)

        }
        val total = calcular_total(total_cuenta, porcentaje)
        contentValues.put(COLUMN_LADO_4, total)

        val media = calcular_media(total_cuenta, personas)
        contentValues.put(COLUMN_LADO_4, media)

        val result = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return result
    }

    private fun calcular_total(total_cuenta: Float, porcentaje: Float,): Float {
        return (total_cuenta*(porcentaje/100))+total_cuenta
    }

    private fun calcular_media(total_cuenta: Float, personas: Float,): Float {
        return total_cuenta/personas
    }



    fun getAllData(): ArrayList<String> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY total_cuenta DESC ", null)
        val dataList = ArrayList<String>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val total_cuenta = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_LADO_1))
                val porcentaje = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_LADO_2))
                val personas = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_LADO_3))
                val total = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_LADO_4))
                val media = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_LADO_4))
                val formattedIMC = String.format("%.2f", media)
                val formattedIM = String.format("%.2f", total)

                val data = "ID: $id\nCuenta total: $total_cuenta$\nPropina a pagar: $porcentaje$ \nPersonas: $personas\nCalculo de la media por persona: $formattedIM$\n" +
                        "Media por persona: $formattedIMC$"
                dataList.add(data)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return dataList
    }
}
