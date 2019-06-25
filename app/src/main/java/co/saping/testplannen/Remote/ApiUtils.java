package co.saping.testplannen.Remote;

import co.saping.testplannen.Service.ApiInterface;

public class ApiUtils {

    public static final String BASE_URL = "https://plannen.com/";

    public static ApiInterface getApiService(){
        return RetrofitClient.getClient(BASE_URL).create(ApiInterface.class);
    }

}
