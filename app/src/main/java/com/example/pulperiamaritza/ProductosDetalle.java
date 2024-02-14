package com.example.pulperiamaritza;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pulperiamaritza.Herramientas.ProductosTodos;
import com.example.pulperiamaritza.Modelos.CarritoItemsModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductosDetalle extends AppCompatActivity implements TextWatcher {

    private ImageView imgProducto;
    private TextView lblPrecio, lblNombreProducto, lblCategoria, lblProveedor, lblTipoProducto, lblTotalProducto;
    private EditText lblCantidad;
    private Switch swCambiar;
    private ProductosTodos todos = new ProductosTodos(this);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_detalle);

        imgProducto = findViewById(R.id.imgProductoDet);
        lblPrecio = findViewById(R.id.lblPrecioProductoDet);
        lblNombreProducto = findViewById(R.id.lblNombreProductoDet);
        lblCategoria = findViewById(R.id.lblCategoriaProductoDet);
        lblProveedor = findViewById(R.id.lblProveedorProductoDet);
        lblTipoProducto = findViewById(R.id.lblTipoProductoDet);
        lblCantidad = findViewById(R.id.txtCantidadProductoDet);
        lblTotalProducto = findViewById(R.id.lblTotalProductoDet);
        swCambiar = findViewById(R.id.swCambiar);

        lblCantidad.addTextChangedListener(this); // Agregar el TextWatcher al EditText

        Intent intent = getIntent();
        imgProducto.setImageResource(intent.getIntExtra("productoImagen", 0));
        lblPrecio.setText("L." + intent.getStringExtra("productoPrecio"));
        lblNombreProducto.setText(intent.getStringExtra("productoNombre"));
        lblCategoria.setText(intent.getStringExtra("productoCategoria"));
        lblProveedor.setText(intent.getStringExtra("productoProveedor"));

        if (intent.getStringExtra("productoTipo").contentEquals("1 U")) {
            lblTipoProducto.setText("Unidad");
        }
        else {
            String tipoTexto = intent.getStringExtra("productoTipo");
            tipoTexto = tipoTexto.replaceAll("[^\\p{L}]", "");
            lblTipoProducto.setText(tipoTexto);
        }

        if (intent.getStringExtra("productoTipo2").contentEquals("0")) { //Verificamos que el producto no tenga un segundo tipo (si no lo tiene, mantiene un "0" guardado) para que el switch se vuelva invisible
            swCambiar.setVisibility(View.INVISIBLE);
        }

        String cantidadTxt = lblCantidad.getText().toString();
        int cantidadNum = Integer.parseInt(cantidadTxt);
        calcularTotal(cantidadNum);

        //Toast.makeText(this, intent.getIntExtra("productoImagen", 0), Toast.LENGTH_LONG).show();
    }

    public void cambiarSwitch(View view) {
        if (view.getId() == R.id.swCambiar) {
            if (swCambiar.isChecked()) { //Validamos que el switch esté activado
                Intent intent = getIntent();
                lblPrecio.setText("L." + intent.getStringExtra("productoPrecio2"));

                if (!intent.getStringExtra("productoNombre2").contentEquals("0")) //Si el segundo nombre del producto no es "0", entonces que me asigne dicho nombre en el TextView "lblNombreProducto"
                    lblNombreProducto.setText(intent.getStringExtra("productoNombre2"));

                if (intent.getStringExtra("productoTipo2").contentEquals("1 U")) {
                    lblTipoProducto.setText("Unidad");
                } else {
                    //Extraemos solo el texto que se encuentra en la segunda cantidad que puede tener el producto
                    String tipoTexto = intent.getStringExtra("productoTipo2");
                    tipoTexto = tipoTexto.replaceAll("[^\\p{L}]", "");
                    lblTipoProducto.setText(tipoTexto);
                }

                String cantidadTxt = lblCantidad.getText().toString();
                int cantidadNum = Integer.parseInt(cantidadTxt);
                calcularTotal(cantidadNum);
            }
            else { //Aquí el switch no está activado
                Intent intent = getIntent();
                lblPrecio.setText("L." + intent.getStringExtra("productoPrecio"));
                lblNombreProducto.setText(intent.getStringExtra("productoNombre"));

                if (intent.getStringExtra("productoTipo").contentEquals("1 U")) {
                    lblTipoProducto.setText("Unidad");
                } else {
                    //Extraemos solo el texto que se encuentra en la primera cantidad que puede tener el producto
                    String tipoTexto = intent.getStringExtra("productoTipo");
                    tipoTexto = tipoTexto.replaceAll("[^\\p{L}]", "");
                    lblTipoProducto.setText(tipoTexto);
                }

                String cantidadTxt = lblCantidad.getText().toString();
                int cantidadNum = Integer.parseInt(cantidadTxt);
                calcularTotal(cantidadNum);
            }
        }
    }

    public void productoDisminuir(View view) {
        String cantidadTxt = lblCantidad.getText().toString();
        int cantidadNum = Integer.parseInt(cantidadTxt);

        if (cantidadNum > 1)
            cantidadNum -= 1;

        cantidadTxt = String.valueOf(cantidadNum);

        lblCantidad.setText(cantidadTxt);
        calcularTotal(cantidadNum);
    }

    public void productoAumentar(View view) {
        String cantidadTxt = lblCantidad.getText().toString();
        int cantidadNum = Integer.parseInt(cantidadTxt);

        cantidadNum += 1;
        cantidadTxt = String.valueOf(cantidadNum);

        lblCantidad.setText(cantidadTxt);
        calcularTotal(cantidadNum);
    }

    public void calcularTotal(int cantidad) {
        double total;
        String precioTxt = lblPrecio.getText().toString();

        Intent intent = getIntent();
        String precio1 = intent.getStringExtra("productoPrecio");
        String precio2 = intent.getStringExtra("productoPrecio2");

        if (precioTxt.contains(precio1)) {
            double precioNum = Double.parseDouble(precio1);
            total = cantidad * precioNum;

            lblTotalProducto.setText("L." + String.format("%.2f", total));
        }
        else if (precioTxt.contains(precio2)) {
            double precioNum = Double.parseDouble(precio2);
            total = cantidad * precioNum;

            lblTotalProducto.setText("L." + String.format("%.2f", total));
        }
    }

    public void carrito(View view) {
        boolean verificarProducto = false; //Creamos una variable booleana para comprobar si el producto ya fue agregado al carrito o no
        List<CarritoItemsModel> itemsCarrito; //Creamos una lista de tipo CarritoItemsModel
        itemsCarrito = todos.obtenerCarrito(); //Obtenemos los productos ya agregados al carrito (la tabla CarritoTemporal de la BDD) llamando al método "obtenerCarrito()" de la clase ProductosTodos

        for (int i = 0; i < itemsCarrito.size(); i++) { //Hacemos un for que recorra toda la lista "itemsCarrito"
            if (lblNombreProducto.getText().toString().contentEquals(itemsCarrito.get(i).getNombre())) { //Creamos un if que verifique si el nombre del producto actual coincide con algún producto agregado en el carrito
                verificarProducto = true; //Si algún producto coincide con el producto actual, quiere decir que este producto ya está en el carrito, por lo tanto, la variable "verificarProducto" pasa a ser true
            }
        }

        if (verificarProducto == false) { //Verificamos que si la variable "verificarProducto" es falsa, quiere decir que el producto actual no está en el carrito, por lo tanto, procedemos a hacer lo demás para añadirlo
            String regex = "(?<=L\\.)\\d+\\.\\d+";
            String precio = lblPrecio.getText().toString(); //Extraemos el precio del lblPrecio
            String resultPrecio = "";
            String total = lblTotalProducto.getText().toString(); //Extraemos el total del lblTotalProducto
            String resultTotal = "";

            Pattern patternPrecio = Pattern.compile(regex);
            Matcher matcherPrecio = patternPrecio.matcher(precio);

            if (matcherPrecio.find())
                resultPrecio = matcherPrecio.group();

            Pattern patternTotal = Pattern.compile(regex);
            Matcher matcherTotal = patternTotal.matcher(total);

            if (matcherTotal.find())
                resultTotal = matcherTotal.group();

            Intent intent = getIntent();
            todos.insertarDatosCarrito(lblNombreProducto.getText().toString(), lblTipoProducto.getText().toString(), lblCantidad.getText().toString(), resultPrecio, resultTotal, intent.getIntExtra("productoImagen", 0));
        }
        else { //Pero si "verificarProducto" es true, el producto ya ha sido agregado en el carrito
            Toast.makeText(this, "ESTE PRODUCTO YA SE ENCUENTRA EN EL CARRITO", Toast.LENGTH_SHORT).show(); //Enviamos un mensaje en pantalla con una advertencia indicando que el producto ya está en el carrito
        }
    }

    //Antes de que el texto del Edittext cambie
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    //Durante el texto del Edittext está cambiando
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    //Después de que el texto del Edittext cambie
    @Override
    public void afterTextChanged(Editable editable) {
        //Cuando detecte que el Edittext está vacío, que asigne un 0
        String text = editable.toString().trim();
        if (text.isEmpty()) {
            editable.append("0");
        }

        calcularTotal(Integer.parseInt(editable.toString()));
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