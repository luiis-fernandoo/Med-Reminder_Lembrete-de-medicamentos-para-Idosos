package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.Adapter.SearchAdapter;
import com.example.medreminder_lembretedemedicamentosparaidosos.MyAsync.MyAsyncTask;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsMedicineActivity extends AppCompatActivity implements MyAsyncTask.AsyncTaskListener{

    private String numProcesso;
    private TextView nomeProduto, classeTerapeutica, principioAtivo, razaoSocial, conservacao, restricao, fabricante, prescricao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_medicine);

        nomeProduto = findViewById(R.id.nomeProduto);
        classeTerapeutica = findViewById(R.id.classeTerapeutica);
        principioAtivo = findViewById(R.id.principioAtivo);
        razaoSocial = findViewById(R.id.razaoSocial);
        conservacao = findViewById(R.id.conservacao);
        restricao = findViewById(R.id.restricao);
        fabricante = findViewById(R.id.fabricante);
        prescricao = findViewById(R.id.prescricao);


        Intent it = getIntent();
        numProcesso = it.getStringExtra("numProcesso");

        search(numProcesso);

    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {
        if (result != null) {
            Log.d("", "Restrição: " + result.get("restricaoUso"));
            try {
                nomeProduto.setText(result.getString("nomeComercial"));
                principioAtivo.setText("Princípio Ativo: "+result.getString("principioAtivo"));
                classeTerapeutica.setText("Classe Terapêutica: "+result.getString("classesTerapeuticas"));
                restricao.setText("Restrição de uso: " + result.getJSONArray("restricaoUso"));
                conservacao.setText("Conservação: " + result.getJSONArray("conservacao"));
                fabricante.setText("Fabricante: " + result.getJSONObject("fabricantesNacionais").getString("fabricante"));
                razaoSocial.setText("Empresa - Razão Social: " + result.getJSONObject("empresa").getString("razaoSocial"));
                prescricao.setText("Prescrição: " + result.getJSONArray("restricaoPrescricao"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }else{
            Toast.makeText(this, "Não há medicamentos com esse número, tente outro!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onTaskError(String error) {
    }
    private void search(String query){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(numProcesso != null){
                    String url = "https://consultas.anvisa.gov.br/api/consulta/medicamento/produtos/"+numProcesso;
                    MyAsyncTask myAsyncTaskSearch = new MyAsyncTask(DetailsMedicineActivity.this, url);
                    myAsyncTaskSearch.execute();
                }
            }
        }).start();
    }
}