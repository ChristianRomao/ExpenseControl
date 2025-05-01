package com.example.expensecontrol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.expensecontrol.adapter.MeuAdapter
import com.example.expensecontrol.database.DataBaseHandler
import com.example.expensecontrol.databinding.ActivityListarBinding

class ListarActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListarBinding
    private lateinit var banco : DataBaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DataBaseHandler(this)

        val cursor = banco.listar()
        val adapter = MeuAdapter(this, cursor)

        binding.lvLancamentos.adapter = adapter

    }
}