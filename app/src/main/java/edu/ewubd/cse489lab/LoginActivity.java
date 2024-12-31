package edu.ewubd.cse489lab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;

    private CheckBox cbRememberUser, cbRememberLogin;

    private Button btnDoesntHaveAccount, btnLogin;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        cbRememberUser = findViewById(R.id.cbRememberUser);
        cbRememberLogin = findViewById(R.id.cbRememberLogin);

        btnDoesntHaveAccount = findViewById(R.id.btnDoesntHaveAccount);
        btnLogin = findViewById(R.id.btnLogin);

        sp = this.getSharedPreferences("my_sp", MODE_PRIVATE);
        String email = sp.getString("USER-EMAIL", "");
        String pass = sp.getString("PASSWORD", "");
        boolean remUser = sp.getBoolean("REMEMBER-USER", false);
        boolean remLogin = sp.getBoolean("REMEMBER-LOGIN", false);

        if(remUser){
            etEmail.setText(email);
            cbRememberUser.setChecked(true);
        }

        if (remLogin){
            etPassword.setText(pass);
            cbRememberLogin.setChecked(true);
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = etEmail.getText().toString().trim();
                String p = etPassword.getText().toString().trim();

                if(!e.equals(email)){
                    Toast.makeText(LoginActivity.this, "Email didn't match", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!p.equals(pass)){
                    Toast.makeText(LoginActivity.this, "Password didn't match", Toast.LENGTH_LONG).show();
                    return;
                }

                if(e.isEmpty() || p.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Email and Password can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                System.out.println(email);
                System.out.println(pass);

                SharedPreferences.Editor ed = sp.edit();
                ed.putBoolean("REMEMBER-USER", cbRememberLogin.isChecked());
                ed.putBoolean("REMEMBER-LOGIN", cbRememberUser.isChecked());
                ed.apply();

                Intent i = new Intent(LoginActivity.this, ReportActivity.class);
                startActivity(i);

            }
        });

        btnDoesntHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                i.putExtra("FROM-LOGIN", true);
                startActivity(i);
            }
        });

    }
}