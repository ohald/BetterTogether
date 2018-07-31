package com.BetterTogether.app;
import java.util.Objects;


public class Person {

    private String username;
    private String name;
    private boolean active;

    private String image;

    public Person(String username, String name, String image) {
        this(username, name,  image, true);
    }

    public Person(String username, String name, String image, boolean active) {
        this.username = username.toLowerCase();
        this.name = name;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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
