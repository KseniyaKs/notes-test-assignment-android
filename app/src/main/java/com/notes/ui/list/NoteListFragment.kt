package com.notes.ui.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.notes.databinding.FragmentNoteListBinding
import com.notes.databinding.ListItemNoteBinding
import com.notes.di.DependencyManager
import com.notes.ui._base.FragmentNavigator
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.findImplementationOrThrow
import com.notes.ui.change.NoteChangeFragment
import com.notes.ui.create.NoteCreateFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NoteListFragment : ViewBindingFragment<FragmentNoteListBinding>(
    FragmentNoteListBinding::inflate
) {

    private val viewModel by lazy { DependencyManager.noteListViewModel() }

    private val recyclerViewAdapter =
        RecyclerViewAdapter(shortClick = object : OnItemClickListener {
            override fun onItemClick(noteListItem: NoteListItem) {
                viewModel.onChangeNoteClick(noteListItem)
            }
        },
            longClick = object : OnItemClickListener {
                override fun onItemClick(noteListItem: NoteListItem) {
                    viewModel.deleteNote(noteListItem)
                    Toast.makeText(context, "DELETE NOTE", Toast.LENGTH_SHORT).show()
                }
            })

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteListBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)

        viewBinding.list.adapter = recyclerViewAdapter
        viewBinding.list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )
        viewBinding.createNoteButton.setOnClickListener {
            viewModel.onCreateNoteClick()
        }

        viewModel.notes.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
                    recyclerViewAdapter.setItems(it as MutableList<NoteListItem>)
                }
            }
        )

        viewModel.navigateToNoteCreation.observe(
            viewLifecycleOwner,
            {
                findImplementationOrThrow<FragmentNavigator>()
                    .navigateTo(
                        NoteCreateFragment()
                    )
            }
        )

        viewModel.navigateToNoteChange.observe(
            viewLifecycleOwner,
            {
                findImplementationOrThrow<FragmentNavigator>()
                    .navigateTo(NoteChangeFragment.newInstance(it))
            }
        )
    }

    fun refreshList() {
        lifecycleScope.launch {
            delay(100)
            viewModel.getAllItemList()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(noteListItem: NoteListItem)
    }

    private class RecyclerViewAdapter(
        val shortClick: OnItemClickListener,
        val longClick: OnItemClickListener
    ) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        private val items = mutableListOf<NoteListItem>()
//        private var oldItems = items


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ) =
            ViewHolder(
                ListItemNoteBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {
            holder.bind(items[position], shortClick, longClick)
        }

        override fun getItemCount() = items.size

        fun setItems(newItems: MutableList<NoteListItem>) {
            val diffUtil = MyDiffUtil(items, newItems)
            val diffUtilResults = DiffUtil.calculateDiff(diffUtil)
            this.items.clear()
            this.items.addAll(newItems)
            diffUtilResults.dispatchUpdatesTo(this)
        }

//        fun setItems(items: List<NoteListItem>) {
//            this.items.clear()
//            this.items.addAll(items)
//            notifyDataSetChanged()
//        }

        private class ViewHolder(private val binding: ListItemNoteBinding) :
            RecyclerView.ViewHolder(binding.root) {

            @SuppressLint("ClickableViewAccessibility")
            fun bind(
                note: NoteListItem,
                shortClick: OnItemClickListener,
                longClick: OnItemClickListener
            ) {
                binding.titleLabel.text = note.title
                binding.contentLabel.text = note.content

                binding.root.setOnTouchListener(object : View.OnTouchListener {
                    val gestureDetector = GestureDetector(binding.root.context,
                        object : GestureDetector.SimpleOnGestureListener() {
                            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                                shortClick.onItemClick(note)
                                return super.onSingleTapConfirmed(e)
                            }

                            override fun onLongPress(e: MotionEvent?) {
                                longClick.onItemClick(note)
                                super.onLongPress(e)
                            }
                        })

                    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                        gestureDetector.onTouchEvent(event)
                        return true
                    }
                })
            }
        }
    }
}

class MyDiffUtil(
    private val oldItems: MutableList<NoteListItem>,
    private val newItems: MutableList<NoteListItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].title == newItems[newItemPosition].title &&
                oldItems[oldItemPosition].content == newItems[newItemPosition].content
    }
}