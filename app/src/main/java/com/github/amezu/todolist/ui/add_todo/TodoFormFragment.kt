package com.github.amezu.todolist.ui.add_todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.amezu.todolist.R
import com.github.amezu.todolist.hideKeyboard
import com.github.amezu.todolist.model.Todo
import kotlinx.android.synthetic.main.add_todo_fragment.*

class TodoFormFragment : Fragment() {
    private lateinit var viewModel: TodoFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_todo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TodoFormViewModel::class.java)

        val todo = arguments?.get("todo") as Todo?
        todo?.let { initFormWithTodo(it) }
        initSaveButton()
    }

    private fun initFormWithTodo(todo: Todo) {
        viewModel.todoToEdit = todo
        tf_title.editText?.setText(todo.title)
        tf_description.editText?.setText(todo.description)
    }

    private fun initSaveButton() {
        btn_save.setOnClickListener {
            hideKeyboard()
            viewModel.doOnSaveClick(
                tf_title.editText?.text.toString(),
                tf_description.editText?.text.toString()
            )
        }
        viewModel.result.observe(viewLifecycleOwner, Observer { error ->
            error?.let { showError(it) }
                ?: back()
        })
    }

    private fun back() {
        findNavController().popBackStack()
    }

    private fun showError(error: Throwable) {
        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
    }
}