package com.atrio.donateblood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class ResigrationActivity extends AppCompatActivity {
    AutoCompleteTextView atvPlaces;
    PlacesTask placesTask;
    ParserTask parserTask;
    Spinner spin_state, sp_bloodgr, et_age, et_weight,spin_country;
    RadioButton radioSexButton,rb_male,rb_female;
    RadioGroup rg_group;
    int index;
    //    CheckBox cb_never, cb_above, cb_below;
    Button btn_reg, btn_info, btn_update;
    EditText et_name, et_emailid;
    TextView tv_info,sp_text;
    String state_data, blood_data, radio_data,  name, emailid, age, phoneno, weight, city_data, count, token,groupFirst,groupLast;//cb_data
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    List age_data, weight_data;
    private SpotsDialog dialog;
    String topics = null;
    ArrayList<String> phn_nolist,present_list;
    SharedPreferences sharedpreferences;


    public static final String MyPREFERENCES = "BloodDonate" ;
    public static final String city = "cityKey";
    public static final String state = "stateKey";
    public static final String blood_group = "blood_groupKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resigration);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        dialog = new SpotsDialog(ResigrationActivity.this, R.style.Custom);

        dialog.show();
        spin_state = (Spinner) findViewById(R.id.spin_state);
        sp_bloodgr = (Spinner) findViewById(R.id.spin_bloodGrp);
        spin_country = (Spinner) findViewById(R.id.spin_country);
        rg_group = (RadioGroup) findViewById(R.id.radioSex);
        rb_male = (RadioButton) findViewById(R.id.radioMale);
        rb_female = (RadioButton) findViewById(R.id.radioFemale);
//        cb_never = (CheckBox) findViewById(R.id.cb_never);
//        cb_above = (CheckBox) findViewById(R.id.cb_above);
//        cb_below = (CheckBox) findViewById(R.id.cb_below);
        btn_reg = (Button) findViewById(R.id.bt_reg);
        btn_info = (Button) findViewById(R.id.btn_info);
        btn_update = (Button) findViewById(R.id.bt_update);
        et_name = (EditText) findViewById(R.id.input_name);
        et_emailid = (EditText) findViewById(R.id.input_email);
        tv_info = (TextView) findViewById(R.id.tv_info);
        et_age = (Spinner) findViewById(R.id.input_age);
        et_weight = (Spinner) findViewById(R.id.input_weight);
        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        btn_update.setVisibility(View.GONE);
        btn_reg.setVisibility(View.VISIBLE);

        if (networkInfo == null) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        phn_nolist = new ArrayList<>();
        present_list = new ArrayList<>();
        atvPlaces.setThreshold(1);
        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference();

        if (user!=null){

            Query getalldata = db_ref.child("Donor").orderByKey();

            getalldata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            for (DataSnapshot dataSnapshot1 : data.getChildren()){
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                                    if (dataSnapshot2.getKey().equals(user.getPhoneNumber())){
//                                        Log.i("userdetail4",""+dataSnapshot2.getValue());
                                        UserDetail userDetail = dataSnapshot2.getValue(UserDetail.class);

                                        name = userDetail.getName();
                                        emailid = userDetail.getEmailid();
                                        city_data = userDetail.getCity();
                                        radio_data = userDetail.getGender();
                                        age=userDetail.getAge();
                                        weight=userDetail.getWeight();
                                        blood_data=userDetail.getBloodgroup();
                                        state_data=userDetail.getState();
                                        phoneno = user.getPhoneNumber();
                                        topics=userDetail.getTopics();

                                        dialog.dismiss();
                                        btn_reg.setVisibility(View.GONE);
                                        btn_update.setVisibility(View.VISIBLE);
                                        et_name.setText(name);
                                        et_name.setTextColor(Color.BLACK);
                                        et_emailid.setText(emailid);
                                        et_emailid.setTextColor(Color.BLACK);
                                        atvPlaces.setText(city_data);
                                        atvPlaces.setTextColor(Color.BLACK);
                                        if (rb_male.getText()==radio_data){
                                            rb_male.setChecked(true);
                                        }else {
                                            rb_female.setChecked(true);
                                        }
                                        et_age.setSelection(((ArrayAdapter<String>)et_age.getAdapter()).getPosition(age));
                                        et_weight.setSelection(((ArrayAdapter<String>)et_weight.getAdapter()).getPosition(weight));
                                        sp_bloodgr.setSelection(((ArrayAdapter<String>)sp_bloodgr.getAdapter()).getPosition(blood_data));
                                        spin_state.setSelection(((ArrayAdapter<String>)spin_state.getAdapter()).getPosition(state_data));
                                        et_name.setEnabled(false);
                                        et_emailid.setEnabled(false);
                                        et_age.setClickable(false);
                                        et_weight.setClickable(false);
                                        sp_bloodgr.setClickable(false);
                                        spin_country.setClickable(false);
                                        spin_state.setClickable(false);
                                        atvPlaces.setEnabled(false);
                                        rb_male.setClickable(false);
                                        rb_female.setClickable(false);
                                    }
                                    dialog.dismiss();

                                }
                            }
                        }

                    } else{
                        dialog.dismiss();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }else {
            dialog.dismiss();
            btn_reg.setVisibility(View.VISIBLE);
            btn_update.setVisibility(View.GONE);
        }



        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (state_data.equals("Select Your State")) {
                    Toast.makeText(getApplicationContext(), "Select Your state", Toast.LENGTH_LONG).show();
                } else {
                    placesTask = new PlacesTask();
                    placesTask.execute(s.toString());
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        sp_bloodgr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blood_data = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state_data = parent.getItemAtPosition(position).toString();
//                String totrim=state_data.replace(" ","");
               /* Log.i("statedata",""+state_data);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        age_data = new ArrayList<Integer>();
        age_data.add("Select Your Age");
        for (int i = 18; i <= 60; i++) {
            age_data.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, age_data);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_age.setAdapter(spinnerArrayAdapter);
        et_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                age = parent.getItemAtPosition(position).toString();
//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
//                TextView oTextView = (TextView)et_age.getChildAt(0);
//                oTextView.setTextColor(Color.BLACK);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        weight_data = new ArrayList<Integer>();
        weight_data.add("Select Your Weight");
        for (int i = 45; i <= 100; i++) {
            weight_data.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> weightAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, weight_data);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_weight.setAdapter(weightAdapter);
        et_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weight = parent.getItemAtPosition(position).toString();
//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


/*
        if (cb_never.isChecked()) {
            cb_never.setChecked(false);
        } else if (cb_below.isChecked()) {
            cb_below.setChecked(false);
        } else {
            cb_above.setChecked(false);
        }
        cb_never.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_never.setChecked(true);
                cb_below.setChecked(false);
                cb_above.setChecked(false);
                cb_data = cb_never.getText().toString();
            }
        });
        cb_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_never.setChecked(false);
                cb_below.setChecked(true);
                cb_above.setChecked(false);
                cb_data = cb_below.getText().toString();

            }
        });
        cb_above.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_never.setChecked(false);
                cb_below.setChecked(false);
                cb_above.setChecked(true);
                cb_data = cb_never.getText().toString();

            }
        });

*/
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                String data = null;
                int selectedId = rg_group.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) findViewById(selectedId);
                name = et_name.getText().toString();
                emailid = et_emailid.getText().toString();
                city_data = atvPlaces.getText().toString().toLowerCase();
                radio_data = radioSexButton.getText().toString();
                phoneno = user.getPhoneNumber();

                if (validate()) {
                    dialog.dismiss();
                    phn_nolist.clear();

                    dialog.show();
                    Query writequery = db_ref.child("Donor").orderByKey();
                    writequery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() != 0) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    for (DataSnapshot dataSnapshot1 : data.getChildren()) {
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                            String phn_no = dataSnapshot2.getKey();
                                            phn_nolist.add(phn_no);
                                        }
                                    }
                                }
                                sendPhonelist(phn_nolist);

                            } else {
                                dialog.dismiss();
                                subscribeToPushService(blood_data);
                               /* Log.i("printdata34",""+topics);*/
                                createUser(name, emailid, age, weight, blood_data, state_data, city_data, radio_data, phoneno, count,topics);
                                et_name.setEnabled(false);
                                et_emailid.setEnabled(false);
                                et_age.setClickable(false);
                                et_weight.setClickable(false);
                                sp_bloodgr.setClickable(false);
                                spin_country.setClickable(false);
                                spin_state.setClickable(false);
                                atvPlaces.setEnabled(false);
                                rb_male.setClickable(false);
                                rb_female.setClickable(false);
                                btn_reg.setVisibility(View.GONE);
                                btn_update.setVisibility(View.VISIBLE);
                                Toast.makeText(ResigrationActivity.this, "Successsully Registred", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(city, city_data);
                                editor.putString(state, state_data);
                                editor.putString(blood_group, blood_data);
                                editor.commit();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else {
                    dialog.dismiss();
                }



            }
        });
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResigrationActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
        tv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResigrationActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               db_ref.child("Donor").child(state_data).child(city_data).child(phoneno).removeValue();
               /* Log.i("unsub1234",""+topics);*/
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topics);

                btn_update.setVisibility(View.GONE);
                btn_reg.setVisibility(View.VISIBLE);
                et_name.setEnabled(true);
                et_emailid.setEnabled(true);
                et_age.setClickable(true);
                et_weight.setClickable(true);
                sp_bloodgr.setClickable(true);
                spin_country.setClickable(true);
                spin_state.setClickable(true);
                atvPlaces.setEnabled(true);
                rb_male.setClickable(true);
                rb_female.setClickable(true);
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (btn_update.getVisibility()==View.GONE){
            if (validate()) {

                subscribeToPushService(blood_data);
                createUser(name, emailid, age, weight, blood_data, state_data, city_data, radio_data, phoneno, count, topics);
                et_name.setText("");
                et_emailid.setText("");
                atvPlaces.setText("");
                et_age.setSelection(0);
                et_weight.setSelection(0);
                sp_bloodgr.setSelection(0);
                spin_state.setSelection(0);
                rb_male.setChecked(true);
                /*cb_never.setChecked(false);
                cb_above.setChecked(false);
                cb_below.setChecked(false);*/
//            Toast.makeText(ResigrationActivity.this, "Successsully Registred ", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(city, city_data);
                editor.putString(state, state_data);
                editor.putString(blood_group, blood_data);
                editor.commit();
            }else {
                Toast.makeText(ResigrationActivity.this, "Registration not done ", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void sendPhonelist(ArrayList<String> phn_nolist) {
        if (phn_nolist.size()!=0){
            for (int i=0;i<phn_nolist.size();i++){
                String data = null;
                if (phn_nolist.get(i).equals(phoneno)){
                    data = "present";
                    present_list.add(data);

                }else{
                }
            }
        }
        if (phn_nolist.size()!=0){
            if (present_list.size()!=0) {
                dialog.dismiss();
                Toast.makeText(ResigrationActivity.this, "This Number is already  Registred", Toast.LENGTH_SHORT).show();
                et_name.setText("");
                et_emailid.setText("");
                atvPlaces.setText("");
                et_age.setSelection(0);
                et_weight.setSelection(0);
                sp_bloodgr.setSelection(0);
                spin_state.setSelection(0);
                rb_male.setChecked(true);
              /*  cb_never.setChecked(false);
                cb_above.setChecked(false);
                cb_below.setChecked(false);*/
            }else {
                dialog.dismiss();
                subscribeToPushService(blood_data);
                createUser(name, emailid, age, weight, blood_data, state_data, city_data, radio_data,  phoneno, count,topics);
                et_name.setTextColor(Color.BLACK);
                et_name.setEnabled(false);
                et_emailid.setTextColor(Color.BLACK);
                et_emailid.setEnabled(false);
                et_age.setClickable(false);
                et_weight.setClickable(false);
                sp_bloodgr.setClickable(false);
                spin_country.setClickable(false);
                spin_state.setClickable(false);
                atvPlaces.setTextColor(Color.BLACK);
                atvPlaces.setEnabled(false);
                rb_male.setClickable(false);
                rb_female.setClickable(false);
                btn_reg.setVisibility(View.GONE);
                btn_update.setVisibility(View.VISIBLE);
                Toast.makeText(ResigrationActivity.this, "Successsully Registred ", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(city, city_data);
                editor.putString(state, state_data);
                editor.putString(blood_group, blood_data);
                editor.commit();
            }

        }else{
        }
    }

    private void subscribeToPushService(String blood_data) {

        groupFirst = blood_data.substring(0,blood_data.length()-1);
        groupLast = blood_data.substring(blood_data.length()-1);

        if (groupLast.equals("+")){
          topics = state_data.replace(" ","")+groupFirst+"positive";
        }else{
            topics = state_data.replace(" ","")+groupFirst+"negative";
        }
   /*     Log.i("sub1234",""+topics);*/
        FirebaseMessaging.getInstance().subscribeToTopic(topics);
        token = FirebaseInstanceId.getInstance().getToken();
    }

    private void createUser(String name, String emailid, String age, String weight, String blood_data, String state_data, String city_data, String radio_data, String phoneno, String count,String topics) {
        UserDetail userDetail = new UserDetail();

        userDetail.setName(name);
        userDetail.setEmailid(emailid);
        userDetail.setPhoneno(phoneno);
        userDetail.setAge(age);
        userDetail.setWeight(weight);
        userDetail.setBloodgroup(blood_data);
        userDetail.setState(state_data);
        userDetail.setCity(city_data);
        userDetail.setGender(radio_data);
        userDetail.setTopics(topics);
        userDetail.setCount(count);

        db_ref.child("Donor").child(state_data).child(city_data).child(phoneno).setValue(userDetail);

    }

    private boolean validate() {
        if (et_name.getText().toString().trim().length() < 1) {
            et_name.setError("Please Fill This Field");
            et_name.requestFocus();
            return false;

        } else if (et_emailid.getText().toString().trim().length() < 1 || isEmailValid(et_emailid.getText().toString()) == false) {
            et_emailid.setError("Invalid Email Address");
            et_emailid.requestFocus();
            return false;

        } else if (age.equals("Select Your Age")) {
            Toast.makeText(getApplicationContext(), "Select Your Age", Toast.LENGTH_LONG).show();
            return false;

        } else if (weight.equals("Select Your Weight")) {
            Toast.makeText(getApplicationContext(), "Select Your weight", Toast.LENGTH_LONG).show();
            return false;
        } else if (blood_data.equals("Select Your Blood Group")) {
            Toast.makeText(getApplicationContext(), "Select Your Blood Group", Toast.LENGTH_LONG).show();
            return false;


        } else if (state_data.equals("Select Your State")) {
            Toast.makeText(getApplicationContext(), "Select Your state", Toast.LENGTH_LONG).show();
            return false;

        } else if (atvPlaces.getText().toString().trim().length() < 1) {
            atvPlaces.setError("Please Fill This Field");
            atvPlaces.requestFocus();
            return false;

        } /*else if (cb_data.equals("")) {
            Toast.makeText(getApplicationContext(), "Select Donation Period", Toast.LENGTH_LONG).show();
            return false;
        }*/ else
            return true;

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }

    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            String data = "";
            String key = "key=AIzaSyAG-AdjAgToyXceK6-ghWS38ho8cALPaUw";
            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            String types = "types=(cities)";
            String sensor = "sensor=false";
            String parameters = input + "&" + types + "&" + sensor + "&" + key;
            String output = "json";
            String url ="https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters + "&components=country:IN";

            try {
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser(state_data);

            try {
                jObject = new JSONObject(jsonData[0]);
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);
            atvPlaces.setAdapter(adapter);
            synchronized (adapter) {
                adapter.notifyDataSetChanged();
            }
        }
    }

}
