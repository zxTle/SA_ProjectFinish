package sa_project.model;

import java.util.Date;

public class ReqForm {
    private String rqNumber;
    private String  rqDate;
    private String rqDueDate;
    private String deliveriedDate;
    private String rqStatus;
    private String orderNum;
    private String empId;
    private String empName;

    public ReqForm(String rqNumber, String rqDate, String rqDueDate, String deliveriedDate, String rqStatus, String orderNum, String empId,String empName) {
        this.rqNumber = rqNumber;
        this.rqDate = rqDate;
        this.rqDueDate = rqDueDate;
        this.deliveriedDate = deliveriedDate;
        this.rqStatus = rqStatus;
        this.orderNum = orderNum;
        this.empId = empId;
        this.empName = empName;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public void setRqNumber(String rqNumber) {
        this.rqNumber = rqNumber;
    }

    public void setRqDate(String rqDate) {
        this.rqDate = rqDate;
    }

    public void setRqDueDate(String rqDueDate) {
        this.rqDueDate = rqDueDate;
    }

    public void setDeliveriedDate(String deliveriedDate) {
        this.deliveriedDate = deliveriedDate;
    }

    public void setRqStatus(String rqStatus) {
        this.rqStatus = rqStatus;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getRqNumber() {
        return rqNumber;
    }

    public String getRqDate() {
        return rqDate;
    }

    public String getRqDueDate() {
        return rqDueDate;
    }

    public String getDeliveriedDate() {
        return deliveriedDate;
    }

    public String getRqStatus() {
        return rqStatus;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public String getEmpId() {
        return empId;
    }
}
