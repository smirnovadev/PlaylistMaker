package com.example.playlistmaker.playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CreatePlaylistFragment : Fragment() {

    private val viewModel: CreatePlaylistViewModel by viewModel()
    private val binding get() = _binding!!

    private var _binding: FragmentNewPlaylistBinding? = null
    private var isImageLoaded = false

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private val nameEditText get() = binding.nameEditText
    private val descriptionEditText get() = binding.descriptionEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBackPressHandler()

        val descriptionText = binding.descriptionText
        val nameText = binding.nameText
        binding.createButton.isEnabled = false
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.createButton.setOnClickListener {
            viewModel.savePlaylist()
            Toast.makeText(
                requireContext(), "Плейлист '${nameEditText.text}' создан ",
                Toast.LENGTH_SHORT
            ).show()
            onBackPressedCallback.isEnabled = false
            findNavController().popBackStackOr(parentFragmentManager)
        }


        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.coverPlaceholder.visibility = View.GONE
                    binding.playlistCover.setImageURI(uri)
                    isImageLoaded = true
                    updateBackPressCallback()
                    viewModel.saveCoverArt(uri)
                } else {
                    Timber.tag("PhotoPicker").d("No media selected")
                }
            }

        binding.playlistCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        nameEditText.addTextChangedListener(
            afterTextChanged = { text ->
                viewModel.updateName(text.toString())
                binding.createButton.isEnabled = text?.isNotEmpty() == true
                updateBackPressCallback()
            }
        )
        nameEditText.setOnFocusChangeListener { view, hasFocus ->
            updateEditTextUI(nameEditText, nameText, view)
        }

        descriptionEditText.addTextChangedListener(
            afterTextChanged = { text ->
                viewModel.updateDescription(text.toString())
                updateBackPressCallback()
            })

        descriptionEditText.setOnFocusChangeListener { view, hasFocus ->
            updateEditTextUI(descriptionEditText, descriptionText, view)
        }
    }

    private fun updateEditTextUI(editText: EditText, associatedText: TextView, view: View) {
        val defaultBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.unfocused_edit_tex_playllist)
        val backgroundWithText =
            ContextCompat.getDrawable(requireContext(), R.drawable.focused_edit_text_playlist)
        val textIsNotEmpty = editText.text.toString().isNotEmpty()

        if (editText.hasFocus() || textIsNotEmpty) {
            associatedText.visibility = View.VISIBLE
            view.background = backgroundWithText
        } else {
            associatedText.visibility = View.GONE
            view.background = defaultBackground
        }
    }


    private fun updateBackPressCallback() {
        val hasTextOrImage = !nameEditText.text.isNullOrEmpty() ||
                !descriptionEditText.text.isNullOrEmpty() ||
                isImageLoaded
        onBackPressedCallback.isEnabled = hasTextOrImage
    }

    private fun initBackPressHandler() {
        onBackPressedCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                showDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_title)
            .setMessage(R.string.dialog_message)
            .setPositiveButton(R.string.dialog_positive_button) { dialog, which ->
                findNavController().popBackStackOr(parentFragmentManager)
            }
            .setNeutralButton(R.string.dialog_neutral_button) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    fun NavController.popBackStackOr(fragmentManager: FragmentManager) {
        if (currentDestination?.id != null) {
            popBackStack()
        } else {
            fragmentManager.popBackStack()
        }
    }

}