package com.example.pulperiamaritza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.pulperiamaritza.Adaptadores.HistComprasAdapter;
import com.example.pulperiamaritza.Herramientas.ProductosTodos;
import com.example.pulperiamaritza.Modelos.CarritoItemsModel;
import com.example.pulperiamaritza.Modelos.HistComprasItemsModel;

import java.util.List;

public class Historial extends AppCompatActivity {

    private RecyclerView rvHistorialCompras; //Creamos una variable de tipo RecyclerView
    private List<HistComprasItemsModel> itemsCompras; //Creamos una lista de tipo HistVentasItemsModel
    private TextView lblTotalComprado;
    public static int idUsuario; //Variable estática que recibirá el ID del usuario justo cuando este inicie sesión
    private double totalVentas = 0; //Variable para guardar la sumatoria de los totales de las ventas
    private ProductosTodos todos = new ProductosTodos(this); //Objeto de la clase "ProductosTodos"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        rvHistorialCompras = findViewById(R.id.rvHistorial);
        lblTotalComprado = findViewById(R.id.lblTotalVendidoHist);

        rvHistorialCompras.setLayoutManager(new LinearLayoutManager(this));
        itemsCompras = todos.obtenerCompras(2, idUsuario); //Obtenemos los datos de las compras llamando al método "obtenerCompras()" de la clase ProductosTodos y los guardamos en la lista "itemsCompras"

        if (itemsCompras != null) { //Verificamos que la lista "itemsCompras" no sea nula
            HistComprasAdapter adapter = new HistComprasAdapter(itemsCompras); //Creamos una variable de tipo HistComprasAdapter y le pasamos al método constructor de la clase la lista "itemsCompras"

            adapter.setOnClickListener(new View.OnClickListener() { //Usando el objeto de "adapter" llamamos al método "setOnClickListener" de la clase HistComprasAdapter
                @Override
                public void onClick(View view) { //Al dar click en una tarjeta del RecyclerView "rvProductos" se realizará lo siguiente
                    Intent intent = new Intent(getApplicationContext(), CompraDetalle.class);
                    intent.putExtra("ventaCodigo", itemsCompras.get(rvHistorialCompras.getChildAdapterPosition(view)).getCodigo());
                    intent.putExtra("ventaFecha", itemsCompras.get(rvHistorialCompras.getChildAdapterPosition(view)).getFecha());
                    startActivity(intent);
                }
            });

            rvHistorialCompras.setAdapter(adapter); //Asignamos el adaptador al RecyclerView "rvHistorialCompras"

            for (int i = 0; i < itemsCompras.size(); i++) { //Creamos un for que recorra toda la lista "itemsCompras"
                totalVentas += Double.parseDouble(itemsCompras.get(i).getTotal()); //Vamos sumando y guardando los totales de las ventas en la variable "totalVentas"
            }

            lblTotalComprado.setText("L." + String.format("%.2f", totalVentas)); //Asignamos el totalVentas al elemento lblTotalComprado
        }
    }

    public void verConsultarTodo(View view) {
        Intent pagina = new Intent(this, ConsultarTodo.class);
        startActivity(pagina);
    }

    public void verCarrito(View view) {
        List<CarritoItemsModel> itemsCarrito = todos.obtenerCarrito(); //Guardamos los productos del Carrito en la lista "itemsCarrito" de tipo CarritoItemsModel, esto lo hacemos llamando al método "obtenerCarrito()"
        if (itemsCarrito.isEmpty()) {
            Intent pagina = new Intent(this, CarritoVacio.class);
            startActivity(pagina);
        }
        else {
            Intent pagina = new Intent(this, CarritoCompra.class);
            startActivity(pagina);
        }
    }

    public void verSoporte(View view) {
        Intent pagina = new Intent(this, Soporte.class);
        startActivity(pagina);
    }

    public void verPerfil(View view) {
        Intent pagina = new Intent(this, Perfil.class);
        startActivity(pagina);
    }

    public void regresarInicio(View view) {
        Intent pagina = new Intent(this, ConsultarTodo.class);
        startActivity(pagina);
    }
}