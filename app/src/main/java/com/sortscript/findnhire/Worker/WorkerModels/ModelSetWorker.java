package com.sortscript.findnhire.Worker.WorkerModels;

public class ModelSetWorker {
    String WorkerName, WorkerCategory, WorkerPhone, WorkerAvailableDate, WorkerAvailableTime,
            WorkerAddress, WorkerImage, WorkerGender, WorkerSpecification;

    public ModelSetWorker() {
    }

    public ModelSetWorker(String workerName, String workerCategory, String workerPhone,
                          String workerAvailableDate, String workerAvailableTime, String workerAddress,
                          String workerImage, String workerGender, String workerSpecification) {
        WorkerName = workerName;
        WorkerCategory = workerCategory;
        WorkerPhone = workerPhone;
        WorkerAvailableDate = workerAvailableDate;
        WorkerAvailableTime = workerAvailableTime;
        WorkerAddress = workerAddress;
        WorkerImage = workerImage;
        WorkerGender = workerGender;
        WorkerSpecification = workerSpecification;
    }

    public String getWorkerName() {
        return WorkerName;
    }

    public void setWorkerName(String workerName) {
        WorkerName = workerName;
    }

    public String getWorkerCategory() {
        return WorkerCategory;
    }

    public void setWorkerCategory(String workerCategory) {
        WorkerCategory = workerCategory;
    }

    public String getWorkerPhone() {
        return WorkerPhone;
    }

    public void setWorkerPhone(String workerPhone) {
        WorkerPhone = workerPhone;
    }

    public String getWorkerAvailableDate() {
        return WorkerAvailableDate;
    }

    public void setWorkerAvailableDate(String workerAvailableDate) {
        WorkerAvailableDate = workerAvailableDate;
    }

    public String getWorkerAvailableTime() {
        return WorkerAvailableTime;
    }

    public void setWorkerAvailableTime(String workerAvailableTime) {
        WorkerAvailableTime = workerAvailableTime;
    }

    public String getWorkerAddress() {
        return WorkerAddress;
    }

    public void setWorkerAddress(String workerAddress) {
        WorkerAddress = workerAddress;
    }

    public String getWorkerImage() {
        return WorkerImage;
    }

    public void setWorkerImage(String workerImage) {
        WorkerImage = workerImage;
    }

    public String getWorkerGender() {
        return WorkerGender;
    }

    public void setWorkerGender(String workerGender) {
        WorkerGender = workerGender;
    }

    public String getWorkerSpecification() {
        return WorkerSpecification;
    }

    public void setWorkerSpecification(String workerSpecification) {
        WorkerSpecification = workerSpecification;
    }
}
