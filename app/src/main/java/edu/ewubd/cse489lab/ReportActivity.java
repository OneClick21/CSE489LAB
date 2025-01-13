package edu.ewubd.cse489lab;

import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private ListView lvExpenditureList;

    private TextView tvTotalCost;

    private Button btnAddNew, btnBack, btnSearch;

    private EditText etSearch;

    private ArrayList<Item> items = new ArrayList<>();

    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        lvExpenditureList = findViewById(R.id.lvExpenditureList);
        tvTotalCost = findViewById(R.id.tvTotalCost);

        btnBack = findViewById(R.id.btnBack);
        btnAddNew = findViewById(R.id.btnAddNew);
        btnSearch = findViewById(R.id.btnSearch);

        etSearch = findViewById(R.id.etSearch);

        adapter = new ItemAdapter(this, items);
        lvExpenditureList.setAdapter(adapter);

        lvExpenditureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item selectedItem = items.get(position);
                Intent intent = new Intent(ReportActivity.this, AddItemActivity.class);
                intent.putExtra("ID", selectedItem.id);
                intent.putExtra("ITEM-NAME", selectedItem.itemName);
                intent.putExtra("DATE", selectedItem.date);
                intent.putExtra("COST", selectedItem.cost);
                startActivity(intent);
            }
        });

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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchBy = etSearch.getText().toString().trim();
                loadLocalData(searchBy);
            }
        });

    }

    public void onStart() {
        super.onStart();
        loadLocalData("");
        loadRemoteData();
    }

    private void loadLocalData(String searchBy) {
        items.clear();
        double totalCost = 0;
        ItemDB db = new ItemDB(this);
        String q = "SELECT * FROM items";
        if (!searchBy.isEmpty()) {
            long date = getDateInMilliSecond(searchBy);
            q += " WHERE itemName LIKE '%" + searchBy + "%' OR date LIKE '%" + date + "%'";
        }
        Cursor c = db.selectItems(q);
        while (c.moveToNext()) {
            String id = c.getString(0);
            String itemName = c.getString(1);
            double cost = c.getDouble(2);
            long date = c.getLong(3);

            //System.out.println("ID: "+id);
            //System.out.println("itemName: "+itemName);
            //System.out.println("date: "+date);
            //System.out.println("cost: "+cost);
            Item i = new Item(id, itemName, cost, date);
            items.add(i);
            totalCost += cost;
        }
        adapter.notifyDataSetChanged();
        tvTotalCost.setText(String.valueOf(totalCost));
    }

    private void loadRemoteData() {
        String keys[] = {"action", "sid", "semester"};
        String values[] = {"restore", "2021-2-60-071", "2024-3"};
        httpRequest(keys, values);
    }

    private void httpRequest(final String keys[], final String values[]) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (int i = 0; i < keys.length; i++) {
                    params.add(new BasicNameValuePair(keys[i], values[i]));
                }
                String url = "https://www.muthosoft.com/univ/cse489/index.php";
                try {
                    String data = RemoteAccess.getInstance().makeHttpRequest(url, "POST", params);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String data) {
                if (data != null) {
                    updateLocalDBByServerData(data);
                }
            }
        }.execute();
    }

    private void updateLocalDBByServerData(String data) {
        System.out.println("found");
        try {
            JSONObject jo = new JSONObject(data);
            if (jo.has("classes")) {
                items.clear();
                double totalCost = 0;
                JSONArray ja = jo.getJSONArray("classes");
                ItemDB db = new ItemDB(this);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject item = ja.getJSONObject(i);
                    String id = item.getString("id");
                    String itemName = item.getString("itemName");
                    double cost = item.getDouble("cost");
                    long date = item.getLong("date");
                    Item item1 = new Item(id, itemName, cost, date);
                    items.add(item1);
                    totalCost += cost;

                    // Write code here to insert lecture information in SQL Database
                    db.updateItem(id, itemName, cost, date);
                }
                db.close();
                adapter.notifyDataSetChanged();
                tvTotalCost.setText(String.valueOf(totalCost));
            }
        } catch (Exception e) {
        }
    }

    private boolean deleteRemoteData(String deleteId) {
        String[] keys = {"action", "sid", "semester", "id"};
        String[] values = {"remove", "2021-2-60-071", "2024-3", deleteId};
        try {
            ItemDB db = new ItemDB(ReportActivity.this);
            db.deleteItem(deleteId);
            db.close();
            httpRequest(keys, values);
            onStart();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private long getDateInMilliSecond(String date) {
        try {
            String[] dateParts = date.split("-");
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTimeInMillis(0);
            currentCal.set(Calendar.YEAR, Integer.parseInt(dateParts[2]));
            currentCal.set(Calendar.MONTH, Integer.parseInt(dateParts[1]) - 1); // Calendar.MONTH is zero-based
            currentCal.set(Calendar.DATE, Integer.parseInt(dateParts[0]));
            return currentCal.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return a default value or handle the error as needed
        }
    }
}