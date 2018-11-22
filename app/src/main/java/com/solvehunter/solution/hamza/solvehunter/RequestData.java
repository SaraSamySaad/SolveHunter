package com.solvehunter.solution.hamza.solvehunter;

/**
 * Created by C.M on 21/10/2018.
 */

public class RequestData {

    private String  docId;
    private String userId;
    private String problemHint;

    public RequestData(String docId, String userId, String problemHint) {
        this.docId = docId;
        this.userId = userId;
        this.problemHint = problemHint;
    }

    public RequestData() {
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProblemHint(String problemHint) {
        this.problemHint = problemHint;
    }

    public String getDocId() {
        return docId;
    }

    public String getUserId() {
        return userId;
    }

    public String getProblemHint() {
        return problemHint;
    }
}
