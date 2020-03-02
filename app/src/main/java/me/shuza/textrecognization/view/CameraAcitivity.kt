package me.shuza.textrecognization.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_camera_acitivity.*
import kotlinx.android.synthetic.main.camera_layout.*
import me.shuza.textrecognization.R
import me.shuza.textrecognization.inter.ICheckColor
import me.shuza.textrecognization.model.Model
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import kotlin.properties.Delegates

class CameraAcitivity : AppCompatActivity() {
    private var mCameraSource by Delegates.notNull<CameraSource>()
    private var textRecognizer by Delegates.notNull<TextRecognizer>()
    private var checkedItemsCounter = 0
    var checkedColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_acitivity)
        startCameraSource()
        delete.setOnClickListener{
            Data.counterList.clear()
            checked_counter.text = Data.counterList.size.toString()
        }
    }

    private fun startCameraSource() {
        //  Create text Recognizer
        textRecognizer = TextRecognizer.Builder(this).build()
        if (!textRecognizer.isOperational) {
            toast("Dependencies are not loaded yet...please try after few moment!!")
            Logger.d("Dependencies are downloading....try after few moment")
            return
        }

        //  Init camera source to use high resolution and auto focus
        mCameraSource = CameraSource.Builder(this, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setAutoFocusEnabled(true)
                .setRequestedFps(2.0f)
                .build()

        surface_camera_preview.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

            }

            override fun surfaceDestroyed(p0: SurfaceHolder?) {
                mCameraSource.stop()
            }

            @SuppressLint("MissingPermission")
            override fun surfaceCreated(p0: SurfaceHolder?) {
                try {
                    mCameraSource.start(surface_camera_preview.holder)
                } catch (e: Exception) {
                    toast("Error:  ${e.message}")
                }
            }
        })

        textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
            override fun release() {}
            override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
                val items = detections.detectedItems

                if (items.size() <= 0) {
                    return
                }
                tv_result_id.post {
                    for (i in 0 until items.size()) {
                        val list: ArrayList<String> = ArrayList()
                        var item = items.valueAt(i)
                        var model = Model()

                        if (item.value.length == 9 ) {
                            var itemValue = isText(item.value)
                            if (Data.dataList.contains(itemValue)) {
                                tv_result_id.setTextColor(Color.GREEN)
//                                model.name = item.value
////                                model.checked = true
                                if (Data.counterList.size == 0) {
                                    Data.counterList.add(itemValue)
                                    checked_counter.text = Data.counterList.size.toString()
                                    Log.e("TAG", "პირველი შესვლა-> ${Data.counterList.size}")
                                } else {
                                    if (!(Data.counterList.contains(itemValue))) {
                                        Data.counterList.add(itemValue)
                                        checked_counter.text = Data.counterList.size.toString()
                                        Log.e("TAG", "shevida-> ${Data.counterList.size}")
                                    } else {
                                        println("contains")
                                        Log.e("TAG", "ar shevida -> ${Data.counterList.size}")
                                    }
                                }


                            } else {
                                tv_result_id.setTextColor(Color.RED)
                            }
                            tv_result_id.text = itemValue
                        }
                    }
                }
            }
        })
    }

    private fun isNumber(str: String): Boolean {
        var isNumber = false
        str.forEach {
            if (it in '0'..'9') {
                isNumber = true
            } else {
                isNumber = false
                return false
            }
        }
        return isNumber
    }

    fun isText(string: String): String {
        val index0 = string.get(0)
        val index1 = string.get(1)
        var element = ""
        var one = ""
        for (i in string.subSequence(2, 9)){
            one = i.toString()
            if(i == '0' || i =='o' || i == 'O'){
                one = '0'.toString()
            }
            element += one
        }
        var all = index0.toString() + index1.toString() + element
        return all
    }

//    var checkListener: ICheckColor? = null
////    fun setChecked(listener: ICheckColor){
////        this.checkListener = listener
////    }
//
//    var closeListener: ICloseCamera? = null
//    interface ICloseCamera{
//        fun closeCamera()
//    }

}
