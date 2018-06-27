package JSONReader;

import android.content.Context;
import android.util.Log;

import com.BetterTogether.app.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import DB.Tables.Person;
import DB.Tables.Threshold;


import java.io.InputStream;

public class JSONReader {

    private static ParsedPerson[] getJsonPersonFromJsonFile(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream is = context.getResources().openRawResource(R.raw.users);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");


            ParsedPerson[] parsedPeople  = mapper.readValue(json, new TypeReference<ParsedPerson[]>(){});
            return parsedPeople;
        } catch(Exception e){
            Log.d("Error", e.toString());
            return null;
        }
    }

    public static Person[] parsePersonsFromJSON(Context context){
        ParsedPerson[] parsedPeople = getJsonPersonFromJsonFile(context);
        Person[] personEntries = new Person[parsedPeople.length];
        for(int i = 0; i < parsedPeople.length; i++){
            ParsedPerson p = parsedPeople[i];
            Person newP = new Person(p.getUsername(), p.getFirstname(), p.getLastname());
            personEntries[i] = newP;
        }

        return personEntries;
    }

    //TODO: code duplication, should be possible to fix.
    private static ParsedThreshold[] getJsonThresholdFromJsonFile(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream is = context.getResources().openRawResource(R.raw.threshold);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");


            ParsedThreshold[] parsedThresholds  = mapper.readValue(json, new TypeReference<ParsedThreshold[]>(){});
            return parsedThresholds;
        } catch(Exception e){
            Log.d("Error", e.toString());
            return null;
        }
    }

    public static Threshold[] parseThresholdsFromJSON(Context context){
        ParsedThreshold[] parsedThresholds = getJsonThresholdFromJsonFile(context);
        Threshold[] thresholdEntries = new Threshold[parsedThresholds.length];
        for(int i = 0; i < parsedThresholds.length; i++){
            ParsedThreshold t = parsedThresholds[i];
            Threshold newT = new Threshold(t.getRewardtype(), t.getThreshold());
            thresholdEntries[i] = newT;
        }

        return thresholdEntries;
    }


    private byte[] getImage(){
        //TODO: write method to get image for databasefriendly storage
        return null;
    }
}
