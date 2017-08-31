package com.atrio.donateblood;

import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
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
    Spinner spin_state, sp_bloodgr,et_age,et_weight;
    //    RadioButton rb_male,rb_female;
    RadioButton radioSexButton;
    RadioGroup rg_group;
    CheckBox cb_never, cb_above, cb_below;
    Button btn_reg,btn_info;
    EditText et_name,et_emailid;
    TextView tv_info;
    String state_data, blood_data, radio_data, cb_data, name,emailid, age,phoneno, weight, city_data,count,temp;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    List age_data,weight_data;
    ListAdapter listAdapter;
    private SpotsDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resigration);
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        dialog = new SpotsDialog(ResigrationActivity.this, R.style.Custom);

        spin_state = (Spinner) findViewById(R.id.spin_state);
        sp_bloodgr = (Spinner) findViewById(R.id.spin_bloodGrp);
        rg_group = (RadioGroup) findViewById(R.id.radioSex);
        cb_never = (CheckBox) findViewById(R.id.cb_never);
        cb_above = (CheckBox) findViewById(R.id.cb_above);
        cb_below = (CheckBox) findViewById(R.id.cb_below);
        btn_reg = (Button) findViewById(R.id.bt_reg);
        btn_info = (Button) findViewById(R.id.btn_info);
        et_name = (EditText) findViewById(R.id.input_name);
        et_emailid = (EditText) findViewById(R.id.input_email);
        tv_info = (TextView) findViewById(R.id.tv_info);
        et_age = (Spinner) findViewById(R.id.input_age);
        et_weight = (Spinner) findViewById(R.id.input_weight);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);

        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (state_data.equals("Select Your State")) {
                    Toast.makeText(getApplicationContext(), "Select Your state", Toast.LENGTH_LONG).show();
                }else {
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
        atvPlaces.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    // on focus off
                    String str = atvPlaces.getText().toString();

                    listAdapter = atvPlaces.getAdapter();
                    if (listAdapter!=null) {
                        for (int i = 0; i < listAdapter.getCount(); i++) {

                            temp = listAdapter.getItem(i).toString();
                            if (str.compareTo(temp) == 0) {
                                return;
                            }
                        }

                        atvPlaces.setText("");
                    }

                }
            }
        });
        atvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItem =parent.getItemAtPosition(position).toString();
//                int selectedPos = state_data.indexOf((((TextView)view).getText()).toString());
//                atvPlaces.setText(selectedPos.);
                // here is your selected item
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
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        et_age.setAdapter(spinnerArrayAdapter);
        et_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                age = parent.getItemAtPosition(position).toString();

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
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        et_weight.setAdapter(weightAdapter);
        et_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weight = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
                cb_data=cb_never.getText().toString();
            }
        });
        cb_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_never.setChecked(false);
                cb_below.setChecked(true);
                cb_above.setChecked(false);
                cb_data=cb_below.getText().toString();

            }
        });
        cb_above.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_never.setChecked(false);
                cb_below.setChecked(false);
                cb_above.setChecked(true);
                cb_data=cb_never.getText().toString();

            }
        });

        db_instance = FirebaseDatabase.getInstance();

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                ConnectivityManager connMgr = (ConnectivityManager)getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo == null) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }else {
                    dialog.dismiss();
                    if (validate()) {

                        db_ref = db_instance.getReference();

                        int selectedId = rg_group.getCheckedRadioButtonId();
                        radioSexButton = (RadioButton)findViewById(selectedId);
                        name = et_name.getText().toString();
                        emailid = et_emailid.getText().toString();
                        city_data = atvPlaces.getText().toString();
                        radio_data = radioSexButton.getText().toString();
                        phoneno=user.getPhoneNumber();
                        dialog.show();

                        Query writequery = db_ref.child("Donor").child(state_data).child(city_data).orderByKey();
writequery.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getChildrenCount()==0){
            count="001";
            createUser(name,emailid, age, weight, blood_data,state_data, city_data, radio_data, cb_data,phoneno,count);
            et_name.setText("");
            et_emailid.setText("");
            atvPlaces.setText("");
            et_age.setSelection(0);
            et_weight.setSelection(0);
            sp_bloodgr.setSelection(0);
            spin_state.setSelection(0);
            cb_never.setChecked(false);
            cb_above.setChecked(false);
            cb_below.setChecked(false);
//            rg_group.
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "SuccessFully Register", Toast.LENGTH_LONG).show();
        }
        else {
            long countchild = dataSnapshot.getChildrenCount();
            countchild++;
            count=String.format("%03d",countchild);
            dialog.show();
            createUser(name,emailid, age, weight, blood_data,state_data, city_data, radio_data, cb_data,phoneno,count);
            et_name.setText("");
            et_emailid.setText("");
            atvPlaces.setText("");
            et_age.setSelection(0);
            et_weight.setSelection(0);
            sp_bloodgr.setSelection(0);
            spin_state.setSelection(0);
            cb_never.setChecked(false);
            cb_above.setChecked(false);
            cb_below.setChecked(false);
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "SuccessFully Register", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});




                    }
                }


            }
        });
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResigrationActivity.this,InfoActivity.class);
                startActivity(intent);
            }
        });
        tv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResigrationActivity.this,InfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createUser(String name, String emailid, String age, String weight, String blood_data, String state_data, String city_data, String radio_data, String cb_data, String phoneno, String count) {
        UserDetail userDetail=new UserDetail();

        userDetail.setName(name);
        userDetail.setEmailid(emailid);
        userDetail.setPhoneno(phoneno);
        userDetail.setAge(age);
        userDetail.setWeight(weight);
        userDetail.setBloodgroup(blood_data);
        userDetail.setState(state_data);
        userDetail.setCity(city_data);
        userDetail.setGender(radio_data);
        userDetail.setTimeperiod(cb_data);
        userDetail.setCount(count);

        db_ref.child("Donor").child(state_data).child(city_data).child(count).setValue(userDetail);

    }

    private boolean validate() {
// check whether the field is empty or not
        if (et_name.getText().toString().trim().length() < 1) {
            et_name.setError("Please Fill This Field");
            et_name.requestFocus();
            return false;

        } else if (et_emailid.getText().toString().trim().length() < 1 || isEmailValid(et_emailid.getText().toString()) == false) {
            et_emailid.setError("Invalid Email Address");
            et_emailid.requestFocus();
            return false;

        } else  if (age.equals("Select Your Age")) {
            Toast.makeText(getApplicationContext(), "Select Your Age", Toast.LENGTH_LONG).show();
            return false;

        } else   if (weight.equals("Select Your Weight")) {
            Toast.makeText(getApplicationContext(), "Select Your weight", Toast.LENGTH_LONG).show();
            return false;
        }

        else if (blood_data.equals("Select Your Blood Group")) {
            Toast.makeText(getApplicationContext(), "Select Your Blood Group", Toast.LENGTH_LONG).show();
            return false;


        }else if (state_data.equals("Select Your State")){
            Toast.makeText(getApplicationContext(), "Select Your state", Toast.LENGTH_LONG).show();
            return false;

        }
      else if (atvPlaces.getText().toString().trim().length() < 1) {
            atvPlaces.setError("Please Fill This Field");
            atvPlaces.requestFocus();
            return false;

        }  else  if (cb_data.equals("")) {
            Toast.makeText(getApplicationContext(), "Select Donation Period", Toast.LENGTH_LONG).show();
            return false;

        }  else
            return true;

    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }

    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyAG-AdjAgToyXceK6-ghWS38ho8cALPaUw";

            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=(cities)";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url =
//                    "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=India&sensor=false&types=(regions)&key=AIzaSyAG-AdjAgToyXceK6-ghWS38ho8cALPaUw";
                    "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters + "&components=country:IN";

            try {
                // Fetching the data from we service
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
            Log.i("url45", "" + e.toString());
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
            synchronized (adapter){
                adapter.notifyDataSetChanged();
            }
        }
    }

}
