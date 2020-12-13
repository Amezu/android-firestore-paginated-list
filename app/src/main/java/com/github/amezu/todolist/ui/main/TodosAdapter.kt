package com.github.amezu.todolist.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.amezu.todolist.R
import com.github.amezu.todolist.model.Todo

class TodosAdapter constructor(
    private val todos: List<Todo>,
    private val clickListener: (Todo) -> Unit,
    private val longClickListener: (Todo) -> Unit
) : RecyclerView.Adapter<TodosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(todos[position])
    }

    override fun getItemCount() = todos.size

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val titleView: TextView = v.findViewById(R.id.tv_title)
        private val descriptionView: TextView = v.findViewById(R.id.tv_desc)

        internal fun bind(item: Todo) {
            titleView.text = item.title
            descriptionView.text = item.description
            itemView.setOnClickListener { clickListener(item) }
            itemView.setOnLongClickListener { longClickListener(item); true }
        }
    }
}
