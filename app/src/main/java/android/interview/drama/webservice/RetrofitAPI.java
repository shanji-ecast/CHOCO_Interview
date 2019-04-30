package android.interview.drama.webservice;

import android.interview.drama.model.DramaList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitAPI {

    @GET("/v2/5a97c59c30000047005c1ed2")
    Call<DramaList> loadDramaList();

}
