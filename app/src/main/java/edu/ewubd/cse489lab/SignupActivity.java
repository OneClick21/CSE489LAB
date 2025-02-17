package edu.ewubd.cse489lab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText etUserName, etEmail, etPhone, etPassword, etCPassword;

    private CheckBox cbRememberUser, cbRememberLogin;

    private Button btnHaveAccount, btnSignup;


    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences("my_sp", MODE_PRIVATE);
        String email = sp.getString("USER-EMAIL", "NOT-YET-CREATED");

        if (!getIntent().getBooleanExtra("FROM-LOGIN", false)
                && !email.equals("NOT-YET-CREATED")) {
            Toast.makeText(this, "You have been redirected form Signup page",
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(i);
            finishAffinity();
        }


        setContentView(R.layout.activity_signup);

        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etCPassword);

        cbRememberUser = findViewById(R.id.cbRememberUser);
        cbRememberLogin = findViewById(R.id.cbRememberLogin);

        btnHaveAccount = findViewById(R.id.btnHaveAccount);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
                String cPass = etCPassword.getText().toString().trim();

                System.out.println(userName);
                System.out.println(email);
                System.out.println(phone);
                System.out.println(pass);
                System.out.println(cPass);

                if (userName.length() < 4) {
                    Toast.makeText(SignupActivity.this, "Username should be 4-8 letters", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignupActivity.this, "Use a valid Email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (phone.length() < 8) {
                    Toast.makeText(SignupActivity.this, "Phone should be 8-13 digits", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pass.length() < 4) {
                    Toast.makeText(SignupActivity.this, "Password should be 4 digits", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!pass.equals(cPass)) {
                    Toast.makeText(SignupActivity.this, "Password didn't match", Toast.LENGTH_LONG).show();
                    return;
                }

                SharedPreferences.Editor e = sp.edit();
                e.putString("USER-EMAIL", email);
                e.putString("USER-NAME", userName);
                e.putString("USER-PHONE", phone);
                e.putString("PASSWORD", pass);
                e.putBoolean("REMEMBER-USER", cbRememberUser.isChecked());
                e.putBoolean("REMEMBER-LOGIN", cbRememberLogin.isChecked());
                e.apply();

                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });


        btnHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }
}