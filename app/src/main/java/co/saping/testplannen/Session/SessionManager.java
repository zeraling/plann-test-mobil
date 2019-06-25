package co.saping.testplannen.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

import co.saping.testplannen.LoginActivity;

public class SessionManager {
    SharedPreferences pref;
    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref filename
    private static final String PREF_NAME = "TestPlannen";

    // Constantes
    private static final String IS_LOGIN = "Logueado";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_GET_LISTA = "getLista";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void crearLoginSession(String userId, String email){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_EMAIL, email);

        editor.putBoolean(KEY_GET_LISTA, true);

        editor.commit();
    }

    /**
     * verifica el estado del usuario
     */
    public void checkLogin(){
        if(!this.isLoggedIn()){
            // redirigue al login
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }

    /**
     * Obtener las pref
     * */
    public HashMap getUserPref(){
        HashMap user = new HashMap();
        // userId
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));

        // email
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // getLista
        user.put(KEY_GET_LISTA, pref.getBoolean(KEY_GET_LISTA, false));

        return user;
    }

    /**
     * Cerrar session
     * */
    public void cerrarSession(){
        // se borran las pref
        editor.clear();
        editor.commit();

        // redirecciona al login
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    // verifica si la session esta iniciada
    public boolean isLoggedIn(){

        return pref.getBoolean(IS_LOGIN, false);
    }
}
