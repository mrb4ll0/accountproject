/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.general;

/**
 *
 * @author Muhammad
 */
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.primefaces.PrimeFaces;

@Named("assetDisposalBean")
@ViewScoped
public class AssetDisposal implements Serializable {

    
    private String selectedCategory;
    private Asset selectedAsset;
    private String purchasedDate;
    private BigDecimal amount;
    private String branch;
    private Integer duration;
    private BigDecimal currentValue;
    private Integer depreciatedMonths;
    private BigDecimal disposedAmount;
    
    
    private String recordStatus = "Pending";
    private String inputter = "Current User";
    private String inputTime;
    private String authoriser = "Not Approved";
    private String authTime = "N/A";
    
    
    private List<String> categories;
    private List<Asset> availableAssets;
    private List<Asset> allAssets;

    @PostConstruct
    public void init() {
        loadCategories();
        loadAllAssets();
        initializeAuditFields();
    }

    private void loadCategories() {
        categories = new ArrayList<>();
        categories.add("Electronics");
        categories.add("Furniture");
        categories.add("Vehicles");
        categories.add("Equipment");
    }

    private void loadAllAssets() {
        allAssets = new ArrayList<>();
        allAssets.add(new Asset("L001", "Laptop", "Electronics", 
                              LocalDate.now().minusMonths(12), 
                              new BigDecimal("1200.00"), "HQ", 
                              36, 12, new BigDecimal("800.00")));
        allAssets.add(new Asset("D001", "Desk", "Furniture", 
                              LocalDate.now().minusMonths(24), 
                              new BigDecimal("500.00"), "Branch A", 
                              60, 24, new BigDecimal("300.00")));
    }

    private void initializeAuditFields() {
        inputTime = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")
        );
    }

    public void onCategoryChange() {
        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            availableAssets = allAssets.stream()
                .filter(a -> a.getCategory().equals(selectedCategory))
                .collect(Collectors.toList());
        } else {
            availableAssets = new ArrayList<>();
        }
        resetAssetDetails();
    }

    public void onAssetSelected() {
        if (selectedAsset != null) {
            purchasedDate = selectedAsset.getPurchaseDate().format(
                DateTimeFormatter.ofPattern("dd-MMM-yyyy")
            );
            amount = selectedAsset.getAmount();
            branch = selectedAsset.getBranch();
            duration = selectedAsset.getDuration();
            depreciatedMonths = selectedAsset.getDepreciatedMonths();
            currentValue = selectedAsset.getCurrentValue();
        } else {
            resetAssetDetails();
        }
    }

    private void resetAssetDetails() {
        purchasedDate = null;
        amount = null;
        branch = null;
        duration = null;
        depreciatedMonths = null;
        currentValue = null;
        disposedAmount = null;
    }

    public void commitDisposal() {
        if (validateDisposal()) {
            FacesContext.getCurrentInstance().addMessage("growl", 
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Success", 
                "Asset " + selectedAsset.getAssetCode() + " disposed successfully"));
            
            PrimeFaces.current().ajax().update("growl");
            resetForm();
        }
    }

    private boolean validateDisposal() {
        if (selectedAsset == null) {
            showError("Please select an asset");
            return false;
        }
        if (disposedAmount == null || disposedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            showError("Disposal amount must be positive");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        FacesContext.getCurrentInstance().addMessage("growl", 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
        PrimeFaces.current().ajax().update("growl");
    }

    private void resetForm() {
        selectedCategory = null;
        selectedAsset = null;
        availableAssets = new ArrayList<>();
        resetAssetDetails();
    }

    public String getSelectedCategory() { return selectedCategory; }
    public void setSelectedCategory(String selectedCategory) { this.selectedCategory = selectedCategory; }
    public Asset getSelectedAsset() { return selectedAsset; }
    public void setSelectedAsset(Asset selectedAsset) { this.selectedAsset = selectedAsset; }
    public String getPurchasedDate() { return purchasedDate; }
    public void setPurchasedDate(String purchasedDate) { this.purchasedDate = purchasedDate; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
    public Integer getDepreciatedMonths() { return depreciatedMonths; }
    public void setDepreciatedMonths(Integer depreciatedMonths) { this.depreciatedMonths = depreciatedMonths; }
    public BigDecimal getDisposedAmount() { return disposedAmount; }
    public void setDisposedAmount(BigDecimal disposedAmount) { this.disposedAmount = disposedAmount; }
    public String getRecordStatus() { return recordStatus; }
    public void setRecordStatus(String recordStatus) { this.recordStatus = recordStatus; }
    public String getInputter() { return inputter; }
    public void setInputter(String inputter) { this.inputter = inputter; }
    public String getInputTime() { return inputTime; }
    public void setInputTime(String inputTime) { this.inputTime = inputTime; }
    public String getAuthoriser() { return authoriser; }
    public void setAuthoriser(String authoriser) { this.authoriser = authoriser; }
    public String getAuthTime() { return authTime; }
    public void setAuthTime(String authTime) { this.authTime = authTime; }
    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }
    public List<Asset> getAvailableAssets() { return availableAssets; }
    public void setAvailableAssets(List<Asset> availableAssets) { this.availableAssets = availableAssets; }


    public static class Asset implements Serializable {
        private String assetCode;
        private String assetName;
        private String category;
        private LocalDate purchaseDate;
        private BigDecimal amount;
        private String branch;
        private Integer duration;
        private Integer depreciatedMonths;
        private BigDecimal currentValue;

        public Asset() {}

        public Asset(String assetCode, String assetName, String category, LocalDate purchaseDate, 
                    BigDecimal amount, String branch, Integer duration, Integer depreciatedMonths, 
                    BigDecimal currentValue) {
            this.assetCode = assetCode;
            this.assetName = assetName;
            this.category = category;
            this.purchaseDate = purchaseDate;
            this.amount = amount;
            this.branch = branch;
            this.duration = duration;
            this.depreciatedMonths = depreciatedMonths;
            this.currentValue = currentValue;
        }

        
        public String getAssetCode() { return assetCode; }
        public void setAssetCode(String assetCode) { this.assetCode = assetCode; }
        public String getAssetName() { return assetName; }
        public void setAssetName(String assetName) { this.assetName = assetName; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public LocalDate getPurchaseDate() { return purchaseDate; }
        public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getBranch() { return branch; }
        public void setBranch(String branch) { this.branch = branch; }
        public Integer getDuration() { return duration; }
        public void setDuration(Integer duration) { this.duration = duration; }
        public Integer getDepreciatedMonths() { return depreciatedMonths; }
        public void setDepreciatedMonths(Integer depreciatedMonths) { this.depreciatedMonths = depreciatedMonths; }
        public BigDecimal getCurrentValue() { return currentValue; }
        public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
    }

    
   
}