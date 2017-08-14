package com.atrio.donateblood;

/**
 * Created by Arpita Patel on 14-08-2017.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceJSONParser {
    String City, State, Country;

    public List<HashMap<String, String>> parse(JSONObject jObject) {

        JSONArray jPlaces = null;
        try {
/** Retrieves all the elements in the 'places' array */
            jPlaces = jObject.getJSONArray("predictions");
        } catch (JSONException e) {
            e.printStackTrace();
        }
/** Invoking getPlaces with the array of json object
          * where each json object represent a place
         */
        return getPlaces(jPlaces);
    }


    private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> place = null;
//        HashMap<String, String> State = null;
/** Taking each place, parses and adds to list object */
        for (int i = 0; i < placesCount; i++) {
            try {
/** Call getPlace with place JSON object to parse the place */
                place = getPlace((JSONObject) jPlaces.get(i));
                JSONObject zero2 = jPlaces.getJSONObject(i);
                String long_name = zero2.getString("description");
                JSONArray mtypes = zero2.getJSONArray("types");
                String Type = mtypes.getString(0);

                if (Type.equalsIgnoreCase("locality")) {
                    City = long_name;

                    Log.i("place74", "" + City);

                    Log.e("current city name:", "city:" + City);


                } else {
                    if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                        State = long_name;

                        Log.i("place774", "" + State);

                        Log.e("current city name:", "administrative_area_level_1:" + State);
                    } else if (Type.equalsIgnoreCase("country")) {
                        Country = long_name;
                        Log.i("place784", "" + Country);

                    }
                }

                Log.i("place4S", "" + place);
                placesList.add(place);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;
    }

    /**
     * Parsing the Place JSON object
     */


    private HashMap<String, String> getPlace(JSONObject jPlace) {

        HashMap<String, String> place = new HashMap<String, String>();

        String id = "";
        String reference = "";
        String description = "";

        try {

            description = jPlace.getString("description");
            Log.i("place45city", "" + description.contains("Uttar Pradesh"));

            String desdata = description.substring(description.indexOf("Uttar Pradesh"));
            id = jPlace.getString("id");
            reference = jPlace.getString("reference");

            place.put("description", desdata);
            place.put("_id", id);
            place.put("reference", reference);
            Log.i("place45State", "" + description);
            Log.i("place45Statesss", "" + place);


//            Log.i("place45",""+id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}
