package com.derrick.cart.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.derrick.cart.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}
