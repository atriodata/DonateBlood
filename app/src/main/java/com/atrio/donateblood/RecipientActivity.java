package com.atrio.donateblood;

import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.atrio.donateblood.model.RecipientDetail;
import com.atrio.donateblood.model.UserDetail;
import com.atrio.donateblood.sendmail.SendMail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecipientActivity extends AppCompatActivity {
    AutoCompleteTextView atvPlaces;
    PlacesTask placesTask;
    ParserTask parserTask;
    Spinner spin_state, sp_bloodgr;
    Button btn_send;
    EditText et_phoneno, et_emailid, et_date, et_remark;
    String state_data, blood_data, emailid, phoneno, date_req, city_data, other_detail, send_mail, regId,msg_id;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private SpotsDialog dialog;
    ArrayList<String> store_list;
    JSONArray jsonArray;
    OkHttpClient mClient;
    Intent iget;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        dialog = new SpotsDialog(RecipientActivity.this, R.style.Custom);

        spin_state = (Spinner) findViewById(R.id.spin_state);
        sp_bloodgr = (Spinner) findViewById(R.id.spin_bloodGrp);
        btn_send = (Button) findViewById(R.id.bt_reg);
        et_phoneno = (EditText) findViewById(R.id.input_phoneno);
        et_emailid = (EditText) findViewById(R.id.input_email);
        et_date = (EditText) findViewById(R.id.input_date);
        et_remark = (EditText) findViewById(R.id.et_remark);

        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);
        store_list = new ArrayList<>();
        iget = getIntent();
        regId = iget.getStringExtra("tokenid");
        mClient = new OkHttpClient();
        sp_bloodgr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blood_data = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                DatePickerDialog datePickerDialog = new DatePickerDialog(RecipientActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                et_date.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
//                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
        atvPlaces.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    // on focus off
                    String str = atvPlaces.getText().toString();

                    ListAdapter listAdapter = atvPlaces.getAdapter();
                    if (listAdapter != null) {
                        for (int i = 0; i < listAdapter.getCount(); i++) {
                            String temp = listAdapter.getItem(i).toString();
                            if (str.compareTo(temp) == 0) {
                                return;
                            }
                        }

                        atvPlaces.setText("");
                    }

                }
            }
        });

        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.show();
                ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo == null) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    dialog.dismiss();
                    if (validate()) {
                        phoneno = et_phoneno.getText().toString();
                        emailid = et_emailid.getText().toString();
                        city_data = atvPlaces.getText().toString();
                        date_req = et_date.getText().toString();
                        other_detail = et_remark.getText().toString();
                        dialog.show();

                        Query readqery = db_ref.child("Donor").child(state_data).child(city_data).orderByChild("bloodgroup").equalTo(blood_data);

                        readqery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() == 0) {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "No Donor Available", Toast.LENGTH_LONG).show();
                                } else {
                                    Iterator<DataSnapshot> item = dataSnapshot.getChildren().iterator();

                                    while (item.hasNext()) {
                                        DataSnapshot items = item.next();
                                        UserDetail user_info = items.getValue(UserDetail.class);
                                        send_mail = user_info.getEmailid();
                                        store_list.add(send_mail);
                                    }
                                  dialog.dismiss();
                                    Log.i("Mainresult:47", "" + regId);
                                    sendNotificationToUser(regId);
                                    Toast.makeText(RecipientActivity.this, "Request Send", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        dialog.dismiss();
                    }
                }


            }
        });
    }

    private void createRecipientDetail(String state_data, String blood_data, String emailid, String phoneno, String date_req, String city_data, String other_detail, String msg_id) {
        RecipientDetail recipientDetail=new RecipientDetail();

        recipientDetail.setReq_date(date_req);
        recipientDetail.setEmailid(emailid);
        recipientDetail.setPhoneno(phoneno);
        recipientDetail.setMsg_id(msg_id);
        recipientDetail.setOther_detail(other_detail);
        recipientDetail.setBloodgroup(blood_data);
        recipientDetail.setState(state_data);
        recipientDetail.setCity(city_data);

        db_ref.child("Notification").child("Recipient").child(msg_id).setValue(recipientDetail);
        Log.i("Mainresult:45689 ", "" + store_list);
        sendmail(store_list);

    }
    public void sendNotificationToUser(final String regId) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String topic = null;
                    String groupFirst = blood_data.substring(0,blood_data.length()-1);
                    Log.d("blood_data11", groupFirst);
                    String grouplast = blood_data.substring(blood_data.length()-1);

                    if (grouplast.equals("+")){
                        topic = groupFirst+"positive";
                        Log.d("blood_data56", topic);
                    }else{
                        topic = groupFirst+"negative";
                        Log.d("blood_data56", topic);
                    }
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();

                    String message1 = "There is requirement of blood group " + blood_data + " in " + city_data + " on " + date_req;
                    notification.put("body", message1);
                    notification.put("title", title);
                    notification.put("icon", "myicon");


                    JSONObject data = new JSONObject();
                    data.put("token_id", regId);
                    data.put("msg_id", msg_id);

                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("priority","high");
//                    root.put("registration_ids", recipients);
                    root.put("to","/topics/"+topic);
                    Log.i("Mainresult:4 ","" + recipients.toString());
                    String result = postToFCM(root.toString());
                    Log.i("Mainresult: ","" + result);

                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                Log.i("Mainresult:45689 ","" + store_list);

                sendmail(store_list);

//                Toast.makeText(RecipientActivity.this, result, Toast.LENGTH_LONG).show();
                dialog.show();
                try {
                    JSONObject resultJson = new JSONObject(result);
                    msg_id = resultJson.getString("message_id");
                    createRecipientDetail(state_data, blood_data, emailid, phoneno, date_req, city_data, other_detail,msg_id);
                    dialog.dismiss();
                    Toast.makeText(RecipientActivity.this, "Message Success: " + msg_id, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("error56",""+e);
                    dialog.dismiss();
                    Toast.makeText(RecipientActivity.this, "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }


    String postToFCM(String bodyString) throws IOException {



//        String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .addHeader("Authorization", "key=AIzaSyB0xP6z55MHoQJkx2uK6rgbXcuYouBNPXM")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }


    private void sendmail(final ArrayList<String> store_list) {

        Log.i("childrollno", "" + blood_data);
        String email = "info@atriodata.com";
        String mail_subject = "Blood Required";
        String message = "There is requirement of blood group " + blood_data + " in " + city_data + " on " + date_req +
                ".\n\n\nDetails of Recipient:\n\nEmail-Id:" + emailid + "\nPhone No: " + phoneno + "\nOther Details: " + other_detail;
        SendMail sm = new SendMail(this, email, mail_subject, message, store_list);
        sm.execute();



        et_date.setText("");
        et_emailid.setText("");
        atvPlaces.setText("");
        et_phoneno.setText("");
        et_remark.setText("");
        sp_bloodgr.setSelection(0);
        spin_state.setSelection(0);
        dialog.dismiss();


    }


    private boolean validate() {
// check whether the field is empty or not
        if (blood_data.equals("Select Your Blood Group")) {
            Toast.makeText(getApplicationContext(), "Select Your Blood Group", Toast.LENGTH_LONG).show();
            return false;


        } else if (et_date.getText().toString().trim().length() < 1) {
            et_date.setError("Please Fill This Field");
            et_date.requestFocus();
            return false;

        } else if (state_data.equals("Select Your State")) {
            Toast.makeText(getApplicationContext(), "Select Your state", Toast.LENGTH_LONG).show();
            return false;

        } else if (atvPlaces.getText().toString().trim().length() < 1) {
            atvPlaces.setError("Please Fill This Field");
            atvPlaces.requestFocus();
            return false;

        } else if (et_emailid.getText().toString().trim().length() < 1 || isEmailValid(et_emailid.getText().toString()) == false) {
            et_emailid.setError("Invalid Email Address");
            et_emailid.requestFocus();
            return false;

        } else if (et_phoneno.getText().toString().trim().length() < 1 || et_phoneno.getText().toString().trim().length() > 12 || et_phoneno.getText().toString().trim().length() < 10) {
            et_phoneno.setError("Please Fill This Field");
            et_phoneno.requestFocus();
            return false;
        } else if (et_remark.getText().toString().trim().length() < 1) {
            et_remark.setError("Please Fill This Field");
            et_remark.requestFocus();
            return false;


        } else
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

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
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

                // Getting the parsed data as a List construct
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

