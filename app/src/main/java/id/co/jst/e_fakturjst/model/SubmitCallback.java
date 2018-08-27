package id.co.jst.e_fakturjst.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitCallback {
    @SerializedName("data")
    @Expose
    private Scan data;
    @SerializedName("error_msg")
    @Expose
    private String error_msg;
    @SerializedName("error_code")
    @Expose
    private int error_code;

    public Scan getData() {
        return data;
    }

    public void setData(Scan data) {
        this.data = data;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}
