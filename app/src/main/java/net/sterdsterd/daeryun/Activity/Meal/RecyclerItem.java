package net.sterdsterd.daeryun.Activity.Meal;

public class RecyclerItem {
    private String date, today, lunch, dinner;
    private int colour;

    public RecyclerItem (String date, String today, String lunch, String dinner, int colour){
        this.date = date;
        this.today = today;
        this.lunch = lunch;
        this.dinner = dinner;
        this.colour = colour;
    }

    public String getDate() {
        return date;
    }

    public String getToday() {
        return today;
    }

    public String getLunch() {
        return lunch;
    }

    public String getDinner() {
        return dinner;
    }

    public int getColour() { return  colour; }
}