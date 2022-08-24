package me.shuza.textrecognization.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_camera_acitivity.*
import me.shuza.textrecognization.R
import me.shuza.textrecognization.model.Model
import me.shuza.textrecognization.utils.ApiConstants
import me.shuza.textrecognization.utils.MySharedPref
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
//        Data.testList.add("623405")
//        Data.testList.add("0022299")
//        Data.testList.add("0011040")

        startCameraSource()
        delete.setOnClickListener {
            Data.counterList.clear()
            checked_counter.text = Data.counterList.size.toString()
        }
    }

    private fun startCameraSource() {
        //  Create text Recognizer
        textRecognizer = TextRecognizer.Builder(this)
                .build()
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
                        var item = items.valueAt(i)

                        val checkedRadioBtn = MySharedPref.getCheckedRadioBtn(this@CameraAcitivity)
                        when (checkedRadioBtn) {
                            ApiConstants.PERSONAL_IDENTITY -> {
                                checkPersonalIdentity(item)
                            }
                            ApiConstants.DRIVING_LICENCE -> {
                                checkDrivingLicence(item)
                                Log.e("TAG", "checked: martvis mowmobebi")
                            }
                            ApiConstants.PASPORTS -> {
                                Log.e("TAG", "checked: checked: passports")
                            }
                            ApiConstants.OTHER -> {
                                checkOther(item)
                            }
                            ApiConstants.NONE -> {
                                Log.e("TAG", "აირჩიეთ ტიპი")
                            }
                            else -> {
                                Log.e("TAG", "ar vici ra xdeba")
                            }
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

    private fun checkForTextPersonalIdentity(value: String): Boolean{
        var result = false
        val index2 = value[2]
        val index3 = value[3]
        if (!index2.isDigit() && !index3.isDigit()){
            Log.e("TAG", "is text ->>>>>>>>>>>>>>>>> " + index2.toString() + index3)
            result = true
        }
        return result
    }
    private fun checkForNumbersPersonalIdentity(value: String): Boolean{
        var result = false
        val index0= value[0]
        val index1 = value[1]
        val index2 = value[2]
        val index3 = value[3]
        val index4 = value[4]
        val index5 = value[5]
        val index6 = value[6]
        val index7 = value[7]
        val index8 = value[8]
//        val numbers = index0.toString() + index1 + index4 + index5 + index6 + index7 + index8
        if (index0.isDigit() && index1.isDigit() && !index2.isDigit() && !index3.isDigit() && index4.isDigit()
                && index5.isDigit() && index6.isDigit() && index7.isDigit() && index8.isDigit()){
            result = true
        }
        return result
    }

    fun isText(string: String): String {
        val index0 = string.get(0)
        val index1 = string.get(1)
        var element = ""
        var one = ""
        for (i in string.subSequence(2, 9)) {
            one = i.toString()
            if (i == '0' || i == 'o' || i == 'O') {
                one = '0'.toString()
            }
            element += one
        }
        val all = index0.toString() + index1.toString() + element
        return all
    }

    private fun isTextForOther(string: String): String {
        val endIndex = if (string.length == 9) 9 else 8
        return string.subSequence(2, endIndex).toString()
    }

    private fun checkPersonalIdentity(item: TextBlock) {
        if (item.value.length == 9) {
            var isNumber = checkForNumbersPersonalIdentity(item.value)
            if (isNumber){
                Log.e("TAG", item.value)
                if (Data.dataList.contains(item.value)) {
                    tv_result_id.setTextColor(Color.GREEN)
                    if (Data.counterList.size == 0) {
                        Data.counterList.add(item.value)
                        checked_counter.text = Data.counterList.size.toString()
                        Log.e("TAG", "პირველი შესვლა-> ${Data.counterList.size}")
                    } else {
                        if (!(Data.counterList.contains(item.value))) {
                            Data.counterList.add(item.value)
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
                tv_result_id.text = item.value
            }
        }
    }


    fun checkDrivingLicence(item: TextBlock) {
        if (item.value.length == 9) {
            var itemValue = isText(item.value)
            if (Data.dataList.contains(itemValue)) {
                tv_result_id.setTextColor(Color.GREEN)
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

    fun checkOther(item: TextBlock) {
        if (item.value.length == 9 || item.value.length == 8) {
            val itemValue = isTextForOther(item.value)
            if (Data.dataList.contains(itemValue)) {
                tv_result_id.setTextColor(Color.GREEN)
                if (Data.counterList.size == 0) {
                    Data.counterList.add(itemValue)
                    checked_counter.text = Data.counterList.size.toString()
                } else {
                    if (!(Data.counterList.contains(itemValue))) {
                        Data.counterList.add(itemValue)
                        checked_counter.text = Data.counterList.size.toString()
                    } else {
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
