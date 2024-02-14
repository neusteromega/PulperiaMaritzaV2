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

public class EditarPerfil extends AppCompatActivity {

    private EditText txtNombreApellido, txtTelefono, txtCorreo, txtContra, txtConfContra;
    private ImageView imgContra, imgConfContra;
    private int clicksContra = 0, clicksConfContra = 0; //Se utilizarán en la parte de ocultar la contraseña
    public static int idUsuario; //Variable global estática que recibe el ID del usuario desde la pantalla de Perfil

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        txtNombreApellido = (EditText) findViewById(R.id.txtNombreEdtPerfil);
        txtTelefono = (EditText) findViewById(R.id.txtTelefonoEdtPerfil);
        txtCorreo = (EditText) findViewById(R.id.txtCorreoEdtPerfil);
        txtContra = (EditText) findViewById(R.id.txtContraEdtPerfil);
        txtConfContra = (EditText) findViewById(R.id.txtConfContraEdtPerfil);

        imgContra = (ImageView) findViewById(R.id.imgVerContraEdtPerfil);
        imgConfContra = (ImageView) findViewById(R.id.imgVerConfContraEdtPerfil);
    }

    public void modificar(View view) { //Método que se ejecuta cuando se da clic en el botón "Guardar Cambios"
        //Creamos un objeto de la clase AdminSQLiteOpen y le mandamos los parámetros al constructor de dicha clase
        AdminSQLiteOpen admin = new AdminSQLiteOpen(this, "PulperiaMaritza", null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();

        //Enlazamos los EditText con las variables String
        String nombreApellido = txtNombreApellido.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String correo = txtCorreo.getText().toString();
        String contra = txtContra.getText().toString();
        String confContra = txtConfContra.getText().toString();

        //Como al editar los datos del perfil, no necesariamente deben actualizarse todos a la vez, entonces, con que uno de los textboxs tenga texto, podrá entrar a este if y poder actualizar ese dato en específico
        if (!nombreApellido.isEmpty() || !telefono.isEmpty() || !correo.isEmpty() || !contra.isEmpty() || !confContra.isEmpty()) {
            if (confContra.contentEquals(contra)) { //Si uno de esos que se quieren actualizar es la contraseña, esta debe ser confirmada en la casilla de "Confirmar Contraseña"
                try {
                    ContentValues usuario = new ContentValues(); //Creamos un contenedor que almacenará los datos a actualizar en la base de datos
                    int cant = 0; //Variable que nos servirá si se han actualizado los datos

                    //En los siguientes cinco if, si uno o varios de sus respectivos textboxs no está vacío, entonces que ese texto se guarde en el contenedor "usuario" en el campo específico de la tabla "Usuarios" de la BDD
                    if (!nombreApellido.isEmpty()) {
                        usuario.put("UsuNombreApellido", nombreApellido);
                    }

                    if (!telefono.isEmpty()) {
                        usuario.put("UsuTelefono", telefono);
                    }

                    if (!correo.isEmpty()) {
                        usuario.put("UsuCorreo", correo);
                    }

                    if (!contra.isEmpty()) {
                        usuario.put("UsuContrasena", contra);
                    }

                    if (usuario.size() > 0) //Verificamos que si el tamaño del contenedor "usuario" es mayor que 0, significa que se quiere realizar uno o más cambios
                        cant = baseDatos.update("Usuarios", usuario, "UsuID = " + idUsuario, null); //Realizamos la actualización de la tabla "Usuarios" utilizando en la claúsula WHERE el ID del usuario que está en la variable estática "idUsuario", si la actualización fue exitosa, la variable "cant" recibirá un 1

                    baseDatos.close(); //Cerramos la conexión a la BDD

                    if (cant == 1) { //Si "cant" es 1 quiere decir que los datos se actualizaron con éxito
                        Toast.makeText(this, "DATOS ACTUALIZADOS", Toast.LENGTH_SHORT).show();
                        Intent pagina = new Intent(this, Perfil.class); //Como se lograron actualizar los datos, enviamos al usuario a la pantalla de perfil
                        startActivity(pagina);
                    } else {
                        Toast.makeText(this, "ERROR AL ACTUALIZAR", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Toast.makeText(this, "ERROR EN LA BASE DE DATOS", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "LAS CONTRASEÑAS NO COINCIDEN", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "NO PUEDE DEJAR TODOS LOS CAMPOS VACÍOS", Toast.LENGTH_SHORT).show();
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

    public void regresarPerfil(View view) {
        Intent pagina = new Intent(this, Perfil.class);
        startActivity(pagina);
    }
}