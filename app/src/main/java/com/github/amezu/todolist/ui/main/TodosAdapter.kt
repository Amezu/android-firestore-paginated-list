package com.github.amezu.todolist.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.amezu.todolist.R
import com.github.amezu.todolist.model.Todo

class TodosAdapter :
    ListAdapter<Todo, TodosAdapter.ViewHolder>(TodosDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = getItem(position)
        holder.apply {
            titleView.text = todo.title
            descriptionView.text = todo.description
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        internal val titleView: TextView = v.findViewById(R.id.tv_title)
        internal val descriptionView: TextView = v.findViewById(R.id.tv_desc)
    }
}

object TodosDiffCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }
}
