package com.lucas.app_gastos


import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var expensesRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private val expensesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa o RecyclerView e o adaptador
        expensesRecyclerView = findViewById(R.id.expenses_recyclerview)
        expenseAdapter = ExpenseAdapter(expensesList)
        expensesRecyclerView.adapter = expenseAdapter
        expensesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Configura o FloatingActionButton para mostrar o diálogo de adição de despesas
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            showAddExpenseDialog()
        }
    }

    private fun showAddExpenseDialog() {
        // Infla o layout do diálogo
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_expense, null)
        val expenseNameInput: EditText = dialogView.findViewById(R.id.expense_name_input)
        val expenseValueInput: EditText = dialogView.findViewById(R.id.expense_value_input)
        val addExpenseButton: Button = dialogView.findViewById(R.id.add_expense_button)

        // Cria o diálogo com o layout inflado
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Configura o botão de adicionar despesa para adicionar a despesa à lista e fechar o diálogo
        addExpenseButton.setOnClickListener {
            val expenseName = expenseNameInput.text.toString()
            val expenseValue = expenseValueInput.text.toString()
            if (expenseName.isNotEmpty() && expenseValue.isNotEmpty()) {
                val expense = "$expenseName - R$ $expenseValue"
                expenseAdapter.addExpense(expense)
                updateTotal() // Atualiza o total das despesas
                dialog.dismiss()
            }
        }

        // Mostra o diálogo
        dialog.show()
    }

    private fun updateTotal() {
        var total = 0.0
        for (expense in expensesList) {
            val expenseValue = expense.substringAfterLast("R$").trim().toDoubleOrNull()
            if (expenseValue != null) {
                total += expenseValue
            }
        }
        findViewById<TextView>(R.id.total_expenses_textview).text = "Total: R$ $total"
    }
}
