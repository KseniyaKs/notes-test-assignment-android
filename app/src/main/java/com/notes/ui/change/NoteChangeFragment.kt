package com.notes.ui.change

import android.os.Bundle
import android.util.Log
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
    private val noteListItem by lazy { arguments?.get(NOTE_CHANGE_KEY) as NoteListItem }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("qwe", "onDestroyView")
    }

    override fun onResume() {
        super.onResume()
        Log.d("qwe", "onResume")

    }

    override fun onStop() {
        super.onStop()
        Log.d("qwe", "onStop")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("qwe", "onCreate")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("qwe", "onDestroy")

    }

    override fun onPause() {
        super.onPause()
        Log.d("qwe", "onPause")

        viewBinding?.let { binding ->
            if (binding.titleLabel.text.isNullOrEmpty() &&
                binding.contentLabel.text.isNullOrEmpty()
            ) {
                viewModel.deleteEmptyNote(
                    id = noteListItem.id,
                    title = binding.titleLabel.text.toString(),
                    content = binding.contentLabel.text.toString(),
                    createdAt = noteListItem.createdAt
                )
                Toast.makeText(context, "You can not save empty note.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.changeNote(
                    id = noteListItem.id,
                    title = binding.titleLabel.text.toString(),
                    content = binding.contentLabel.text.toString(),
                    createdAt = noteListItem.createdAt
                )
            }
        }
    }

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteChangeBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)

        viewBinding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

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
