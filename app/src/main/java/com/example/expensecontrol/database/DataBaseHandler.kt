package com.example.expensecontrol.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.expensecontrol.entity.Lancamentos

class DataBaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(banco: SQLiteDatabase?) {
        banco?.execSQL("CREATE TABLE $TABLE_NAME (id INTEGER PRIMARY KEY AUTOINCREMENT, tipo TEXT, detalhe TEXT, valor REAL, data TEXT)")
    }

    override fun onUpgrade(banco: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        banco?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(banco)
    }

    fun incluir(lancamentos: Lancamentos) {
        val banco = this.writableDatabase

        val registros = ContentValues()
        registros.put("tipo", lancamentos.tipo)
        registros.put("detalhe", lancamentos.detalhe)
        registros.put("valor", lancamentos.valor)
        registros.put("data", lancamentos.data)

        banco.insert(TABLE_NAME, null, registros)
    }

    fun calularSaldoLanc() : Double {
        val banco = this.writableDatabase
        var saldo = 0.0

        val cursor = banco.rawQuery("SELECT tipo, valor FROM $TABLE_NAME", null)

        if(cursor.moveToNext()) {
            do {
                val tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
                val valor = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"))

                if(tipo == "Crédito") {
                    saldo += valor.toDouble()
                } else {
                    saldo -= valor.toDouble()
                }
            } while (cursor.moveToNext())
        }

        return saldo
    }

    fun listar(): Cursor {
        val banco = this.writableDatabase

        return banco.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY data, valor", null)
    }

    companion object{
        public const val DATABASE_VERSION = 1
        public const val DATABASE_NAME = "dbfile.sqlite"
        public const val TABLE_NAME = "lancamentos"
        public const val ID = "0"
    }
}