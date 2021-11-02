package sa_project.model;

public class RtForm {
    private String rtNum;
    private String inNum;
    private String rtStatus;

    public RtForm(String rtNum, String inNum, String rtStatus) {
        this.rtNum = rtNum;
        this.inNum = inNum;
        this.rtStatus = rtStatus;
    }

    public String getRtNum() {
        return rtNum;
    }

    public void setRtNum(String rtNum) {
        this.rtNum = rtNum;
    }

    public String getInNum() {
        return inNum;
    }

    public void setInNum(String inNum) {
        this.inNum = inNum;
    }

    public String getRtStatus() {
        return rtStatus;
    }

    public void setRtStatus(String rtStatus) {
        this.rtStatus = rtStatus;
    }
}
