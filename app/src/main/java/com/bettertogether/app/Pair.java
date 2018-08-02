package com.bettertogether.app;

public class Pair {

    private String person1;
    private String person2;
    private String date;

    public Pair(String date) {
        this.date = date;
    }

    public Pair(String person1, String person2, String date) {
        this.person1 = person1;
        this.person2 = person2;
        this.date = date;
    }

    public String getPerson1() {
        return person1;
    }

    public void setPerson1(String person1) {
        this.person1 = person1;
    }

    public String getPerson2() {
        return person2;
    }

    public void setPerson2(String person2) {
        this.person2 = person2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
