package com.example.pulperiamaritza;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pulperiamaritza.Herramientas.AdminSQLiteOpen;
import com.example.pulperiamaritza.Herramientas.ProductosTodos;
import com.example.pulperiamaritza.Modelos.ProductoItemsModel;

import java.util.List;

public class IniciarSesion extends AppCompatActivity {

    private EditText txtCorreo, txtContra;
    private ImageView imgContra;
    private int cantidadClicks = 0; //Se utilizará en la parte de ocultar la contraseña
    private ProductosTodos todos = new ProductosTodos(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        txtCorreo = (EditText) findViewById(R.id.txtCorreoLogin);
        txtContra = (EditText) findViewById(R.id.txtContrasenaLogin);
        imgContra = (ImageView) findViewById(R.id.imgVerContrasenaLogin);

        List<ProductoItemsModel> items = todos.obtenerProductosBDD(); //Obtenemos la lista de los productos en la BDD

        //Mandamos a llamar el método "insertarDatos" solamente si el tamaño de la lista "items" es de 392, si es mayor, significa que ya se insertó la tercera porción de productos
        if (items.size() == 392)
            todos.insertarDatos("Productos", 3); //Le mandamos el nombre de la tabla "Productos" y el número 3 para indicar que se debe insertar la tercera lista de productos
    }

    public void consultarCredenciales (View view) {
        //Creamos un objeto de la clase AdminSQLiteOpen y le mandamos los parámetros al constructor de dicha clase
        AdminSQLiteOpen admin = new AdminSQLiteOpen(this, "PulperiaMaritza", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();

        String correo = txtCorreo.getText().toString();
        String contra = txtContra.getText().toString();

        if (!correo.isEmpty() && !contra.isEmpty()) { //Si las cajas de texto no están vacías, que entre al if
            try {
                //Creamos un cursor llamado "fila" con el cual podemos ejecutar la instrucción SELECT que me busque el correo, contraseña y ID de rol usando el correo ingresado por el usuario
                Cursor fila = baseDatos.rawQuery("SELECT UsuID, UsuCorreo, UsuContrasena, RolID FROM Usuarios WHERE UsuCorreo = '" + correo + "'", null);

                if (fila.moveToFirst()) { //Utilizamos el "if (fila.moveToFirst())" para mover el cursor "fila" a la primera fila de registros que encontró en la sentencia SELECT, sino encontró registros, no entrará a este if
                    if (fila.getString(2).contentEquals(contra)) { //Una vez entremos al if, verificamos que la contraseña del usuario sea la correspondiente al correo ingresado por el mismo
                        Perfil perfil = new Perfil(); //Creamos un objeto de la clase "Perfil"
                        perfil.idUsuario = fila.getInt(0); //Enviamos el ID del usuario que está iniciando sesión a la variable estática "idUsuario" en la clase "Perfil"

                        CarritoCompra carrito = new CarritoCompra(); //Creamos un objeto de la clase "CarritoCompra"
                        carrito.idUsuario = fila.getInt(0); //Enviamos el ID del usuario que está iniciando sesión a la variable estática "idUsuario" en la clase "CarritoCompra"

                        Historial historial = new Historial(); //Creamos un objeto de la clase "Historial"
                        historial.idUsuario = fila.getInt(0); //Enviamos el ID del usuario que está iniciando sesión a la variable estática "idUsuario" en la clase "Historial"

                        Intent pagina = new Intent(this, ConsultarTodo.class);
                        startActivity(pagina);
                    }
                    else {
                        Toast.makeText(this, "CONTRASEÑA INCORRECTA", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "NO SE ENCONTRÓ EL CORREO ELECTRÓNICO", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Toast.makeText(this, "ERROR AL OBTENER LAS CREDENCIALES", Toast.LENGTH_SHORT).show();
            }
            finally {
                if (baseDatos != null) { //Verificamos si "baseDatos" no es null para poder cerrar la conexión
                    baseDatos.close();
                }
            }
        }
        else {
            Toast.makeText(this, "TODOS LOS CAMPOS DEBEN LLENARSE", Toast.LENGTH_SHORT).show();
        }
    }

    public void ocultarContra(View view) {
        //En este if verificamos si la variable "cantidadClicks" es divisible entre 2 y si al realizar la división su residuo es 1
        if (cantidadClicks % 2 == 1) { //La cantidad de clicks al principio es 0 (Lo inicializamos en 0 arriba),
            //Si el residuo del número de "cantidadClicks" al dividirlo entre 2 es 1, se ocultará la contraseña y se mostrará el icono del ojo
            txtContra.setTransformationMethod(PasswordTransformationMethod.getInstance()); //El EditText ofrece diferentes formas de transformar el texto que se muestra. La clase "PasswordTransformationMethod" es una implementación de "TransformationMethod" diseñada específicamente para ocultar contraseñas al reemplazar el texto visible con caracteres ocultos, como puntos o asteriscos.
            imgContra.setImageResource(R.mipmap.icono_showcontraazul); //Cambiamos el icono del ojo
        }
        else {
            //Si el residuo del número de "cantidadClicks" al dividirlo entre 2 no es 0, se mostrará la contraseña y también el icono cambiará al ojo tachado
            txtContra.setTransformationMethod(null);
            imgContra.setImageResource(R.mipmap.icono_hidecontrazul); //Cambiamos el icono del ojo
        }

        cantidadClicks++; //Aquí vamos aumentando la cantidad de clicks cada vez que se entre a la función
        txtContra.setSelection(txtContra.getText().length()); // Mover el cursor al final del texto
    }

    public void olvidarContrasena(View view) {
        new AlertDialog.Builder(this).setTitle("RECUPERAR CONTRASEÑA").setMessage("Envíe un mensaje al siguiente correo para solicitar información al respecto: pulperiamaritza@gmail.com")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    public void registrarse(View view) {
        Intent pagina = new Intent(this, Registrarse.class);
        startActivity(pagina);
    }
}