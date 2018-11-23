package com.naufal.androidocrlearning

import android.Manifest
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.view.View
import com.camerakit.CameraKitView
import com.google.firebase.ml.vision.FirebaseVision
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class MainActivity : AppCompatActivity() {

    val disposable = CompositeDisposable()

    private var cameraKitView: CameraKitView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        take_picture.setOnClickListener {
            take_picture.isClickable = false
            checkPermission()
        }

        cameraKitView = findViewById(R.id.camera_view)

    }

    fun checkPermission() {
        val rxPermission = RxPermissions(this)

        disposable.add(rxPermission
            .request(Manifest.permission.CAMERA)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it) {
                    captureImage()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun captureImage() {
        camera_view.captureImage { _, bytes ->
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            saveToFile(bmp as Bitmap)
            processBmp(bmp)
        }
    }

    private fun saveToFile(bmp: Bitmap) {
        val uts = System.currentTimeMillis()
        val imageFile =
            File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), uts.toString() + ".jpg")

        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error writing bitmap", e)
        }
    }

    fun processBmp(bmp: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bmp)

        val textRecognizer = FirebaseVision.getInstance()
            .onDeviceTextRecognizer

        textRecognizer.processImage(image)
            .addOnSuccessListener {
                val text = it.text
                text_result.text = text
                take_picture.isClickable = true
            }.addOnFailureListener {
                take_picture.isClickable = true
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
            }
    }

    override fun onStart() {
        super.onStart()
        cameraKitView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        cameraKitView?.onResume()
    }

    override fun onPause() {
        cameraKitView?.onPause()
        super.onPause()
    }

    override fun onStop() {
        cameraKitView?.onStop()
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraKitView?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}