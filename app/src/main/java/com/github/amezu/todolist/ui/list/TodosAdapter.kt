package com.github.amezu.todolist.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.amezu.todolist.R
import com.github.amezu.todolist.data.model.Todo
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class TodosAdapter(
    private val clickListener: (Todo) -> Unit,
    private val longClickListener: (Todo) -> Unit
) : RecyclerView.Adapter<TodosAdapter.ViewHolder>() {
    private val content = mutableListOf<Todo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(content.elementAt(position))
    }

    override fun getItemCount() = content.count()

    fun updateItems(newContent: Iterable<Todo>) {
        val diff = DiffUtil.calculateDiff(DiffCallback(content, newContent))
        content.clear()
        content.addAll(newContent)
        diff.dispatchUpdatesTo(this)
    }

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
                    .noPlaceholder()
                    .into(iconView, object : Callback {
                        override fun onSuccess() = Unit

                        override fun onError(e: Exception?) {
                            e?.printStackTrace()
                        }
                    })
            } ?: iconView.setImageResource(R.drawable.ic_todo_placeholder)
            itemView.setOnClickListener { clickListener(item) }
            itemView.setOnLongClickListener { longClickListener(item); true }
        }
    }

    class DiffCallback(
        private val oldContent: Iterable<Todo>,
        private val newContent: Iterable<Todo>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldContent.elementAt(oldItemPosition).id == newContent.elementAt(newItemPosition).id
        }

        override fun getOldListSize(): Int {
            return oldContent.count()
        }

        override fun getNewListSize(): Int {
            return newContent.count()
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldContent.elementAt(oldItemPosition) == newContent.elementAt(newItemPosition)
        }
    }
}
