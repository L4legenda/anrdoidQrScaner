package ru.l4legenda.qrscaner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRead = findViewById<Button>(R.id.btnReadQr)
        btnRead.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, readerQr::class.java)
            )
        }

        val btnGenerate = findViewById<Button>(R.id.btnGenerateQr)
        btnGenerate.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, generateQr::class.java)
            )
        }

    }
}






















