package com.github.amezu.todolist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.amezu.todolist.R
import com.github.amezu.todolist.data.model.Todo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}