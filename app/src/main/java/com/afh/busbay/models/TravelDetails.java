package com.afh.busbay.models;

public class TravelDetails {
    private String date;
    private EveningTravel evening;
    private MorningTravel morning;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public EveningTravel getEvening() {
        return evening;
    }

    public void setEvening(EveningTravel evening) {
        this.evening = evening;
    }

    public MorningTravel getMorning() {
        return morning;
    }

    public void setMorning(MorningTravel morning) {
        this.morning = morning;
    }
}
