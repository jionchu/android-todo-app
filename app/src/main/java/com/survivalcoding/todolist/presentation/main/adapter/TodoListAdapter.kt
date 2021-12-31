package com.survivalcoding.todolist.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.survivalcoding.todolist.databinding.ItemTodoBinding
import com.survivalcoding.todolist.model.Todo

class TodoListAdapter :
    ListAdapter<Todo, TodoViewHolder>(TodoDiffItemCallback) {
    var itemClickListener: (Todo) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            ItemTodoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), itemClickListener
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}
