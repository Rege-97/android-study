package com.example.newtodolist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.newtodolist.databinding.ActivityAddTodoBinding
import com.example.newtodolist.db.AppDatabase
import com.example.newtodolist.db.TodoDao
import com.example.newtodolist.db.TodoEntity
import java.util.Date

class ActivityAddTodo : AppCompatActivity() {

    private lateinit var binding: ActivityAddTodoBinding

    lateinit var db: AppDatabase
    lateinit var todoDao: TodoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getInstance(this)!!
        todoDao = db.getTodoDao()

        binding.btnComplete.setOnClickListener {
            insertTodo()
        }
    }

    fun insertTodo() {
        val todoTitle = binding.edtTitle.text.toString()
        val todoCategory = binding.edtCategory.text.toString()
        var pointColor = binding.radioGroup.checkedRadioButtonId

        var ckColor = false
        lateinit var impColor: String

        when (pointColor) {
            R.id.btn_red -> {
                ckColor = true
                impColor = "red"
            }

            R.id.btn_green -> {
                ckColor = true
                impColor = "green"
            }

            R.id.btn_yellow -> {
                ckColor = true
                impColor = "yellow"
            }

            R.id.btn_purple -> {
                ckColor = true
                impColor = "purple"
            }
        }

        if (!ckColor || todoTitle.isBlank() || todoCategory.isBlank()) {
            Toast.makeText(this, "모든 항목을 채워주세요.", Toast.LENGTH_SHORT).show()
        } else {
            Thread {
                todoDao.insertTodo(
                    TodoEntity(
                        null,
                        todoTitle,
                        todoCategory,
                        impColor,
                        Date(),
                        Date()
                    )
                )
                runOnUiThread {
                    Toast.makeText(this, "할 일이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }
    }
}