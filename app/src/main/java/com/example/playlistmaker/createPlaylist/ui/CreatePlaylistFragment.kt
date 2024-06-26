package com.example.playlistmaker.createPlaylist.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CreatePlaylistFragment : Fragment() {

    private val viewModel: CreatePlaylistViewModel by viewModel()
    private val binding get() = _binding!!

    private var _binding: FragmentNewPlaylistBinding? = null
    private var isImageLoaded = false

    private var onBackPressedCallback: OnBackPressedCallback? = null

    private var mode: Mode = Mode.CREATE

    private val nameEditText get() = binding.nameEditText
    private val descriptionEditText get() = binding.descriptionEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val descriptionText = binding.descriptionText
        val nameText = binding.nameText
        binding.createButton.isEnabled = false

        val playlistId = requireArguments().getLong(KEY_PLAYLIST)
        mode = if (playlistId != 0L)  {
             Mode.EDIT
        } else {
             Mode.CREATE
        }

        if (mode == Mode.EDIT) {
            binding.toolbar.setTitle(R.string.editTitle)
            binding.createButton.setText(R.string.save)

            binding.toolbar.setOnClickListener {
                findNavController().popBackStack()
            }
        } else {
            binding.toolbar.setTitle(R.string.newPlaylist)
            binding.createButton.setText(R.string.create)

            initBackPressHandler()
            binding.toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        binding.createButton.setOnClickListener {
            if (mode == Mode.EDIT) {
                viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) { playlist ->
                    viewModel.updatePlaylist(playlist)
                    findNavController().popBackStack()
                }
            } else {
                viewModel.createPlaylist()
                Toast.makeText(
                    requireContext(), "Плейлист '${nameEditText.text}' создан ",
                    Toast.LENGTH_SHORT
                ).show()
                onBackPressedCallback?.isEnabled = false
                findNavController().popBackStack()

            }
        }

        if (mode == Mode.EDIT) {
            viewModel.loadPlaylist(playlistId)
            viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) { playlist ->
                showPlaylist(playlist)
            }
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
        nameEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                nameEditText.hint = ""
            } else {
                nameEditText.hint = requireActivity().resources.getString(R.string.name_hint)
            }
            updateEditTextUI(nameEditText, nameText, v)
        }

        descriptionEditText.addTextChangedListener(
            afterTextChanged = { text ->
                viewModel.updateDescription(text.toString())
                updateBackPressCallback()
            })

        descriptionEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                descriptionEditText.hint = ""
            } else {
                descriptionEditText.hint =
                    requireActivity().resources.getString(R.string.description_hint)
            }
            updateEditTextUI(descriptionEditText, descriptionText, v)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

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
        onBackPressedCallback?.isEnabled = hasTextOrImage
    }

    private fun initBackPressHandler() {
        onBackPressedCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                hideKeyboard()
                showDialog()
            }
        }.also {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, it)
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogTheme)
            .setTitle(R.string.dialog_title)
            .setMessage(R.string.dialog_message)
            .setPositiveButton(R.string.dialog_positive_button) { dialog, which ->
                findNavController().popBackStack()
            }
            .setNeutralButton(R.string.dialog_neutral_button) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }


    private fun showPlaylist(playlist: Playlist) {
        Glide.with(requireActivity())
            .load(playlist.coverArtPath)
            .centerCrop()
            .placeholder(R.drawable.track_placeholder_45)
            .into(binding.playlistCover)
        binding.nameEditText.setText(playlist.playlistName)
        binding.descriptionEditText.setText(playlist.description)
        binding.coverPlaceholder.visibility = View.GONE

    }

    companion object {
        private const val KEY_PLAYLIST = "id_playlist"
        fun createArgs(playlistId: Long? = null): Bundle {
            return bundleOf(
                KEY_PLAYLIST to (playlistId ?: 0L),
            )
        }
    }
}