package com.github.amezu.todolist

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    currentFocus?.let { focusedView ->
        inputMethodManager.hideSoftInputFromWindow(
            focusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun Fragment.hideKeyboard() {
    requireActivity().hideKeyboard()
}