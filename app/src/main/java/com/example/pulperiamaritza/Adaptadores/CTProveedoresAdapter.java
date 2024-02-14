package com.example.pulperiamaritza.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulperiamaritza.Modelos.CTCatPrvItemsModel;
import com.example.pulperiamaritza.R;

import java.util.List;

public class CTProveedoresAdapter extends RecyclerView.Adapter<CTProveedoresAdapter.RecyclerHolder> implements View.OnClickListener {
    private List<CTCatPrvItemsModel> items; //Creamos una lista de tipo CTCatPrvItemsModel
    private View.OnClickListener listener; //Creamos un escuchador (listener) de tipo "View.OnClickListener" que nos servirá para el onClick de cada tarjeta del RecyclerView

    public CTProveedoresAdapter(List<CTCatPrvItemsModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_ctproveedores, parent, false); //Inflamos la vista que utilizaremos para las tarjetas del RecyclerView
        view.setOnClickListener(this);
        return new RecyclerHolder(view); //Retornamos un nuevo objeto de tipo RecyclerHolder (La clase estática de abajo) y le mandamos la vista de la variable "view"
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        CTCatPrvItemsModel item = items.get(position); //Creamos una lista de tipo CTCatPrvItemsModel llamada "item" la cual igualamos a la otra lista "items" extrayendo posición por posición

        //Haciendo uso del objeto "holder", asignamos los textos a las diferentes variables que se encuentran en la clase estática "RecyclerHolder"
        holder.imgProveedor.setImageResource(item.getImagen());
        holder.tvProveedor.setText(item.getNombre());
    }

    @Override
    public int getItemCount() {
        return items.size(); //Retornamos la cantidad de elementos que tiene la lista "items"
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public static class RecyclerHolder extends RecyclerView.ViewHolder {
        //Variables para cada elemento cambiante de las tarjetas del RecyclerView
        private ImageView imgProveedor;
        private TextView tvProveedor;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);

            //Referenciamos los elementos de la vista de las tarjetas del RecyclerView a las variables de arriba
            imgProveedor = itemView.findViewById(R.id.imgCTProveedor);
            tvProveedor = itemView.findViewById(R.id.lblCTProveedor);
        }
    }
}