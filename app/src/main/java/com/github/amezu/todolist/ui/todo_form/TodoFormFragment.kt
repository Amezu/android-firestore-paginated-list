package com.github.amezu.todolist.ui.todo_form

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.amezu.todolist.R
import com.github.amezu.todolist.di.DaggerTodoFormFragmentComponent
import com.github.amezu.todolist.hideKeyboard
import com.github.amezu.todolist.model.Todo
import kotlinx.android.synthetic.main.todo_form_fragment.*
import javax.inject.Inject

class TodoFormFragment : Fragment() {

    @Inject
    lateinit var viewModel: TodoFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.todo_form_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerTodoFormFragmentComponent.create().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todo = arguments?.get("todo") as Todo?
        todo?.let { initFormWithTodo(it) }
        initSaveButton()
    }

    private fun initFormWithTodo(todo: Todo) {
        viewModel.selectedTodo = todo
        tf_title.editText?.setText(todo.title)
        tf_description.editText?.setText(todo.description)
        tf_iconUrl.editText?.setText(todo.iconUrl)
    }

    private fun initSaveButton() {
        btn_save.setOnClickListener {
            hideKeyboard()
            viewModel.doOnSaveClick(
                tf_title.editText?.text.toString(),
                tf_description.editText?.text.toString(),
                tf_iconUrl.editText?.text.toString()
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