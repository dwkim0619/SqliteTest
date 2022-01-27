package com.example.sqlitetest;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MyDBHelper myDBHelper;
    SQLiteDatabase sqlDB;

    String[] items = {"버그신고", "문의", "기타"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("구매 리스트 관리");

        Button btnInit = findViewById(R.id.btnInit);
        Button btnInsert = findViewById(R.id.btnInsert);
        Button btnSelect = findViewById(R.id.btnSelect);

        myDBHelper = new MyDBHelper(this);

        btnInit.setOnClickListener(v -> {
            sqlDB = myDBHelper.getWritableDatabase();
            myDBHelper.onUpgrade(sqlDB, 1, 2);
            sqlDB.close();
        });

        btnInsert.setOnClickListener(v -> {
            EditText edtName = findViewById(R.id.edtName);
            EditText edtNumber = findViewById(R.id.edtNumber);

            String strName = edtName.getText().toString();
            String strNumber = edtNumber.getText().toString();

            sqlDB = myDBHelper.getWritableDatabase();
            sqlDB.execSQL("INSERT INTO groupTBL VALUES('" + strName + "', '" + strNumber + "')");
            sqlDB.close();

            Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();
        });

        btnSelect.setOnClickListener(v -> {
            String strNames = "목록 리스트\r\n\r\n";
            String strNumbers = "수량\r\n\r\n";

            sqlDB = myDBHelper.getWritableDatabase();
            Cursor cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);

            while(cursor.moveToNext()) {
                strNames += cursor.getString(0) + "\r\n";
                strNumbers+= cursor.getString(1) + "\r\n";
            }
            cursor.close();
            sqlDB.close();

            EditText edtNameResult = findViewById(R.id.edtNameResult);
            EditText edtNumberResult = findViewById(R.id.edtNumberResult);
            edtNameResult.setText(strNames);
            edtNumberResult.setText(strNumbers);
        });

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), items[position], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "선택되지 않음", Toast.LENGTH_LONG).show();
            }
        });

    }
}