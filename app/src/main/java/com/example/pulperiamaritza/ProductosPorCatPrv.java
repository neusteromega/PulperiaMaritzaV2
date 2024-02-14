package com.example.pulperiamaritza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.pulperiamaritza.Adaptadores.ProductosAdapter;
import com.example.pulperiamaritza.Modelos.CarritoItemsModel;
import com.example.pulperiamaritza.Modelos.ProductoItemsModel;
import com.example.pulperiamaritza.Herramientas.ProductosTodos;

import java.util.List;

public class ProductosPorCatPrv extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView searchView;
    private TextView catprv;
    private RecyclerView rvProductos;
    private ProductosAdapter adapter;
    private List<ProductoItemsModel> items;
    private ProductosTodos todos = new ProductosTodos(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_por_catprv);

        catprv = findViewById(R.id.lblProductosCatPrv);

        Intent intent = getIntent();
        if (intent.getStringExtra("tipo").contentEquals("Categoria"))
            catprv.setText(intent.getStringExtra("categoria"));
        else
            catprv.setText(intent.getStringExtra("proveedor"));

        inicializarVistas();
        inicializarValores();
    }

    private void inicializarVistas() {
        rvProductos = findViewById(R.id.rvProductosCatPrv);
        searchView = findViewById(R.id.svBuscarProductoCatPrv);
    }

    private void inicializarValores() {
        LinearLayoutManager manager = new LinearLayoutManager(this);

        rvProductos.setLayoutManager(manager);

        Intent intent = getIntent();
        if (intent.getStringExtra("tipo").contentEquals("Categoria"))
            items = todos.obtenerProductosPorCategoria(catprv.getText().toString());
        else
            items = todos.obtenerProductosPorProveedor(catprv.getText().toString());

        adapter = new ProductosAdapter(items);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProductosDetalle.class);
                intent.putExtra("productoImagen", items.get(rvProductos.getChildAdapterPosition(view)).getImagen());
                intent.putExtra("productoPrecio", items.get(rvProductos.getChildAdapterPosition(view)).getPrecio1());
                intent.putExtra("productoPrecio2", items.get(rvProductos.getChildAdapterPosition(view)).getPrecio2());
                intent.putExtra("productoNombre", items.get(rvProductos.getChildAdapterPosition(view)).getNombre1());
                intent.putExtra("productoNombre2", items.get(rvProductos.getChildAdapterPosition(view)).getNombre2());
                intent.putExtra("productoCategoria", items.get(rvProductos.getChildAdapterPosition(view)).getCategoria());
                intent.putExtra("productoProveedor", items.get(rvProductos.getChildAdapterPosition(view)).getProveedor());
                intent.putExtra("productoTipo", items.get(rvProductos.getChildAdapterPosition(view)).getTipo1());
                intent.putExtra("productoTipo2", items.get(rvProductos.getChildAdapterPosition(view)).getTipo2());
                startActivity(intent);
            }
        });

        rvProductos.setAdapter(adapter);

        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filtrado(s);
        return false;
    }

    public void verConsultarTodo(View view) {
        Intent pagina = new Intent(this, ConsultarTodo.class);
        startActivity(pagina);
    }

    public void verHistorial(View view) {
        Intent pagina = new Intent(this, Historial.class);
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