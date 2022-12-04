package com.n21.choizy.model;

public class Feedback {

    String compId,studentName,branchId,msg,studID;

    public Feedback(String compId, String studentName, String branchId, String msg) {
        this.compId = compId;
        this.studentName = studentName;
        this.branchId = branchId;
        this.msg = msg;
    }

    public Feedback() {

    }

    public String getCompId() {
        return compId;
    }

    public String getStudID() {
        return studID;
    }

    public void setStudID(String studID) {
        this.studID = studID;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
