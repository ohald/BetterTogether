package com.BetterTogether.app.Logic;

import java.util.Date;

public class Pair {

    private String person1;
    private String person2;
    private Date date;

    public Pair(Date date) {
        this.date = date;
    }

    public Pair(String person1, String person2, Date date) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
