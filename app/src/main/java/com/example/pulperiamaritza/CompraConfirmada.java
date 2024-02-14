package com.example.pulperiamaritza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CompraConfirmada extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_confirmada);
    }

    public void verConsultarTodo(View view) {
        Intent pagina = new Intent(this, ConsultarTodo.class);
        startActivity(pagina);
    }
}