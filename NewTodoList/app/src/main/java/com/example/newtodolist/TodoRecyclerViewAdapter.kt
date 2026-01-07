package com.example.newtodolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.newtodolist.databinding.ItemTodoBinding
import com.example.newtodolist.db.TodoEntity

class TodoRecyclerViewAdapter(
    private val todoList: ArrayList<TodoEntity>
) :
    RecyclerView.Adapter<TodoRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        val tv_title = binding.tvTitle
        val tv_category = binding.tvCategory
        val root = binding.root
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding: ItemTodoBinding =
            ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val todoData = todoList[position]
        when (todoData.pointColor) {
            "red" -> {
                holder.root.setBackgroundResource(R.color.point_color_red)
            }

            "green" -> {
                holder.root.setBackgroundResource(R.color.point_color_green)
            }

            "yellow" -> {
                holder.root.setBackgroundResource(R.color.point_color_yellow)
            }

            "purple" -> {
                holder.root.setBackgroundResource(R.color.point_color_purple)
            }
        }

        holder.tv_title.text = todoData.title
        holder.tv_category.text = todoData.category
    }

    override fun getItemCount(): Int {
        return todoList.size
    }


}