
package Models.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


    public class DriverPendingBooking
    {

        @SerializedName("TripId")
        @Expose
        private String tripId;
        @SerializedName("PickUpPlace")
        @Expose
        private String pickUpPlace;
        @SerializedName("DropPlace")
        @Expose
        private String dropPlace;
        @SerializedName("PickupLatitude")
        @Expose
        private String pickupLatitude;
        @SerializedName("DropLatitude")
        @Expose
        private String dropLatitude;
        @SerializedName("PickupLongitude")
        @Expose
        private String pickupLongitude;
        @SerializedName("DropLongitude")
        @Expose
        private String dropLongitude;
        @SerializedName("PickUpTime")
        @Expose
        private String pickUpTime;
        @SerializedName("TaxiId")
        @Expose
        private String taxiId;
        @SerializedName("CompanyId")
        @Expose
        private String companyId;
        @SerializedName("DriverId")
        @Expose
        private String driverId;
        @SerializedName("PassengerId")
        @Expose
        private String passengerId;
        @SerializedName("RoundTrip")
        @Expose
        private String roundTrip;
        @SerializedName("PassengerPhone")
        @Expose
        private String passengerPhone;
        @SerializedName("PassengerName")
        @Expose
        private String passengerName;
        @SerializedName("PassengerProfileImage")
        @Expose
        private String passengerProfileImage;
        @SerializedName("BookedBy")
        @Expose
        private String bookedBy;
        @SerializedName("DriverNotes")
        @Expose
        private String driverNotes;
        @SerializedName("TimeToReachPassenger")
        @Expose
        private String timeToReachPassenger;
        @SerializedName("NotificationTime")
        @Expose
        private String notificationTime;
        @SerializedName("TaxiMinimumSpeed")
        @Expose
        private String taxiMinimumSpeed;
        @SerializedName("SubLogId")
        @Expose
        private String subLogId;
        @SerializedName("TravelStatus")
        @Expose
        private String travelStatus;
        @SerializedName("Distance")
        @Expose
        private String distance;
        @SerializedName("WaitingTime")
        @Expose
        private String waitingTime;
        @SerializedName("BookBy")
        @Expose
        private String bookBy;
        @SerializedName("TripFare")
        @Expose
        private String tripFare;
        @SerializedName("ReferDiscount")
        @Expose
        private String referDiscount;
        @SerializedName("PromotionDiscount")
        @Expose
        private String promotionDiscount;
        @SerializedName("PromotionDiscountType")
        @Expose
        private String promotionDiscountType;
        @SerializedName("PromotionDiscountAmount")
        @Expose
        private String promotionDiscountAmount;
        @SerializedName("PassengerDiscount")
        @Expose
        private String passengerDiscount;
        @SerializedName("NightFareApplicable")
        @Expose
        private String nightFareApplicable;
        @SerializedName("EveningFareApplicable")
        @Expose
        private String eveningFareApplicable;
        @SerializedName("EveningFare")
        @Expose
        private String eveningFare;
        @SerializedName("NightFare")
        @Expose
        private String nightFare;
        @SerializedName("WaitingCost")
        @Expose
        private String waitingCost;
        @SerializedName("TaxAmount")
        @Expose
        private String taxAmount;
        @SerializedName("TotalFare")
        @Expose
        private String totalFare;
        @SerializedName("CompanyTax")
        @Expose
        private String companyTax;
        @SerializedName("WaitingPerHour")
        @Expose
        private String waitingPerHour;
        @SerializedName("MinutesTraveled")
        @Expose
        private String minutesTraveled;
        @SerializedName("MinutesFare")
        @Expose
        private String minutesFare;
        @SerializedName("Metric")
        @Expose
        private String metric;
        @SerializedName("ActualTripFare")
        @Expose
        private String actualTripFare;
        @SerializedName("BaseFare")
        @Expose
        private String baseFare;
        @SerializedName("Flag")
        @Expose
        private String flag;

        public String getTripId() {
            return tripId;
        }

        public void setTripId(String tripId) {
            this.tripId = tripId;
        }

        public String getPickUpPlace() {
            return pickUpPlace;
        }

        public void setPickUpPlace(String pickUpPlace) {
            this.pickUpPlace = pickUpPlace;
        }

        public String getDropPlace() {
            return dropPlace;
        }

        public void setDropPlace(String dropPlace) {
            this.dropPlace = dropPlace;
        }

        public String getPickupLatitude() {
            return pickupLatitude;
        }

        public void setPickupLatitude(String pickupLatitude) {
            this.pickupLatitude = pickupLatitude;
        }

        public String getDropLatitude() {
            return dropLatitude;
        }

        public void setDropLatitude(String dropLatitude) {
            this.dropLatitude = dropLatitude;
        }

        public String getPickupLongitude() {
            return pickupLongitude;
        }

        public void setPickupLongitude(String pickupLongitude) {
            this.pickupLongitude = pickupLongitude;
        }

        public String getDropLongitude() {
            return dropLongitude;
        }

        public void setDropLongitude(String dropLongitude) {
            this.dropLongitude = dropLongitude;
        }

        public String getPickUpTime() {
            return pickUpTime;
        }

        public void setPickUpTime(String pickUpTime) {
            this.pickUpTime = pickUpTime;
        }

        public String getTaxiId() {
            return taxiId;
        }

        public void setTaxiId(String taxiId) {
            this.taxiId = taxiId;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getDriverId() {
            return driverId;
        }

        public void setDriverId(String driverId) {
            this.driverId = driverId;
        }

        public String getPassengerId() {
            return passengerId;
        }

        public void setPassengerId(String passengerId) {
            this.passengerId = passengerId;
        }

        public String getRoundTrip() {
            return roundTrip;
        }

        public void setRoundTrip(String roundTrip) {
            this.roundTrip = roundTrip;
        }

        public String getPassengerPhone() {
            return passengerPhone;
        }

        public void setPassengerPhone(String passengerPhone) {
            this.passengerPhone = passengerPhone;
        }

        public String getPassengerName() {
            return passengerName;
        }

        public void setPassengerName(String passengerName) {
            this.passengerName = passengerName;
        }

        public String getPassengerProfileImage() {
            return passengerProfileImage;
        }

        public void setPassengerProfileImage(String passengerProfileImage) {
            this.passengerProfileImage = passengerProfileImage;
        }

        public String getBookedBy() {
            return bookedBy;
        }

        public void setBookedBy(String bookedBy) {
            this.bookedBy = bookedBy;
        }

        public String getDriverNotes() {
            return driverNotes;
        }

        public void setDriverNotes(String driverNotes) {
            this.driverNotes = driverNotes;
        }

        public String getTimeToReachPassenger() {
            return timeToReachPassenger;
        }

        public void setTimeToReachPassenger(String timeToReachPassenger) {
            this.timeToReachPassenger = timeToReachPassenger;
        }

        public String getNotificationTime() {
            return notificationTime;
        }

        public void setNotificationTime(String notificationTime) {
            this.notificationTime = notificationTime;
        }

        public String getTaxiMinimumSpeed() {
            return taxiMinimumSpeed;
        }

        public void setTaxiMinimumSpeed(String taxiMinimumSpeed) {
            this.taxiMinimumSpeed = taxiMinimumSpeed;
        }

        public String getSubLogId() {
            return subLogId;
        }

        public void setSubLogId(String subLogId) {
            this.subLogId = subLogId;
        }

        public String getTravelStatus() {
            return travelStatus;
        }

        public void setTravelStatus(String travelStatus) {
            this.travelStatus = travelStatus;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getWaitingTime() {
            return waitingTime;
        }

        public void setWaitingTime(String waitingTime) {
            this.waitingTime = waitingTime;
        }

        public String getBookBy() {
            return bookBy;
        }

        public void setBookBy(String bookBy) {
            this.bookBy = bookBy;
        }

        public String getTripFare() {
            return tripFare;
        }

        public void setTripFare(String tripFare) {
            this.tripFare = tripFare;
        }

        public String getReferDiscount() {
            return referDiscount;
        }

        public void setReferDiscount(String referDiscount) {
            this.referDiscount = referDiscount;
        }

        public String getPromotionDiscount() {
            return promotionDiscount;
        }

        public void setPromotionDiscount(String promotionDiscount) {
            this.promotionDiscount = promotionDiscount;
        }

        public String getPromotionDiscountType() {
            return promotionDiscountType;
        }

        public void setPromotionDiscountType(String promotionDiscountType) {
            this.promotionDiscountType = promotionDiscountType;
        }

        public String getPromotionDiscountAmount() {
            return promotionDiscountAmount;
        }

        public void setPromotionDiscountAmount(String promotionDiscountAmount) {
            this.promotionDiscountAmount = promotionDiscountAmount;
        }

        public String getPassengerDiscount() {
            return passengerDiscount;
        }

        public void setPassengerDiscount(String passengerDiscount) {
            this.passengerDiscount = passengerDiscount;
        }

        public String getNightFareApplicable() {
            return nightFareApplicable;
        }

        public void setNightFareApplicable(String nightFareApplicable) {
            this.nightFareApplicable = nightFareApplicable;
        }

        public String getEveningFareApplicable() {
            return eveningFareApplicable;
        }

        public void setEveningFareApplicable(String eveningFareApplicable) {
            this.eveningFareApplicable = eveningFareApplicable;
        }

        public String getEveningFare() {
            return eveningFare;
        }

        public void setEveningFare(String eveningFare) {
            this.eveningFare = eveningFare;
        }

        public String getNightFare() {
            return nightFare;
        }

        public void setNightFare(String nightFare) {
            this.nightFare = nightFare;
        }

        public String getWaitingCost() {
            return waitingCost;
        }

        public void setWaitingCost(String waitingCost) {
            this.waitingCost = waitingCost;
        }

        public String getTaxAmount() {
            return taxAmount;
        }

        public void setTaxAmount(String taxAmount) {
            this.taxAmount = taxAmount;
        }

        public String getTotalFare() {
            return totalFare;
        }

        public void setTotalFare(String totalFare) {
            this.totalFare = totalFare;
        }

        public String getCompanyTax() {
            return companyTax;
        }

        public void setCompanyTax(String companyTax) {
            this.companyTax = companyTax;
        }

        public String getWaitingPerHour() {
            return waitingPerHour;
        }

        public void setWaitingPerHour(String waitingPerHour) {
            this.waitingPerHour = waitingPerHour;
        }

        public String getMinutesTraveled() {
            return minutesTraveled;
        }

        public void setMinutesTraveled(String minutesTraveled) {
            this.minutesTraveled = minutesTraveled;
        }

        public String getMinutesFare() {
            return minutesFare;
        }

        public void setMinutesFare(String minutesFare) {
            this.minutesFare = minutesFare;
        }

        public String getMetric() {
            return metric;
        }

        public void setMetric(String metric) {
            this.metric = metric;
        }

        public String getActualTripFare() {
            return actualTripFare;
        }

        public void setActualTripFare(String actualTripFare) {
            this.actualTripFare = actualTripFare;
        }

        public String getBaseFare() {
            return baseFare;
        }

        public void setBaseFare(String baseFare) {
            this.baseFare = baseFare;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        @Override
        public String toString()
        {
            return "DriverPendingBooking{" +
                    "tripId='" + tripId + '\'' +
                    ", pickUpPlace='" + pickUpPlace + '\'' +
                    ", dropPlace='" + dropPlace + '\'' +
                    ", pickupLatitude='" + pickupLatitude + '\'' +
                    ", dropLatitude='" + dropLatitude + '\'' +
                    ", pickupLongitude='" + pickupLongitude + '\'' +
                    ", dropLongitude='" + dropLongitude + '\'' +
                    ", pickUpTime='" + pickUpTime + '\'' +
                    ", taxiId='" + taxiId + '\'' +
                    ", companyId='" + companyId + '\'' +
                    ", driverId='" + driverId + '\'' +
                    ", passengerId='" + passengerId + '\'' +
                    ", roundTrip='" + roundTrip + '\'' +
                    ", passengerPhone='" + passengerPhone + '\'' +
                    ", passengerName='" + passengerName + '\'' +
                    ", passengerProfileImage='" + passengerProfileImage + '\'' +
                    ", bookedBy='" + bookedBy + '\'' +
                    ", driverNotes='" + driverNotes + '\'' +
                    ", timeToReachPassenger='" + timeToReachPassenger + '\'' +
                    ", notificationTime='" + notificationTime + '\'' +
                    ", taxiMinimumSpeed='" + taxiMinimumSpeed + '\'' +
                    ", subLogId='" + subLogId + '\'' +
                    ", travelStatus='" + travelStatus + '\'' +
                    ", distance='" + distance + '\'' +
                    ", waitingTime='" + waitingTime + '\'' +
                    ", bookBy='" + bookBy + '\'' +
                    ", tripFare='" + tripFare + '\'' +
                    ", referDiscount='" + referDiscount + '\'' +
                    ", promotionDiscount='" + promotionDiscount + '\'' +
                    ", promotionDiscountType='" + promotionDiscountType + '\'' +
                    ", promotionDiscountAmount='" + promotionDiscountAmount + '\'' +
                    ", passengerDiscount='" + passengerDiscount + '\'' +
                    ", nightFareApplicable='" + nightFareApplicable + '\'' +
                    ", eveningFareApplicable='" + eveningFareApplicable + '\'' +
                    ", eveningFare='" + eveningFare + '\'' +
                    ", nightFare='" + nightFare + '\'' +
                    ", waitingCost='" + waitingCost + '\'' +
                    ", taxAmount='" + taxAmount + '\'' +
                    ", totalFare='" + totalFare + '\'' +
                    ", companyTax='" + companyTax + '\'' +
                    ", waitingPerHour='" + waitingPerHour + '\'' +
                    ", minutesTraveled='" + minutesTraveled + '\'' +
                    ", minutesFare='" + minutesFare + '\'' +
                    ", metric='" + metric + '\'' +
                    ", actualTripFare='" + actualTripFare + '\'' +
                    ", baseFare='" + baseFare + '\'' +
                    ", flag='" + flag + '\'' +
                    '}';
        }
    }