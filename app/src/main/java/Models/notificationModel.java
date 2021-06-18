package Models;

/**
 * Created by ASI on 4/16/2017.
 */

public class notificationModel {
    String id,title,messaage,date;

    public notificationModel(String id,String title, String messaage,String date) {
        this.title = title;
        this.messaage = messaage;
        this.id=id;
        this.date=date;
    }

    public String getTitle() {
        return title;
    }

    public String getMessaage() {
        return messaage;
    }

    public String getId()
    {
        return id;
    }

    public String getDate() {
        return date;
    }
}
