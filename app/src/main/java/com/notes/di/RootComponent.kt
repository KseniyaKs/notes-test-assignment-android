package com.notes.di

import com.notes.ui.change.NoteChangeViewModel
import com.notes.ui.create.NoteCreateViewModel
import com.notes.ui.list.NoteListViewModel
import dagger.Component

@RootScope
@Component(
    dependencies = [
        AppComponent::class,
    ],
    modules = [
    ]
)
interface RootComponent {

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): RootComponent
    }

    fun getNoteListViewModel(): NoteListViewModel

    fun getNoteDetailsViewModel(): NoteCreateViewModel

    fun getNoteChangeViewModel(): NoteChangeViewModel
}