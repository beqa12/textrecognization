//package me.shuza.textrecognization
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.pm.PackageManager
//import android.graphics.Color
//import android.os.Bundle
//import android.support.v4.app.ActivityCompat
//import android.support.v7.app.AppCompatActivity
//import android.view.SurfaceHolder
//import com.google.android.gms.vision.CameraSource
//import com.google.android.gms.vision.Detector
//import com.google.android.gms.vision.text.TextBlock
//import com.google.android.gms.vision.text.TextRecognizer
//import com.orhanobut.logger.Logger
//import kotlinx.android.synthetic.main.activity_main.*
//import me.shuza.textrecognization.helper.CheckedColors.Companion.setColor
//import me.shuza.textrecognization.inter.ICheckColor
//import me.shuza.textrecognization.utils.MySharedPref
//import org.jetbrains.anko.toast
//import kotlin.properties.Delegates
//
//
//class MainActivity : AppCompatActivity() {
//
//    private var mCameraSource by Delegates.notNull<CameraSource>()
//    private var textRecognizer by Delegates.notNull<TextRecognizer>()
//
//    private val PERMISSION_REQUEST_CAMERA = 100
//    private var checkedItemsCounter = 0
//    var checkedColor = 0
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        startCameraSource()
//    }
//
//    private fun startCameraSource() {
//
//        //  Create text Recognizer
//        textRecognizer = TextRecognizer.Builder(this).build()
//
//        if (!textRecognizer.isOperational) {
//            toast("Dependencies are not loaded yet...please try after few moment!!")
//            Logger.d("Dependencies are downloading....try after few moment")
//            return
//        }
//
//        //  Init camera source to use high resolution and auto focus
//        mCameraSource = CameraSource.Builder(applicationContext, textRecognizer)
//                .setFacing(CameraSource.CAMERA_FACING_BACK)
//                .setRequestedPreviewSize(1280, 1024)
//                .setAutoFocusEnabled(true)
//                .setRequestedFps(2.0f)
//                .build()
//
//        surface_camera_preview.holder.addCallback(object : SurfaceHolder.Callback {
//            override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun surfaceDestroyed(p0: SurfaceHolder?) {
//                mCameraSource.stop()
//            }
//
//            @SuppressLint("MissingPermission")
//            override fun surfaceCreated(p0: SurfaceHolder?) {
//                try {
//                    if (isCameraPermissionGranted()) {
//                        mCameraSource.start(surface_camera_preview.holder)
//                    } else {
//                        requestForPermission()
//                    }
//                } catch (e: Exception) {
//                    toast("Error:  ${e.message}")
//                }
//            }
//        })
//
//        textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
//            override fun release() {}
//
//            override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
//                val items = detections.detectedItems
//
//                if (items.size() <= 0) {
//                    return
//                }
//
//                tv_result_id.post {
//                    for (i in 0 until items.size()) {
//                        val item = items.valueAt(i)
//                        if (item.value.length == 9) {
//                            if (Data.list.contains(item.value)) {
//                                tv_result_id.setTextColor(Color.GREEN)
//                                checkedColor  = 1
//                                MySharedPref.saveChecked(this@MainActivity, checkedColor)
//                                checkedColor = 0
//                            } else {
//                                tv_result_id.setTextColor(Color.RED)
//                            }
//                            tv_result_id.text = item.value
//                        }
//                    }
//                }
//            }
//        })
//    }
//
//    private fun isNumber(str: String): Boolean {
//        var isNumber = false
//        str.forEach {
//            if (it in '0'..'9') {
//                isNumber = true
//            } else {
//                isNumber = false
//                return false
//            }
//        }
//        return isNumber
//    }
//
//    fun isText(str: String): Boolean {
//        var isText = false
//        val str = str.toCharArray()
//        if (str[0] in 'a'..'z'.toUpperCase()) {
//            isText = true
//        } else {
//            isText = false
//            return false
//
//        }
//        return isText
//    }
//
//    fun isCameraPermissionGranted(): Boolean {
//        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
//                PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestForPermission() {
//        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
//    }
//
//    @SuppressLint("MissingPermission")
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        if (requestCode != PERMISSION_REQUEST_CAMERA) {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//            return
//        }
//
//        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            if (isCameraPermissionGranted()) {
//                mCameraSource.start(surface_camera_preview.holder)
//            } else {
//                toast("Permission need to grant")
//                finish()
//            }
//        }
//    }
//
//}
