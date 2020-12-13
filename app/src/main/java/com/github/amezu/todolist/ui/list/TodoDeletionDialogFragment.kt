package com.github.amezu.todolist.ui.list

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.github.amezu.todolist.R
import com.github.amezu.todolist.di.DaggerMainFragmentComponent
import javax.inject.Inject

class TodoDeletionDialogFragment : DialogFragment() {

    @Inject
    lateinit var sharedViewModel: MainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerMainFragmentComponent.create().inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.delete_todo_title))
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                val id = requireArguments().getString(ARG_ID)!!
                sharedViewModel.doOnDeleteAccepted(id)
                dialog.dismiss()
            }.setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }.create()
    }

    companion object {
        private const val ARG_ID = "ID"

        fun newInstance(id: String): TodoDeletionDialogFragment {
            return TodoDeletionDialogFragment()
                .apply { arguments = bundleOf(ARG_ID to id) }
        }
    }
}