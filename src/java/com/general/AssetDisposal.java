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
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import org.primefaces.PrimeFaces;

@ManagedBean(name="assetDisposalBean")
@ViewScoped
public class AssetDisposal implements Converter, Serializable {

    
    private String selectedCategory;
    private String selectedBranch;
    private Asset selectedAsset;
    private String purchasedDate;
    private BigDecimal amount;
    private String branch;
    private Integer duration;
    private BigDecimal currentValue;
    private Integer depreciatedMonths;
    private BigDecimal disposedAmount;
    private List<String> branches;
    
    
    private String recordStatus ;
    private String inputter ;
    private String inputTime;
    private String authoriser;
    private String authTime;
    private BigDecimal profit;
    private BigDecimal profitAmount;

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }
    
    
    private List<String> categories;
    private List<Asset> availableAssets;
    private List<Asset> allAssets;

    @PostConstruct
    public void init() {
        loadCategories();
        loadAllAssets();
        initializeAuditFields();
    }

    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(List<String> branches) {
        this.branches = branches;
    }
    
    
    public String getSelectedBranch() {
        return selectedBranch;
    }

    public void setSelectedBranch(String selectedBranch) {
        this.selectedBranch = selectedBranch;
    }

    public List<Asset> getAllAssets() {
        return allAssets;
    }

    public void setAllAssets(List<Asset> allAssets) {
        this.allAssets = allAssets;
    }

    private void loadCategories() {
        categories = new ArrayList<>();
    }
    
    public void onSelectBranch()
    {
        
    }

    private void loadAllAssets() {
        allAssets = new ArrayList<>();
    }
    
    

    private void initializeAuditFields() {

    }

    public void onCategoryChange() {
        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            availableAssets = allAssets.stream()
                .filter(a -> a.getFAPcategory().equals(selectedCategory))
                .collect(Collectors.toList());
        } else {
            availableAssets = new ArrayList<>();
        }
        resetAssetDetails();
    }

    public void onAssetSelected() {
        
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


    public static class Asset implements Serializable{

    private String FAPcatID;
    private String FAPcategory;
    private String FAPdepExpAcct;
    private String FAPdepExpAcctName;
    private String FAPPrePayAcct;
    private String FAPPrePayAcctName;
    private String AssetAccount;
    private String AssetAccountName;
    private String DepExpenseAccount;
    private String DepExpenseAccountName;
    private String FAPdepDate;
    private String RecordStatus;
    private String Inputter;
    private String InputterRec;
    private String Authoriser;
    private String AuthoriserRec;
    private String updatetype;
    private String FAPtenancy;
    private String assetName;
    private BigDecimal assetAmount;
    private int durationsMonth;
    private String branch; 
    private java.sql.Date purchasedDate;
    private String FAPdepDay;

    public String getFAPdepDay() {
        return FAPdepDay;
    }

    public void setFAPdepDay(String FAPdepDay) {
        this.FAPdepDay = FAPdepDay;
    }

    public java.sql.Date getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(java.sql.Date purchasedDate) {
        this.purchasedDate = new java.sql.Date(purchasedDate.getTime());
    }

    

    // Getters and Setters
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getDurationsMonth() {
        return durationsMonth;
    }

    public void setDurationsMonth(int durationsMonth) {
        this.durationsMonth = durationsMonth;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public BigDecimal getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(BigDecimal assetAmount) {
        this.assetAmount = assetAmount;
    }

    public String getFAPcatID() {
        return FAPcatID;
    }

    public void setFAPcatID(String FAPcatID) {
        this.FAPcatID = FAPcatID;
    }

    public String getFAPcategory() {
        return FAPcategory;
    }

    public void setFAPcategory(String FAPcategory) {
        this.FAPcategory = FAPcategory;
    }

    public String getFAPdepExpAcct() {
        return FAPdepExpAcct;
    }

    public void setFAPdepExpAcct(String FAPdepExpAcct) {
        this.FAPdepExpAcct = FAPdepExpAcct;
    }

    public String getFAPdepExpAcctName() {
        return FAPdepExpAcctName;
    }

    public void setFAPdepExpAcctName(String FAPdepExpAcctName) {
        this.FAPdepExpAcctName = FAPdepExpAcctName;
    }

    public String getFAPPrePayAcct() {
        return FAPPrePayAcct;
    }

    public void setFAPPrePayAcct(String FAPPrePayAcct) {
        this.FAPPrePayAcct = FAPPrePayAcct;
    }

    public String getFAPPrePayAcctName() {
        return FAPPrePayAcctName;
    }

    public void setFAPPrePayAcctName(String FAPPrePayAcctName) {
        this.FAPPrePayAcctName = FAPPrePayAcctName;
    }

    public String getAssetAccount() {
        return AssetAccount;
    }

    public void setAssetAccount(String assetAccount) {
        AssetAccount = assetAccount;
    }

    public String getAssetAccountName() {
        return AssetAccountName;
    }

    public void setAssetAccountName(String assetAccountName) {
        AssetAccountName = assetAccountName;
    }

    public String getDepExpenseAccount() {
        return DepExpenseAccount;
    }

    public void setDepExpenseAccount(String depExpenseAccount) {
        DepExpenseAccount = depExpenseAccount;
    }

    public String getDepExpenseAccountName() {
        return DepExpenseAccountName;
    }

    public void setDepExpenseAccountName(String depExpenseAccountName) {
        DepExpenseAccountName = depExpenseAccountName;
    }

    public String getFAPdepDate() {
        return FAPdepDate;
    }

    public void setFAPdepDate(String FAPdepDate) {
        this.FAPdepDate = FAPdepDate;
    }

    public String getRecordStatus() {
        return RecordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        RecordStatus = recordStatus;
    }

    public String getInputter() {
        return Inputter;
    }

    public void setInputter(String inputter) {
        Inputter = inputter;
    }

    public String getInputterRec() {
        return InputterRec;
    }

    public void setInputterRec(String inputterRec) {
        InputterRec = inputterRec;
    }

    public String getAuthoriser() {
        return Authoriser;
    }

    public void setAuthoriser(String authoriser) {
        Authoriser = authoriser;
    }

    public String getAuthoriserRec() {
        return AuthoriserRec;
    }

    public void setAuthoriserRec(String authoriserRec) {
        AuthoriserRec = authoriserRec;
    }

    public String getUpdatetype() {
        return updatetype;
    }

    public void setUpdatetype(String updatetype) {
        this.updatetype = updatetype;
    }

    public String getFAPtenancy() {
        return FAPtenancy;
    }

    public void setFAPtenancy(String FAPtenancy) {
        this.FAPtenancy = FAPtenancy;
    }

    @Override
    public String toString() {
        return "FixedAsset{" +
                "FAPcatID='" + FAPcatID + '\'' +
                ", FAPcategory='" + FAPcategory + '\'' +
                ", FAPdepExpAcct='" + FAPdepExpAcct + '\'' +
                ", FAPdepExpAcctName='" + FAPdepExpAcctName + '\'' +
                ", FAPPrePayAcct='" + FAPPrePayAcct + '\'' +
                ", FAPPrePayAcctName='" + FAPPrePayAcctName + '\'' +
                ", AssetAccount='" + AssetAccount + '\'' +
                ", AssetAccountName='" + AssetAccountName + '\'' +
                ", DepExpenseAccount='" + DepExpenseAccount + '\'' +
                ", DepExpenseAccountName='" + DepExpenseAccountName + '\'' +
                ", FAPdepDate='" + FAPdepDate + '\'' +
                ", RecordStatus='" + RecordStatus + '\'' +
                ", Inputter='" + Inputter + '\'' +
                ", InputterRec='" + InputterRec + '\'' +
                ", Authoriser='" + Authoriser + '\'' +
                ", AuthoriserRec='" + AuthoriserRec + '\'' +
                ", updatetype='" + updatetype + '\'' +
                ", FAPtenancy='" + FAPtenancy + '\'' +
                ", assetName='" + assetName + '\'' +
                ", assetAmount='" + assetAmount + '\'' +
                ", durationsMonth='" + durationsMonth + '\'' +
                ", branch='" + branch + '\'' +  // 
                '}';
    }
}
    
    
    
    

        @Override
        public AssetDisposal.Asset getAsObject(FacesContext context, UIComponent component, String value) {
            if (value == null || value.isEmpty()) {
                return null;
            }

            
            AssetDisposal bean = context.getApplication()
                    .evaluateExpressionGet(context, "#{assetDisposalBean}", AssetDisposal.class);
            
            if (bean == null) {
                return null;
            }

            List<AssetDisposal.Asset> assets = bean.getAvailableAssets();
            if (assets == null) return null;
            
            return assets.stream()
                    .filter(a -> a.getFAPcatID().equals(value))
                    .findFirst()
                    .orElse(null);
        }

       

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
         if (value == null) {
        return "";
    }

    if (value instanceof AssetDisposal.Asset) {
        AssetDisposal.Asset asset = (AssetDisposal.Asset) value;
        return asset.getFAPcatID(); 
    }

    return "";
    }
    
   
}