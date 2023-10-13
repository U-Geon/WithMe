package com.akj.withme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< Updated upstream
=======
import android.widget.Button
import com.akj.withme.databinding.ActivityAccoutFindBinding
>>>>>>> Stashed changes

class AccountFindActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        <<<<<<< Updated upstream
        setContentView(R.layout.activity_account_find)
        =======
        var bind = ActivityAccoutFindBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.idFind.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.targetLayout, IdFindFragment())
                .commit()
        }

        bind.pwFind.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.targetLayout, PwFindFragment())
                .commit()
        }
        >>>>>>> Stashed changes
    }
}