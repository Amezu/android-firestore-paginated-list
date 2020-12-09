package com.github.amezu.todolist.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.amezu.todolist.R
import com.github.amezu.todolist.model.Todo
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: TodosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initAdapter()
    }

    @SuppressLint("CheckResult")
    private fun initAdapter() {
        adapter = TodosAdapter()
        lv_todos.adapter = adapter
        lv_todos.layoutManager = LinearLayoutManager(activity)

        viewModel.loadTodos()
            .subscribe(
                { t: List<Todo>? -> adapter.submitList(t) },
                { t: Throwable -> showError(t) }
            )
    }

    private fun showError(throwable: Throwable?) {
        Toast.makeText(context, throwable.toString(), Toast.LENGTH_SHORT).show()
    }
}