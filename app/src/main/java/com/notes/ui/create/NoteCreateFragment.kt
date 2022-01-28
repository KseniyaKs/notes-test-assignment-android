package com.notes.ui.create

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.notes.databinding.FragmentNoteCreateBinding
import com.notes.di.DependencyManager
import com.notes.ui._base.ViewBindingFragment

class NoteCreateFragment : ViewBindingFragment<FragmentNoteCreateBinding>(
    FragmentNoteCreateBinding::inflate
) {

    private val viewModel by lazy { DependencyManager.noteDetailsViewModel() }

    override fun onPause() {
        super.onPause()
        if (!isRemoving){
            viewBinding?.let { binding ->
                if (!binding.titleLabel.text.isNullOrEmpty() ||
                    !binding.contentLabel.text.isNullOrEmpty()
                ) {
                    viewModel.createNote(
                        title = binding.titleLabel.text.toString(),
                        content = binding.contentLabel.text.toString()
                    )
                }
            }
        }
    }

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteCreateBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)

        viewBinding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }


}