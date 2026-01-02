package com.example.activityandfragment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        settingButton()
    }

    fun settingButton() {
        // id로 변수에 버튼 지정
        val button = findViewById<Button>(R.id.button)

        // 클릭 이벤트 지정
        button.setOnClickListener {
            // subActivity로 이동
            // intent : 의지, 지향, 의도
            // Intent(현재 Context, 이동할 Activity)
            // 이 화면에서 저 화면으로 가겠다는 의사표시
            val intent = Intent(this, SubActivity::class.java)
            // OS에 등록
            startActivity(intent)
        }
    }
}