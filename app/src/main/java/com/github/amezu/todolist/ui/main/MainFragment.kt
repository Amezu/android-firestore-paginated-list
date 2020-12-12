package com.github.amezu.todolist.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.amezu.todolist.R
import com.github.amezu.todolist.model.Todo
import com.github.amezu.todolist.repo.Change
import com.github.amezu.todolist.repo.ChangeType
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment(), DeleteTodoDialogFragment.Callback {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: TodosAdapter
    private val todos = mutableListOf<Todo>()
    private var isScrolling = false

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
        getItems()
        initFab()
    }

    private fun initAdapter() {
        adapter = TodosAdapter(todos, this::showDeleteItemDialog)
        lv_todos.adapter = adapter
        lv_todos.layoutManager = LinearLayoutManager(activity)
        lv_todos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (layoutManager != null) {
                    val firstVisibleProductPosition =
                        layoutManager.findFirstVisibleItemPosition()
                    val visibleProductCount = layoutManager.childCount
                    val totalProductCount = layoutManager.itemCount
                    if (isScrolling && firstVisibleProductPosition + visibleProductCount == totalProductCount) {
                        isScrolling = false
                        getItems()
                    }
                }
            }
        })
    }

    private fun getItems() {
        viewModel.getNextPage()?.let {
            it.observe(viewLifecycleOwner) { change ->
                when (change.type) {
                    ChangeType.ADDED -> todos.add(change.item)
                    ChangeType.MODIFIED -> {
                        val index = todos.findItemIndex(change)
                        index?.let { todos.removeAt(it) }
                        todos.add(index ?: todos.size, change.item)
                    }
                    ChangeType.REMOVED -> {
                        val index = todos.findItemIndex(change)
                        index?.let { todos.removeAt(it) }
                        todos.add(
                            index ?: todos.size,
                            Todo(requireContext().getString(R.string.todos_item_deleted))
                        )
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initFab() {
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addTodoFragment)
        }
    }

    private fun showDeleteItemDialog(item: Todo) {
        DeleteTodoDialogFragment.newInstance(this, item.id)
            .show(parentFragmentManager, "dialog")
    }

    override fun onDeleteAccepted(id: String) {
        viewModel.delete(id, this::showError)
    }

    private fun showError(throwable: Throwable?) {
        Toast.makeText(context, throwable.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun List<Todo>.findItemIndex(change: Change<Todo>): Int? {
        return indexOfFirst { it.id == change.item.id }.takeUnless { it < 0 }
    }
}
