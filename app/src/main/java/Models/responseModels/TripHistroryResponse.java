
package Models.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripHistroryResponse {

    @SerializedName("TripId")
    @Expose
    private Integer tripId;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("TripDistance")
    @Expose
    private Float tripDistance;
    @SerializedName("TripFare")
    @Expose
    private Float tripFare;
    @SerializedName("TripRate")
    @Expose
    private Float tripRate;
    @SerializedName("PassengerImage")
    @Expose
    private String passengerImage;
    @SerializedName("Flag")
    @Expose
    private Integer flag;

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Float getTripDistance() {
        return tripDistance;
    }

    public void setTripDistance(Float tripDistance) {
        this.tripDistance = tripDistance;
    }

    public Float getTripFare() {
        return tripFare;
    }

    public void setTripFare(Float tripFare) {
        this.tripFare = tripFare;
    }

    public Float getTripRate() {
        return tripRate;
    }

    public void setTripRate(Float tripRate) {
        this.tripRate = tripRate;
    }

    public String getPassengerImage() {
        return passengerImage;
    }

    public void setPassengerImage(String passengerImage) {
        this.passengerImage = passengerImage;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

}
