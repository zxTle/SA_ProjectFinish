package sa_project.model;

public class InForm {
    private String inNum;
    private String prNum;
    private String inDate;
    private String empId;
    private String empName;

    public InForm(String inNum, String prNum, String inDate, String empId, String empName) {
        this.inNum = inNum;
        this.prNum = prNum;
        this.inDate = inDate;
        this.empId = empId;
        this.empName = empName;
    }

    public String getInNum() {
        return inNum;
    }

    public void setInNum(String inNum) {
        this.inNum = inNum;
    }

    public String getPrNum() {
        return prNum;
    }

    public void setPrNum(String prNum) {
        this.prNum = prNum;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
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
