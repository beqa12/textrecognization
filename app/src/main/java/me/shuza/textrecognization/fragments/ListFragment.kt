package me.shuza.textrecognization.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import jxl.Workbook
import kotlinx.android.synthetic.main.alert_dialog_layout.view.*
import kotlinx.android.synthetic.main.list_layout.view.*
import me.shuza.textrecognization.R
import me.shuza.textrecognization.adapters.ModelRecyclerAdapter
import me.shuza.textrecognization.inter.ICheckColor
import me.shuza.textrecognization.model.Model
import me.shuza.textrecognization.utils.MySharedPref
import me.shuza.textrecognization.view.CameraAcitivity
import me.shuza.textrecognization.view.Data
import me.shuza.textrecognization.view.IFragment
import java.io.FileInputStream

class ListFragment : Fragment() {
    private var builder: AlertDialog? = null
    private var columnQuantities = 0
    private var counter = 0
    var models: ArrayList<Model> = ArrayList()
    private var model: Model? = null
    var modelAdapter: ModelRecyclerAdapter? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
//        listener = context as? IFragment
    }

    override fun onDetach() {
        super.onDetach()
//        listener = null
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(container?.context).inflate(R.layout.list_layout, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        view.menu_id.setOnClickListener {
            showPopupMenu(it)
        }
        view.start_btn_id.setOnClickListener {
            val intent = Intent(context, CameraAcitivity::class.java)
            startActivity(intent)
        }
        view.fetch_btn_id.setOnClickListener {
            try {
                models.clear()
                counter = 0
                if (MySharedPref.getColumnNumber(context) < 0) {
                    Toast.makeText(context, "შეიყვანე ქოლუმნის ნომერი მენიუ ღილაკზე დაჭერისას",
                            Toast.LENGTH_LONG).show()
                } else {
                    val fileName = "უტილიზაცია"
                    val file = Environment.getExternalStoragePublicDirectory(fileName)
                    val excelFiles = file.listFiles()
                    if (file.isDirectory) {
                        if (excelFiles[0].exists()) {
                            val stream = FileInputStream(excelFiles[0])
                            val wb = Workbook.getWorkbook(stream)
                            val s = wb.getSheet(0)
                            val row = s.rows
                            //int col = s.getColumns();
                            var xx = ""
                            for (i in 0 until row) {
                                counter++
                                for (j in 0..0) {
                                    val savedColumnNumber = MySharedPref.getColumnNumber(context)
                                    val z = s.getCell(savedColumnNumber, i)
                                    model = Model(z.contents)
                                    xx = z.contents
                                    println(xx)
                                }
                                models.add(model!!)
                                Data.dataList.add(xx)
                            }
                        }
                    } else {
                        Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show()
                    }
                    modelAdapter?.addModels(models)
                    view.count_id.setText(counter.toString() + "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        fun getInstance(): ListFragment {
            return ListFragment()
        }
    }
    private fun initUI(view: View){
        modelAdapter = ModelRecyclerAdapter()
        view.recycler_id.adapter = modelAdapter
        view.recycler_id.layoutManager = LinearLayoutManager(context)
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.column_id -> {
                    val dialog = AlertDialog.Builder(context!!)
                    val view1 = LayoutInflater.from(view.context).inflate(R.layout.alert_dialog_layout, null, false)
                    dialog.setTitle("რიგის ნომრის შეყვანა")
                            .setView(view1)
                    val column = view1.findViewById<EditText>(R.id.column_edit_txt_id)
                    val saveBtn = view1.findViewById<Button>(R.id.save_btn_id)
                    view1.cancel_btn_id.setOnClickListener(View.OnClickListener { builder?.dismiss() })
                    saveBtn.setOnClickListener { v: View? ->
                        val columnNumber = column.text.toString()
                        if (!columnNumber.isEmpty()) {
                            columnQuantities = columnNumber.toInt()
                            MySharedPref.saveColumnNumber(view.context, columnQuantities)
                            builder?.dismiss()
                        } else {
                            Toast.makeText(context, "ველი ცარიელია", Toast.LENGTH_SHORT).show()
                        }
                    }
                    builder = dialog.show()
                    builder?.setCancelable(false)
                }
            }
            true
        })
    }

//    var listener: IFragment? = null
//    override fun checkColor(model: Model) {
//        Toast.makeText(context, "Movida", Toast.LENGTH_LONG).show()
//        models.add(model)
//        modelAdapter?.addModels(models)
//    }
}