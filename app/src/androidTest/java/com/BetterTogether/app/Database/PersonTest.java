package com.BetterTogether.app.Database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import DB.Dao.PersonDao;
import DB.SQLiteDB;
import DB.Tables.Person;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class PersonTest {

    private PersonDao mPersonDao;
    private SQLiteDB mDb;

    private Person person = new Person("esog");


    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, SQLiteDB.class).build();
        mPersonDao = mDb.personDao();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    public void addPersonToDB() {
        person.setFirstName("Eirin");
        person.setLastName("S");
        mPersonDao.insertPerson(person);
    }


    @Test
    public void getPersonByUsernameGivesBackUserInDB() throws Exception {
        addPersonToDB();

        Person personSearch = mPersonDao.getPerson("esog");

        assertThat(personSearch.getUsername(), equalTo(person.getUsername()));
        assertThat(personSearch.getFirstName(), equalTo(person.getFirstName()));
        assertThat(personSearch.getLastName(), equalTo(person.getLastName()));
    }


    @Test
    public void cannotInsertTwoPersonsWithSameUsername() {
        addPersonToDB();

        Person person2 = new Person("esog");
        try {
            mPersonDao.insertPerson(person2);
            fail("Should get exception when adding two people with same username.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void cannotInsertTwoPersonsWithSameUsernameButDifferentUpperAndLowerCase() {
        addPersonToDB();

        Person person2 = new Person("ESOG");
        try {
            mPersonDao.insertPerson(person2);
            fail("Should get exception when adding two people with same username.");
        } catch (Exception e) {
            //test passed. Should not be able to insert two people with same username.
        }
    }

    @Test
    public void insertPersonIsTheSamePersonWhenFoundBySearch() {
        mPersonDao.insertPerson(person);
        Person personInDB = mPersonDao.getPerson(person.getUsername());
        assertThat(person.getUsername(), equalTo(personInDB.getUsername()));
    }

    @Test
    public void addingPersonYieldsOnePersonInDB() {
        addPersonToDB();

        assertThat(1, equalTo(mPersonDao.getNumberOfUsers()));
    }

    @Test
    public void deletePersonFromDBWithOnlyOnePersonGivesEmptyDB() {
        addPersonToDB();

        mPersonDao.deletePerson(person);
        assertThat(0, equalTo(mPersonDao.getNumberOfUsers()));
    }

    @Test
    public void getsNullWhenTryingToGetDeletedPersonFromDB() {
        addPersonToDB();

        mPersonDao.deletePerson(person);
        Person p = mPersonDao.getPerson("esog");
        assertEquals(null, p);
    }


}
