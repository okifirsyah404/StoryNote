package com.okifirsyah.storynote.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.okifirsyah.storynote.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModel()
    private var _binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding?.root)

        viewModel.getThemeSettings().observe(this) { isDarkMode ->
            Timber.tag("MainActivity").d("isDarkMode: $isDarkMode")

            AppCompatDelegate.setDefaultNightMode(isDarkMode?.let {
                if (it) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            } ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        }

        viewModel.getLocaleSettings().observe(this) { locale ->
            Timber.tag("MainActivity").d("locale: $locale")

            AppCompatDelegate.setApplicationLocales(
                if (locale.isNullOrEmpty()) {
                    LocaleListCompat.forLanguageTags(Locale.US.language)
                } else {
                    LocaleListCompat.forLanguageTags(
                        locale
                    )
                }
            )
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}