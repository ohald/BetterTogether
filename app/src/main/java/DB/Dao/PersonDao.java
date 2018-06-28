package DB.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;


import DB.Tables.Person;

@Dao
public interface PersonDao {


    @Query("SELECT * FROM people_table")
    List<Person> getAllPersons();

    @Query("SELECT * FROM people_table WHERE username=:user")
    Person getPerson(String user);

    @Query("SELECT COUNT(*) FROM people_table")
    int getNumberOfUsers();

    @Insert
    void insertPerson(Person person);

    @Insert
    long[] insertAll(Person ... persons);

    @Delete
    //note, all usernames are lower case
    void deletePerson(Person person);
}
