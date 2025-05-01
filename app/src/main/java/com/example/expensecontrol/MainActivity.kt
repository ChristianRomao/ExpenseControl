package com.example.expensecontrol

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.expensecontrol.database.DataBaseHandler
import com.example.expensecontrol.databinding.ActivityMainBinding
import com.example.expensecontrol.entity.Lancamentos
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var banco : DataBaseHandler

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)

        setButtonsListeners()

        banco = DataBaseHandler(this)

        val tipo = listOf("Crédito", "Débito")
        val detalhesCredito = listOf("Salário", "Freelance", "Reembolso")
        val detalhesDebito  = listOf("Aluguel", "Transporte", "Alimentação")

        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipo)
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sTpLancamento.adapter = adapterTipo

        binding.sTpLancamento.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long ) {
                val detalhe = if (position == 0) detalhesCredito else detalhesDebito

                val adapterDetalhe = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, detalhe)
                adapterDetalhe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                binding.sTpDetalhe.adapter = adapterDetalhe
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        binding.etData.inputType = InputType.TYPE_NULL

        binding.etData.setOnClickListener {
            showDatePicker()
        }

        binding.etData.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker()
            }
        }

    }

    private fun setButtonsListeners() {
        binding.btLancar.setOnClickListener {
            btLancarOnClick()
        }

        binding.btLancamentos.setOnClickListener {
            btLancamentosOnClick()
        }

        binding.btSaldo.setOnClickListener {
            btSaldoOnClick()
        }
    }

    private fun btSaldoOnClick() {
        val saldoLancamentos = banco.calularSaldoLanc()
        Toast.makeText(this, "Saldo atual de: R$ %.2f".format(saldoLancamentos), Toast.LENGTH_SHORT).show()
    }

    private fun btLancamentosOnClick() {
        val intent = Intent(this, ListarActivity::class.java)
        startActivity(intent)
    }

    private fun btLancarOnClick() {
        if (binding.etValor.text.toString().isNotEmpty() && binding.etData.text.toString().isNotEmpty()) {
            val lancamento = Lancamentos(
                0,
                binding.sTpLancamento.selectedItem.toString(),
                binding.sTpDetalhe.selectedItem.toString(),
                binding.etValor.text.toString().toDouble(),
                binding.etData.text.toString()
            )

            banco.incluir(lancamento)

            Toast.makeText(this, "Lançamento realizado com sucesso.", Toast.LENGTH_SHORT).show()

            binding.etValor.text.clear()
            binding.etData.text.clear()
            binding.sTpLancamento.setSelection(0)
            binding.sTpDetalhe.setSelection(0)

            binding.etValor.requestFocus()

        } else {
            Toast.makeText(this, "Insira um valor ou data para ser lançado.", Toast.LENGTH_SHORT).show()
            binding.etValor.requestFocus()
        }
    }

    private fun showDatePicker() {
        val calendario = Calendar.getInstance()
        val datePikerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendario.set(year, month, dayOfMonth)
                val dataFormatada = dateFormat.format(calendario.time)
                binding.etData.setText(dataFormatada)
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )

        datePikerDialog.show()
    }
}