//package me.shuza.textrecognization;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.AppCompatTextView;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.io.File;
//import java.io.FileInputStream;
//
//import jxl.Cell;
//import jxl.Sheet;
//import jxl.Workbook;
//import me.shuza.textrecognization.helper.CheckedColors;
//import me.shuza.textrecognization.inter.ICheckColor;
//import me.shuza.textrecognization.utils.MySharedPref;
//
//public class Main2Activity extends AppCompatActivity {
////    private DataRecyclerAdapter adapter;
////    private int counter;
////    private TextView counterTxtView;
////     RecyclerView recyclerView;
////    private AppCompatTextView startBtn;
////    private AppCompatTextView fetchBtn;
////    private ImageView menuItem;
//    private int columnQuantities;
//    private AlertDialog builder;
////    private Button cancelBtn;
////    private int STORAGE_REQUEST_CODE = 10;
//    private int writeExternalStorage;
//    private int readExternalStorage;
////    private MainActivity mainActivity;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
////        adapter = new DataRecyclerAdapter(this);
////        recyclerView = findViewById(R.id.recycler_id);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////        recyclerView.setAdapter(adapter);
//        checkPermission();
//    }
//
//    private void initUI() {
////        startBtn = findViewById(R.id.start_btn_id);
////        fetchBtn = findViewById(R.id.fetch_btn_id);
////        counterTxtView = findViewById(R.id.count_id);
////        menuItem = findViewById(R.id.menu_id);
//        initActions();
//    }
//
//    private void checkPermission(){
//        writeExternalStorage = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        readExternalStorage = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//        if (writeExternalStorage == PackageManager.PERMISSION_GRANTED && readExternalStorage == PackageManager.PERMISSION_GRANTED){
//            initUI();
//        }else {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 10){
//            initUI();
//        }else {
//            showdeniedAlert(this);
//        }
//    }
//
//    private void showdeniedAlert(Context context) {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(Main2Activity.this);
//        View deniedView = LayoutInflater.from(context).inflate(R.layout.denied, null, false);
//        dialog.setTitle("W A R N I N G ! ! !")
//                .setView(deniedView);
//        AppCompatTextView okBtn = deniedView.findViewById(R.id.ok);
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                builder.dismiss();
//            }
//        });
//        builder = dialog.show();
//        builder.setCancelable(false);
//    }
//    private void initActions() {
//        startBtn.setOnClickListener(v -> startChecking());
//        menuItem.setOnClickListener(v -> {
//            showPopupMenu(v);
//        });
//        fetchBtn.setOnClickListener(v -> {
//            try {
//                if (MySharedPref.getColumnNumber(v.getContext()) < 0){
//                    Toast.makeText(this, "შეიყვანე ქოლუმნის ნომერი მენიუ ღილაკზე დაჭერისას",
//                            Toast.LENGTH_LONG).show();
//                }else {
//                    String fileName = "უტილიზაცია";
//                    File file = Environment.getExternalStoragePublicDirectory(fileName);
//                    File[] excelFiles = file.listFiles();
//                    if (file.isDirectory()) {
//                        if (excelFiles[0].exists()) {
//                            FileInputStream stream = new FileInputStream(excelFiles[0]);
//                            Workbook wb = Workbook.getWorkbook(stream);
//                            Sheet s = wb.getSheet(0);
//                            int row = s.getRows();
//                            //int col = s.getColumns();
//                            String xx = "";
//                            for (int i = 0; i < row; i++) {
//                                counter++;
//                                for (int j = 0; j < 1; j++) {
//                                    int savedColumnNumber = MySharedPref.getColumnNumber(v.getContext());
//                                    Cell z = s.getCell(savedColumnNumber, i);
//                                    xx = z.getContents();
//                                    System.out.println(xx);
//                                }
//                                Data.list.add(xx);
//                            }
//                        }
//                    } else {
//                        Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
//                    }
//                    adapter.addDatas(Data.list);
//                    counterTxtView.setText(counter + "");
////                    counter = 0;
////                    Data.list.clear();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    private void startChecking() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }
//
//    private void showPopupMenu(View view) {
//        PopupMenu popupMenu = new PopupMenu(this, view);
//        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
//        popupMenu.show();
//        popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) item -> {
//            switch (item.getItemId()) {
//                case R.id.column_id:
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(Main2Activity.this);
//                    View view1 = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_dialog_layout, null, false);
//                    dialog.setTitle("რიგის ნომრის შეყვანა")
//                            .setView(view1);
//                    EditText column = view1.findViewById(R.id.column_edit_txt_id);
//                    Button saveBtn = view1.findViewById(R.id.save_btn_id);
//                    cancelBtn = view1.findViewById(R.id.cancel_btn_id);
//                    cancelBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            builder.dismiss();
//                        }
//                    });
//                    saveBtn.setOnClickListener(v -> {
//                        String columnNumber = column.getText().toString();
//                        if (!columnNumber.isEmpty()) {
//                            columnQuantities = Integer.parseInt(columnNumber);
//                            MySharedPref.saveColumnNumber(view.getContext(), columnQuantities);
//                            builder.dismiss();
//                        } else {
//                            Toast.makeText(this, "ველი ცარიელია", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    builder = dialog.show();
//                    builder.setCancelable(false);
//
//            }
//            return true;
//        });
//
//    }
//
//}
