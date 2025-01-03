package edu.ewubd.cse489lab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName, etCost, etDate;
    private Button btnSave, btnBack;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItemName = findViewById(R.id.etItemName);
        etCost = findViewById(R.id.etCost);
        etDate = findViewById(R.id.etDate);

        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        // Set up DatePickerDialog
        etDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddItemActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year1);
                        etDate.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        Intent i = getIntent();
        if (i != null && i.hasExtra("ID")) {
            id = i.getStringExtra("ID");
            String ItemName = i.getStringExtra("ITEM-NAME");
            long dateInMilliSeconds = i.getLongExtra("DATE", 0);
            double Cost = i.getDoubleExtra("COST", 0);
            Date date = new Date(dateInMilliSeconds); // code to convert milliseconds to date
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String Date = sdf.format(date);
            etItemName.setText(ItemName);
            etCost.setText(String.valueOf(Cost));
            etDate.setText(Date);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = etItemName.getText().toString().trim();
                String cost = etCost.getText().toString().trim();
                String date = etDate.getText().toString().trim();

                // Data validation
                if (itemName.length() > 20) {
                    Toast.makeText(AddItemActivity.this, "Item name should be 20 letters", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!cost.matches("\\d+(\\.\\d+)?")) {
                    Toast.makeText(AddItemActivity.this, "Cost should be a number", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!DateChecker.isValidDate(date)) {
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

                double costValue = Double.parseDouble(cost);
                long dateValue = currentCal.getTimeInMillis();


                ItemDB db = new ItemDB(AddItemActivity.this);
                if (id.isEmpty()) {
                    id = itemName + ":" + currentTime;
                    db.insertItem(id, itemName, costValue, dateValue);
                } else {
                    db.updateItem(id, itemName, costValue, dateValue);
                }

                // Store in remote database
                String keys[] = {"action", "sid", "semester", "id", "itemName", "cost", "date"};
                String values[] = {"backup", "2021-2-60-071","2024-3", id, itemName, String.valueOf(costValue), String.valueOf(dateValue)};
                httpRequest(keys, values);

                db.close();
                finish();

                Intent i = new Intent(AddItemActivity.this, ReportActivity.class);
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

    private void httpRequest(final String keys[],final String values[]){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params=new ArrayList<NameValuePair >();
                for (int i=0; i<keys.length; i++){
                    params.add(new BasicNameValuePair(keys[i],values[i]));
                }
                String url= "https://www.muthosoft.com/univ/cse489/index.php";
                try {
                    String data= RemoteAccess.getInstance().makeHttpRequest(url,"POST",params);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(String data){
                if(data!=null){
                    Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
