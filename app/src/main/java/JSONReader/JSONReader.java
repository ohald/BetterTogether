package JSONReader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.BetterTogether.app.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import DB.Tables.Person;
import DB.Tables.Reward;
import DB.Tables.Threshold;


import java.io.InputStream;
import java.util.Date;
import java.util.GregorianCalendar;


public class JSONReader {

    private static ImageReader reader = new ImageReader();
    private static ObjectMapper mapper = new ObjectMapper();


    public static Person[] parsePersonsFromJson(Context context) {
        ParsedPerson[] parsedPeople = getJsonPersonFromAppJsonFile(context);
        Person[] personEntries = new Person[parsedPeople.length];
        for (int i = 0; i < parsedPeople.length; i++) {
            ParsedPerson p = parsedPeople[i];

            //set default picture if none set.
            byte[] image = getImage(context, p.getImage());

            Person newP = new Person(p.getUsername(), p.getFirstname(), p.getLastname(), image, p.getActive());
            personEntries[i] = newP;
        }

        return personEntries;
    }


    private static ParsedPerson[] getJsonPersonFromAppJsonFile(Context context) {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.users);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            ParsedPerson[] parsedPeople = mapper.readValue(json, new TypeReference<ParsedPerson[]>() {
            });
            return parsedPeople;
        } catch (Exception e) {
            Log.d("Error", e.toString());
            return null;
        }
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
            ParsedThreshold[] parsedThresholds = mapper.readValue(json, new TypeReference<ParsedThreshold[]>() {
            });
            return parsedThresholds;
        } catch (Exception e) {
            Log.d("Error", e.toString());
            return null;
        }
    }

    public static Threshold[] parseThresholdsFromJSON(Context context) {
        ParsedThreshold[] parsedThresholds = getJsonThresholdFromJsonFile(context);
        Threshold[] thresholdEntries = new Threshold[parsedThresholds.length];
        for (int i = 0; i < parsedThresholds.length; i++) {
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
            ParsedReward[] parsedRewards = mapper.readValue(json, new TypeReference<ParsedReward[]>() {
            });
            return parsedRewards;
        } catch (Exception e) {
            Log.d("Error", e.toString());
            return null;
        }
    }

    public static Reward[] parseRewardFromJSON(Context context) {
        ParsedReward[] parsedRewards = getJsonRewardFromJsonFile(context);
        Reward[] rewardEntries = new Reward[parsedRewards.length];
        Date d = new Date(new GregorianCalendar(1900, 01, 01).getTimeInMillis());
        for (int i = 0; i < parsedRewards.length; i++) {
            ParsedReward r = parsedRewards[i];
            Reward newR = new Reward(d, r.getRewardtype(), r.getUsedreward());
            d = new Date(d.getTime() + 100);
            rewardEntries[i] = newR;
        }
        return rewardEntries;
    }


    private static byte[] getImage(Context context, String imgname) {
        if (imgname == null)
            return ImageReader.imageToByte(context, "unknown");
        Bitmap bitmap = reader.imageToBitmap(context, imgname);
        return reader.bitmapToByte(bitmap);
    }



    //INTERNAL STORAGE CODE, USELESS??


    /*
    private static ParsedPerson createParsedPersonFromPerson(Person p) {
        ParsedPerson person = new ParsedPerson();
        person.setUsername(p.getUsername());
        person.setFirstname(p.getFirstName());
        person.setLastname(p.getLastName());
        person.setActive(Boolean.toString(p.isActive()));
        return person;
    }


    /
     * Copies the res/raw/users.json file to internal storage, to use
     * for keeping database updated.
     * @param context
     /
    public static void createInternalJsonUsersFileFromAppUsersFile(Context context) {
        new File(context.getFilesDir(), personFileName);
        try {
            //open JSON file on app
            InputStream is = context.getResources().openRawResource(R.raw.users);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            //write to internal file
            FileOutputStream outputStream = context.openFileOutput(personFileName, context.MODE_APPEND);
            outputStream.write(json.getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void writeUserToInternalUsersJsonFile(Context context, Person p) {
        try {
            //create a new list with new element
            List<ParsedPerson> parsedPeople = Arrays.asList(readUsersFromInternalStorage(context));
            List<ParsedPerson> parsedPeopleList = new ArrayList<ParsedPerson>(parsedPeople);

            ParsedPerson person = createParsedPersonFromPerson(p);
            parsedPeopleList.add(person);

            //write new entries to internal jsonfile
            FileOutputStream out = context.openFileOutput(personFileName, context.MODE_PRIVATE);
            mapper.writer().withDefaultPrettyPrinter().writeValue(out, parsedPeopleList);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ParsedPerson[] readUsersFromInternalStorage(Context context) {
        try {
            //read from
            InputStream is = context.openFileInput(personFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String filecontent = new String(buffer, "UTF-8");
            return mapper.readValue(filecontent, new TypeReference<ParsedPerson[]>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    */


}
