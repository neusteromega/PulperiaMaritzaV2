package com.example.pulperiamaritza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pulperiamaritza.Adaptadores.CTCategoriasAdapter;
import com.example.pulperiamaritza.Adaptadores.CTProductosAdapter;
import com.example.pulperiamaritza.Adaptadores.CTProveedoresAdapter;
import com.example.pulperiamaritza.Herramientas.ProductosTodos;
import com.example.pulperiamaritza.Modelos.CTCatPrvItemsModel;
import com.example.pulperiamaritza.Modelos.CTProductosItemsModel;
import com.example.pulperiamaritza.Modelos.CarritoItemsModel;
import com.example.pulperiamaritza.Modelos.ProductoItemsModel;

import java.util.List;

public class ConsultarTodo extends AppCompatActivity {

    private RecyclerView rvCategorias, rvProductos, rvProveedores;
    private CTProductosAdapter adapterProductos;
    private CTCategoriasAdapter adapterCategorias;
    private CTProveedoresAdapter adapterProveedores;
    public List<CTCatPrvItemsModel> itemsCategorias;
    public List<ProductoItemsModel> itemsProductos;
    public List<CTCatPrvItemsModel> itemsProveedores;
    private ProductosTodos todos = new ProductosTodos(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_todo);

        inicializarVistas();
        inicializarValores();
    }

    private void inicializarVistas() {
        rvCategorias = findViewById(R.id.rvCategoriasCT);
        rvProductos = findViewById(R.id.rvProductosCT);
        rvProveedores = findViewById(R.id.rvProveedoresCT);
    }

    private void inicializarValores() {
        //Creamos 3 LinearLayoutManager para que los 3 RecyclerViews se vean en forma de tarjetas
        LinearLayoutManager managerPrd = new LinearLayoutManager(this);
        LinearLayoutManager managerCat = new LinearLayoutManager(this);
        LinearLayoutManager managerPrv = new LinearLayoutManager(this);

        //Asignamos que la orientación de los recyclerviews sea horizontal en cada LinearLayoutManager
        managerPrd.setOrientation(LinearLayoutManager.HORIZONTAL);
        managerCat.setOrientation(LinearLayoutManager.HORIZONTAL);
        managerPrv.setOrientation(LinearLayoutManager.HORIZONTAL);

        //Asignamos el manager a los tres RecyclerView
        rvCategorias.setLayoutManager(managerCat);
        rvProductos.setLayoutManager(managerPrd);
        rvProveedores.setLayoutManager(managerPrv);

        //Obtenemos los registros de las categorias, productos y proveedores a mostrar en los Recyclerviews al mandar a llamar a los métodos "obtenerCategoriasCTBDD", "obtenerProductosCTBDD" y "obtenerProveedoresCTBB" respectivamente y le enviamos la cantidad de registros que queremos obtener, en este caso, son 5
        itemsCategorias = todos.obtenerCategoriasCTBDD(5);
        itemsProductos = todos.obtenerProductosCTBDD(5);
        itemsProveedores = todos.obtenerProveedoresCTBDD(5);

        //Creamos 3 objetos de tipo CTCategoriasAdapter, CTProductosAdapter y CTProveedoresAdapter en los cual enviamos las listas "itemsCategorias", "itemsProductos" e "itemsProveedores", y dichos objetos los igualamos a los otros objetos de tipo CTCategoriasAdapter llamado "adapterCategorias", CTProductosAdapter llamado "adapterProductos" y CTProveedoresAdapter llamado "adapterProveedores"
        adapterCategorias = new CTCategoriasAdapter(itemsCategorias);
        adapterProductos = new CTProductosAdapter(itemsProductos);
        adapterProveedores = new CTProveedoresAdapter(itemsProveedores);

        adapterCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProductosPorCatPrv.class);
                intent.putExtra("categoria", itemsCategorias.get(rvCategorias.getChildAdapterPosition(view)).getNombre());
                intent.putExtra("tipo", "Categoria");
                startActivity(intent);
            }
        });

        adapterProductos.setOnClickListener(new View.OnClickListener() { //Usando el objeto de "adapter" llamamos al método "setOnClickListener" de la clase CTProductosAdapter
            @Override
            public void onClick(View view) { //Al dar click en una tarjeta del RecyclerView "rvProductos" se realizará lo siguiente
                Intent intent = new Intent(getApplicationContext(), ProductosDetalle.class);
                intent.putExtra("productoImagen", itemsProductos.get(rvProductos.getChildAdapterPosition(view)).getImagen());
                intent.putExtra("productoPrecio", itemsProductos.get(rvProductos.getChildAdapterPosition(view)).getPrecio1());
                intent.putExtra("productoPrecio2", itemsProductos.get(rvProductos.getChildAdapterPosition(view)).getPrecio2());
                intent.putExtra("productoNombre", itemsProductos.get(rvProductos.getChildAdapterPosition(view)).getNombre1());
                intent.putExtra("productoNombre2", itemsProductos.get(rvProductos.getChildAdapterPosition(view)).getNombre2());
                intent.putExtra("productoCategoria", itemsProductos.get(rvProductos.getChildAdapterPosition(view)).getCategoria());
                intent.putExtra("productoProveedor", itemsProductos.get(rvProductos.getChildAdapterPosition(view)).getProveedor());
                intent.putExtra("productoTipo", itemsProductos.get(rvProductos.getChildAdapterPosition(view)).getTipo1());
                intent.putExtra("productoTipo2", itemsProductos.get(rvProductos.getChildAdapterPosition(view)).getTipo2());
                startActivity(intent);
            }
        });

        adapterProveedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProductosPorCatPrv.class);
                intent.putExtra("proveedor", itemsProveedores.get(rvProveedores.getChildAdapterPosition(view)).getNombre());
                intent.putExtra("tipo", "Proveedor");
                startActivity(intent);
            }
        });

        //Asignamos el adaptador correspondiente a su RecyclerView
        rvCategorias.setAdapter(adapterCategorias);
        rvProductos.setAdapter(adapterProductos);
        rvProveedores.setAdapter(adapterProveedores);
    }

    public void verCategorias(View view) {
        Intent pagina = new Intent(this, Categorias.class);
        startActivity(pagina);
    }

    public void verProductos(View view) {
        Intent pagina = new Intent(this, Productos.class);
        startActivity(pagina);
    }

    public void verProveedores(View view) {
        Intent pagina = new Intent(this, Proveedores.class);
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
}