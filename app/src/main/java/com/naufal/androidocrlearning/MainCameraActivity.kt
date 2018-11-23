package com.naufal.androidocrlearning

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.camerakit.CameraKitView
import java.io.File
import java.io.FileOutputStream
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import kotlinx.android.synthetic.main.activity_main_camera.*
import java.io.OutputStream


class MainCameraActivity : AppCompatActivity() {

    val disposable = CompositeDisposable()

    private var cameraKitView: CameraKitView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_camera)

        cameraKitView = findViewById(R.id.camera_view_box1)

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

                    take_picture_box.setOnClickListener {
                        Toast.makeText(this, "Berhasil menyimpan", Toast.LENGTH_LONG).show()
                        captureImage()
                    }

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun captureImage() {
//        val layoutParams = RelativeLayout.LayoutParams(dpToPx(300), dpToPx(180))
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
//        cameraKitView?.layoutParams =
//                RelativeLayout.LayoutParams(layoutParams)
        cameraKitView?.captureImage { _, bytes ->
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            processBmp(cropBitmap(bmp))
        }
    }

    fun dpToPx(dp: Int): Int {
        val density = this.resources
            .displayMetrics
            .density
        return Math.round(dp.toFloat() * density)
    }

    fun cropBitmap(srcBmp: Bitmap): Bitmap {

        var dstBmp: Bitmap

        if (srcBmp.width >= srcBmp.height) {

            val panjang = dpToPx(180)
            val lebar = dpToPx(300)

            var x = srcBmp.height / 2 - (panjang / 2)
            var y = srcBmp.width / 2 - (lebar / 2)

            Log.v("ukuran", "" + srcBmp.height + ", " + srcBmp.width + ", " + panjang+ ", " + lebar)

            dstBmp = Bitmap.createBitmap(
                srcBmp,
                x,
                y,
                lebar,
                panjang
            )

//            dstBmp = Bitmap.createBitmap(
//                srcBmp,
//                srcBmp.width / 2 - srcBmp.height / 2,
//                0,
//                srcBmp.height,
//                srcBmp.height
//            )


        } else {

            val lebar= dpToPx(300)
            val panjang = dpToPx(187)

            var y = srcBmp.height / 2 - (panjang / 2)
            var x = srcBmp.width / 2 - (lebar / 2)

            Log.v("ukuran", "" + srcBmp.height + ", " + srcBmp.width + ", " + panjang+ ", " + lebar)

            Log.v("ukuran xy", "$x, $y")

            dstBmp = Bitmap.createBitmap(
                srcBmp,
                x,
                y,
                lebar,
                panjang
            )

//            dstBmp = Bitmap.createBitmap(
//                srcBmp,
//                0,
//                srcBmp.height / 2 - srcBmp.width / 2,
//                srcBmp.width,
//                srcBmp.width
//            )
        }

        return dstBmp
    }

    fun pxToDp(dp: Int): Float {
        val r = getResources();
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            r.getDisplayMetrics()
        )

        return px
    }

    fun processBmp(bmp: Bitmap) {
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