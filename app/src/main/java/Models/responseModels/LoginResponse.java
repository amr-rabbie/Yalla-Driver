
package Models.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("ProfilePicture")
    @Expose
    private String profilePicture;
    @SerializedName("DriverLicenceId")
    @Expose
    private String driverLicenceId;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("ShiftId")
    @Expose
    private String shiftId;
    @SerializedName("CountryCode")
    @Expose
    private String countryCode;
    @SerializedName("TaxiNo")
    @Expose
    private String taxiNo;
    @SerializedName("ModelName")
    @Expose
    private String modelName;
    @SerializedName("DriverRating")
    @Expose
    private String driverRating;
    @SerializedName("CompanyId")
    @Expose
    private String companyId;
    @SerializedName("DeviceId")
    @Expose
    private String deviceId;
    @SerializedName("AccountType")
    @Expose
    private String accountType;
    @SerializedName("RefCode")
    @Expose
    private String refCode;
    @SerializedName("Flag")
    @Expose
    private String flag;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getDriverLicenceId() {
        return driverLicenceId;
    }

    public void setDriverLicenceId(String driverLicenceId) {
        this.driverLicenceId = driverLicenceId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTaxiNo() {
        return taxiNo;
    }

    public void setTaxiNo(String taxiNo) {
        this.taxiNo = taxiNo;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(String driverRating) {
        this.driverRating = driverRating;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
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
        return "LoginResponse{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", driverLicenceId='" + driverLicenceId + '\'' +
                ", lastName='" + lastName + '\'' +
                ", shiftId='" + shiftId + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", taxiNo='" + taxiNo + '\'' +
                ", modelName='" + modelName + '\'' +
                ", driverRating='" + driverRating + '\'' +
                ", companyId='" + companyId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", accountType='" + accountType + '\'' +
                ", refCode='" + refCode + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}