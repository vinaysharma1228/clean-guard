package com.rbhagat.cleanguard;

public class AddressInformation {

    String id,imagelink,description,city,fullAddress,pincode;
    double latitude,longitude;

    public AddressInformation(String id, String image, String description, String city, String fullAddress, String pincode, double latitude, double longitude) {
        this.id = id;
        this.imagelink = image;
        this.description = description;
        this.city = city;
        this.fullAddress = fullAddress;
        this.pincode = pincode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AddressInformation(String imagelink)
    {
        this.imagelink=imagelink;
    }


    public AddressInformation(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return imagelink;
    }

    public void setImage(String image) {
        this.imagelink = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }


    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
