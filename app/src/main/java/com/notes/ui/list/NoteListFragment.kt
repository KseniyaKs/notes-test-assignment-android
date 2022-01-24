package com.notes.ui.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.notes.databinding.FragmentNoteListBinding
import com.notes.databinding.ListItemNoteBinding
import com.notes.di.DependencyManager
import com.notes.ui._base.FragmentNavigator
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.findImplementationOrThrow
import com.notes.ui.change.NoteChangeFragment
import com.notes.ui.details.NoteCreateFragment
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NoteListFragment : ViewBindingFragment<FragmentNoteListBinding>(
    FragmentNoteListBinding::inflate
) {

    private val viewModel by lazy { DependencyManager.noteListViewModel() }

    private val recyclerViewAdapter = RecyclerViewAdapter(shortClick = object : OnItemClickListener{
        override fun onItemClick(noteListItem: NoteListItem) {
            viewModel.onChangeNoteClick(noteListItem)
//            Toast.makeText(context,"shortCLICK", Toast.LENGTH_SHORT).show()
        }
    },
    longClick = object : OnItemClickListener{
        override fun onItemClick(noteListItem: NoteListItem) {
            viewModel.deleteNote(noteListItem)
            Toast.makeText(context,"DELETE NOTE", Toast.LENGTH_SHORT).show()
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("qwe", viewModel.toString())
        Log.d("qwe", "onCreate")

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Log.d("qwe", "onSTSRT")
    }

    override fun onResume() {
        super.onResume()
        Log.d("qwe", "ONRESUME")
    }

    override fun onPause() {
        super.onPause()
        Log.d("qwe", "ONPAUSE")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("qwe", "onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("qwe", "onDestroyView")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



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
                    recyclerViewAdapter.setItems(it)
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

    fun refreshList(){
        lifecycleScope.launch {
            delay(100)
            viewModel.getAllItemList()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(noteListItem: NoteListItem)
    }

    private class RecyclerViewAdapter(val shortClick: OnItemClickListener,
                                      val longClick: OnItemClickListener)
        : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        private val items = mutableListOf<NoteListItem>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int) =
            ViewHolder(ListItemNoteBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            )


        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int) {
            holder.bind(items[position], shortClick, longClick)
        }

        override fun getItemCount() = items.size

        fun setItems (items: List<NoteListItem>) {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }

        private class ViewHolder(private val binding: ListItemNoteBinding)
            : RecyclerView.ViewHolder(binding.root) {

            @SuppressLint("ClickableViewAccessibility")
            fun bind(note: NoteListItem,
                     shortClick: OnItemClickListener,
                     longClick: OnItemClickListener) {
                binding.titleLabel.text = note.title
                binding.contentLabel.text = note.content
                binding.root.setOnTouchListener(object :View.OnTouchListener {
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
//    companion object{
//    fun newInstance() = NoteListFragment()
//    }
}