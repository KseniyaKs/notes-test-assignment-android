package com.notes.ui.details

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.notes.databinding.FragmentNoteCreateBinding
import com.notes.di.DependencyManager
import com.notes.ui._base.ViewBindingFragment

class NoteCreateFragment : ViewBindingFragment<FragmentNoteCreateBinding>(
    FragmentNoteCreateBinding::inflate
) {

    private val viewModel by lazy { DependencyManager.noteDetailsViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteCreateBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)

        viewBinding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this ,object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!viewBinding.titleLabel.text.isNullOrEmpty() ||
                    !viewBinding.contentLabel.text.isNullOrEmpty()
                ) {
                    viewModel.createNote(
                        title = viewBinding.titleLabel.text.toString(),
                        content = viewBinding.contentLabel.text.toString()
                    )
                }
            }

        })


    }
}