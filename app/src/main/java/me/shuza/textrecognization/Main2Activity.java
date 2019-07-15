package me.shuza.textrecognization;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import me.shuza.textrecognization.adapters.DataRecyclerAdapter;
import me.shuza.textrecognization.utils.MySharedPref;

public class Main2Activity extends AppCompatActivity {
    private DataRecyclerAdapter adapter;
    private int counter;
    private TextView counterTxtView;
    private RecyclerView recyclerView;
    private Button startBtn;
    private Button fetchBtn;
    private ImageView menuItem;
    private int columnQuantities;
    private AlertDialog builder;
    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        adapter = new DataRecyclerAdapter();
        recyclerView = findViewById(R.id.recycler_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        initUI();
    }

    private void initUI() {
        startBtn = findViewById(R.id.start_btn_id);
        fetchBtn = findViewById(R.id.fetch_btn_id);
        counterTxtView = findViewById(R.id.count_id);
        menuItem = findViewById(R.id.menu_id);
        initActions();

    }

    private void initActions() {
        startBtn.setOnClickListener(v -> startChecking());
        menuItem.setOnClickListener(v -> {
            showPopupMenu(v);
        });
        fetchBtn.setOnClickListener(v -> {
            try {
                String fileName = "უტილიზაცია";
                File file = Environment.getExternalStoragePublicDirectory(fileName);
                File[] excelFiles = file.listFiles();
                if (file.isDirectory()) {
                    if (excelFiles[0].exists()) {
                        FileInputStream stream = new FileInputStream(excelFiles[0]);
                        Workbook wb = Workbook.getWorkbook(stream);
                        Sheet s = wb.getSheet(0);
                        int row = s.getRows();
                        //int col = s.getColumns();
                        String xx = "";
                        for (int i = 0; i < 50; i++) {
                            counter++;
                            for (int j = 0; j < 1; j++) {
                                int savedColumnNumber = MySharedPref.getColumnNumber(v.getContext());
                                Cell z = s.getCell(savedColumnNumber, i);
                                xx = z.getContents();
                                System.out.println(xx);
                            }
                            xx = xx + "\n";
                            Data.list.add(xx);
                        }
                    }
                } else {
                    Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
                }
                adapter.addDatas(Data.list);
                counterTxtView.setText(counter + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void startChecking() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) item -> {
            switch (item.getItemId()) {
                case R.id.column_id:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Main2Activity.this);
                    View view1 = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_dialog_layout, null, false);
                    dialog.setTitle("რიგის ნომრის შეყვანა")
                            .setView(view1);
                    EditText column = view1.findViewById(R.id.column_edit_txt_id);
                    Button saveBtn = view1.findViewById(R.id.save_btn_id);
                    cancelBtn = view1.findViewById(R.id.cancel_btn_id);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder.dismiss();
                        }
                    });
                    saveBtn.setOnClickListener(v -> {
                        String columnNumber = column.getText().toString();
                        if (!columnNumber.isEmpty()) {
                            columnQuantities = Integer.parseInt(columnNumber);
                            MySharedPref.saveColumnNumber(view.getContext(), columnQuantities);
                            builder.dismiss();
                        } else {
                            Toast.makeText(this, "ველი ცარიელია", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder = dialog.show();
                    builder.setCancelable(false);

            }
            return true;
        });

    }
}
