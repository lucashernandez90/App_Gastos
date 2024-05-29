package com.lucas.app_gastos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.lucas.app_gastos.databinding.ActivitySignUpScreenBinding


class SignUpScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpScreenBinding

    private lateinit var firebase: Firebase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebase = Firebase

        binding.btnCreateLogin.setOnClickListener{
            openMainActivity()
        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
