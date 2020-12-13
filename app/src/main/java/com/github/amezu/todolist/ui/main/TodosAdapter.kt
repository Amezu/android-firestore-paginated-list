package com.github.amezu.todolist.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.amezu.todolist.R
import com.github.amezu.todolist.model.Todo
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class TodosAdapter constructor(
    private val todos: Iterable<Todo>,
    private val clickListener: (Todo) -> Unit,
    private val longClickListener: (Todo) -> Unit
) : RecyclerView.Adapter<TodosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(todos.elementAt(position))
    }

    override fun getItemCount() = todos.count()

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val titleView: TextView = v.findViewById(R.id.tv_title)
        private val descriptionView: TextView = v.findViewById(R.id.tv_desc)
        private val iconView: ImageView = v.findViewById(R.id.iv_icon)

        internal fun bind(item: Todo) {
            titleView.text = item.title
            descriptionView.text = item.description
            item.iconUrl?.takeIf { it.isNotBlank() }?.let { url ->
                Picasso.get()
                    .load(url)
                    .into(iconView, object : Callback {
                        override fun onSuccess() = Unit

                        override fun onError(e: Exception?) {
                            e?.printStackTrace()
                        }
                    })
            }
            itemView.setOnClickListener { clickListener(item) }
            itemView.setOnLongClickListener { longClickListener(item); true }
        }
    }
}
