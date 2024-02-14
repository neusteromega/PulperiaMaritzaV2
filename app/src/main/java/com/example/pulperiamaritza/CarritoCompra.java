package com.example.pulperiamaritza;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pulperiamaritza.Adaptadores.CarritoAdapter;
import com.example.pulperiamaritza.Modelos.CarritoItemsModel;
import com.example.pulperiamaritza.Herramientas.ProductosTodos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CarritoCompra extends AppCompatActivity {

    private RecyclerView rvCarrito;
    private CarritoAdapter adapter;
    private TextView lblSubtotal, lblBolsas, lblBolsasTotal, lblTotal;
    private Spinner spSucursales;
    public List<CarritoItemsModel> itemsCarrito;
    public static int idUsuario; //Variable estática que recibirá el ID del usuario justo cuando este inicie sesión
    private ProductosTodos todos = new ProductosTodos(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_compra);

        lblSubtotal = (TextView) findViewById(R.id.lblSubtotalCar);
        lblBolsas = (TextView) findViewById(R.id.lblCantidadBolsas);
        lblBolsasTotal = (TextView) findViewById(R.id.lblBolsasTotal);
        lblTotal = (TextView) findViewById(R.id.lblTotalCar);
        spSucursales = (Spinner) findViewById(R.id.spnSucursales);
        rvCarrito = findViewById(R.id.rvCarrito);

        inicializarSpinner();
        inicializarValores();
        subtotalCarrito();
        totalCarrito();
    }

    private void inicializarSpinner() {
        List<String> sucursales = todos.obtenerSucursales();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_sucursales, sucursales);
        spSucursales.setAdapter(adapter);
    }

    private void inicializarValores() {
        LinearLayoutManager manager = new LinearLayoutManager(this);

        rvCarrito.setLayoutManager(manager);

        itemsCarrito = todos.obtenerCarrito();
        adapter = new CarritoAdapter(itemsCarrito, this);
        rvCarrito.setAdapter(adapter);
    }

    public void subtotalCarrito() {
        double subtotal = 0;
        List<CarritoItemsModel> items = todos.obtenerCarrito();

        for (int i = 0; i < items.size(); i++) {
            subtotal += Double.parseDouble(items.get(i).getTotal());
        }

        lblSubtotal.setText("L." + String.format("%.2f", subtotal));
    }

    public void totalCarrito() {
        double subtotal = 0;
        double total = 0;
        List<CarritoItemsModel> items = todos.obtenerCarrito();

        for (int i = 0; i < items.size(); i++) {
            subtotal += Double.parseDouble(items.get(i).getTotal());
        }

        total = subtotal + Integer.parseInt(lblBolsas.getText().toString());
        lblTotal.setText("L." + String.format("%.2f", total));
    }

    public void bolsasDisminuir(View view) {
        String cantidadTxt = lblBolsas.getText().toString();
        int cantidadNum = Integer.parseInt(cantidadTxt);

        if (cantidadNum > 0)
            cantidadNum -= 1;

        cantidadTxt = String.valueOf(cantidadNum);

        lblBolsas.setText(cantidadTxt);
        totalBolsas(cantidadNum);
        totalCarrito();
    }

    public void bolsasAumentar(View view) {
        String cantidadTxt = lblBolsas.getText().toString();
        int cantidadNum = Integer.parseInt(cantidadTxt);

        cantidadNum += 1;
        cantidadTxt = String.valueOf(cantidadNum);

        lblBolsas.setText(cantidadTxt);

        totalBolsas(cantidadNum);
        totalCarrito();
    }

    public void totalBolsas(int cantidad) {
        double bolsasTotal = cantidad;
        lblBolsasTotal.setText("L." + String.format("%.2f", bolsasTotal));
    }

    public void eliminarCarrito(View view) {
        new AlertDialog.Builder(this).setTitle("ELIMINAR CARRITO").setMessage("¿Está seguro que desea eliminar todos los productos del carrito?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        todos.eliminarCarritoCompleto();
                        startActivity(new Intent(CarritoCompra.this, CarritoVacio.class));
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Mensaje", "Se canceló la acción"); //Como se canceló la eliminación del carrito, se muestra un mensaje en el Logcat indicando que se canceló la acción
                    }
                }).show();
    }

    public void confirmarVenta(View view) {
        //Creamos un nuevo "AlertDialog" que nos pregunte si deseamos confirmar la venta
        new AlertDialog.Builder(this).setTitle("CONFIRMAR COMPRA").setMessage("¿Está seguro que desea confirmar la compra?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() { //Aquí se ejecutará una acción si el usuario seleccionó la opción de "Confirmar"
                    public void onClick(DialogInterface dialogInterface, int i) { //Es un "onClick" ya que estamos dando clic al botón de "Confirmar" que se muestra en la ventana emergente
                        String bolsasTxt = lblBolsas.getText().toString(); //Guardamos la cantidad de bolsas que se encuentra en el textView "lblBolsas" en una variable String
                        int bolsasNum = Integer.parseInt(bolsasTxt); //Convertimos "bolsasTxt" a int, y guardamos el entero en "bolsasNum"

                        //Guardar la fecha de hoy
                        Calendar calendar = Calendar.getInstance(); //Obtener la fecha actual
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); //Crear el formato de fecha que deseas
                        String fechaActual = dateFormat.format(calendar.getTime()); //Convertir la fecha a un String

                        String sucursal = spSucursales.getSelectedItem().toString(); //Obtenemos la selección hecha en el spinner de sucursales

                        int idSucursal = todos.codigoSucursal(sucursal); //Llamamos al método "codigoSucursal" en la clase de ProductosTodos para extraer el id de la sucursal mediante la selección en el spinner de sucursales
                        int conf = todos.insertarVenta(fechaActual, bolsasNum, idUsuario, idSucursal); //Usando el objeto "todos" de la clase "ProductosTodos", llamamos al método "insertarVenta" y le mandamos la fecha de hoy (fechaActual), la cantidad de bolsas (bolsasNum) como parámetros, el ID del usuario que inició sesión (idUsuario) y el ID de la sucursal seleccionada (idSucursal) como parámetros

                        if (conf == 1) { //Si "conf" es uno, significa que si se realizó la confirmación de la compra...
                            todos.eliminarCarritoCompleto(); //..., por ello, llamamos al método "eliminarCarritoCompleto()" que nos va a borrar los datos de la tabla "CarritoTemporal" y así poder limpiar el carrito de venta
                            startActivity(new Intent(CarritoCompra.this, CompraConfirmada.class)); //Al confirmar la compra que nos mande a la pantalla de "CompraConfirmada"
                        }
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() { //Aquí se ejecutará una acción si el usuario seleccionó la opción de "Cancelar"
                    public void onClick(DialogInterface dialogInterface, int i) { //Aquí también es un "onClick" ya que estamos dando clic al botón de "Cancelar" que se muestra en la ventana emergente
                        Log.d("Mensaje", "Se canceló la acción"); //Como se canceló la confirmación de la venta, se muestra un mensaje en el Logcat indicando que se canceló la acción
                    }
                }).show();
    }

    public void verConsultarTodo(View view) {
        Intent pagina = new Intent(this, ConsultarTodo.class);
        startActivity(pagina);
    }

    public void verHistorial(View view) {
        Intent pagina = new Intent(this, Historial.class);
        startActivity(pagina);
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