package edu.ewubd.cse489lab;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    private ListView lvExpenditureList;

    private Button btnAddNew, btnBack;

    private ArrayList<Item> showItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        lvExpenditureList = findViewById(R.id.lvExpenditureList);

        btnBack = findViewById(R.id.btnBack);
        btnAddNew = findViewById(R.id.btnAddNew);

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportActivity.this, AddItemActivity.class);
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }

    public void onStart(){
        super.onStart();
        ItemDB db = new ItemDB(this);
        Cursor c = db.selectItems("SELECT * FROM items");
        while (c.moveToNext()){
            String id = c.getString(0);
            String itemName = c.getString(1);
            String Cost = c.getString(2);
            String Date = c.getString(3);

            System.out.println("ID: "+ id);
            System.out.println("itemName: "+ itemName);
            System.out.println("Cost: "+ Cost);
            System.out.println("Date: "+ Date);
        }
    }
}