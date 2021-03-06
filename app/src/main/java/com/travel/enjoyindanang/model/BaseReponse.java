package com.travel.enjoyindanang.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonkhanh on 2/27/17.
 */

public class BaseReponse {
    @SerializedName("system_version")
    private String system_version;
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    public String getSystem_version() {
        return system_version;
    }

    public void setSystem_version(String system_version) {
        this.system_version = system_version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseReponse{" +
                "system_version='" + system_version + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
