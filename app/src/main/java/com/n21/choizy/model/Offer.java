package com.n21.choizy.model;

public class Offer{

    String offerId, BranchID,Title,Description,OfferUrl;
    double price;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String branchID) {
        BranchID = branchID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getOfferUrl() {
        return OfferUrl;
    }

    public void setOfferUrl(String offerUrl) {
        OfferUrl = offerUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
