package layout

import androidx.appcompat.app.AppCompatActivity
import com.akj.withme.R

fun AppCompatActivity.setUpActionBar(title: String, displayHomeAsUpEnabled: Boolean = false) {

    setSupportActionBar(findViewById(R.id.toolbar))

    supportActionBar?.apply {
        this.title = title
        setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled)
        setDisplayShowHomeEnabled(displayHomeAsUpEnabled)
    }
}

fun AppCompatActivity.enableBackButton() {
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
}