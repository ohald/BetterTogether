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

    private static SQLiteDB INSTANCE;

    public abstract PersonDao personDao();

    public abstract RewardDao rewardDao();

    public abstract PairDao pairDao();

    public static SQLiteDB getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder
                    (context.getApplicationContext()
                            , SQLiteDB.class
                            , "PairProgrammingDB")
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                        getInstance(context).personDao().insertAll(JSONReader.parsePersonsFromJSON(context));
                                        getInstance(context).rewardDao().addThresholds(JSONReader.parseThresholdsFromJSON(context));
                                    }
                            );
                        }
                        @Override
                        public void onOpen(@NonNull SupportSQLiteDatabase db){
                            Executors.newSingleThreadExecutor().execute(() -> {
                                List<Person> curPersons = getInstance(context).personDao().getAllPersons();
                                List<Person> newPersons = Arrays.asList(JSONReader.parsePersonsFromJSON(context));

                                if (curPersons.equals(newPersons))
                                    return;
                                updatePersonsInDatabase(curPersons, newPersons, context);
                            });
                        }
                    })
                    .build();
        }
        return INSTANCE;
    }

    /**
     * Add persons that exist in json-file user, but not in database, to database.
     * TODO: What about persons that exist in database, but not in file?
     * @param curPersons
     * @param newPersons
     * @param context
     */
    private static void updatePersonsInDatabase(List<Person> curPersons, List<Person> newPersons, Context context){
            for (Person p : newPersons) {
                if (!curPersons.contains(p)) {
                    getInstance(context).personDao().insertPerson(p);
                }
            }
    }
}