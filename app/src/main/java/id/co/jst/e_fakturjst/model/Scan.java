package id.co.jst.e_fakturjst.model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Scan {
    @SerializedName("faktur")
    @Expose
    private String faktur_no;
    @SerializedName("npwp")
    @Expose
    private String npwp;
    @SerializedName("company")
    @Expose
    private String name;
    @SerializedName("ppn")
    @Expose
    private String total_ppn;
    @SerializedName("status_faktur")
    @Expose
    private String status_faktur;

    public Scan(String faktur_no, String npwp, String name, String total_ppn, String status_faktur) {
        this.faktur_no = faktur_no;
        this.npwp = npwp;
        this.name = name;
        this.total_ppn = total_ppn;
        this.status_faktur = status_faktur;
    }

    public String getFaktur_no() {
        return faktur_no;
    }

    public void setFaktur_no(String faktur_no) {
        this.faktur_no = faktur_no;
    }

    public String getNpwp() {
        return npwp;
    }

    public void setNpwp(String npwp) {
        this.npwp = npwp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal_ppn() {
        return total_ppn;
    }

    public void setTotal_ppn(String total_ppn) {
        this.total_ppn = total_ppn;
    }

    public String getStatus_faktur() {
        return status_faktur;
    }

    public void setStatus_faktur(String status_faktur) {
        this.status_faktur = status_faktur;
    }
}
