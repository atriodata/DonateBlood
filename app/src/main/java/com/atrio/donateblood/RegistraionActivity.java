/*
package com.atrio.donateblood;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class RegistraionActivity extends AppCompatActivity {

    String lati,longti;
    PlacesTask placesTask;
    ParserTask parserTask;
    Spinner citizenship;
    AutoCompleteTextView atvPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraion);
        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);

        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
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
     */
/*   LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 1, new MyLocationListners());
//        LocationListener ll = new MyLocation();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
        final Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

//these are your longtitude and latitude
        lati = String.valueOf(location.getLatitude());
        longti = String.valueOf(location.getLongitude());

//here we are getting the address using the geo codes(longtitude and latitude).

        String ad = getAddress(location.getLatitude(),location.getLongitude());
        Log.i("address4566",""+ad);

        Locale[] locale = Locale.getAvailableLocales();
                    Log.i("loc243",""+locale);

//        Geocoder geocoder4=new Geocoder(this,Locale.getAvailableLocales())
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
//            Log.i("loc23",country);
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );

            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        citizenship = (Spinner)findViewById(R.id.input_citizenship);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        citizenship.setAdapter(adapter);*//*



    }
*/
/*
    private String getAddress(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this);
        Log.i("address45632",""+geocoder);

        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE,LONGITUDE, 5);
//            List<Address> addresses = geocoder.getFromLocationName(strAdd,1);

            if (addresses != null) {

                Address returnedAddress = addresses.get(0);
                Log.i("address456372",""+returnedAddress);
                Log.i("address454632",""+returnedAddress.getLocality());

                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.i("address12","" + strReturnedAddress.toString());
            } else {
                Log.i("address56", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("address89", "Canont get Address!");
        }
        return strAdd;
    }
*//*


*/
/*
    public class MyLocationListners implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }
*//*


    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyAG-AdjAgToyXceK6-ghWS38ho8cALPaUw";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url =
//                    "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=India&sensor=false&types=(regions)&key=AIzaSyAG-AdjAgToyXceK6-ghWS38ho8cALPaUw";
                    "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters+"&components=country:IN";

            try{
                // Fetching the data from we service
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
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
        try{
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
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();
            Log.i("data4565",""+data);

            br.close();

        }catch(Exception e){
            Log.i("url45",""+ e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            Log.i("data45",""+from);
            // Setting the adapter
            atvPlaces.setAdapter(adapter);
        }
    }
}
*/
