package com.github.amezu.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.amezu.todolist.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}