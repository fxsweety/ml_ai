package com.sample.ml_ai

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import kotlinx.android.synthetic.main.language.*

class IdentifyLangActivity : AppCompatActivity() {

    private var textView: TextView? = null

    private var languageIdentifier: LanguageIdentifier? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.language)
        textView = findViewById(R.id.result_text)

        languageIdentifier = LanguageIdentification.getClient()

        tamil.setOnClickListener{
            processTheLanguage("How are you?")
        }
        spanish.setOnClickListener{
            processTheLanguage("me siento genial")
        }
        german.setOnClickListener{
            processTheLanguage("Vielen Dank für Ihre Teilnahme an meinem Vortrag")
        }
        chinese.setOnClickListener{
            processTheLanguage("好天气")
        }
    }

    fun processTheLanguage(text: String) {
        var language = ""
        var confidence = 0.0f
        languageIdentifier?.identifyPossibleLanguages(text)
                ?.addOnSuccessListener { identifiedLanguages ->
                    for (identifiedLanguage in identifiedLanguages) {
                        language = identifiedLanguage.languageTag
                        confidence = identifiedLanguage.confidence
                    }
                    textView?.text = "Language: $language\n Confidence: $confidence\n"
                }
                ?.addOnFailureListener {
                    // Model couldn’t be loaded or other internal error.
                    textView?.text = "Language: Not Detected\n Confidence $confidence\n"
                }
    }


}