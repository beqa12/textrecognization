//package me.shuza.textrecognization.adapters;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import me.shuza.textrecognization.Data;
//import me.shuza.textrecognization.R;
//import me.shuza.textrecognization.utils.MySharedPref;
//
//public class DataRecyclerAdapter extends RecyclerView.Adapter<DataRecyclerAdapter.DataHolder> {
//    private Context context;
//    public DataRecyclerAdapter(Context con){
//        this.context = con;
//    }
//    List<String> dataList = new ArrayList<>();
//    @NonNull
//    @Override
//    public DataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_list_layout, viewGroup, false);
//        return new DataHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull DataHolder dataHolder, int i) {
//        String data =  dataList.get(i);
//        dataHolder.setData(data);
//    }
//
//    @Override
//    public int getItemCount() {
//        return dataList.size();
//    }
//    public void addDatas(List<String> data){
//        dataList.clear();
//        dataList.addAll(data);
//        notifyDataSetChanged();
//    }
//
//    class DataHolder extends RecyclerView.ViewHolder {
//        private TextView dataName;
//        public DataHolder(@NonNull View itemView) {
//            super(itemView);
//            dataName = itemView.findViewById(R.id.data_name_id);
//        }
//        private void setData(String data){
//            dataName.setText(data);
//            if (MySharedPref.getChecked(context) == 1){
//
//            }
//        }
//    }
//}
