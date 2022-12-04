package com.n21.choizy.model;

public class Category {

    private String category_name,category_colour,imageURL,key;
    public Category() {

    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_colour() {
        return category_colour;
    }

    public void setCategory_colour(String category_colour) {
        this.category_colour = category_colour;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
