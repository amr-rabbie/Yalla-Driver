package Models;

/**
 * Created by m.khalid on 4/12/2017.
 */

public class tripshistorymodel
{

    String id,date,distance,fare,rate,image;

    public tripshistorymodel(String id, String date, String distance, String fare, String rate, String image) {
        this.id = id;
        this.date = date;
        this.distance = distance;
        this.fare = fare;
        this.rate = rate;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDistance() {
        return distance;
    }

    public String getFare() {
        return fare;
    }

    public String getRate() {
        return rate;
    }

    public String getImage() {
        return image;
    }
}
