package edu.ewubd.cse489lab;

import static android.util.Patterns.*;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName, etCost, etDate;

    private Button btnSave, btnBack;

    private String id = "";

    private boolean isValidDate(String date) {
        if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return false;
        }
        String[] parts = date.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > 31) {
            return false;
        }
        // Check for February and leap year
        if (month == 2) {
            if (isLeapYear(year)) {
                return day <= 29;
            } else {
                return day <= 28;
            }
        }
        // Check for months with 30 days
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30;
        }
        else {
            return true;
        }
    }
    private boolean isLeapYear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            return true;
        }
        return false;
    }

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

                Intent i = getIntent();
                if(i != null && i.hasExtra("ID")){
                    id = i.getStringExtra("ID");
                    String ItemName = i.getStringExtra("ITEM-NAME");
                    long dateInMilliSeconds = i.getLongExtra("DATE", 0);
                    double Cost = i.getDoubleExtra("COST", 0);
                    String Date = dateInMilliSeconds+""; // write code to convert milliseconds to date
                    etItemName.setText(itemName);
                    etCost.setText(String.valueOf(cost));
                    etDate.setText(date);

                // Data validation
                if (itemName.length() > 20) {
                    Toast.makeText(AddItemActivity.this, "Item name should be 20 letters", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!TextUtils.isDigitsOnly(cost)) {
                    Toast.makeText(AddItemActivity.this, "Cost should be a number", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!isValidDate(date)) {
                    Toast.makeText(AddItemActivity.this, "Invalid date", Toast.LENGTH_LONG).show();
                    return;
                }

                // Split Date
                String[] dateParts = date.split("-");
                int year = Integer.parseInt(dateParts[2]);
                int month = Integer.parseInt(dateParts[1]) - 1; // Calendar.MONTH is zero-based
                int day = Integer.parseInt(dateParts[0]);

                // Store valid data
                Calendar currentCal = Calendar.getInstance();
                long currentTime = currentCal.getTimeInMillis();
                currentCal.setTimeInMillis(0);
                currentCal.set(Calendar.YEAR, year);
                currentCal.set(Calendar.MONTH, month);
                currentCal.set(Calendar.DATE, day);

                double CostValue = Double.parseDouble(cost);
                long dateValue = currentCal.getTimeInMillis();

                String prevId = null;
                Intent i = getIntent();
                if (i.hasExtra("Item_ID")) {
                    prevId = i.getStringExtra("Item_ID");
                }

                ItemDB db = new ItemDB(AddItemActivity.this);
                if (prevId == null) {
                    String id = itemName + ":" + currentTime;
                    db.insertItem(id, itemName, CostValue, dateValue);
                }

                i = new Intent(AddItemActivity.this, ReportActivity.class);
                startActivity(i);

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