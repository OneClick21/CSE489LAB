package edu.ewubd.cse489lab;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName, etCost, etDate;

    private Button btnSave, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItemName = findViewById(R.id.etItemName);
        etCost = findViewById(R.id.etCost);
        etDate = findViewById(R.id.etDate);

        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = etItemName.getText().toString().trim();
                String cost = etCost.getText().toString().trim();
                String date = etDate.getText().toString().trim();

                // Data validation

                // Split Date
                int VALUEOFYEAR = 2024;
                int VALUEOFMONTH = 12;
                int VALUEOFDATE = 10;

                // Store valid data

                Calendar currentCal = Calendar.getInstance();
                long currentTime = currentCal.getTimeInMillis();
                currentCal.setTimeInMillis(0);
                currentCal.set(Calendar.YEAR, VALUEOFYEAR);
                currentCal.set(Calendar.MONTH, VALUEOFMONTH);
                currentCal.set(Calendar.DATE, VALUEOFDATE);

                double CostValue = Double.parseDouble(cost);
                long dateValue =  currentCal.getTimeInMillis();

                String prevId = null;
                Intent i = getIntent();
                if(i.hasExtra("Item_ID")){
                    prevId = i.getStringExtra("Item_ID");
                }

                ItemDB db = new ItemDB(AddItemActivity.this);
                if(prevId == null){
                    String id = itemName+":"+currentTime;
                    db.insertItem(id, itemName, CostValue, dateValue);
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddItemActivity.this, ReportActivity.class);
                startActivity(i);
            }
        });

    }
}