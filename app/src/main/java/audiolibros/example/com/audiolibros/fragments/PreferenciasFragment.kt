package audiolibros.example.com.audiolibros.fragments

import android.os.Bundle
import android.preference.PreferenceFragment

import audiolibros.example.com.audiolibros.R

/**
 * Created by everis on 8/02/19.
 */
class PreferenciasFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }

}
