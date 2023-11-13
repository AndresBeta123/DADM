package com.example.unal.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EmpresaAdapter extends RecyclerView.Adapter<EmpresaAdapter.EmpresaViewHolder> {

    private List<Empresa> empresas;

    public EmpresaAdapter(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    @NonNull
    @Override
    public EmpresaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empresa, parent, false);
        return new EmpresaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpresaViewHolder holder, int position) {
        Empresa empresa = empresas.get(position);
        holder.bind(empresa);
    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }

    public static class EmpresaViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNombreEmpresa;
        private TextView tvId;
        public EmpresaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreEmpresa = itemView.findViewById(R.id.textViewNombreEmpresa);
            tvId = itemView.findViewById(R.id.textViewId);
            // Este ID debe coincidir con el TextView en tu diseño de elemento
        }

        public void bind(Empresa empresa) {
            textViewNombreEmpresa.setText(empresa.nombreEmpresa);
            tvId.setText(empresa.id);
            // Puedes configurar otros TextView u otros elementos de la vista aquí
        }
    }
}