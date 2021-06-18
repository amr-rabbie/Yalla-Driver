package Models;

/**
 * Created by ASI on 4/23/2017.
 */

public class commentsModel {

    String comment,rate,date;

    public commentsModel(String comment, String rate, String date) {
        this.comment = comment;
        this.rate = rate;
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public String getRate() {
        return rate;
    }

    public String getDate() {
        return date;
    }
}
