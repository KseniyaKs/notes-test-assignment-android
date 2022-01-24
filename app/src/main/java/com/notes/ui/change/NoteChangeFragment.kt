package com.notes.ui.change

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.notes.R
import com.notes.databinding.FragmentNoteChangeBinding
import com.notes.di.DependencyManager
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui.list.NoteListItem

class NoteChangeFragment : ViewBindingFragment<FragmentNoteChangeBinding>(
    FragmentNoteChangeBinding::inflate
) {

    private val viewModel by lazy { DependencyManager.noteChangeViewModel() }

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteChangeBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)

        val noteListItem = arguments?.get(NOTE_CHANGE_KEY) as NoteListItem


        viewBinding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewBinding.titleLabel.text.isNullOrEmpty() &&
                        viewBinding.contentLabel.text.isNullOrEmpty()
                    ) {
                        viewModel.deleteEmptyNote(
                            id = noteListItem.id,
                            title = viewBinding.titleLabel.text.toString(),
                            content = viewBinding.contentLabel.text.toString(),
                            createdAt = noteListItem.createdAt
                        )
                        Toast.makeText(context, "You can not save empty note.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.changeNote(
                            id = noteListItem.id,
                            title = viewBinding.titleLabel.text.toString(),
                            content = viewBinding.contentLabel.text.toString(),
                            createdAt = noteListItem.createdAt
                        )
                    }
                }
            })

        viewBinding.titleLabel.setText(noteListItem.title)
        viewBinding.contentLabel.setText(noteListItem.content)
    }

    companion object {
        private const val NOTE_CHANGE_KEY = "NOTE_CHANGE_KEY"

        fun newInstance(noteListItem: NoteListItem) = NoteChangeFragment().apply {
            arguments = Bundle().apply {
                putSerializable(NOTE_CHANGE_KEY, noteListItem)
            }
        }
    }
}
