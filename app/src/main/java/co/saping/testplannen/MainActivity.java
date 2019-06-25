package co.saping.testplannen;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import co.saping.testplannen.Adapter.ListaProspectosAdapter;
import co.saping.testplannen.Database.DatabaseHelper;
import co.saping.testplannen.Model.Prospectos;
import co.saping.testplannen.Remote.ApiUtils;
import co.saping.testplannen.Service.ApiInterface;
import co.saping.testplannen.Session.SessionManager;
import co.saping.testplannen.Utils.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // api
    ApiInterface apiInterface;

    // Session Manager
    SessionManager session;
    TextView txtEmail,txtUserName;

    //userId
    String userId;
    Boolean getLista;

    //db
    DatabaseHelper conn;

    //lista
    RecyclerView rcViewPropectos;
    ArrayList<Prospectos> listaProspectos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instancia de Session
        session = new SessionManager(getApplicationContext());

        //instancia apirest
        apiInterface = ApiUtils.getApiService();

        //instancia db
        conn=new DatabaseHelper(getApplicationContext(),"bd_prospectos",null,1);

        //verifica el login
        session.checkLogin();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //elementos de la vista
        txtEmail = (TextView) findViewById(R.id.txtMailUser);
        txtUserName = (TextView) findViewById(R.id.txtNameUser);
        // obtener datos de session
        HashMap user = session.getUserPref();

        userId = (String) user.get(SessionManager.KEY_USER_ID);
        //txtUserName.setText("Test User");

        String email = (String) user.get(SessionManager.KEY_EMAIL);
        //txtEmail.setText(email);

        getLista = (Boolean) user.get(SessionManager.KEY_GET_LISTA);

        listaProspectos=new ArrayList<>();

        // obtener a lista de prospectos
        consultarListaProspectos();

        if(listaProspectos.size()== 0){
            getListaProspectos();
        }

        rcViewPropectos= (RecyclerView) findViewById(R.id.recyclerProspectos);
        rcViewPropectos.setLayoutManager(new LinearLayoutManager(this));

        ListaProspectosAdapter adapter=new ListaProspectosAdapter(this,listaProspectos,conn);
        rcViewPropectos.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home_app) {

        } else if (id == R.id.nav_reload) {
            deleteLista();
        }
        else if (id == R.id.nav_close_session) {
            session.cerrarSession();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getListaProspectos() {
        try {
            Call call = apiInterface.lista(userId);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if(response.isSuccessful()){
                        ArrayList<Prospectos> resObj = (ArrayList<Prospectos>) response.body();

                        if(resObj.size()>0){
                            for (Prospectos propecto :resObj) {
                                registrarLista(propecto);
                            }
                            //para no descargar la lista otra ves
                            getSharedPreferences("TestPlannen", 0 /*FILE_MODE*/)
                                    .edit()
                                    .putBoolean("getLista", false)
                                    .apply();

                            // se consulta la lista
                            consultarListaProspectos();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Error! No hay coneccion!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(MainActivity.this, "No se pudo obtener la lista", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.getMessage();
        }
    }

    private void deleteLista(){
        SQLiteDatabase db=conn.getWritableDatabase();
        db.execSQL("DELETE FROM "+Constantes.TABLA_PROSPECTOS); //delete all rows in a table
        db.close();

    }

    private void registrarLista(Prospectos prospecto) {

        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put(Constantes.PROSPECTO_CUSTOMER_ID, prospecto.getCustomerId());
        values.put(Constantes.PROSPECTO_NOMBRE, prospecto.getNombre());
        values.put(Constantes.PROSPECTO_APELLIDO, prospecto.getApellido());
        values.put(Constantes.PROSPECTO_DIRECCION, prospecto.getDireccion());
        values.put(Constantes.PROSPECTO_TELEFONO, prospecto.getTelefono());
        values.put(Constantes.PROSPECTO_ESTADO, prospecto.getEstado());
        values.put(Constantes.PROSPECTO_USER_ID, prospecto.getUserId());

        db.insert(Constantes.TABLA_PROSPECTOS,Constantes.PROSPECTO_CUSTOMER_ID,values);

        db.close();
    }



    private void consultarListaProspectos() {
        SQLiteDatabase db=conn.getReadableDatabase();

        Prospectos prospecto=null;
        listaProspectos =new ArrayList();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM "+ Constantes.TABLA_PROSPECTOS,null);

        while (cursor.moveToNext()){
            prospecto=new Prospectos();

            prospecto.setCustomerId(cursor.getString(0));
            prospecto.setNombre(cursor.getString(1));
            prospecto.setApellido(cursor.getString(2));
            prospecto.setDireccion(cursor.getString(3));
            prospecto.setTelefono(cursor.getString(4));
            prospecto.setEstado(cursor.getString(5));
            prospecto.setUserId(cursor.getString(6));

            listaProspectos.add(prospecto);
        }
    }


}
