package com.naufal.androidocrlearning

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.camerakit.CameraKitView
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wonderkiln.camerakit.CameraView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_camera_view_kit.*
import kotlinx.android.synthetic.main.activity_main_camera.*

class CameraViewKit : AppCompatActivity() {

    val disposable : CompositeDisposable = CompositeDisposable()

    var ckv : CameraView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_view_kit)

        ckv = findViewById(R.id.camera_wunder)

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

                    take_picture_2.setOnClickListener {
                        Toast.makeText(this, "Berhasil menyimpan", Toast.LENGTH_LONG).show()
                        captureImage()
                    }

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun captureImage() {

        ckv?.captureImage()
    }

    override fun onResume() {
        super.onResume()
        ckv?.start()
    }

    override fun onPause() {
        ckv?.stop()
        super.onPause()
    }

}
