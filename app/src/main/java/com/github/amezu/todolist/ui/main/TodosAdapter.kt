package com.github.amezu.todolist.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.github.amezu.todolist.R
import com.github.amezu.todolist.model.Todo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TodosAdapter constructor(
    lifecycleOwner: LifecycleOwner,
    private val longClickListener: (Todo, Int) -> Unit
) : FirestorePagingAdapter<Todo, TodosAdapter.ViewHolder>(PagingOptionsBuilder(lifecycleOwner).build()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int, item: Todo) {
        viewHolder.apply {
            titleView.text = item.title
            descriptionView.text = item.description
            itemView.setOnLongClickListener { longClickListener(item, adapterPosition); true }
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        internal val titleView: TextView = v.findViewById(R.id.tv_title)
        internal val descriptionView: TextView = v.findViewById(R.id.tv_desc)
    }

    private class PagingOptionsBuilder(
        private val lifecycleOwner: LifecycleOwner,
        private val pageSize: Int = 30
    ) {
        fun build(): FirestorePagingOptions<Todo> {
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(pageSize)
                .setPageSize(pageSize)
                .build()
            val query = Firebase.firestore.collection("todos")

            return FirestorePagingOptions.Builder<Todo>()
                .setLifecycleOwner(lifecycleOwner)
                .setQuery(query, config, Todo::class.java)
                .build()
        }
    }
}
