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
import android.view.View
import com.camerakit.CameraKitView
import com.google.firebase.ml.vision.FirebaseVision
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import android.R.attr.bitmap
import android.R.attr.name
import android.os.Environment
import android.support.v7.widget.LinearLayoutCompat
import android.util.Log
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main_camera.*
import java.io.OutputStream


class GeniusCamera : AppCompatActivity() {

    val disposable = CompositeDisposable()

    private var cameraKitView: CameraKitView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genius_camera)

        cameraKitView = findViewById(R.id.camera_genius)

        val box = Box(this)

        addContentView(box,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )

        checkPermission()
    }

    fun checkPermission() {
        val rxPermission = RxPermissions(this)

        disposable.add(rxPermission
            .request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it) {

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            })
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