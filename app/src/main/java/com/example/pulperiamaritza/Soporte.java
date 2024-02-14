package com.example.pulperiamaritza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pulperiamaritza.Herramientas.ProductosTodos;
import com.example.pulperiamaritza.Modelos.CarritoItemsModel;

import java.util.List;

public class Soporte extends AppCompatActivity {

    private ProductosTodos todos = new ProductosTodos(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soporte);
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

    public void verPerfil(View view) {
        Intent pagina = new Intent(this, Perfil.class);
        startActivity(pagina);
    }
}