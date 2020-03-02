package me.shuza.textrecognization.helper

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.activity_main2.view.*
import me.shuza.textrecognization.utils.MySharedPref
import org.jetbrains.anko.forEachChild

class CheckedColors {
    companion object{
       public fun setColor(view: RecyclerView, context: Context){
            view.forEachChild {
                val checkedColor = MySharedPref.getChecked(context)
                    it.setBackgroundColor(Color.GREEN)
            }

        }
    }
}