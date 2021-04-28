package com.sample.ml_ai

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Pair
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class VisionActivity : AppCompatActivity() {

    private var preview: ImageView? = null
    private var textView: TextView? = null
    private var labeler : ImageLabeler? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_vision_image)
        preview = findViewById(R.id.image_preview)
        textView = findViewById(R.id.result_text)

        val localModel = LocalModel.Builder()
            .setAssetFilePath("flowers_model.tflite")
            // or .setAbsoluteManifestFilePath(absolute file path to manifest file)
            .build()

        val customImageLabelerOptions = CustomImageLabelerOptions.Builder(localModel)
            .setConfidenceThreshold(0.5f)
            .setMaxResultCount(5)
            .build()
        labeler = ImageLabeling.getClient(customImageLabelerOptions)
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


            labeler?.process(image)
                ?.addOnSuccessListener { labels ->
                    // Task completed successfully
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        textView?.text = "Label: $label.text\n Confidence Level: $label.confidence\n"
                    }
                }
                ?.addOnFailureListener { e ->
                    // Task failed with an exception
                    Log.e(
                        " TAG",
                        "Failed to get vision image"
                    )
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

