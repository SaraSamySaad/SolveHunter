package com.solvehunter.solution.hamza.solvehunter;

/**
 * Created by C.M on 21/10/2018.
 */

public class AllDoctorsData {
    private String chatPrice;
    private String gender;
    private String image;
    private String name;
    private String phone;
    private String type;

    private String Id;

    public AllDoctorsData(String chatPrice, String gender, String image, String name, String phone, String type) {
        this.chatPrice = chatPrice;
        this.gender = gender;
        this.image = image;
        this.name = name;
        this.phone = phone;
        this.type = type;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public AllDoctorsData() {
    }

    public void setChatPrice(String chatPrice) {
        this.chatPrice = chatPrice;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChatPrice() {
        return chatPrice;
    }

    public String getGender() {
        return gender;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }
}
