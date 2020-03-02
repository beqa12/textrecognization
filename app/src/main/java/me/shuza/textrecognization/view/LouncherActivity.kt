package me.shuza.textrecognization.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.widget.Toast
import me.shuza.textrecognization.R
import me.shuza.textrecognization.inter.ICheckColor
import me.shuza.textrecognization.model.Model


class LouncherActivity : AppCompatActivity(), ICheckColor {

    private var writeExternalStorage = -1
    private var readExternalStorage = -1
    private var camera = -1
    private var STORAGE_REQUEST_CODE = 10
    private var builder: AlertDialog? = null
    private var listFragment: me.shuza.textrecognization.fragments.ListFragment? = null
//    private var camerFragmetn: CameraFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_louncher)
        listFragment = me.shuza.textrecognization.fragments.ListFragment.getInstance()
//        camerFragmetn = CameraFragment.getInstance()
        checkPermission()

    }

    private fun checkPermission() {
        writeExternalStorage = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        readExternalStorage = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        camera = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (writeExternalStorage == PackageManager.PERMISSION_GRANTED && readExternalStorage == PackageManager.PERMISSION_GRANTED
                && camera == PackageManager.PERMISSION_GRANTED) {
            addFragments()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA), STORAGE_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 10) {
            addFragments()
        } else {
            showdeniedAlert(this)
        }
    }

//    override fun onBackPressed() {
//        val count = supportFragmentManager.backStackEntryCount
//        if (count == 0) {
//            super.onBackPressed()
//            //additional code
//        } else {
//           val current = supportFragmentManager.findFragmentById(R.id.frame_contaoner)
//            if (current is CameraFragment){
//                supportFragmentManager
//                        .beginTransaction()
//                        .remove(current)
//                        .commit()
//            }
//            supportFragmentManager.popBackStack()
//        }
//
//    }

    private fun showdeniedAlert(context: Context) {
        val dialog = AlertDialog.Builder(this)
        val deniedView = LayoutInflater.from(context).inflate(R.layout.denied, null, false)
        dialog.setTitle("W A R N I N G ! ! !")
                .setView(deniedView)
        val okBtn: AppCompatTextView = deniedView.findViewById(R.id.ok)
        okBtn.setOnClickListener { builder?.dismiss() }
        builder = dialog.show()
        builder?.setCancelable(false)
    }

    private fun addFragments() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
                .replace(R.id.frame_contaoner, listFragment!!)
                .commit()
    }

//    override fun addcameraFragment() {
//        supportFragmentManager
//                .beginTransaction()
//                .add(R.id.frame_contaoner, camerFragmetn!!)
//                .commit()
//    }

    override fun checkColor(model: Model) {
        Toast.makeText(this, "Movida", Toast.LENGTH_LONG).show()
        listFragment?.models?.add(model)
        listFragment?.modelAdapter?.notifyDataSetChanged()
    }

//    override fun closeCamera() {
//        val current = supportFragmentManager.findFragmentById(R.id.frame_contaoner)
//        if (current is CameraFragment){
//            supportFragmentManager
//                    .beginTransaction()
//                    .remove(current)
//                    .commit()
//        }
//    }
}
