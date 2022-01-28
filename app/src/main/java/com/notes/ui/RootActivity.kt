package com.notes.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.notes.databinding.ActivityRootBinding
import com.notes.ui._base.FragmentNavigator
import com.notes.ui.list.NoteListFragment

class RootActivity : AppCompatActivity(), FragmentNavigator {

    private var viewBinding: ActivityRootBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityRootBinding.inflate(layoutInflater)
        this.viewBinding = viewBinding
        setContentView(viewBinding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    viewBinding.container.id,
                    NoteListFragment()
                )
                .commit()
        }
    }

    override fun navigateTo(
        fragment: Fragment
    ) {
        val viewBinding = this.viewBinding ?: return
        supportFragmentManager
            .beginTransaction()
            .add(
                viewBinding.container.id,
                fragment
            )
            .addToBackStack(fragment.toString())
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            val noteListFragment = supportFragmentManager.fragments.find {
                it is NoteListFragment
            } as NoteListFragment
            noteListFragment.refreshList()
            closeKeyboard()
        } else {
            super.onBackPressed()
        }
    }


    private fun closeKeyboard() {
        val view: View? = this.currentFocus

        if (view != null) {
            val manager: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager
                .hideSoftInputFromWindow(
                    view.getWindowToken(), 0
                )
        }
    }
}