package me.shuza.textrecognization.view

import android.app.LauncherActivity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_choose_type.*
import me.shuza.textrecognization.R
import me.shuza.textrecognization.utils.MySharedPref

class ChooseTypeActivity : AppCompatActivity() {
    private var secs = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_type)
        secs = 100000

        object : CountDownTimer(((secs + 1) * 1000).toLong(), 1400)
        {
            override fun onFinish() {
                println("finished")
            }
            override fun onTick(millisUntilFinished: Long) {
                if (choose_text_view.text.contains("Choose")) {
                    choose_text_view.text = "აირჩიეთ შემოწმების ტიპი"
                    val connectingAnimation = AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_in)
                    choose_text_view.startAnimation(connectingAnimation)
                } else if (choose_text_view.text.contains("აირჩიეთ")) {
                    choose_text_view.text = "Choose a Check Type"
                    val connectingAnimation = AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_in)
                    choose_text_view.startAnimation(connectingAnimation)
                }
            }
        }.start()
        confirm.setOnClickListener{
            val checkedRadioBtn = radio_group.checkedRadioButtonId
            Log.e("TAG", "id ->>>>>>>>>>>>>>  ${checkedRadioBtn}")
            if (checkedRadioBtn == -1){
                Toast.makeText(this, "გთხოვთ, აირჩიოთ შემოწმების ტიპი", Toast.LENGTH_SHORT).show()
            }else {
                MySharedPref.saveCheckedRadioBtn(this, checkedRadioBtn)
                val intent = Intent(this, LouncherActivity::class.java)

                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        secs = 0
    }
}