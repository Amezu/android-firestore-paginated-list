package com.github.amezu.todolist.ui.add_todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.amezu.todolist.R
import com.github.amezu.todolist.hideKeyboard
import kotlinx.android.synthetic.main.add_todo_fragment.*

class AddTodoFragment : Fragment() {
    private lateinit var viewModel: AddTodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_todo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddTodoViewModel::class.java)

        initSaveButton()
    }

    private fun initSaveButton() {
        btn_save.setOnClickListener {
            hideKeyboard()
            viewModel.doOnSaveClick(
                tf_title.editText?.text.toString(),
                tf_description.editText?.text.toString(),
                this::back,
                this::showError
            )
        }
    }

    private fun back() {
        findNavController().popBackStack()
    }

    private fun showError(throwable: Throwable) {
        Toast.makeText(context, throwable.toString(), Toast.LENGTH_SHORT).show()
    }
}