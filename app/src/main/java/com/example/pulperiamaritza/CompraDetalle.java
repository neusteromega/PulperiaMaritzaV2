package com.example.pulperiamaritza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pulperiamaritza.Adaptadores.DetalleCompraAdapter;
import com.example.pulperiamaritza.Herramientas.ProductosTodos;
import com.example.pulperiamaritza.Modelos.DetalleCompraItemsModel;

import java.util.List;

public class CompraDetalle extends AppCompatActivity {

    private TextView idCompra, fechaCompra, sucursalCompra, subtotalCompra, bolsasCompra, totalCompra;
    private RecyclerView rvProductos;
    public List<DetalleCompraItemsModel> items;
    private ProductosTodos todos = new ProductosTodos(this);
    private DetalleCompraAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_detalle);

        idCompra = (TextView) findViewById(R.id.lblIDCompra);
        fechaCompra = (TextView) findViewById(R.id.lblFechaCompra);
        sucursalCompra = (TextView) findViewById(R.id.lblSucursalCompra);
        subtotalCompra = (TextView) findViewById(R.id.lblSubtotalCompra);
        bolsasCompra = (TextView) findViewById(R.id.lblBolsasCompra);
        totalCompra = (TextView) findViewById(R.id.lblTotalCompra);

        rvProductos = findViewById(R.id.rvProductosCompra);

        inicializarValores();
    }

    private void inicializarValores() {
        Intent intent = getIntent();
        int codigoCompra = Integer.parseInt(intent.getStringExtra("ventaCodigo"));
        int bolsas = todos.comprasBolsas(codigoCompra);
        double subtotal = todos.comprasSubtotal(codigoCompra);
        double total = bolsas + subtotal;

        LinearLayoutManager manager = new LinearLayoutManager(this); //Creamos un manager para que el RecyclerView se vea en forma de tarjetas
        rvProductos.setLayoutManager(manager); //Asignamos el manager al RecyclerView

        items = todos.obtenerDetalleCompra(codigoCompra); //Obtenemos los productos de la compra llamando a la funcion "obtenerDetalleCompra"

        adapter = new DetalleCompraAdapter(items); //Creamos un objeto de tipo DetalleCompraAdapter en el cual enviamos la lista "items", y dicho objeto lo igualamos al otro objeto de tipo DetalleCompraAdapter llamado "adapter"
        rvProductos.setAdapter(adapter); //Asignamos el adaptador al RecyclerView "rvProductos"

        //Asignamos los distintos datos a los TextViews de la pantalla
        idCompra.setText(String.valueOf(codigoCompra));
        fechaCompra.setText(intent.getStringExtra("ventaFecha"));
        sucursalCompra.setText(todos.comprasSucursal(codigoCompra));
        subtotalCompra.setText("L." + String.format("%.2f", subtotal));
        bolsasCompra.setText("L." + bolsas + ".00");
        totalCompra.setText("L." + String.format("%.2f", total));
    }

    public void regresarHistorial(View view) {
        Intent pagina = new Intent(this, Historial.class);
        startActivity(pagina);
    }
}