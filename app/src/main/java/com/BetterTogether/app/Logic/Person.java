package com.BetterTogether.app.Logic;
import java.util.Objects;


public class Person {

    private String username;
    private String name;
    private boolean active;

    private byte[] image;

    public Person(String username, String name) {
        this(username, name, null, true);
    }

    public Person(String username, String name, byte[] image) {
        this(username, name,  image, true);
    }

    public Person(String username, String name, byte[] image, boolean active) {
        this.username = username.toLowerCase();
        this.name = name;
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

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
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
