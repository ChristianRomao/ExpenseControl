package com.example.expensecontrol.adapter

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.expensecontrol.R
import com.example.expensecontrol.entity.Lancamentos

class MeuAdapter(var context: Context, var cursor: Cursor): BaseAdapter() {
    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any {
        cursor.moveToPosition(position)

        val lancamentos = Lancamentos(
            cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
            cursor.getString(cursor.getColumnIndexOrThrow("tipo")),
            cursor.getString(cursor.getColumnIndexOrThrow("detalhe")),
            cursor.getDouble(cursor.getColumnIndexOrThrow("valor")),
            cursor.getString(cursor.getColumnIndexOrThrow("data"))

        )
        return lancamentos
    }

    override fun getItemId(position: Int): Long {
        cursor.moveToPosition(position)
        return cursor.getLong(cursor.getColumnIndexOrThrow("_id"))
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val elementoLista = inflater.inflate(R.layout.elemento_lista, null)

        val tvTipo = elementoLista.findViewById<TextView>(R.id.tvTipo)
        val tvData = elementoLista.findViewById<TextView>(R.id.tvData)
        val tvDetalheLanc = elementoLista.findViewById<TextView>(R.id.tvDetalheLanc)
        val tvValor = elementoLista.findViewById<TextView>(R.id.tvValorLanc)


        cursor.moveToPosition(position)

        tvTipo.text = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
        tvData.text = cursor.getString(cursor.getColumnIndexOrThrow("data"))
        tvDetalheLanc.text = cursor.getString(cursor.getColumnIndexOrThrow("detalhe"))
        val valor = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"))
        tvValor.text = "R$ %.2f".format(valor)

        if(tvTipo.text == "Crédito"){
            tvTipo.text = "C"
            tvTipo.setTextColor(Color.parseColor("#00FF00"))
            tvValor.setTextColor(Color.parseColor("#00FF00"))
        }else{
            tvTipo.text = "D"
            tvTipo.setTextColor(Color.parseColor("#FF0000"))
            tvValor.setTextColor(Color.parseColor("#FF0000"))
        }

        return elementoLista
    }
}