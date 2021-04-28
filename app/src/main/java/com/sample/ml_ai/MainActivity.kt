package com.sample.ml_ai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vision_edge?.setOnClickListener{
            val intent = Intent(this, VisionActivity::class.java)
            startActivity(intent)
        }
        text_recognize?.setOnClickListener{
            val intent = Intent(this, TextRecognitionActivity::class.java)
            startActivity(intent)
        }
        face_detection?.setOnClickListener {
            val intent = Intent(this, DetectFacesActivity::class.java)
            startActivity(intent)
        }
        bar_code?.setOnClickListener{
            val intent = Intent(this, BarCodeActivity::class.java)
            startActivity(intent)
        }
        identify_lang?.setOnClickListener{
            val intent = Intent(this, IdentifyLangActivity::class.java)
            startActivity(intent)
        }
        translate?.setOnClickListener{
            val intent = Intent(this, TranslateActivity ::class.java)
            startActivity(intent)
        }
    }
}