package Models;

/**
 * Created by ASI on 5/30/2017.
 */

public class promotionCodeModel {
    String tripId,code,discountAmount,date;

    public promotionCodeModel(String tripId, String code, String discountAmount, String date) {
        this.tripId = tripId;
        this.code = code;
        this.discountAmount = discountAmount;
        this.date = date;
    }

    public String getTripId() {
        return tripId;
    }

    public String getCode() {
        return code;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public String getDate() {
        return date;
    }
}
