package com.example.pulperiamaritza;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pulperiamaritza.Herramientas.AdminSQLiteOpen;
import com.example.pulperiamaritza.Herramientas.ProductosTodos;
import com.example.pulperiamaritza.Modelos.CarritoItemsModel;

import java.util.List;

public class Perfil extends AppCompatActivity {

    private TextView lblNombre, lblCorreo, lblTelefono, lblContra;
    private ImageView imgContra;
    private ProductosTodos todos = new ProductosTodos(this);
    public static int idUsuario; //Variable estática que recibirá el ID del usuario justo cuando este inicie sesión
    private static String contraUsuario; //Variable estática que recibirá la contraseña del usuario desde el método "mostrarDatos()"
    private int cantidadClicks = 0; //Se utilizará en la parte de ocultar la contraseña

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        lblNombre = (TextView) findViewById(R.id.lblNombrePerfil);
        lblCorreo = (TextView) findViewById(R.id.lblCorreoPerfil);
        lblTelefono = (TextView) findViewById(R.id.lblTelefonoPerfil);
        lblContra = (TextView) findViewById(R.id.lblContraPerfil);
        imgContra = (ImageView) findViewById(R.id.imgVerContraPerfil);

        mostrarDatos();
    }

    private void mostrarDatos() {
        //Creamos un objeto de la clase AdminSQLiteOpen y le mandamos los parámetros al constructor de dicha clase
        AdminSQLiteOpen admin = new AdminSQLiteOpen(this, "PulperiaMaritza", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();

        try {
            //Creamos un cursor llamado "datos" con el cual podemos ejecutar la instrucción SELECT que me busque el nombre y apellido, correo, teléfono y contraseña del usuario usando el ID del mismo
            Cursor datos = baseDatos.rawQuery("SELECT UsuNombreApellido, UsuCorreo, UsuTelefono, UsuContrasena FROM Usuarios WHERE UsuID = " + idUsuario, null);

            if (datos.moveToFirst()) { //Utilizamos el "if (datos.moveToFirst())" para mover el cursor "datos" a la primera fila de registros que encontró en la sentencia SELECT, sino encontró registros, no entrará a este if
                //Asignamos a los TextViews los datos del usuario extraídos en el cursor "datos"
                lblNombre.setText(datos.getString(0));
                lblCorreo.setText(datos.getString(1));
                lblTelefono.setText(datos.getString(2));
                lblContra.setText(datos.getString(3));

                contraUsuario = datos.getString(3); //Guardamos la contraseña del usuario en la variable global estática "contraUsuario"
                ocultarContra(); //Llamamos al método "ocultarContra()" para que nos oculte la contraseña al cargar el activity
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "ERROR AL OBTENER EL PERFIL", Toast.LENGTH_SHORT).show();
        }
        finally {
            if (baseDatos != null) { //Verificamos si "baseDatos" no es null para poder cerrar la conexión
                baseDatos.close();
            }
        }
    }

    public void ocultarContraClick(View view) { //Cuando demos click en el bóton del ojo para mostrar/ocultar contraseña, nos mandará a este método
        ocultarContra();
    }

    private void ocultarContra() {
        //En este if verificamos si la variable "cantidadClicks" es divisible entre 2 y si al realizar la división su residuo es 0
        if (cantidadClicks % 2 == 0) { //La cantidad de clicks al principio es 0 (Lo inicializamos en 0 arriba),
            //Si el residuo del número de "cantidadClicks" al dividirlo entre 2 es 0, se ocultará la contraseña y se mostrará el icono del ojo
            lblContra.setText("********");
            imgContra.setImageResource(R.mipmap.icono_showcontraazul); //Cambiamos el icono del ojo
        }
        else {
            //Si el residuo del número de "cantidadClicks" al dividirlo entre 2 no es 0, se mostrará la contraseña y también el icono cambiará al ojo tachado
            lblContra.setText(contraUsuario);
            imgContra.setImageResource(R.mipmap.icono_hidecontrazul); //Cambiamos el icono del ojo
        }

        cantidadClicks++; //Aquí vamos aumentando la cantidad de clicks cada vez que se entre a la función
    }

    public void editarPerfil(View view) {
        Intent pagina = new Intent(this, EditarPerfil.class);

        EditarPerfil perfil = new EditarPerfil(); //Creamos un objeto de la clase "Perfil"
        perfil.idUsuario = idUsuario; //Enviamos el ID del usuario que inició sesión a la variable estática "idUsuario" de "EditarPerfil"

        startActivity(pagina);
    }

    public void cerrarSesion(View view) {
        //Creamos un nuevo "AlertDialog" que nos pregunte si deseamos cerrar sesión
        new AlertDialog.Builder(this).setTitle("CERRAR SESIÓN").setMessage("¿Está seguro que desea cerrar sesión?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() { //Aquí se ejecutará una acción si el usuario seleccionó la opción de "Confirmar"
                    public void onClick(DialogInterface dialogInterface, int i) { //Es un "onClick" ya que estamos dando clic al botón de "Confirmar" que se muestra en la ventana emergente
                        startActivity(new Intent(Perfil.this, Inicio.class)); //Al confirmar el cierre de la sesión que nos mande a la pantalla inicial
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() { //Aquí se ejecutará una acción si el usuario seleccionó la opción de "Cancelar"
                    public void onClick(DialogInterface dialogInterface, int i) { //Aquí también es un "onClick" ya que estamos dando clic al botón de "Cancelar" que se muestra en la ventana emergente
                        Log.d("Mensaje", "Se canceló la acción"); //Como se canceló el cierre de la sesión, se muestra un mensaje en el Logcat indicando que se canceló la acción
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
}