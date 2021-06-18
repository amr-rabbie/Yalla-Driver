
package Models.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverWeeklyEarning {

    @SerializedName("StartDt")
    @Expose
    private String startDt;
    @SerializedName("EndDt")
    @Expose
    private String endDt;
    @SerializedName("Sat")
    @Expose
    private String sat;
    @SerializedName("Sun")
    @Expose
    private String sun;
    @SerializedName("Mon")
    @Expose
    private String mon;
    @SerializedName("Tus")
    @Expose
    private String tus;
    @SerializedName("Wen")
    @Expose
    private String wen;
    @SerializedName("Thr")
    @Expose
    private String thr;
    @SerializedName("Fri")
    @Expose
    private String fri;
    @SerializedName("Total")
    @Expose
    private String total;

    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public String getSun() {
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getTus() {
        return tus;
    }

    public void setTus(String tus) {
        this.tus = tus;
    }

    public String getWen() {
        return wen;
    }

    public void setWen(String wen) {
        this.wen = wen;
    }

    public String getThr() {
        return thr;
    }

    public void setThr(String thr) {
        this.thr = thr;
    }

    public String getFri() {
        return fri;
    }

    public void setFri(String fri) {
        this.fri = fri;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
