package co.saping.testplannen.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.saping.testplannen.Utils.Constantes;

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constantes.CREAR_TABLA_PROSPECTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_PROSPECTOS);
        onCreate(db);
    }
}

