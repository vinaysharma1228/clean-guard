package com.rbhagat.cleanguard;

public class Users {

    String uName,uPassword,uEmail,uid,profileImage;
    int rewardPoint;

    public Users(String uName, String uPassword, String uEmail, String uid, String profileImage, int rewardPoint) {
        this.uName = uName;
        this.uPassword = uPassword;
        this.uEmail = uEmail;
        this.uid = uid;
        this.profileImage = profileImage;
        this.rewardPoint = rewardPoint;
    }

    public Users()
    {

    }



    public int getRewardPoint() {
        return rewardPoint;
    }

    public void setRewardPoint(int rewardPoint) {
        this.rewardPoint = rewardPoint;
    }


    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
