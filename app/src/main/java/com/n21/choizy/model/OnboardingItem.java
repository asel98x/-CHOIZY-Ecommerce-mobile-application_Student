package com.n21.choizy.model;

public class OnboardingItem {
    String title;
    String subTitle;
    String description;
    int img;

    public OnboardingItem() {
    }

    public OnboardingItem(String title, String subTitle, String description, int img) {
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
