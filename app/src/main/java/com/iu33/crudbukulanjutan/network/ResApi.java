package com.iu33.crudbukulanjutan.network;

import com.iu33.crudbukulanjutan.model.ModelBuku;
import com.iu33.crudbukulanjutan.model.ModelKategoriBuku;
import com.iu33.crudbukulanjutan.model.ModelUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by hp on 12/10/2017.
 */

public interface ResApi {

    @FormUrlEncoded
    @POST("registeruserbuku.php/")
    Call<ModelUser> registeruser(
            @Field("vsnama") String strnama,
            @Field("vsalamat") String stralamat,
            @Field("vsjenkel") String strjenkel,
            @Field("vsnotelp") String strnotelp,
            @Field("vsusername") String strusername,
            @Field("vslevel") String strlevel,
            @Field("vspassword") String strpassword
    );

    @FormUrlEncoded
    @POST("loginuserbuku.php/")
    Call<ModelUser> loginuser(
            @Field("edtusername") String strusername,
            @Field("edtpassword") String strpassword,
            @Field("vslevel") String strlevel
    );

    @GET("ambildataCarikategoribuku.php/")
    public Call<ModelKategoriBuku> getDataCarikategoriBuku();

    @FormUrlEncoded
    @POST("getdatabuku.php")
    Call<ModelBuku> getdatabuku(
            @Field("vsiduser") String striduser,
            @Field("vsidkastrkategoribuku") String strkartbuku
    );
    @FormUrlEncoded
    @POST("deletedatabuku.php")
    Call<ModelUser> deleteData(
            @Field("vsidbuku") String stridbuku
    );
}