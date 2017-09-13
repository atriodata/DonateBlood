package com.atrio.donateblood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atrio.donateblood.model.UserDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class RecipentDetailsActivity extends AppCompatActivity {
    private EditText et_rcName, et_rcPhn,et_rcaddress;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    String phno_no;
    Button bt_send;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    String name,phn_no,address;
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipentdetail);
        et_rcName = (EditText)findViewById(R.id.input_rcName);
        et_rcPhn = (EditText)findViewById(R.id.input_rcphn);
        et_rcaddress = (EditText)findViewById(R.id.et_address);
        bt_send = (Button)findViewById(R.id.bt_reg);

        dialog = new SpotsDialog(RecipentDetailsActivity.this, R.style.Custom);
        et_rcPhn.setEnabled(false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        phno_no = user.getPhoneNumber();
        et_rcPhn.setText(phno_no);
        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference();
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    dialog.show();
                    name = et_rcName.getText().toString().trim();
                    phn_no = et_rcPhn.getText().toString().trim();
                    address = et_rcaddress.getText().toString().trim();
                    createRecipient(name,phn_no,address);
                }
            }
        });
    }

    private void createRecipient(String name, final String phn_no, String address) {
        final UserDetail userDetail = new UserDetail();
        userDetail.setName(name);
        userDetail.setPhoneno(phn_no);
        userDetail.setAddress(address);
        Query query = db_ref.child("Recipient").orderByKey().equalTo(phn_no);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()!=0){
                    dialog.dismiss();

                }else{
                    dialog.dismiss();
                    Toast.makeText(RecipentDetailsActivity.this,"successfully submitted",Toast.LENGTH_SHORT).show();
                    db_ref.child("Recipient").child(phn_no).setValue(userDetail);
                    Intent intent = new Intent(RecipentDetailsActivity.this, RecipientActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private boolean validate() {
        if (et_rcName.getText().toString().trim().length() < 1) {
            et_rcName.setError("Please Fill This Field");
            et_rcName.requestFocus();
            return false;

        } else if (et_rcaddress.getText().toString().trim().length() < 1) {
            et_rcaddress.setError("Enter your Address");
            et_rcaddress.requestFocus();
            return false;

        }else
            return true;
    }
}
