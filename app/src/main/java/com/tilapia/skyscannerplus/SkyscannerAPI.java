package com.tilapia.skyscannerplus;

import com.tilapia.skyscannerplus.models.PollResult;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Pomo on 10/08/2018.
 */

public interface SkyscannerAPI {
    String BASE_URL = "http://partners.api.skyscanner.net/apiservices/";

    @FormUrlEncoded
    @POST("pricing/v1.0/")
    Observable<Response<Void>> createSession(@Field("country") String country, @Field("currency") String currency, @Field("locale") String locale, @Field("originPlace") String originPlace, @Field("destinationPlace") String destinationPlace, @Field("outboundDate") String outboundDate, @Field("inboundDate") String inboundDate, @Field("adults") int adults, @Field("apiKey") String apiKey);

    @GET
    Observable<Response<PollResult>> pollResults(@Url String url, @Query("apiKey") String key);
}
