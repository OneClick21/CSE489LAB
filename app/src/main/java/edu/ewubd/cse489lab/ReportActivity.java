package edu.ewubd.cse489lab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReportActivity extends AppCompatActivity {

    private ListView lvExpenditureList;

    private Button btnAddNew, btnBack;

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
}