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
            try {
                Log.d("", "String"+ result.toString());
                nomeProduto.setText(result.getString("nomeComercial"));
                principioAtivo.setText(getApplicationContext().getString(R.string.active_principle) + ": " + result.getString("principioAtivo"));

                JSONArray classesTerapeuticasArray = result.optJSONArray("classesTerapeuticas");
                if (classesTerapeuticasArray != null && classesTerapeuticasArray.length() > 0) {
                    String classeTerapeuticaString = classesTerapeuticasArray.getString(0);
                    classeTerapeutica.setText(getApplicationContext().getString(R.string.therapeutic_class) + ": " + classeTerapeuticaString);
                }

                JSONArray jsonApresentacoes = result.optJSONArray("apresentacoes");
                if (jsonApresentacoes != null && jsonApresentacoes.length() > 0) {
                    String restricaoUso = jsonApresentacoes.getJSONObject(0).getJSONArray("restricaoUso").getString(0);
                    String conservacaoString = jsonApresentacoes.getJSONObject(0).getJSONArray("conservacao").getString(0);
                    String viasAdm = jsonApresentacoes.getJSONObject(0).getJSONArray("viasAdministracao").getString(0);
                    String prescricaoString = jsonApresentacoes.getJSONObject(0).getJSONArray("restricaoPrescricao").getString(0);

                    restricao.setText(getApplicationContext().getString(R.string.restriction) + ": " + restricaoUso);
                    conservacao.setText(getApplicationContext().getString(R.string.conservation) + ": " + conservacaoString);
                    fabricante.setText(getApplicationContext().getString(R.string.use) + ": " + viasAdm);
                    prescricao.setText(getApplicationContext().getString(R.string.prescription) + ": " + prescricaoString);
                }

                JSONObject empresaObject = result.getJSONObject("empresa");
                String razaoSocialString = empresaObject.getString("razaoSocial");
                razaoSocial.setText(getApplicationContext().getString(R.string.company_corporate_name) + ": " + razaoSocialString);

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