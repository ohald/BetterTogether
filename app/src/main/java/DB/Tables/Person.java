package DB.Tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;




@Entity(tableName = "people_table")
public class Person {

    @PrimaryKey
    @NonNull
    private String username;

    @ColumnInfo
    private String firstName;

    @ColumnInfo
    private String lastName;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public Person(@NonNull String username) {
        this.username = username.toLowerCase();
    }

    @Ignore
    public Person(@NonNull String username, String firstName, String lastName) {
        this.username = username.toLowerCase();
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getUsername() {
        return username;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    public String getFirstName() {

        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public byte[] getImage() {
        return image;
    }


    public static Person[] initialUsers(){
        Person[] p = new Person[3];
        p[0] = (new Person("esog", "Eirin", "Sognnes"));
        p[1] = (new Person("ohald", "øyvor", "haldorsen"));
        p[2] = (new Person("mleik", "magnus", "leikvoll"));
        return p;
    }

}
