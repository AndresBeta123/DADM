package com.example.unal.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import com.example.unal.myapplication.DatabaseHelper;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public EditText  eTId;
    public EditText  eTNombreEmpresa;
    public EditText  eTUrlE;
    public EditText  eTTelefono;
    public EditText  eTEmail;
    public EditText  eTProductos;
    public EditText  eTClasificacion;

    public RecyclerView recyclerView;
    public DatabaseManager dbManager;
    public EmpresaAdapter empresaAdapter;

    public EditText  eTFiltroNombreEmpresa;
    public EditText  eTFiltroClasificacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eTId = (EditText) findViewById(R.id.editTextNumber);
        eTNombreEmpresa = (EditText) findViewById(R.id.editTextText2);
        eTUrlE = (EditText) findViewById(R.id.editTextText3);
        eTTelefono = (EditText) findViewById(R.id.editTextText4);
        eTEmail = (EditText) findViewById(R.id.editTextText5);
        eTProductos = (EditText) findViewById(R.id.editTextText6);
        eTClasificacion = (EditText) findViewById(R.id.editTextText7);
        eTFiltroNombreEmpresa = (EditText) findViewById(R.id.editTextText8);
        eTFiltroClasificacion = (EditText) findViewById(R.id.editTextText9);

        dbManager = new DatabaseManager(this);

        try{
            dbManager.open();
        }catch (Exception e) {
            e.printStackTrace();
        }
        recyclerView = findViewById(R.id.recyclerView); // Asegúrate de que este ID coincida con el RecyclerView en tu diseño XML
        // Configura el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Crea una lista de empresas (puedes obtenerla de tu base de datos o de cualquier fuente de datos)
        List<Empresa> empresas = fetching("",""); // Debes implementar este método
        // Crea y configura el adaptador
        empresaAdapter = new EmpresaAdapter(empresas);
        recyclerView.setAdapter(empresaAdapter);
    }
    public void btnInsert(View v){

        DataEmpresa dataEmpresa = new DataEmpresa();
        dataEmpresa.nombreEmpresa		= eTNombreEmpresa.getText().toString();
        dataEmpresa.urlE				= eTUrlE.getText().toString();
        dataEmpresa.telefono			= eTTelefono.getText().toString();
        dataEmpresa.email				= eTEmail.getText().toString();
        dataEmpresa.productos			= eTProductos.getText().toString();
        dataEmpresa.clasificacion		= eTClasificacion.getText().toString();
        try {
            dbManager.insert(dataEmpresa);
        }catch (Exception e) {
            Log.i("mesa","ERRROR");
            e.printStackTrace();
        }
    }
    public void btnUpdate(View v){
        DataEmpresa dataEmpresa = new DataEmpresa();
        dataEmpresa.nombreEmpresa		= eTNombreEmpresa.getText().toString();
        dataEmpresa.urlE				= eTUrlE.getText().toString();
        dataEmpresa.telefono			= eTTelefono.getText().toString();
        dataEmpresa.email				= eTEmail.getText().toString();
        dataEmpresa.productos			= eTProductos.getText().toString();
        dataEmpresa.clasificacion		= eTClasificacion.getText().toString();
        dbManager.update(Long.parseLong(eTId.getText().toString()),dataEmpresa);
    }
    public void btnDelete(View v){
        dbManager.delete(Long.parseLong(eTId.getText().toString()));
    }
    public void btnFetch(View v){
        String filtroNombre = eTFiltroNombreEmpresa.getText().toString();
        String filtroClasificacion = eTFiltroClasificacion.getText().toString();
        recyclerView = findViewById(R.id.recyclerView); // Asegúrate de que este ID coincida con el RecyclerView en tu diseño XML
        // Configura el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Crea una lista de empresas (puedes obtenerla de tu base de datos o de cualquier fuente de datos)
        List<Empresa> empresas =  fetching(filtroNombre,filtroClasificacion); // Debes implementar este método
        // Crea y configura el adaptador
        empresaAdapter = new EmpresaAdapter(empresas);
        recyclerView.setAdapter(empresaAdapter);
    }

    public List<Empresa> fetching(String filtroNombre,String filtroClasificacion){
        Cursor cursor = dbManager.fetch(filtroNombre,filtroClasificacion);

        List<Empresa> empresas = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Empresa empresa = new Empresa();
                String ID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID));
                String nombreEmpresa = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_EMPRESA));
                empresa.id = ID;
                empresa.nombreEmpresa = nombreEmpresa;
                empresas.add(empresa);
                Log.i("database_tag","i have read id : "+ID+" nombreEmpresa "+nombreEmpresa);
            }while(cursor.moveToNext());
        }
        return empresas;
    }
}