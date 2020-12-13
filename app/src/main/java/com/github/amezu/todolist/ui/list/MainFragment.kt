package com.github.amezu.todolist.ui.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.amezu.todolist.R
import com.github.amezu.todolist.di.DaggerMainFragmentComponent
import com.github.amezu.todolist.model.Todo
import com.github.amezu.todolist.ui.form.TodoFormFragment
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var adapter: TodosAdapter
    private var isScrolling = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerMainFragmentComponent.create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initErrorsHandling()
        initProgressBar()
        initList()
        initFab()
    }

    private fun initErrorsHandling() {
        viewModel.error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it?.message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initProgressBar() {
        viewModel.isLoadingNextPage.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })
    }

    private fun initList() {
        adapter = TodosAdapter(this::openEditItemView, this::showDeleteItemDialog)
        lv_todos.adapter = adapter
        lv_todos.layoutManager = LinearLayoutManager(activity)
        viewModel.todos.observe(viewLifecycleOwner, Observer(adapter::updateItems))
        viewModel.loadNextPage()
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
                        viewModel.loadNextPage()
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

    private fun initFab() {
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_todoFormFragment)
        }
    }

    private fun showDeleteItemDialog(item: Todo) {
        DeleteTodoDialogFragment.newInstance(item.id)
            .show(childFragmentManager, "delete")
    }

    private fun openEditItemView(item: Todo) {
        findNavController().navigate(
            R.id.action_mainFragment_to_todoFormFragment,
            bundleOf(TodoFormFragment.ARG_TODO to item)
        )
    }
}
