//package me.shuza.textrecognization.fragments
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.pm.PackageManager
//import android.graphics.Color
//import android.os.Bundle
//import android.support.v4.app.ActivityCompat
//import android.support.v4.app.Fragment
//import android.util.SparseArray
//import android.view.LayoutInflater
//import android.view.SurfaceHolder
//import android.view.View
//import android.view.ViewGroup
//import com.google.android.gms.vision.CameraSource
//import com.google.android.gms.vision.Detector
//import com.google.android.gms.vision.text.TextBlock
//import com.google.android.gms.vision.text.TextRecognizer
//import com.orhanobut.logger.Logger
//import kotlinx.android.synthetic.main.camera_layout.*
//import kotlinx.android.synthetic.main.camera_layout.view.*
//import me.shuza.textrecognization.R
//import me.shuza.textrecognization.adapters.ModelRecyclerAdapter
//import me.shuza.textrecognization.inter.ICheckColor
//import me.shuza.textrecognization.model.Model
//import me.shuza.textrecognization.utils.MySharedPref
//import me.shuza.textrecognization.view.Data
//import org.jetbrains.anko.support.v4.toast
//import kotlin.properties.Delegates
//
//class CameraFragment : Fragment() {
//    private var mCameraSource by Delegates.notNull<CameraSource>()
//    private var textRecognizer by Delegates.notNull<TextRecognizer>()
//    private val PERMISSION_REQUEST_CAMERA = 100
//    private var checkedItemsCounter = 0
//    var checkedColor = 0
//    var items: SparseArray<TextBlock>? = null
//    companion object{
//        fun getInstance(): CameraFragment{
//            return CameraFragment()
//        }
//    }
//
//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//        checkListener = context as? ICheckColor
//        closeListener = context as? ICloseCamera
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        checkListener = null
//        closeListener = null
//    }
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = LayoutInflater.from(context).inflate(R.layout.camera_layout, container, false)
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        view.back.setOnClickListener{
//            closeListener?.closeCamera()
//        }
//        startCameraSource()
//    }
//
//    private fun startCameraSource() {
//        //  Create text Recognizer
//        textRecognizer = TextRecognizer.Builder(context).build()
//        if (!textRecognizer.isOperational) {
//            toast("Dependencies are not loaded yet...please try after few moment!!")
//            Logger.d("Dependencies are downloading....try after few moment")
//            return
//        }
//
//        //  Init camera source to use high resolution and auto focus
//        mCameraSource = CameraSource.Builder(context, textRecognizer)
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
//                    mCameraSource.start(surface_camera_preview.holder)
//                } catch (e: Exception) {
//                    toast("Error:  ${e.message}")
//                }
//            }
//        })
//
//        textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
//            override fun release() {}
//            override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
//                items = detections.detectedItems
//
//                if (items?.size()!! <= 0) {
//                    return
//                }
//                tv_result_id.post {
//                    for (i in 0 until items!!.size()) {
//                        val item = items!!.valueAt(i)
//                        var model = Model()
//                        if (item.value.length == 9) {
//                                if (Data.dataList.contains(item.value)) {
//                                    tv_result_id.setTextColor(Color.GREEN)
//                                    model.name = item.value
//                                    model.checked = true
//                                    checkListener?.checkColor(model)
//                                } else {
//                                    tv_result_id.setTextColor(Color.RED)
//                                }
//                                tv_result_id.text = item.value
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
//    var checkListener: ICheckColor? = null
////    fun setChecked(listener: ICheckColor){
////        this.checkListener = listener
////    }
//
//    var closeListener: ICloseCamera? = null
//    interface ICloseCamera{
//        fun closeCamera()
//    }
//}