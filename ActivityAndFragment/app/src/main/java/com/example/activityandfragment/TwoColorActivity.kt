package com.example.activityandfragment

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TwoColorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_two_color)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        settingButtons()
    }

    fun settingButtons(){
        val button_red = findViewById<Button>(R.id.button_red_fragment)
        val button_blue = findViewById<Button>(R.id.button_blue_fragment)

        button_red.setOnClickListener {
            // supportFragmentManager - 프래그먼트 관리 객체
            // 프래그먼트 작업은 반드시 트랜잭션 단위로 처리함
            // beginTransaction() - 트랜잭션 시작
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            // frame_layout 안에 있는 Fragment를 지우고 RedFragment를 생성하여 넣는 작업
            fragmentTransaction.replace(R.id.frame_layout, RedFragment())
            // 커밋하여 확정
            fragmentTransaction.commit()
        }
        button_blue.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, BlueFragment())
            fragmentTransaction.commit()
        }
    }
}