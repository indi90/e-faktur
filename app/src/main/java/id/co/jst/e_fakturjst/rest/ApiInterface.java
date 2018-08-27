package id.co.jst.e_fakturjst.rest;

import id.co.jst.e_fakturjst.model.EmailCallback;
import id.co.jst.e_fakturjst.model.SubmitCallback;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("api/transaction/submit")
    @FormUrlEncoded
    Call<SubmitCallback> submit(@Field("barcode") String barcode, @Field("credit") int credit);

    @POST("api/transaction/email")
    @FormUrlEncoded
    Call<EmailCallback> email(@Field("month") int month, @Field("year") int year);

}
