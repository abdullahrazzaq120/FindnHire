package com.sortscript.findnhire.Activities.ModelsActivities;

public class ModelWorkersList {
    String workerImage, workerName, workerProfession, workerSpecification;

    public ModelWorkersList() {
    }

    public ModelWorkersList(String workerImage, String workerName, String workerProfession, String workerSpecification) {
        this.workerImage = workerImage;
        this.workerName = workerName;
        this.workerProfession = workerProfession;
        this.workerSpecification = workerSpecification;
    }

    public String getWorkerImage() {
        return workerImage;
    }

    public void setWorkerImage(String workerImage) {
        this.workerImage = workerImage;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerProfession() {
        return workerProfession;
    }

    public void setWorkerProfession(String workerProfession) {
        this.workerProfession = workerProfession;
    }

    public String getWorkerSpecification() {
        return workerSpecification;
    }

    public void setWorkerSpecification(String workerSpecification) {
        this.workerSpecification = workerSpecification;
    }
}
