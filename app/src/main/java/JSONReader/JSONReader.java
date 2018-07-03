package JSONReader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.BetterTogether.app.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import DB.Tables.Person;
import DB.Tables.Reward;
import DB.Tables.Threshold;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.GregorianCalendar;

public class JSONReader {

    private static ImageReader reader = new ImageReader();
    private static ObjectMapper mapper = new ObjectMapper();

    //TODO: Write method (that works) to write to JSON file.
    /*
    public static void addPersonToJSONFile(ParsedPerson p){
        try {
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            File jsonfile = new File("file:///../raw/users.json");
            writer.writeValue(jsonfile, p);
        }catch(IOException e){
            e.printStackTrace();
        }
    }*/

    private static ParsedPerson[] getJsonPersonFromJsonFile(Context context) {
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

            //set default picture if none set.
            byte[] image = getImage(context, p.getImage());

            Person newP = new Person(p.getUsername(), p.getFirstname(), p.getLastname(), image, p.getActive());
            personEntries[i] = newP;
        }

        return personEntries;
    }


    //TODO: code duplication, should be possible to fix.
    private static ParsedThreshold[] getJsonThresholdFromJsonFile(Context context) {
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

    private static ParsedReward[] getJsonRewardFromJsonFile(Context context) {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.reward);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            ParsedReward[] parsedRewards  = mapper.readValue(json, new TypeReference<ParsedReward[]>(){});
            return parsedRewards;
        } catch(Exception e){
            Log.d("Error", e.toString());
            return null;
        }
    }

    public static Reward[] parseRewardFromJSON(Context context){
        ParsedReward[] parsedRewards = getJsonRewardFromJsonFile(context);
        Reward[] rewardEntries = new Reward[parsedRewards.length];
        Date d = new Date(new GregorianCalendar(1900,01,01).getTimeInMillis());
        for(int i = 0; i < parsedRewards.length; i++){
            ParsedReward r = parsedRewards[i];
            Reward newR = new Reward(d, r.getRewardtype(), r.getUsedreward());
            d = new Date(d.getTime()+100);
            rewardEntries[i] = newR;
        }
        return rewardEntries;
    }


    private static byte[] getImage(Context context, String imgname){
        if(imgname == null)
            return ImageReader.imageToByte(context, "unknown");
        Bitmap bitmap = reader.imageToBitmap(context, imgname);
        return reader.bitmapToByte(bitmap);
    }
}
