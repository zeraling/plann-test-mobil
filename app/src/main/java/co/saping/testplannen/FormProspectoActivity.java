package co.saping.testplannen;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import co.saping.testplannen.Database.DatabaseHelper;
import co.saping.testplannen.Utils.Constantes;

public class FormProspectoActivity extends AppCompatActivity {

    //db
    DatabaseHelper conn;

    //input
    TextView nombre,apellido,direccion,telefono,customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_prospecto);

        //instancia db
        conn=new DatabaseHelper(getApplicationContext(),"bd_prospectos",null,1);
        customerId = (TextView) findViewById(R.id.txtCustomerId);

        nombre = (TextView) findViewById(R.id.txtNombre);
        apellido = (TextView) findViewById(R.id.txtApellido);
        direccion = (TextView) findViewById(R.id.txtDireccion);
        telefono = (TextView) findViewById(R.id.txtTelefono);

        Button btnActualizar = (Button) findViewById(R.id.btnActualizar);



        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNombre = nombre.getText().toString();
                String strApellido =  apellido.getText().toString();
                String strDireccion =  direccion.getText().toString();
                String strTelefono =   telefono.getText().toString();
                String strCustomerId =   customerId.getText().toString();

                updateData(strNombre,strApellido,strDireccion,strTelefono,strCustomerId);

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);

            }
        });

        getDataIntent();

    }

    private void getDataIntent(){
        if(getIntent().hasExtra("customerId")){
            String customerID = getIntent().getStringExtra("customerId");

            SQLiteDatabase db=conn.getReadableDatabase();

            //select * from usuarios

            String sql = "SELECT * FROM "+ Constantes.TABLA_PROSPECTOS
                    +" WHERE "+Constantes.PROSPECTO_CUSTOMER_ID+" = '"+customerID+"'";

            Cursor cursor=db.rawQuery(sql,null);

            String strNombre = "",strApellido = "" ,strDireccion = "",strTelefono = "",strCustomerId = "";

            if(cursor.getCount()>0){
                //Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        strCustomerId= cursor.getString(0);
                        strNombre = cursor.getString(1);
                        strApellido = cursor.getString(2);
                        strDireccion = cursor.getString(3);
                        strTelefono = cursor.getString(4);
                    } while(cursor.moveToNext());
                }

                customerId.setText(strCustomerId);
                nombre.setText(strNombre);
                apellido.setText(strApellido);
                direccion.setText(strDireccion);
                telefono.setText(strTelefono);
            }

        }
    }

    private boolean updateData(String strNombre, String strApellido, String strDireccion, String strTelefono, String strCustomerId){
        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Constantes.PROSPECTO_NOMBRE,strNombre); //These Fields should be your String values of actual column names
        cv.put(Constantes.PROSPECTO_APELLIDO,strApellido);
        cv.put(Constantes.PROSPECTO_DIRECCION,strDireccion);
        cv.put(Constantes.PROSPECTO_TELEFONO,strTelefono);

        String codUpdate = Constantes.PROSPECTO_CUSTOMER_ID+" = '"+strCustomerId+"'";

        int i = db.update(Constantes.TABLA_PROSPECTOS, cv, codUpdate, null);

        db.close();

        return i > 0;
    }


}
