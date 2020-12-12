package com.github.amezu.todolist.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.amezu.todolist.R
import com.github.amezu.todolist.model.Todo
import com.github.amezu.todolist.repo.RealtimeTodo
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class TodosAdapter constructor(
    private val longClickListener: (Todo) -> Unit
) : PagedListAdapter<RealtimeTodo, TodosAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<RealtimeTodo?>() {
        override fun areItemsTheSame(oldItem: RealtimeTodo, newItem: RealtimeTodo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RealtimeTodo, newItem: RealtimeTodo): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        getItem(position)?.let { viewHolder.bind(it, position) }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val titleView: TextView = v.findViewById(R.id.tv_title)
        private val descriptionView: TextView = v.findViewById(R.id.tv_desc)

        private val disposables = CompositeDisposable()

        internal fun bind(realtimeTodo: RealtimeTodo, position: Int) {
            realtimeTodo.todo.subscribe(
                {
                    if (it.isPresent) {
                        bind(it.get())
                    } else {
                        bind(Todo(itemView.context.getString(R.string.todos_item_deleted)))
                    }
                }, Throwable::printStackTrace
            ).addTo(disposables)
        }

        private fun bind(item: Todo) {
            titleView.text = item.title
            descriptionView.text = item.description
            itemView.setOnLongClickListener { longClickListener(item); true }
        }
    }
}
