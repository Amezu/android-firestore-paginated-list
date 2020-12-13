package com.github.amezu.todolist.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.amezu.todolist.R
import com.github.amezu.todolist.model.Change
import com.github.amezu.todolist.model.ChangeType
import com.github.amezu.todolist.model.Todo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

        viewModel.isLoadingNextPage.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })
        initList()
        initFab()
    }

    private fun initList() {
        adapter = TodosAdapter(todos, this::openEditItemView, this::showDeleteItemDialog)
        lv_todos.adapter = adapter
        lv_todos.layoutManager = LinearLayoutManager(activity)
        loadNextPage()
        setupLoadingNextPage()
    }

    private fun setupLoadingNextPage() {
        lv_todos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                layoutManager?.let {
                    if (isScrolling && it.isNextItemNotLoaded()) {
                        isScrolling = false
                        loadNextPage()
                    }
                }
            }

            private fun LinearLayoutManager.isNextItemNotLoaded(): Boolean {
                val firstVisibleItemPosition = findFirstVisibleItemPosition()
                val visibleItemsCount = childCount
                val totalProductCount = itemCount
                return firstVisibleItemPosition + visibleItemsCount == totalProductCount
            }
        })
    }

    private fun loadNextPage() {
        viewModel.loadNextPage()
            ?.subscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnNext {
                it.forEach { change ->
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
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                viewModel.doOnPageLoaded()
            }?.subscribe()
            ?: viewModel.doOnPageLoaded()
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

    private fun openEditItemView(item: Todo) {
        findNavController().navigate(
            R.id.action_mainFragment_to_addTodoFragment,
            bundleOf("todo" to item)
        )
    }

    private fun showError(throwable: Throwable?) {
        Toast.makeText(context, throwable?.message, Toast.LENGTH_SHORT).show()
    }

    private fun List<Todo>.findItemIndex(change: Change<Todo>): Int? {
        return indexOfFirst { it.id == change.item.id }.takeUnless { it < 0 }
    }
}
