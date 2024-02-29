package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getIsNightThemeLiveData().observe(this) { isNightTheme ->
            binding.themeSwitcher.isChecked = isNightTheme
        }

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setTitle(R.string.setting)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switch, checked ->
            (applicationContext as App).switchTheme(checked)
            viewModel.saveTheme(checked)
        }

        binding.share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.offer))
            startActivity(shareIntent)
        }

        binding.support.setOnClickListener {
            val message = getString(R.string.message_to_developers)
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("annaparfenova"))
            supportIntent.putExtra(Intent.EXTRA_TEXT, message)
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.letter_subject))
            startActivity(supportIntent)
        }

        binding.agreement.setOnClickListener {
            val url = getString(R.string.link_to_offer)
            val openIntent = Intent(Intent.ACTION_VIEW)
            openIntent.data = Uri.parse(url)
            startActivity(openIntent)
        }
    }
}
