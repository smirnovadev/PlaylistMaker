package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private  var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getIsNightThemeLiveData().observe(viewLifecycleOwner) { isNightTheme ->
            binding.themeSwitcher.isChecked = isNightTheme
        }


        binding.themeSwitcher.setOnCheckedChangeListener { switch, checked ->
            (requireActivity().application as App).switchTheme(checked)
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
            supportIntent.data = Uri.parse(getString(R.string.address_of_the_recipient))
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
