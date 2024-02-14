package com.example.pulperiamaritza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pulperiamaritza.Herramientas.ProductosTodos;
import com.example.pulperiamaritza.Modelos.ProductoItemsModel;

import java.util.List;

public class Inicio extends AppCompatActivity {

    ProductosTodos todos = new ProductosTodos(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //Llamamos al método "verificarDatos" para corroborar que la tabla de Roles y Sucursales estén vacías, si lo están, retornará un 0 que se guardará en las variables enteras
        int rol = todos.verificarDatos("Roles");
        int suc = todos.verificarDatos("Sucursales");
        List<ProductoItemsModel> items = todos.obtenerProductosBDD(); //Obtenemos la lista de los productos en la BDD

        //Solo si las variables "rol, suc" son 0, que se ejecute la inserción de los datos llamando al método "insertarDatos" de la clase "ProductosTodos"
        if (rol == 0)
            todos.insertarRolesSucursales(1);

        if (suc == 0)
            todos.insertarRolesSucursales(2);

        //Mandamos a llamar el método "insertarDatos" solamente si el tamaño de la lista "items" es de 203, si es mayor, significa que ya se insertó la segunda porción de productos
        if (items.size() == 203)
            todos.insertarDatos("Productos", 2); //Le mandamos el nombre de la tabla "Productos" y el número 2 para indicar que se debe insertar la segunda lista de productos
    }

    public void iniciarSesion(View view) {
        Intent pagina = new Intent(this, IniciarSesion.class);
        startActivity(pagina);
    }

    public void registrarse(View view) {
        Intent pagina = new Intent(this, Registrarse.class);
        startActivity(pagina);
    }
}