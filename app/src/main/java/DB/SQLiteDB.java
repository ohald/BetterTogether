package DB;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import DB.Dao.PairDao;
import DB.Dao.PersonDao;
import DB.Dao.RewardDao;
import DB.Tables.Pair;
import DB.Tables.Person;
import DB.Tables.Reward;
import DB.Tables.Threshold;
import JSONReader.JSONReader;

@Database(entities = {Person.class, Pair.class, Reward.class, Threshold.class}, version = 2, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class SQLiteDB extends RoomDatabase {

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

        }
    };
    private static SQLiteDB INSTANCE;

    public static SQLiteDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext()
                            , SQLiteDB.class
                            , "PairProgrammingDB")
                    .addMigrations(MIGRATION_2_3)
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            Executors.newSingleThreadExecutor().execute(() -> {
                                getInstance(context).rewardDao().addThresholds(JSONReader.parseThresholdsFromJSON(context));
                                getInstance(context).rewardDao().addReward(JSONReader.parseRewardFromJSON(context));
                            });
                        }})
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    /**
     * This method only allows you to add or remove persons from database, using the file
     * users.json. It does NOT make changes to images or other fields.
     *
     * @param curPersons
     * @param newPersons
     * @param context
     */
    private static void updatePersonsInDatabase(List<Person> curPersons, List<Person> newPersons, Context context) {
        addNewEntriesToDatabase(curPersons, newPersons, context);
        //setDeletedPersonEntriesToAnonymous(curPersons, newPersons, context);
    }

    private static void addNewEntriesToDatabase(List<Person> curPersons, List<Person> newPersons, Context context) {
        for (Person p : newPersons) {
            if (!curPersons.contains(p)) {
                getInstance(context).personDao().insertPerson(p);
            }
        }
    }

    public void refreshDB(Context context) {
            List<Person> curPersons = personDao().getAllPersons();
            Person[] newPersonsArray = JSONReader.parsePersonsFromJson(context);

            if(newPersonsArray == null)
                return;

            List<Person> newPersons = Arrays.asList(newPersonsArray);
            if (curPersons.equals(newPersons))
                return;
            updatePersonsInDatabase(curPersons, newPersons, context);
    }

    public abstract PersonDao personDao();

    public abstract RewardDao rewardDao();

    public abstract PairDao pairDao();

    //Not useful when database cannot write to resources.
    /*
    private static void setDeletedPersonEntriesToAnonymous(List<Person> curPersons, List<Person> newPersons, Context context) {
        PersonDao personDao = getInstance(context).personDao();

        for(Person p : curPersons){
            if(!newPersons.contains(p)){
                p.setFirstName("Anonymous");
                p.setLastName("Worker");
                p.setActive(false);
                p.setImage(ImageReader.imageToByte(context, "unknown"));
                personDao.updatePerson(p);
            }

        }

    }
    */

}