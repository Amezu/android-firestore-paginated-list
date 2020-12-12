package com.github.amezu.todolist.ui.main

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class DeleteTodoDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage("Delete todo?")
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    val id = requireArguments().getString(ARG_ID)!!
                    (targetFragment as Callback).onDeleteAccepted(id)
                    dialog.dismiss()
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface Callback {
        fun onDeleteAccepted(id: String)
    }

    companion object {
        const val ARG_ID = "ID"

        fun <T> newInstance(
            callbackFragment: T,
            id: String
        ): DeleteTodoDialogFragment
                where T : Callback, T : Fragment {
            return DeleteTodoDialogFragment().apply {
                setTargetFragment(callbackFragment, 0)
                arguments = bundleOf(ARG_ID to id)
            }
        }
    }
}