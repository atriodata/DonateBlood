package com.atrio.donateblood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class MainActivity extends AppCompatActivity {

    private EditText et_phn;
    private Button btn_nxt;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String phn_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_phn = (EditText) findViewById(R.id.et_phone);
        btn_nxt = (Button) findViewById(R.id.btn_next);
        mAuth=FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        } else {
        }
        btn_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePhoneNumber()) {
                    return;
                }
                phn_no = "+91" + et_phn.getText().toString().trim();
                Intent intent = new Intent(MainActivity.this, VerifyOTP.class);
                intent.putExtra("phn_number", phn_no);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = et_phn.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)||et_phn.getText().toString().trim().length() >12 ||et_phn.getText().toString().trim().length()<10) {
            et_phn.setError("Invalid phone number.");
            return false;
        }

        return true;
    }


}
