package DB.Tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;


@Entity(tableName = "people_table")
public class Person {

    @PrimaryKey
    @NonNull
    private String username;
    private String firstName;
    private String lastName;
    private boolean active;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public Person(@NonNull String username) {
        this.username = username.toLowerCase();
    }

    @Ignore
    public Person(@NonNull String username, String firstName, String lastName, byte[] image) {
        this.username = username.toLowerCase();
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.active = true;
    }

    @Ignore
    public Person(@NonNull String username, String firstName, String lastName, byte[] image, boolean active) {
        this.username = username.toLowerCase();
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(username, person.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
