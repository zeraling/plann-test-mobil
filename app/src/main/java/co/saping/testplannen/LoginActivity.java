package co.saping.testplannen;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.saping.testplannen.Model.Usuario;
import co.saping.testplannen.Remote.ApiUtils;
import co.saping.testplannen.Service.ApiInterface;
import co.saping.testplannen.Session.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;
    ApiInterface apiInterface;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        apiInterface = ApiUtils.getApiService();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                //validate form
                if(validateLogin(username, password)){
                    //do login
                    doLogin(username, password);
                }
            }
        });
    }

    private boolean validateLogin(String username, String password){
        if(username == null || username.trim().length() == 0){
            Toast.makeText(this, "Correo es requerido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this, "Password es requerido", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doLogin(final String username,final String password){
        Call call = apiInterface.login(username,password);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    Usuario resObj = (Usuario) response.body();
                    if(resObj.getSucces().equals("false")){

                        // Se crea la session del usuario
                        session.crearLoginSession("3", username);
                        //login start main activity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        String msj = resObj.getResultMessage();

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Importante");
                        builder.setMessage(msj);
                        builder.setPositiveButton("OK",null);

                        builder.create();
                        builder.show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error! No hay coneccion!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
