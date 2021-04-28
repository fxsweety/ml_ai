package com.sample.ml_ai

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.android.synthetic.main.language.*

class TranslateActivity : AppCompatActivity() {

    private var textView: TextView? = null

    private var germanenglishTranslator: Translator? = null

    private var tamilenglishTranslator: Translator? = null

    private var spanishenglishTranslator: Translator? = null

    private var chineseenglishTranslator: Translator? = null

    private var conditions: DownloadConditions? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.language)
        textView = findViewById(R.id.result_text)

        conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        val germanOptions = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.GERMAN)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        germanenglishTranslator = Translation.getClient(germanOptions)

        val tamilOptions = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.TAMIL)
            .build()
        tamilenglishTranslator = Translation.getClient(tamilOptions)

        val spanishOptions = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.SPANISH)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        spanishenglishTranslator = Translation.getClient(spanishOptions)

        val chineseOptions = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.CHINESE)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        chineseenglishTranslator = Translation.getClient(chineseOptions)


        tamil.setOnClickListener{
            tamilenglishTranslator?.downloadModelIfNeeded(conditions)
                ?.addOnSuccessListener {
                    // Model downloaded successfully. Okay to start translating.
                    textView?.text = "Model Downloaded"
//                    processTheLanguage("நீங்கா எப்பாடி இருகீங்கா", tamilenglishTranslator!!)

                    tamilenglishTranslator?.translate("How are you?")
                        ?.addOnSuccessListener { translatedText ->
                            textView?.text = translatedText
                        }
                        ?.addOnFailureListener { exception ->
                            textView?.text = "Not able to Translate"
                        }
                }
                ?.addOnFailureListener { exception ->
                    // Model couldn’t be downloaded or other internal error.
                    textView?.text = "Not able to download model"
                }
           // processTheLanguage("நீங்கா எப்பாடி இருகீங்கா", tamilenglishTranslator!!)
        }
        spanish.setOnClickListener{
            spanishenglishTranslator?.downloadModelIfNeeded(conditions)
                ?.addOnSuccessListener {
                    // Model downloaded successfully. Okay to start translating.
                    textView?.text = "Model Downloaded"
                    processTheLanguage("me siento genial", spanishenglishTranslator!!)
                }
                ?.addOnFailureListener { exception ->
                    // Model couldn’t be downloaded or other internal error.
                    textView?.text = "Not able to download model"
                }
        }
        german.setOnClickListener{
            germanenglishTranslator?.downloadModelIfNeeded(conditions)
                ?.addOnSuccessListener {
                    // Model downloaded successfully. Okay to start translating.
                    textView?.text = "Model Downloaded"
                    processTheLanguage("Vielen Dank für Ihre Teilnahme an meinem Vortrag", germanenglishTranslator!!)
                }
                ?.addOnFailureListener { exception ->
                    // Model couldn’t be downloaded or other internal error.
                    textView?.text = "Not able to download model"
                }
        }
        chinese.setOnClickListener{
            chineseenglishTranslator?.downloadModelIfNeeded(conditions)
                ?.addOnSuccessListener {
                    // Model downloaded successfully. Okay to start translating.
                    textView?.text = "Model Downloaded"
                    processTheLanguage("好天气", chineseenglishTranslator!!)
                }
                ?.addOnFailureListener { exception ->
                    // Model couldn’t be downloaded or other internal error.
                    textView?.text = "Not able to download model"
                }
        }
    }

    fun processTheLanguage(text: String, translator: Translator) {
        translator.translate(text)
            .addOnSuccessListener { translatedText ->
                textView?.text = translatedText
            }
            .addOnFailureListener { exception ->
                textView?.text = "Not able to Translate"
            }
    }

}