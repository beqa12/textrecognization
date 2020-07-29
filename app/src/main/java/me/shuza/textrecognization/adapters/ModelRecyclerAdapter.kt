package me.shuza.textrecognization.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.data_list_layout.view.*
import me.shuza.textrecognization.R
import me.shuza.textrecognization.model.Model

class ModelRecyclerAdapter : RecyclerView.Adapter<ModelRecyclerAdapter.ModelHolder>() {
    var modelList: ArrayList<Model> = ArrayList()


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ModelHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.data_list_layout, p0, false)
        return ModelHolder(view)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    override fun onBindViewHolder(modelHolder: ModelHolder, position: Int) {
        val model = modelList.get(position)
        modelHolder.setData(model)
//        if (model.checked){
//            modelHolder.itemView.setBackgroundColor(Color.GREEN)
//        }else{
//            modelHolder.itemView.setBackgroundColor(Color.RED)
//        }
    }

    fun addModels(models: List<Model>) {
        modelList.clear()
        modelList.addAll(models)
        notifyDataSetChanged()
    }

    inner class ModelHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun setData(model: Model) {
            itemView.data_name_id.text = model.name
        }
    }

}