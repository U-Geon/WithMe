package com.example.withme

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.withme.databinding.ActivityAccountFindBinding

class AccountFindActivity : AppCompatActivity() {
    private lateinit var bind: ActivityAccountFindBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val defaultBackground: Drawable? = ContextCompat.getDrawable(this, R.drawable.stroke_bottom)
        val selectedBackground: Drawable? = ContextCompat.getDrawable(this, R.drawable.stroke_bottom_blue)

        super.onCreate(savedInstanceState)
        bind = ActivityAccountFindBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.imgbtnBack.setOnClickListener {
            finish()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.accountFind, IdFindFragment())
            .commit()

        bind.idFind.setOnClickListener {
            bind.idFind.background = selectedBackground
            bind.idFind.setTextColor(Color.BLUE)
            bind.pwFind.setTextColor(Color.BLACK)
            bind.pwFind.background = defaultBackground

            supportFragmentManager.beginTransaction()
                .replace(R.id.accountFind, IdFindFragment())
                .commit()
        }

        bind.pwFind.setOnClickListener {
            bind.idFind.background = defaultBackground
            bind.pwFind.background = selectedBackground
            bind.idFind.setTextColor(Color.BLACK)
            bind.pwFind.setTextColor(Color.BLUE)
            supportFragmentManager.beginTransaction()
                .replace(R.id.accountFind, PwFindFragment())
                .commit()
        }
    }
}