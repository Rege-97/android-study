package com.example.stopwatch

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Timer
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btn_start: Button
    private lateinit var btn_refresh: Button
    private lateinit var tv_minute: TextView
    private lateinit var tv_second: TextView
    private lateinit var tv_millisecond: TextView

    private var isRunning = false

    private var timer: Timer? = null
    private var time = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_start = findViewById(R.id.btn_start)
        btn_refresh = findViewById(R.id.btn_refresh)
        tv_minute = findViewById(R.id.tv_minute)
        tv_second = findViewById(R.id.tv_second)
        tv_millisecond = findViewById(R.id.tv_millisecond)

        btn_start.setOnClickListener(this)
        btn_refresh.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_start -> {
                if (isRunning) {
                    pause()
                } else {
                    start()
                }
            }

            R.id.btn_refresh -> {
                refresh()
            }
        }
    }

    private fun start() {
        btn_start.text = getString(R.string.btn_pause)
        btn_start.setBackgroundColor(getColor(R.color.btn_pause))
        isRunning = true

        timer = timer(period = 10) {  // 10ms마다 실행(항상 백그라운드에서 실행됨)
            time++
            val milli_second = time % 100
            val second = (time % 6000) / 100
            val minute = time / 6000

            runOnUiThread { // 백그라운드 스레드에서 ui 조작시 에러 -> runOnUiThread를 구현하여 메인 스레드에서 실행되도록
                tv_millisecond.text =
                    if (milli_second < 10) ".0${milli_second}" else ".${milli_second}"
                tv_second.text = if (second < 10) ":0${second}" else ".${second}"
                tv_minute.text = "${minute}"
            }
        }
    }

    private fun pause() {
        btn_start.text = getString(R.string.btn_start)
        btn_start.setBackgroundColor(getColor(R.color.btn_start))

        isRunning = false
        timer?.cancel() // 타이머 함수 종료
    }

    private fun refresh() {

    }
}