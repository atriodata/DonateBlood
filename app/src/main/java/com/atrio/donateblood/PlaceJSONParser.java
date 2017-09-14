package com.atrio.donateblood;

/**
 * Created by Arpita Patel on 14-08-2017.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceJSONParser {
    String state_data;

    public PlaceJSONParser(String state_data) {
        this.state_data=state_data;
    }

    public List<HashMap<String, String>> parse(JSONObject jObject) {
        JSONArray jPlaces = null;
        try {
            jPlaces = jObject.getJSONArray("predictions");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jPlaces);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> place = null;
        for (int i = 0; i < placesCount; i++) {
            try {
                place = getPlace((JSONObject) jPlaces.get(i));
                if (!place.isEmpty()){
                    placesList.add(place);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }
    private HashMap<String, String> getPlace(JSONObject jPlace) {

        HashMap<String, String> place = new HashMap<String, String>();
        String id = "";
        String reference = "";
        String description = "";
        Boolean desdata ;

        try {
            description = jPlace.getString("description");
            id = jPlace.getString("id");
            reference = jPlace.getString("reference");
//            Log.i("datacity",""+jPlace.toString());
//            Log.i("statedata",""+state_data);

            desdata = description.contains(state_data);//.substring(description.indexOf(state_data));
            if (desdata==true){
                place.put("description", description.substring(0,description.indexOf(",")));
                place.put("_id", id);
                place.put("reference", reference);
            }
            else {
                if (!description.contains("India")){
                    place.put("description", description);
                    place.put("_id", id);
                    place.put("reference", reference);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}
