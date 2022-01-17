package com.example.secret_diary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity: AppCompatActivity() {

    //getMainLooper에 의해 handler가 메인쓰레드에 연결되도록 함
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)
        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        diaryEditText.setText(detailPreferences.getString("detail", ""))

        val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                putString("detail", diaryEditText.text.toString())
            }
            Log.d("DiaryActivity", "SAVE!!!! ${detailPreferences.getString("detail", "")}")
        }

        //0.5초 이후에 remove 되지 않은 runnable이 있다면 해당 runnable을 실행하게 된다.
        diaryEditText.addTextChangedListener {
            Log.d("DiaryActivity", "TextChanged :: $it")
            //0.5초 이전에 아직 실행되지 않고 pending 되어 있는 runnable이 있다면 해당 runnable을 지워줌
            handler.removeCallbacks(runnable)
            //0.5초 이후에 runnable 실행
            handler.postDelayed(runnable, 500)
        }
    }

}