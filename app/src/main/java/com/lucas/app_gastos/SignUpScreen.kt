package com.lucas.app_gastos

import android.content.Context
import android.content.Intent
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.firebase.Firebase
import android.os.Bundle
<<<<<<< HEAD
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
=======
import android.util.AttributeSet
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
>>>>>>> 4077e75ac87974873d718b5fb21f22c2b6b92e2a
import com.lucas.app_gastos.databinding.ActivitySignUpScreenBinding

class SignUpScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpScreenBinding
<<<<<<< HEAD
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebase: Firebase

=======

    private lateinit var firebase: Firebase
>>>>>>> 4077e75ac87974873d718b5fb21f22c2b6b92e2a
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

<<<<<<< HEAD
        // Inicializa FirebaseAuth e Firestore
        auth = FirebaseAuth.getInstance()
        firebase = Firebase
        firestore = FirebaseFirestore.getInstance()

        binding.btnCreateLogin.setOnClickListener {
            val firstName = binding.editTextName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextCreatePass.text.toString()
            val confirmPassword = binding.editTextConfirmPass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword && firstName.isNotEmpty() && lastName.isNotEmpty()) {
                registerUser(email, password, firstName, lastName)
                openMainActivity()
            } else {
                Toast.makeText(this, "Please fill out all fields and make sure passwords match.", Toast.LENGTH_SHORT).show()
            }

=======
        firebase = Firebase
>>>>>>> 4077e75ac87974873d718b5fb21f22c2b6b92e2a

        }

    }

    private fun registerUser(email: String, password: String, firstName: String, lastName: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid

                    if (userId != null) {
                        val userMap = hashMapOf(
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "email" to email
                        )

                        firestore.collection("users").document(userId).set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                                openMainActivity()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                Log.e("Firestore", "Failed to save user data", e)
                            }
                    }
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    Log.e("Auth", "Registration failed", task.exception)
                }
            }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
