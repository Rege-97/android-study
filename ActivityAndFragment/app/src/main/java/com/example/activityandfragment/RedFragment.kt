package com.example.activityandfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class RedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflater - XML에서 View 객체로 바꿔주는 도구
        // inflater(레이아웃 리소스참조값, 부모뷰, attachToRoot)
        // fragment_red.xml을 View 객체로 만들고 아직 container에는 붙이지 말고 View만 반환
        return inflater.inflate(R.layout.fragment_red, container, false)
    }
}