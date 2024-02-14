package com.example.pulperiamaritza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;

import com.example.pulperiamaritza.Adaptadores.CatPrvAdapter;
import com.example.pulperiamaritza.Modelos.CarritoItemsModel;
import com.example.pulperiamaritza.Modelos.CatPrvItemsModel;
import com.example.pulperiamaritza.Herramientas.ProductosTodos;

import java.util.ArrayList;
import java.util.List;

public class Proveedores extends AppCompatActivity implements SearchView.OnQueryTextListener {

    List<CatPrvItemsModel> items = new ArrayList<>();

    GridView gridView;
    SearchView searchView;
    CatPrvAdapter customAdapter;
    ProductosTodos todos = new ProductosTodos(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores);

        //Inicializamos el GridView y el SearchView
        gridView = findViewById(R.id.gvProveedor);
        searchView = findViewById(R.id.svBuscarProveedor);

        //Llenamos la Lista con los elementos (Categoría e Imagen)
        items = todos.obtenerProveedoresBDD();

        //Pasamos la lista de elementos a la clase "CustomAdapter", y a su vez, se recorre toda esa clase que nos retorna el adaptador para luego asignarlo en el GridView
        customAdapter = new CatPrvAdapter(items, this);

        //Asignamos el adaptador al GridView
        gridView.setAdapter(customAdapter);

        //Esto hace que la función "onQueryTextSubmit" se ejecute
        searchView.setOnQueryTextListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ProductosPorCatPrv.class);
                intent.putExtra("proveedor", items.get(i).getNombre());
                intent.putExtra("tipo", "Proveedor");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        customAdapter.filtrado(s);
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