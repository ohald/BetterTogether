package com.BetterTogether.app.Database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;

import DB.Dao.PairDao;
import DB.Dao.PersonDao;
import DB.SQLiteDB;
import DB.Tables.Pair;
import DB.Tables.Person;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PairTest {

    private PairDao mPairDao;
    private PersonDao mPersonDao;
    private SQLiteDB mDb;
    private Pair pair;
    private GregorianCalendar calendar = new GregorianCalendar(1900, 01, 1, 00, 00, 00);
    private Date date = new Date(calendar.getTimeInMillis());

    private Date testDate = new Date();

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, SQLiteDB.class).build();
        mPairDao = mDb.pairDao();
        mPersonDao = mDb.personDao();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    private void addPairToDB() {
        Person esog = new Person("esog");
        Person ohald = new Person("ohald");
        mPersonDao.insertPerson(esog);
        mPersonDao.insertPerson(ohald);

        this.pair = new Pair(testDate);
        pair.setPerson1("esog");
        pair.setPerson2("ohald");
        mPairDao.insertPair(pair);
    }

    @Test
    public void updatingSecondPersonInPairChangesSecondPersonInPair() {
        addPairToDB();
        mPersonDao.insertPerson(new Person("other"));
        this.pair.setPerson2("other");
        mPairDao.updatePair(pair);

        assertThat("other", equalTo(mPairDao.getPair(testDate).getPerson2()));
        assertThat(1, equalTo(mPairDao.getPairProgrammingTotalFromDate(date)));
    }


    @Test
    public void addPairToEmptyDBGivesOnePairInDB() {
        addPairToDB();
        assertThat(1, equalTo(mPairDao.getHistory(date).size()));
    }

    @Test
    public void deletePairFromDBRemovesPairFromDB() {
        addPairToDB();
        mPairDao.deletePair(pair);

        assertThat(mPairDao.getTimesPairProgrammed("ohald"), equalTo(0));
        assertThat(mPairDao.getTimesPairProgrammed("esog"), equalTo(0));
        assertThat(mPairDao.getPairProgrammingTotalFromDate(date), equalTo(0));
    }


}
