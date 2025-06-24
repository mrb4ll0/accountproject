/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.general;

/**
 *
 * @author dell
 */
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Named("assetDisposalExceptionBean")
@ViewScoped
public class AssetDisposalException implements Serializable {

    private List<DisposalExceptionAsset> disposalExceptions;

    @PostConstruct
    public void init() {
        loadSampleData();
    }

    private void loadSampleData() {
        disposalExceptions = new ArrayList<>();
        
        disposalExceptions.add(new DisposalExceptionAsset(
            "Electronics", "Laptop", 
            toDate(LocalDate.of(2022, 1, 15)),
            new BigDecimal("1200.00"), "HQ", 36, 
            new BigDecimal("800.00"), 12, new BigDecimal("500.00")
        ));
        
        disposalExceptions.add(new DisposalExceptionAsset(
            "Furniture", "Office Desk", 
            toDate(LocalDate.of(2021, 3, 20)),
            new BigDecimal("500.00"), "Branch A", 60, 
            new BigDecimal("300.00"), 24, new BigDecimal("200.00")
        ));
        
        disposalExceptions.add(new DisposalExceptionAsset(
            "Vehicles", "Company Car", 
            toDate(LocalDate.of(2020, 6, 10)),
            new BigDecimal("25000.00"), "Main Office", 84, 
            new BigDecimal("18000.00"), 36, new BigDecimal("20000.00")
        ));
    }

    // Convert LocalDate to java.util.Date
    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public void approve(DisposalExceptionAsset asset) {
        try {
            // Business logic to approve exception
            disposalExceptions.remove(asset);
            showMessage("Approved", "Asset exception approved: " + asset.getAssetName());
        } catch (Exception e) {
            showError("Approval failed: " + e.getMessage());
        }
    }

    public void reject(DisposalExceptionAsset asset) {
        try {
            // Business logic to reject exception
            disposalExceptions.remove(asset);
            showMessage("Rejected", "Asset exception rejected: " + asset.getAssetName());
        } catch (Exception e) {
            showError("Rejection failed: " + e.getMessage());
        }
    }

    public void delete(DisposalExceptionAsset asset) {
        try {
            // Business logic to delete exception
            disposalExceptions.remove(asset);
            showMessage("Deleted", "Asset exception deleted: " + asset.getAssetName());
        } catch (Exception e) {
            showError("Deletion failed: " + e.getMessage());
        }
    }

    private void showMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage("growl",
            new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    private void showError(String message) {
        FacesContext.getCurrentInstance().addMessage("growl",
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }

    // Getters and setters
    public List<DisposalExceptionAsset> getDisposalExceptions() {
        return disposalExceptions;
    }

    public void setDisposalExceptions(List<DisposalExceptionAsset> disposalExceptions) {
        this.disposalExceptions = disposalExceptions;
    }

    // Inner class for the asset exception model
    public static class DisposalExceptionAsset {
        private String id;
        private String category;
        private String assetName;
        private Date purchasedDate; // Changed to java.util.Date
        private BigDecimal amount;
        private String branch;
        private Integer duration;
        private BigDecimal currentValue;
        private Integer depMonth;
        private BigDecimal disposedAmount;

        public DisposalExceptionAsset() {
            this.id = UUID.randomUUID().toString();
        }

        public DisposalExceptionAsset(String category, String assetName, Date purchasedDate,
                                     BigDecimal amount, String branch, Integer duration,
                                     BigDecimal currentValue, Integer depMonth,
                                     BigDecimal disposedAmount) {
            this();
            this.category = category;
            this.assetName = assetName;
            this.purchasedDate = purchasedDate;
            this.amount = amount;
            this.branch = branch;
            this.duration = duration;
            this.currentValue = currentValue;
            this.depMonth = depMonth;
            this.disposedAmount = disposedAmount;
        }

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getAssetName() { return assetName; }
        public void setAssetName(String assetName) { this.assetName = assetName; }
        public Date getPurchasedDate() { return purchasedDate; }
        public void setPurchasedDate(Date purchasedDate) { this.purchasedDate = purchasedDate; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getBranch() { return branch; }
        public void setBranch(String branch) { this.branch = branch; }
        public Integer getDuration() { return duration; }
        public void setDuration(Integer duration) { this.duration = duration; }
        public BigDecimal getCurrentValue() { return currentValue; }
        public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
        public Integer getDepMonth() { return depMonth; }
        public void setDepMonth(Integer depMonth) { this.depMonth = depMonth; }
        public BigDecimal getDisposedAmount() { return disposedAmount; }
        public void setDisposedAmount(BigDecimal disposedAmount) { this.disposedAmount = disposedAmount; }
    }
}