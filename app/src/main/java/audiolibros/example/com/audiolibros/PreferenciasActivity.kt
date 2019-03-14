package audiolibros.example.com.audiolibros

import android.app.Activity
import android.os.Bundle

import audiolibros.example.com.audiolibros.fragments.PreferenciasFragment

/**
 * Created by everis on 8/02/19.
 */
class PreferenciasActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, PreferenciasFragment()).commit()
    }

}