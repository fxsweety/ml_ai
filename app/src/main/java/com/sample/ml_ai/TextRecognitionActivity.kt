package com.sample.ml_ai

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import java.io.IOException

class TextRecognitionActivity : AppCompatActivity() {

    private var preview: ImageView? = null
    private var textView: TextView? = null
    private var imageUri: Uri? = null

    private var recognizer: TextRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_vision_image)
        preview = findViewById(R.id.image_preview)
        textView = findViewById(R.id.result_text)
        recognizer = TextRecognition.getClient()
        takePhoto()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun takePhoto() {

        imageUri = null
        preview!!.setImageBitmap(null)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
            imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(
                takePictureIntent,
                REQUEST_IMAGE_CAPTURE
            )
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            tryReloadAndDetectInImage()
        }
        else  {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun tryReloadAndDetectInImage() {
        Log.d(
            TAG,
            "Try reload and detect image"
        )
        try {
            if (imageUri == null) {
                return
            }


            val imageBitmap = BitmapUtils.getBitmapFromContentUri(contentResolver, imageUri!!)
                ?: return

            val image = InputImage.fromBitmap(imageBitmap, 0)

            preview!!.setImageBitmap(imageBitmap)


            recognizer?.process(image)
                ?.addOnSuccessListener { visionText ->
                    var blockText = ""
                    for (block in visionText.textBlocks) {
                        blockText = blockText + " " + block.text

                    }
                    textView?.text = blockText + "\n"
                }
                ?.addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }


        } catch (e: IOException) {
            Log.e(
                " TAG",
                "Error retrieving saved image"
            )
            imageUri = null
        }
    }


    companion object {
        private const val TAG = "VisionActivity"
        private const val REQUEST_IMAGE_CAPTURE = 1001
    }


}