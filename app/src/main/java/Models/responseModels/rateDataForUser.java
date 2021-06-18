
package Models.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class rateDataForUser {

    @SerializedName("TotalTrips")
    @Expose
    private Double totalTrips;
    @SerializedName("TotalReject")
    @Expose
    private Integer totalReject;
    @SerializedName("TotalComplete")
    @Expose
    private Integer totalComplete;
    @SerializedName("TotalCancel")
    @Expose
    private Integer totalCancel;
    @SerializedName("TotalEarning")
    @Expose
    private Double totalEarning;
    @SerializedName("TotalRate")
    @Expose
    private Double totalRate;
    @SerializedName("Flag")
    @Expose
    private Double flag;
    @SerializedName("DriverTime")
    @Expose
    private String DriverTime;
    public Double getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(Double totalTrips) {
        this.totalTrips = totalTrips;
    }

    public Integer getTotalReject() {
        return totalReject;
    }

    public void setTotalReject(Integer totalReject) {
        this.totalReject = totalReject;
    }

    public Integer getTotalComplete() {
        return totalComplete;
    }

    public void setTotalComplete(Integer totalComplete) {
        this.totalComplete = totalComplete;
    }

    public Integer getTotalCancel() {
        return totalCancel;
    }

    public void setTotalCancel(Integer totalCancel) {
        this.totalCancel = totalCancel;
    }

    public Double getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(Double totalEarning) {
        this.totalEarning = totalEarning;
    }

    public Double getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(Double totalRate) {
        this.totalRate = totalRate;
    }

    public Double getFlag() {
        return flag;
    }

    public void setFlag(Double flag) {
        this.flag = flag;
    }

    public String getDriverTime() {
        return DriverTime;
    }

    public void setDriverTime(String driverTime) {
        DriverTime = driverTime;
    }
}
