package com.sortscript.findnhire.ui.notification.ModelUserOfferNotify;

public class ModelUserOfferClass {
    String OfferWorkerId, OfferWorkerName, OfferWorkerSpecification, OfferWorkerImage, OfferWorkerCategory;

    public ModelUserOfferClass() {
    }

    public ModelUserOfferClass(String offerWorkerId, String offerWorkerName, String offerWorkerSpecification, String offerWorkerImage, String offerWorkerCategory) {
        OfferWorkerId = offerWorkerId;
        OfferWorkerName = offerWorkerName;
        OfferWorkerSpecification = offerWorkerSpecification;
        OfferWorkerImage = offerWorkerImage;
        OfferWorkerCategory = offerWorkerCategory;
    }

    public String getOfferWorkerCategory() {
        return OfferWorkerCategory;
    }

    public void setOfferWorkerCategory(String offerWorkerCategory) {
        OfferWorkerCategory = offerWorkerCategory;
    }

    public String getOfferWorkerId() {
        return OfferWorkerId;
    }

    public void setOfferWorkerId(String offerWorkerId) {
        OfferWorkerId = offerWorkerId;
    }

    public String getOfferWorkerName() {
        return OfferWorkerName;
    }

    public void setOfferWorkerName(String offerWorkerName) {
        OfferWorkerName = offerWorkerName;
    }

    public String getOfferWorkerSpecification() {
        return OfferWorkerSpecification;
    }

    public void setOfferWorkerSpecification(String offerWorkerSpecification) {
        OfferWorkerSpecification = offerWorkerSpecification;
    }

    public String getOfferWorkerImage() {
        return OfferWorkerImage;
    }

    public void setOfferWorkerImage(String offerWorkerImage) {
        OfferWorkerImage = offerWorkerImage;
    }
}