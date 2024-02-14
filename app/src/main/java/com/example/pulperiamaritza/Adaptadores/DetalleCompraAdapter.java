package com.example.pulperiamaritza.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulperiamaritza.Modelos.DetalleCompraItemsModel;
import com.example.pulperiamaritza.R;

import java.util.List;

public class DetalleCompraAdapter extends RecyclerView.Adapter<DetalleCompraAdapter.RecyclerHolder> {
    private List<DetalleCompraItemsModel> items; //Creamos una lista de tipo DetalleCompraItemsModel

    public DetalleCompraAdapter(List<DetalleCompraItemsModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_detalle_compra, parent, false); //Inflamos la vista que utilizaremos para las tarjetas del RecyclerView
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        DetalleCompraItemsModel item = items.get(position); //Creamos una lista de tipo DetalleCompraItemsModel llamada "item" la cual igualamos a la otra lista "items" extrayendo posición por posición

        //Haciendo uso del objeto "holder", asignamos los textos a las diferentes variables que se encuentran en la clase estática "RecyclerHolder"
        holder.tvProducto.setText(item.getNombre());
        holder.tvTipo.setText(item.getTipo() + ":");
        holder.tvPrecio.setText("L."+ item.getPrecio());
        holder.tvCantidad.setText(item.getCantidad());
        holder.tvTotal.setText("L."+ item.getTotal());
    }

    @Override
    public int getItemCount() {
        return items.size(); //Retornamos la cantidad de elementos que tiene la lista "items"
    }

    public static class RecyclerHolder extends RecyclerView.ViewHolder {
        //Variables para cada elemento cambiante de las tarjetas del RecyclerView
        private TextView tvProducto;
        private TextView tvTipo;
        private TextView tvPrecio;
        private TextView tvCantidad;
        private TextView tvTotal;

        public RecyclerHolder(@NonNull View itemView) { //Método Constructor que recibe un View
            super(itemView);

            //Referenciamos los elementos de la vista de las tarjetas del RecyclerView a las variables de arriba
            tvProducto = itemView.findViewById(R.id.lblNombrePrdCompra);
            tvTipo = itemView.findViewById(R.id.lblTipoPrdCompra);
            tvPrecio = itemView.findViewById(R.id.lblPrecioUnitarioCompra);
            tvCantidad = itemView.findViewById(R.id.lblCantidadPrdCompra);
            tvTotal = itemView.findViewById(R.id.lblTotalPrdCompra);
        }
    }
}
