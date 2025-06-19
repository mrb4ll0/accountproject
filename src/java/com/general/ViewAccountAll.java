/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.general;

/**
 *
 * @author muhammad
 */

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@ManagedBean(name = "viewaccountall")
@ViewScoped
public class ViewAccountAll implements Serializable {

    private LazyDataModel<AccountRecord> accountList;

    public LazyDataModel<AccountRecord> getAccountList() {
        return accountList;
    }
    private String accountid;

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    
    @PostConstruct
public void init() {
    accountList = new LazyDataModel<AccountRecord>() {

        @Override
        public List<AccountRecord> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filters) {
            List<AccountRecord> accounts = new ArrayList<>();
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                conn = new DBConnection().get_connection();

                String query = "SELECT a.accountid, " +
                               "COALESCE(a.accounttitle, a.accountdescript) AS description, " +
                               "'NGN' AS currency, " +
                               "p.Productdes AS product_name, " +
                               "a.clearedbalance, " +
                               "a.AcAltTwo, " +
                               "c.cusid AS customer_number, " +
                               "CONCAT(c.cuslastname, ' ', c.cusfirstname, ' ', IFNULL(c.cusothername, '')) AS customer_name, " +
                               "ao.acctofficername AS account_officer, " +
                               "a.accounttransit AS branch "+
                               "FROM account a " +
                               "LEFT JOIN customer c ON a.acctcustomer = c.cusid " +
                               "LEFT JOIN product p ON a.acctproduct = p.productid " +
                               "LEFT JOIN acctofficer ao ON a.acctofficer = ao.acctofficerid " +
                               "LIMIT ?, ?";

                stmt = conn.prepareStatement(query);
                stmt.setInt(1, first);
                stmt.setInt(2, pageSize);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    AccountRecord record = new AccountRecord();
                    record.setRecordId(rs.getString("accountid"));
                    record.setDescription(rs.getString("description"));
                    record.setCurrency(rs.getString("currency"));
                    record.setProductName(rs.getString("product_name"));
                    record.setBalance(rs.getDouble("clearedbalance"));
                    record.setLegacyAccountNumber(rs.getString("AcAltTwo"));
                    record.setAccountOfficer(rs.getString("account_officer"));
                    record.setCustomerNumber(rs.getString("customer_number"));
                    record.setCustomerName(rs.getString("customer_name"));
                    record.setBranch(rs.getString("branch"));
                    accounts.add(record);
                }

                this.setRowCount(getTotalRowCount(conn));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try { if (rs != null) rs.close(); } catch (Exception ignored) {}
                try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
                try { if (conn != null) conn.close(); } catch (Exception ignored) {}
            }

            return accounts;
        }

        private int getTotalRowCount(Connection conn) {
            int count = 0;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                stmt = conn.prepareStatement("SELECT COUNT(*) FROM account");
                rs = stmt.executeQuery();
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try { if (rs != null) rs.close(); } catch (Exception ignored) {}
                try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            }
            return count;
        }
    };
}

public void searchByAccountId() {
    List<AccountRecord> result = findByAccountId(accountid);

    if (result != null) {
        accountList = new LazyDataModel<AccountRecord>() {
            @Override
            public List<AccountRecord> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filters) {
                setRowCount(result.size());
                return result;
            }
        };
    }
}
    
private List<AccountRecord> findByAccountId(String accountId) {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<AccountRecord> records = new ArrayList<>();

    try {
        conn = new DBConnection().get_connection();

        String baseQuery = "SELECT a.accountid, " +
                           "COALESCE(a.accounttitle, a.accountdescript) AS description, " +
                           "'NGN' AS currency, " +
                           "p.Productdes AS product_name, " +
                           "a.clearedbalance, " +
                           "a.AcAltTwo, " +
                           "c.cusid AS customer_number, " +
                           "CONCAT(c.cuslastname, ' ', c.cusfirstname, ' ', IFNULL(c.cusothername, '')) AS customer_name, " +
                           "ao.acctofficername AS account_officer, " +
                           "a.accounttransit AS branch "+
                           "FROM account a " +
                           "LEFT JOIN customer c ON a.acctcustomer = c.cusid " +
                           "LEFT JOIN product p ON a.acctproduct = p.productid " +
                           "LEFT JOIN acctofficer ao ON a.acctofficer = ao.acctofficerid ";

        String queryWithFilter = baseQuery + "WHERE a.accountid LIKE ?";
        stmt = conn.prepareStatement(queryWithFilter);
        stmt.setString(1, "%" + accountId + "%");
        rs = stmt.executeQuery();

        while (rs.next()) {
            AccountRecord record = new AccountRecord();
            record.setRecordId(rs.getString("accountid"));
            record.setDescription(rs.getString("description"));
            record.setCurrency(rs.getString("currency"));
            record.setProductName(rs.getString("product_name"));
            record.setBalance(rs.getDouble("clearedbalance"));
            record.setLegacyAccountNumber(rs.getString("AcAltTwo"));
            record.setAccountOfficer(rs.getString("account_officer"));
            record.setCustomerNumber(rs.getString("customer_number"));
            record.setCustomerName(rs.getString("customer_name"));
            record.setBranch(rs.getString("branch"));
            records.add(record);
        }

        if (records.isEmpty()) {
            stmt.close();
            rs.close();
            stmt = conn.prepareStatement(baseQuery);
            rs = stmt.executeQuery();

            while (rs.next()) {
                AccountRecord record = new AccountRecord();
                record.setRecordId(rs.getString("accountid"));
                record.setDescription(rs.getString("description"));
                record.setCurrency(rs.getString("currency"));
                record.setProductName(rs.getString("product_name"));
                record.setBalance(rs.getDouble("clearedbalance"));
                record.setLegacyAccountNumber(rs.getString("AcAltTwo"));
                record.setAccountOfficer(rs.getString("account_officer"));
                record.setCustomerNumber(rs.getString("customer_number"));
                record.setCustomerName(rs.getString("customer_name"));
                records.add(record);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception ignored) {}
        try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
        try { if (conn != null) conn.close(); } catch (Exception ignored) {}
    }

    return records;
}
    

    

    
    public static class AccountRecord implements Serializable {
        
        private String recordId;
        private String description;
        private String currency;
        private String productName;
        private double balance;
        private String legacyAccountNumber;
        private String accountOfficer;
        private String customerNumber;
        private String CustomerName;
        private String branch;

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerName(String CustomerName) {
            this.CustomerName = CustomerName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        // Getters and setters
        public String getRecordId() { return recordId; }
        public void setRecordId(String recordId) { this.recordId = recordId; }

        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public double getBalance() { return balance; }
        public void setBalance(double balance) { this.balance = balance; }

        public String getLegacyAccountNumber() { return legacyAccountNumber; }
        public void setLegacyAccountNumber(String legacyAccountNumber) { this.legacyAccountNumber = legacyAccountNumber; }

        public String getAccountOfficer() { return accountOfficer; }
        public void setAccountOfficer(String accountOfficer) { this.accountOfficer = accountOfficer; }

        public String getCustomerNumber() { return customerNumber; }
        public void setCustomerNumber(String customerNumber) { this.customerNumber = customerNumber; }
    }
}

