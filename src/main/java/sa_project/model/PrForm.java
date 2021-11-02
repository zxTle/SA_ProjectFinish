package sa_project.model;

public class PrForm {
    private String prNumber;
    private String prDate;
    private String prStatus;
    private String prDueDate;
    private String empId;
    private String empName;

    public PrForm(String prNumber, String prDate, String prStatus, String prDueDate, String empId, String empName) {
        this.prNumber = prNumber;
        this.prDate = prDate;
        this.prStatus = prStatus;
        this.prDueDate = prDueDate;
        this.empId = empId;
        this.empName = empName;
    }

    public String getPrNumber() {
        return prNumber;
    }

    public void setPrNumber(String prNumber) {
        this.prNumber = prNumber;
    }

    public String getPrDate() {
        return prDate;
    }

    public void setPrDate(String prDate) {
        this.prDate = prDate;
    }

    public String getPrStatus() {
        return prStatus;
    }

    public void setPrStatus(String prStatus) {
        this.prStatus = prStatus;
    }

    public String getPrDueDate() {
        return prDueDate;
    }

    public void setPrDueDate(String prDueDate) {
        this.prDueDate = prDueDate;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }
}
