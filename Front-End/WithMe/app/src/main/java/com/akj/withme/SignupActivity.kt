package com.akj.withme
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.EditText

class SignupActivity : AppCompatActivity() {
    private val SEARCH_ADDRESS_ACTIVITY = 10000
    private lateinit var etAddress: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        etAddress = findViewById(R.id.edit_address)

//        val btnSearch = findViewById<Button>(R.id.button)

        etAddress?.setOnClickListener {
            val i = Intent(this@SignupActivity, WebviewActivity::class.java)
            startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            SEARCH_ADDRESS_ACTIVITY -> {
                if (resultCode == RESULT_OK) {
                    val data = intent?.extras?.getString("data")
                    data?.let {
                        etAddress.setText(it)
                    }
                }
            }
        }
    }
}