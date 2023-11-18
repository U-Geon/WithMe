package com.akj.withme

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.akj.withme.databinding.ActivityAccountFindBinding

class AccountFindActivity : AppCompatActivity() {
    private lateinit var bind: ActivityAccountFindBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val defaultBackground: Drawable? = ContextCompat.getDrawable(this, R.drawable.stroke_bottom)
        val selectedBackground: Drawable? = ContextCompat.getDrawable(this, R.drawable.stroke_bottom_blue)

        super.onCreate(savedInstanceState)
        bind = ActivityAccountFindBinding.inflate(layoutInflater)
        setContentView(bind.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.accountFind, IdFindFragment())
            .commit()

        bind.idFind.setOnClickListener {
            bind.idFind.background = selectedBackground
            bind.pwFind.background = defaultBackground

            supportFragmentManager.beginTransaction()
                .replace(R.id.accountFind, IdFindFragment())
                .commit()
        }

        bind.pwFind.setOnClickListener {
            bind.idFind.background = defaultBackground
            bind.pwFind.background = selectedBackground

            supportFragmentManager.beginTransaction()
                .replace(R.id.accountFind, PwFindFragment())
                .commit()
        }
    }
}