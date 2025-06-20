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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;

@ManagedBean(name = "viewloandisbreport")
@ViewScoped
public class ViewLoanDisbReport implements Serializable {

    private LazyDataModel<LoanDisbReport> loanReports;
    private Date startDate;
    private Date endDate;

    private String startDateText;

    public String getStartDateText() {
        return startDateText;
    }

    public void setStartDateText(String startDateText) {
        this.startDateText = startDateText;
    }

    public String getEndDateText() {
        return endDateText;
    }

    public void setEndDateText(String endDateText) {
        this.endDateText = endDateText;
    }
    private String endDateText;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public LazyDataModel<LoanDisbReport> getLoanReports() {
        return loanReports;
    }

    @PostConstruct
    public void init() {
        loanReports = new LazyDataModel<LoanDisbReport>() {

            private int getTotalRowCount(Connection conn) {
                int count = 0;
                PreparedStatement stmt = null;
                ResultSet rs = null;
                try {
                    stmt = conn.prepareStatement("SELECT COUNT(*) FROM loansanddeposits");
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        count = rs.getInt(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Exception ignored) {
                    }
                    try {
                        if (stmt != null) {
                            stmt.close();
                        }
                    } catch (Exception ignored) {
                    }
                }
                return count;
            }

            @Override
            public List<LoanDisbReport> load(int first, int pageSize, Map<String, SortMeta> map, Map<String, FilterMeta> map1) {
                List<LoanDisbReport> reportList = new ArrayList<>();
                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;

                try {
                    conn = new DBConnection().get_connection();
                    String query = "SELECT ld.LDRecordID AS Arrangement, "
                            + "ld.LDAccount AS Account, "
                            + "ld.LDacctOfficer AS Officer, "
                            + "p.Productdes AS Product_Name, "
                            + "ld.LDCustomer AS Customer, "
                            + "CONCAT(c.cuslastname, ' ', c.cusfirstname) AS Customer_Name, "
                            + "c.cusBVN AS BVN, "
                            + "c.cusclientsms AS Phone_No, "
                            + "c.cusgender AS Gender, "
                            + "CONCAT(IFNULL(c.cusaddress1, ''), ' ', IFNULL(c.cusaddress2, '')) AS Address, "
                            + "ld.LDOpenDate AS Opening_Date, "
                            + "ld.LDRepayStartDate AS First_Payment_Date, "
                            + "CONCAT(ld.LDTenor, ' ', ld.LDRepayFreq) AS Term, "
                            + "ld.LDCurrency AS Ccy, "
                            + "ld.LDPrinAmount AS Commitment, "
                            + "(SELECT IFNULL(SUM(lp.OutstandingBal), 0) FROM loanpastdue lp WHERE lp.PDpaytype = 'PRIN.ONLY' AND lp.LDrecID = ld.LDRecordID) AS Arrears, "
                            + "(ld.LDPrinAmount - (SELECT IFNULL(SUM(lp.OutstandingBal), 0) FROM loanpastdue lp WHERE lp.PDpaytype = 'PRIN.ONLY' AND lp.LDrecID = ld.LDRecordID)) AS Principal, "
                            + //"ld.LDStatus AS Status, " +
                            "ld.LDInterestRate AS Rates, "
                            + "ld.LDPrinAmount AS Loan_Amount, ld.LDProcessDate AS Process_Date "
                            + "FROM loansanddeposits ld "
                            + "LEFT JOIN customer c ON ld.LDCustomer = c.cusid "
                            + "LEFT JOIN product p ON ld.LDProduct = p.productid "
                            + "LIMIT ?, ?";
                    stmt = conn.prepareStatement(query);
                    stmt.setInt(1, first);
                    stmt.setInt(2, pageSize);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
                        LoanDisbReport report = new LoanDisbReport();
                        report.setArrangement(rs.getString("Arrangement"));
                        report.setAccount(rs.getString("Account"));
                        report.setOfficer(rs.getString("Officer"));
                        report.setProductName(rs.getString("Product_Name"));
                        report.setCustomer(rs.getString("Customer"));
                        report.setCustomerName(rs.getString("Customer_Name"));
                        report.setBvn(rs.getString("BVN"));
                        report.setPhoneNo(rs.getString("Phone_No"));
                        report.setGender(rs.getString("Gender"));
                        report.setAddress(rs.getString("Address"));
                        report.setOpeningDate(rs.getDate("Opening_Date"));
                        report.setFirstPaymentDate(rs.getDate("First_Payment_Date"));
                        report.setTerm(rs.getString("Term"));
                        report.setCcy(rs.getString("Ccy"));
                        report.setCommitment(rs.getDouble("Commitment"));
                        report.setArrears(rs.getDouble("Arrears"));
                        report.setPrincipal(rs.getDouble("Principal"));
                        //report.setStatus(rs.getString("Status"));
                        report.setRates(rs.getDouble("Rates"));
                        report.setLoanAmount(rs.getDouble("Loan_Amount"));
                        report.setProcessDate(rs.getDate("Process_Date"));
                        reportList.add(report);
                    }

                    this.setRowCount(getTotalRowCount(conn));

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Exception ignored) {
                    }
                    try {
                        if (stmt != null) {
                            stmt.close();
                        }
                    } catch (Exception ignored) {
                    }
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (Exception ignored) {
                    }
                }

                return reportList;
            }

        };
    }

    private String nameSearch;

    public String getNameSearch() {
        return nameSearch;
    }

    public void setNameSearch(String arrangementSearch) {
        this.nameSearch = arrangementSearch;
    }

    public void searchByName() {
        List<LoanDisbReport> result = findLoanDisbReport(nameSearch);

        if (result != null) {
            loanReports = new LazyDataModel<LoanDisbReport>() {
                @Override
                public List<LoanDisbReport> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filters) {
                    setRowCount(result.size());
                    return result;
                }
            };
        }
    }

    private List<LoanDisbReport> findLoanDisbReport(String customerName) {
        List<LoanDisbReport> reports = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = new DBConnection().get_connection();

            String baseQuery
                    = "SELECT ld.LDRecordID AS Arrangement, "
                    + "ld.LDAccount AS Account, "
                    + "ld.LDacctOfficer AS Officer, "
                    + "p.Productdes AS Product_Name, "
                    + "ld.LDCustomer AS Customer, "
                    + "CONCAT(c.cuslastname, ' ', c.cusfirstname) AS Customer_Name, "
                    + "c.cusBVN AS BVN, "
                    + "c.cusclientsms AS Phone_No, "
                    + "c.cusgender AS Gender, "
                    + "CONCAT(IFNULL(c.cusaddress1, ''), ' ', IFNULL(c.cusaddress2, '')) AS Address, "
                    + "ld.LDOpenDate AS Opening_Date, "
                    + "ld.LDRepayStartDate AS First_Payment_Date, "
                    + "CONCAT(ld.LDTenor, ' ', ld.LDRepayFreq) AS Term, "
                    + "ld.LDCurrency AS Ccy, "
                    + "ld.LDPrinAmount AS Commitment, "
                    + "(SELECT IFNULL(SUM(lp.OutstandingBal), 0) "
                    + " FROM loanpastdue lp WHERE lp.PDpaytype = 'PRIN.ONLY' AND lp.LDrecID = ld.LDRecordID) AS Arrears, "
                    + "(ld.LDPrinAmount - (SELECT IFNULL(SUM(lp.OutstandingBal), 0) "
                    + " FROM loanpastdue lp WHERE lp.PDpaytype = 'PRIN.ONLY' AND lp.LDrecID = ld.LDRecordID)) AS Principal, "
                    + "ld.LDInterestRate AS Rates, "
                    + "ld.LDPrinAmount AS Loan_Amount , ld.LDProcessDate AS Process_Date"
                    + "FROM loansanddeposits ld "
                    + "LEFT JOIN customer c ON ld.LDCustomer = c.cusid "
                    + "LEFT JOIN product p ON ld.LDProduct = p.productid ";

            String queryWithFilter = baseQuery
                    + "WHERE CONCAT(c.cuslastname, ' ', c.cusfirstname, ' ', IFNULL(c.cusothername, '')) LIKE ?";

            stmt = conn.prepareStatement(queryWithFilter);
            stmt.setString(1, "%" + customerName + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {
                LoanDisbReport report = new LoanDisbReport();
                report.setArrangement(rs.getString("Arrangement"));
                report.setAccount(rs.getString("Account"));
                report.setOfficer(rs.getString("Officer"));
                report.setProductName(rs.getString("Product_Name"));
                report.setCustomer(rs.getString("Customer"));
                report.setCustomerName(rs.getString("Customer_Name"));
                report.setBvn(rs.getString("BVN"));
                report.setPhoneNo(rs.getString("Phone_No"));
                report.setGender(rs.getString("Gender"));
                report.setAddress(rs.getString("Address"));
                report.setOpeningDate(rs.getDate("Opening_Date"));
                report.setFirstPaymentDate(rs.getDate("First_Payment_Date"));
                report.setTerm(rs.getString("Term"));
                report.setCcy(rs.getString("Ccy"));
                report.setCommitment(rs.getDouble("Commitment"));
                report.setArrears(rs.getDouble("Arrears"));
                report.setPrincipal(rs.getDouble("Principal"));
                report.setRates(rs.getDouble("Rates"));
                report.setLoanAmount(rs.getDouble("Loan_Amount"));
                report.setProcessDate(rs.getDate("Process_Date"));
                reports.add(report);
            }

            if (reports.isEmpty()) {
                stmt.close();
                rs.close();
                stmt = conn.prepareStatement(baseQuery);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    LoanDisbReport report = new LoanDisbReport();
                    report.setArrangement(rs.getString("Arrangement"));
                    report.setAccount(rs.getString("Account"));
                    report.setOfficer(rs.getString("Officer"));
                    report.setProductName(rs.getString("Product_Name"));
                    report.setCustomer(rs.getString("Customer"));
                    report.setCustomerName(rs.getString("Customer_Name"));
                    report.setBvn(rs.getString("BVN"));
                    report.setPhoneNo(rs.getString("Phone_No"));
                    report.setGender(rs.getString("Gender"));
                    report.setAddress(rs.getString("Address"));
                    report.setOpeningDate(rs.getDate("Opening_Date"));
                    report.setFirstPaymentDate(rs.getDate("First_Payment_Date"));
                    report.setTerm(rs.getString("Term"));
                    report.setCcy(rs.getString("Ccy"));
                    report.setCommitment(rs.getDouble("Commitment"));
                    report.setArrears(rs.getDouble("Arrears"));
                    report.setPrincipal(rs.getDouble("Principal"));
                    report.setRates(rs.getDouble("Rates"));
                    report.setLoanAmount(rs.getDouble("Loan_Amount"));
                    report.setProcessDate(rs.getDate("Process_Date"));
                    reports.add(report);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ignored) {
            }
        }

        return reports;
    }

    public void onDateFieldKeyup() {
        List<LoanDisbReport> result = findLoanDisbReportByTextDate(startDateText,endDateText);

        loanReports = new LazyDataModel<LoanDisbReport>() {
            @Override
            public List<LoanDisbReport> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filters) {
                setRowCount(result.size());
                return result;
            }
        };
    }

    public List<LoanDisbReport> findLoanDisbReportByTextDate(String startText, String endText) {
    List<LoanDisbReport> reports = new ArrayList<>();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        conn = new DBConnection().get_connection();

        StringBuilder query = new StringBuilder(
            "SELECT ld.LDRecordID AS Arrangement, " +
            "ld.LDAccount AS Account, " +
            "ld.LDacctOfficer AS Officer, " +
            "p.Productdes AS Product_Name, " +
            "ld.LDCustomer AS Customer, " +
            "CONCAT(c.cuslastname, ' ', c.cusfirstname) AS Customer_Name, " +
            "c.cusBVN AS BVN, " +
            "c.cusclientsms AS Phone_No, " +
            "c.cusgender AS Gender, " +
            "CONCAT(IFNULL(c.cusaddress1, ''), ' ', IFNULL(c.cusaddress2, '')) AS Address, " +
            "ld.LDOpenDate AS Opening_Date, " +
            "ld.LDRepayStartDate AS First_Payment_Date, " +
            "CONCAT(ld.LDTenor, ' ', ld.LDRepayFreq) AS Term, " +
            "ld.LDCurrency AS Ccy, " +
            "ld.LDPrinAmount AS Commitment, " +
            "(SELECT IFNULL(SUM(lp.OutstandingBal), 0) FROM loanpastdue lp WHERE lp.PDpaytype = 'PRIN.ONLY' AND lp.LDrecID = ld.LDRecordID) AS Arrears, " +
            "(ld.LDPrinAmount - (SELECT IFNULL(SUM(lp.OutstandingBal), 0) FROM loanpastdue lp WHERE lp.PDpaytype = 'PRIN.ONLY' AND lp.LDrecID = ld.LDRecordID)) AS Principal, " +
            "ld.LDInterestRate AS Rates, " +
            "ld.LDPrinAmount AS Loan_Amount, " +
            "ld.LDProcessDate AS Process_Date " +
            "FROM loansanddeposits ld " +
            "LEFT JOIN customer c ON ld.LDCustomer = c.cusid " +
            "LEFT JOIN product p ON ld.LDProduct = p.productid"
        );

        java.util.Date startDate = parseCompleteDate(startText);
        java.util.Date endDate = parseCompleteDate(endText);
        

        List<Object> params = new ArrayList<>();

        if (startDate != null || endDate != null) {
            query.append(" WHERE ");
            if (startDate != null && endDate != null) {
                query.append("ld.LDProcessDate >= ? AND ld.LDProcessDate <= ?");
                params.add(new java.sql.Date(startDate.getTime()));
                params.add(new java.sql.Date(endDate.getTime()));
            } else if (startDate != null) {
                query.append("ld.LDProcessDate >= ?");
                params.add(new java.sql.Date(startDate.getTime()));
            } else {
                query.append("ld.LDProcessDate <= ?");
                params.add(new java.sql.Date(endDate.getTime()));
            }
        }

        stmt = conn.prepareStatement(query.toString());

        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }

        rs = stmt.executeQuery();

        while (rs.next()) {
            LoanDisbReport report = new LoanDisbReport();
            report.setArrangement(rs.getString("Arrangement"));
            report.setAccount(rs.getString("Account"));
            report.setOfficer(rs.getString("Officer"));
            report.setProductName(rs.getString("Product_Name"));
            report.setCustomer(rs.getString("Customer"));
            report.setCustomerName(rs.getString("Customer_Name"));
            report.setBvn(rs.getString("BVN"));
            report.setPhoneNo(rs.getString("Phone_No"));
            report.setGender(rs.getString("Gender"));
            report.setAddress(rs.getString("Address"));
            report.setOpeningDate(rs.getDate("Opening_Date"));
            report.setFirstPaymentDate(rs.getDate("First_Payment_Date"));
            report.setTerm(rs.getString("Term"));
            report.setCcy(rs.getString("Ccy"));
            report.setCommitment(rs.getDouble("Commitment"));
            report.setArrears(rs.getDouble("Arrears"));
            report.setPrincipal(rs.getDouble("Principal"));
            report.setRates(rs.getDouble("Rates"));
            report.setLoanAmount(rs.getDouble("Loan_Amount"));
            report.setProcessDate(rs.getDate("Process_Date"));
            reports.add(report);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception ignored) {}
        try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
        try { if (conn != null) conn.close(); } catch (Exception ignored) {}
    }

    return reports.isEmpty() ? null : reports;
}

private java.util.Date parseCompleteDate(String text) {
    if (text == null || text.trim().isEmpty()) {
        return null;
    }

    if (!text.matches("\\d{2}-\\d{2}-\\d{4}")) {
        return null;
    }

    try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        return sdf.parse(text);
    } catch (ParseException e) {
        return null;
    }
}


    

    public static class LoanDisbReport {

        private String arrangement;
        private String account;
        private String officer;
        private String productName;
        private String customer;
        private String customerName;
        private String bvn;
        private String phoneNo;
        private String gender;
        private String address;
        private Date openingDate;
        private Date firstPaymentDate;
        private String term;
        private String ccy;
        private double commitment;
        private double arrears;
        private double principal;
        private String status;
        private double rates;
        private double loanAmount;
        private Date processDate;

        public Date getProcessDate() {
            return processDate;
        }

        public void setProcessDate(Date processDate) {
            this.processDate = processDate;
        }

        public String getArrangement() {
            return arrangement;
        }

        public void setArrangement(String arrangement) {
            this.arrangement = arrangement;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getOfficer() {
            return officer;
        }

        public void setOfficer(String officer) {
            this.officer = officer;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getBvn() {
            return bvn;
        }

        public void setBvn(String bvn) {
            this.bvn = bvn;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Date getOpeningDate() {
            return openingDate;
        }

        public void setOpeningDate(Date openingDate) {
            this.openingDate = openingDate;
        }

        public Date getFirstPaymentDate() {
            return firstPaymentDate;
        }

        public void setFirstPaymentDate(Date firstPaymentDate) {
            this.firstPaymentDate = firstPaymentDate;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getCcy() {
            return ccy;
        }

        public void setCcy(String ccy) {
            this.ccy = ccy;
        }

        public double getCommitment() {
            return commitment;
        }

        public void setCommitment(double commitment) {
            this.commitment = commitment;
        }

        public double getArrears() {
            return arrears;
        }

        public void setArrears(double arrears) {
            this.arrears = arrears;
        }

        public double getPrincipal() {
            return principal;
        }

        public void setPrincipal(double principal) {
            this.principal = principal;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getRates() {
            return rates;
        }

        public void setRates(double rates) {
            this.rates = rates;
        }

        public double getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(double loanAmount) {
            this.loanAmount = loanAmount;
        }
    }
}
