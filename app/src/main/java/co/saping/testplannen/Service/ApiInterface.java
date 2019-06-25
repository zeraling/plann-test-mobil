package co.saping.testplannen.Service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.saping.testplannen.Model.Prospectos;
import co.saping.testplannen.Model.Usuario;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("appServices/test/login.php?")
    Call<Usuario> login(@Query("email") String email, @Query("pass") String pass);

    @GET("appServices/test/customers.php?")
    Call<ArrayList<Prospectos>> lista(@Query("userId") String userId);


}
