package com.sortscript.findnhire.Worker.WorkerModels;

public class ModelSetWorker {
    String workerName, workerImage, workerProfession, workerContact, workerSpecification;

    public ModelSetWorker() {
    }

    public ModelSetWorker(String workerName, String workerImage, String workerProfession, String workerContact,
                          String workerSpecification) {
        this.workerName = workerName;
        this.workerImage = workerImage;
        this.workerProfession = workerProfession;
        this.workerContact = workerContact;
        this.workerSpecification = workerSpecification;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerImage() {
        return workerImage;
    }

    public void setWorkerImage(String workerImage) {
        this.workerImage = workerImage;
    }

    public String getWorkerProfession() {
        return workerProfession;
    }

    public void setWorkerProfession(String workerProfession) {
        this.workerProfession = workerProfession;
    }

    public String getWorkerContact() {
        return workerContact;
    }

    public void setWorkerContact(String workerContact) {
        this.workerContact = workerContact;
    }

    public String getWorkerSpecification() {
        return workerSpecification;
    }

    public void setWorkerSpecification(String workerSpecification) {
        this.workerSpecification = workerSpecification;
    }
}
