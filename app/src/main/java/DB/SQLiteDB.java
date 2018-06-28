package DB;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

import DB.Dao.PairDao;
import DB.Dao.PersonDao;
import DB.Dao.RewardDao;
import DB.Tables.Pair;
import DB.Tables.Person;
import DB.Tables.Reward;
import DB.Tables.Threshold;

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
                                        getInstance(context).personDao().insertAll(Person.initialUsers());
                                        getInstance(context).rewardDao().addThresholds(Threshold.initialThresholds());
                                    }
                            );
                        }
                    })
                    .build();
        }
        return INSTANCE;

    }
}