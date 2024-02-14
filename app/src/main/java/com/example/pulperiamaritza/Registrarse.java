package com.example.pulperiamaritza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pulperiamaritza.Herramientas.AdminSQLiteOpen;

public class Registrarse extends AppCompatActivity {

    private EditText txtNombreApellido, txtTelefono, txtCorreo, txtContra, txtConfContra;
    private ImageView imgContra, imgConfContra;
    private int clicksContra = 0, clicksConfContra = 0; //Se utilizarán en la parte de ocultar la contraseña

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        txtNombreApellido = (EditText) findViewById(R.id.txtNombreRegistrarse);
        txtTelefono = (EditText) findViewById(R.id.txtTelefonoRegistrarse);
        txtCorreo = (EditText) findViewById(R.id.txtCorreoRegistrarse);
        txtContra = (EditText) findViewById(R.id.txtContraRegistrarse);
        txtConfContra = (EditText) findViewById(R.id.txtConfContraRegistrarse);

        imgContra = (ImageView) findViewById(R.id.imgVerContraRegistrarse);
        imgConfContra = (ImageView) findViewById(R.id.imgVerConfContraRegistrarse);
    }

    public void registro(View view) {
        //Creamos un objeto de la clase AdminSQLiteOpen y le mandamos los parámetros al constructor de dicha clase
        AdminSQLiteOpen admin = new AdminSQLiteOpen(this, "PulperiaMaritza", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();

        //Enlazamos los EditText con las variables String
        String nombreApellido = txtNombreApellido.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String correo = txtCorreo.getText().toString();
        String contra = txtContra.getText().toString();
        String confContra = txtConfContra.getText().toString();

        if (!nombreApellido.isEmpty() && !telefono.isEmpty() && !correo.isEmpty() && !contra.isEmpty() && !confContra.isEmpty()) { //Si las cajas de texto no están vacías, que entre al if
            if (confContra.contentEquals(contra)) { //Si los campos de contraseña y confirmar contraseña no coinciden, no podrá entrar al if
                ContentValues usuario = new ContentValues(); //Creamos un contenedor que almacenará los datos a insertar en la base de datos

                //Guardamos los datos en cada campo de la tabla de la base de datos
                usuario.put("UsuNombreApellido", nombreApellido);
                usuario.put("UsuCorreo", correo);
                usuario.put("UsuTelefono", telefono);
                usuario.put("UsuContrasena", contra);
                usuario.put("RolID", 1);

                //Realizamos la inserción de los datos especificando el nombre de la tabla, y al final, colocamos el ContentValues "usuario"
                long id = baseDatos.insert("Usuarios", null, usuario);
                baseDatos.close();

                if (id > 0) { //Si "id" recibió un valor mayor que 0, significa que se realizó correctamente la inserción de datos
                    Toast.makeText(this, "REGISTRO CONFIRMADO", Toast.LENGTH_SHORT).show();

                    Intent pagina = new Intent(this, IniciarSesion.class);
                    startActivity(pagina);
                }
                else {
                    Toast.makeText(this, "ERROR AL REGISTRARSE", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "LAS CONTRASEÑAS NO COINCIDEN", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "TODOS LOS CAMPOS DEBEN LLENARSE", Toast.LENGTH_SHORT).show();
        }
    }

    public void ocultarContra(View view) {
        //En este if verificamos si la variable "cantidadClicks" es divisible entre 2 y si al realizar la división su residuo es 1
        if (clicksContra % 2 == 1) { //La cantidad de clicks al principio es 0 (Lo inicializamos en 0 arriba),
            //Si el residuo del número de "cantidadClicks" al dividirlo entre 2 es 1, se ocultará la contraseña y se mostrará el icono del ojo
            txtContra.setTransformationMethod(PasswordTransformationMethod.getInstance()); //El EditText ofrece diferentes formas de transformar el texto que se muestra. La clase "PasswordTransformationMethod" es una implementación de "TransformationMethod" diseñada específicamente para ocultar contraseñas al reemplazar el texto visible con caracteres ocultos, como puntos o asteriscos.
            imgContra.setImageResource(R.mipmap.icono_showcontraazul); //Cambiamos el icono del ojo
        }
        else {
            //Si el residuo del número de "cantidadClicks" al dividirlo entre 2 no es 0, se mostrará la contraseña y también el icono cambiará al ojo tachado
            txtContra.setTransformationMethod(null);
            imgContra.setImageResource(R.mipmap.icono_hidecontrazul); //Cambiamos el icono del ojo
        }

        clicksContra++; //Aquí vamos aumentando la cantidad de clicks cada vez que se entre a la función
        txtContra.setSelection(txtContra.getText().length()); // Mover el cursor al final del texto
    }

    public void ocultarConfContra(View view) {
        //En este if verificamos si la variable "cantidadClicks" es divisible entre 2 y si al realizar la división su residuo es 1
        if (clicksConfContra % 2 == 1) { //La cantidad de clicks al principio es 0 (Lo inicializamos en 0 arriba),
            //Si el residuo del número de "cantidadClicks" al dividirlo entre 2 es 1, se ocultará la contraseña y se mostrará el icono del ojo
            txtConfContra.setTransformationMethod(PasswordTransformationMethod.getInstance()); //El EditText ofrece diferentes formas de transformar el texto que se muestra. La clase "PasswordTransformationMethod" es una implementación de "TransformationMethod" diseñada específicamente para ocultar contraseñas al reemplazar el texto visible con caracteres ocultos, como puntos o asteriscos.
            imgConfContra.setImageResource(R.mipmap.icono_showcontraazul); //Cambiamos el icono del ojo
        }
        else {
            //Si el residuo del número de "cantidadClicks" al dividirlo entre 2 no es 0, se mostrará la contraseña y también el icono cambiará al ojo tachado
            txtConfContra.setTransformationMethod(null);
            imgConfContra.setImageResource(R.mipmap.icono_hidecontrazul); //Cambiamos el icono del ojo
        }

        clicksConfContra++; //Aquí vamos aumentando la cantidad de clicks cada vez que se entre a la función
        txtConfContra.setSelection(txtConfContra.getText().length()); // Mover el cursor al final del texto
    }

    public void iniciarSesion(View view) {
        Intent pagina = new Intent(this, IniciarSesion.class);
        startActivity(pagina);
    }
}