package com.example.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemTodoBinding
import com.example.todolist.db.TodoEntity

// RecyclerView 어댑터
// 데이터 목록을 받아서 화면에 표시
class TodoRecyclerViewAdapter(
    private val todoList: ArrayList<TodoEntity>,
    private val listener: OnItemLongClickListener
) :
    RecyclerView.Adapter<TodoRecyclerViewAdapter.MyViewHolder>() {  // RecyclerView.Adapter 상속( 제너릭 타입으로 ViewHolder 클래스 지정 )

    //ViewHolder 클래스
    //RecyclerView.ViewHolder 상속
    //아이템 하나의 View들을 보관
    inner class MyViewHolder(binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        val tv_importance = binding.tvImportance
        val tv_title = binding.tvTitle

        val root = binding.root
    }

    // 아이템 View 생성
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding: ItemTodoBinding =
            ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    // 데이터를 View에 바인딩
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val todoData = todoList[position]   // position 번째 TodoEntity 가져오기
        when (todoData.importance) {
            1 -> {
                holder.tv_importance.setBackgroundResource(R.color.red)
            }

            2 -> {
                holder.tv_importance.setBackgroundResource(R.color.yellow)
            }

            3 -> {
                holder.tv_importance.setBackgroundResource(R.color.green)
            }
        }

        holder.tv_importance.text = todoData.importance.toString()
        holder.tv_title.text = todoData.title

        holder.root.setOnLongClickListener {
            listener.onLongClick(position)
            false
        }
    }

    // RecyclerView에 보여줄 아이템 개수
    override fun getItemCount(): Int {
        return todoList.size
    }

}