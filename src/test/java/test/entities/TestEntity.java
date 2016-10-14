package test.entities;

import java.util.Date;

public class TestEntity {

    private String code;

    private int amount;

    private int accruingAmounts;

    private Date fileCreatedDate;

    private Date fileCreatedTime;

    private String fileBatchNo;

    private String mac;

    public TestEntity() {
        super();
    }

    public TestEntity(String code, int amount, int accruingAmounts, Date fileCreatedDate, Date fileCreatedTime,
            String fileBatchNo, String mac) {
        super();
        this.code = code;
        this.amount = amount;
        this.accruingAmounts = accruingAmounts;
        this.fileCreatedDate = fileCreatedDate;
        this.fileCreatedTime = fileCreatedTime;
        this.fileBatchNo = fileBatchNo;
        this.mac = mac;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestEntity [code=" + code + ", amount=" + amount + ", accruingAmounts=" + accruingAmounts + ", fileCreatedDate="
                + fileCreatedDate + ", fileCreatedTime=" + fileCreatedTime + ", fileBatchNo=" + fileBatchNo + ", mac=" + mac
                + "]";
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * @return the accruingAmounts
     */
    public int getAccruingAmounts() {
        return accruingAmounts;
    }

    /**
     * @param accruingAmounts the accruingAmounts to set
     */
    public void setAccruingAmounts(int accruingAmounts) {
        this.accruingAmounts = accruingAmounts;
    }

    /**
     * @return the fileCreatedDate
     */
    public Date getFileCreatedDate() {
        return fileCreatedDate;
    }

    /**
     * @param fileCreatedDate the fileCreatedDate to set
     */
    public void setFileCreatedDate(Date fileCreatedDate) {
        this.fileCreatedDate = fileCreatedDate;
    }

    /**
     * @return the fileCreatedTime
     */
    public Date getFileCreatedTime() {
        return fileCreatedTime;
    }

    /**
     * @param fileCreatedTime the fileCreatedTime to set
     */
    public void setFileCreatedTime(Date fileCreatedTime) {
        this.fileCreatedTime = fileCreatedTime;
    }

    /**
     * @return the fileBatchNo
     */
    public String getFileBatchNo() {
        return fileBatchNo;
    }

    /**
     * @param fileBatchNo the fileBatchNo to set
     */
    public void setFileBatchNo(String fileBatchNo) {
        this.fileBatchNo = fileBatchNo;
    }

    /**
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac the mac to set
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

}
