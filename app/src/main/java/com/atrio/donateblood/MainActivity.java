package com.atrio.donateblood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.atrio.donateblood.adapter.SpinnerAdapter;
import com.atrio.donateblood.model.ItemData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText et_phn;
    private Button btn_nxt;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String phn_no,country_code;
    Spinner sp_country;
    ArrayList<ItemData> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_phn = (EditText) findViewById(R.id.et_phone);
        btn_nxt = (Button) findViewById(R.id.btn_next);
        sp_country = (Spinner) findViewById(R.id.sp_country);
        mAuth=FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        } else {
        }

        list = new ArrayList<>();
        list.add(new ItemData("INDIA","+91",R.drawable.india_flag));
        list.add(new ItemData("NIGERIA ","+234",R.drawable.nigeria_flags));
        SpinnerAdapter adapter=new SpinnerAdapter(this,R.layout.custom_flag,R.id.txt,list);
        sp_country.setAdapter(adapter);

        sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_code = list.get(position).getCountry_code();
//                String totrim=state_data.replace(" ","");
//                Log.i("statedata",""+country_code.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePhoneNumber()) {
                    return;
                }
                phn_no = country_code + et_phn.getText().toString().trim();
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
