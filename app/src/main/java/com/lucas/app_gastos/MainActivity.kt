package com.lucas.app_gastos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    data class Expense(
        val name: String = "",
        val value: Double = 0.0
    ) {
        override fun toString(): String {
            return "$name - R$ $value"
        }
    }

    private lateinit var expensesRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private val expensesList = mutableListOf<Expense>()
    private lateinit var firebaseManager: FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa o RecyclerView e o adaptador
        expensesRecyclerView = findViewById(R.id.expenses_recyclerview)
        expenseAdapter = ExpenseAdapter(expensesList)
        expensesRecyclerView.adapter = expenseAdapter
        expensesRecyclerView.layoutManager = LinearLayoutManager(this)

        firebaseManager = FirebaseManager()

        // Verifica se o usuário está autenticado
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            Log.d("MainActivity", "Usuário autenticado: ${currentUser.uid}")
            firebaseManager.getAllExpenses(currentUser.uid) { expenses ->
                expensesList.clear()
                expensesList.addAll(expenses)
                this.expenseAdapter.notifyDataSetChanged()
                updateTotal()
            }
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            Log.e("MainActivity", "Usuário não autenticado")
            // Redirecionar para a tela de login se necessário
        }

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
                val expense = Expense(expenseName, expenseValue.toDouble())
                expenseAdapter.addExpense(expense)
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    firebaseManager.saveExpense(currentUser.uid, expense) // Salva a despesa no Firebase
                    Log.d("MainActivity", "Despesa salva: $expense")
                } else {
                    Log.e("MainActivity", "Usuário não autenticado ao tentar salvar despesa")
                }
                updateTotal()
                dialog.dismiss()
            }
        }

        // Mostra o diálogo
        dialog.show()
    }

    private fun updateTotal() {
        var total = 0.0
        for (expense in expensesList) {
            total += expense.value
        }
        findViewById<TextView>(R.id.total_expenses_textview).text = "Total: R$ $total"
    }

    class FirebaseManager {
        companion object {
            private const val TAG = "FirebaseManager"
        }

        private val database = FirebaseDatabase.getInstance()
        private val expensesRef = database.getReference("expenses")

        fun saveExpense(userId: String, expense: Expense) {
            val userExpensesRef = expensesRef.child(userId).push()
            userExpensesRef.setValue(expense)
                .addOnSuccessListener {
                    Log.d(TAG, "Despesa salva com sucesso: $expense")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Erro ao salvar despesa: ${e.message}")
                }
        }

        fun getAllExpenses(userId: String, callback: (List<Expense>) -> Unit) {
            val userExpensesRef = expensesRef.child(userId)
            userExpensesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val expenses = mutableListOf<Expense>()
                    for (expenseSnapshot in dataSnapshot.children) {
                        val expense = expenseSnapshot.getValue(Expense::class.java)
                        expense?.let { expenses.add(it) }
                    }
                    callback(expenses)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, "Erro ao ler os dados do banco de dados", databaseError.toException())
                }
            })
        }
    }
}
