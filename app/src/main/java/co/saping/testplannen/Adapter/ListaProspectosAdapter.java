package co.saping.testplannen.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.saping.testplannen.Database.DatabaseHelper;
import co.saping.testplannen.FormProspectoActivity;
import co.saping.testplannen.MainActivity;
import co.saping.testplannen.Model.Prospectos;
import co.saping.testplannen.R;

public class ListaProspectosAdapter extends RecyclerView.Adapter<ListaProspectosAdapter.ProspectosViewHolder>{

    private static final String TAG = "ListaProspectosAdapter";

    ArrayList<Prospectos> listaProspectos;

    private Context mContext;

    private DatabaseHelper conDb;

    private LayoutInflater mInflater;

    public ListaProspectosAdapter(Context context , ArrayList<Prospectos> listaProspectos, DatabaseHelper db) {
        this.mInflater = LayoutInflater.from(context);

        this.listaProspectos = listaProspectos;
        this.mContext = context;
        this.conDb = db;
    }

    @NonNull
    @Override
    public ProspectosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.list_propectos, viewGroup,false);
        return new ProspectosViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ProspectosViewHolder holder, final int position) {

        final Prospectos prospecto = listaProspectos.get(position);

        holder.nombre.setText(prospecto.getNombre());
        holder.apellido.setText(prospecto.getApellido());
        holder.direccion.setText(prospecto.getDireccion());
        holder.telefono.setText(prospecto.getTelefono());
        holder.customerID.setText(prospecto.getCustomerId());

        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login start main activity
                Intent intent = new Intent(mContext, FormProspectoActivity.class);
                intent.putExtra("customerId", prospecto.getCustomerId());
                mContext.startActivity(intent);
            }
        });

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Importante!");
                builder.setMessage("Esta seguro que desea eliminar este registro!");
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db=conDb.getWritableDatabase();
                        db.delete("prospectos", "customerId="+prospecto.getCustomerId(), null);

                        notifyItemChanged(position);
                    }
                });
                builder.setNegativeButton("Cancelar",null);

                builder.create();
                builder.show();
                
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaProspectos.size();
    }

    public class ProspectosViewHolder extends RecyclerView.ViewHolder {

        TextView nombre,apellido,direccion,telefono,customerID;
        Button btnEditar,btnEliminar;

        public ProspectosViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.txt_Nombre);
            apellido = (TextView) itemView.findViewById(R.id.txt_Apellido);
            direccion = (TextView) itemView.findViewById(R.id.txt_Direccion);
            telefono = (TextView) itemView.findViewById(R.id.txt_Telefono);
            customerID = (TextView) itemView.findViewById(R.id.txt_CustomerID);

            btnEditar = (Button) itemView.findViewById(R.id.btnEditar);
            btnEliminar = (Button) itemView.findViewById(R.id.btnEliminar);

        }
    }

}
