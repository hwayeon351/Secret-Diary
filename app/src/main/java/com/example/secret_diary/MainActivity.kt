package com.example.secret_diary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    private val numberPicker1: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker1)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }
    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }
    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }
    private val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    }
    private val changePasswordButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changePasswordButton)
    }
    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //numberPicker 호출을 통해 lazy init
        numberPicker1
        numberPicker2
        numberPicker3

        openButton.setOnClickListener {
            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중 입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //Preference 파일을 다른 앱과 공유하지 않고 이 앱에서만 사용하기 위해 MODE_PRIVATE으로 설정
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser =
                "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            //password Preference 파일 안에 password라는 key로 값을 가져온다. 초기값은 000으로 셋팅
            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                //패스워드 일치
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                showErrorAlertDialog()
            }
        }

        changePasswordButton.setOnClickListener {
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            if (changePasswordMode) {
                //비밀번호를 저장
                //commit = true -> commit, preference에 데이터가 저장될 때까지 UI쓰레드는 block되므로 화면이 멈춘다.
                    // 무거울 작업을 할 때에는 화면이 멈추기 때문에 앱이 오랫동안 멈춰 있게 되고 사용자 측면에서 앱이 죽었다고 인식할 수 있기 때문에 Thread를 사용해서 적용하는 것이 좋다.
                //commit = false -> apply, UI쓰레드가 block되지 않고 비동기적으로 데이터가 저장된다.
                    // 주로 무거운 작업을 할 때 apply를 적용하는 것이 좋다.
                passwordPreferences.edit(true) {
                    val passwordFromUser =
                        "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"
                    putString("password", passwordFromUser)
                }
                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)
            } else {
                //changePasswordMode가 활성화 :: 비밀번호가 맞는지 체크
                val passwordFromUser =
                    "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

                //password Preference 파일 안에 password라는 key로 값을 가져온다. 초기값은 000으로 셋팅
                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    changePasswordButton.setBackgroundColor(Color.RED)
                } else {
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun showErrorAlertDialog() {
        //패스워드 불일치
        AlertDialog.Builder(this)
            .setTitle("실패!!!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }
}