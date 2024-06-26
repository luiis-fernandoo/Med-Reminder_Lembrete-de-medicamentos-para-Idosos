package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.MyAsync.MyAsyncTask;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsMedicineActivity extends AppCompatActivity implements MyAsyncTask.AsyncTaskListener{

    private String numProcesso;
    private Dialog progressDialog;
    private LinearLayout buttonOk;
    private TextView nomeProduto, warningText, classeTerapeutica, principioAtivo, razaoSocial, conservacao, destiny, fabricante, prescricao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_medicine);

        nomeProduto = findViewById(R.id.nomeProduto);
        classeTerapeutica = findViewById(R.id.classeTerapeutica);
        principioAtivo = findViewById(R.id.principioAtivo);
        razaoSocial = findViewById(R.id.razaoSocial);
        conservacao = findViewById(R.id.conservacao);
        destiny = findViewById(R.id.destiny);
        fabricante = findViewById(R.id.fabricante);
        prescricao = findViewById(R.id.prescricao);

        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_loading);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);

        Intent it = getIntent();
        numProcesso = it.getStringExtra("numProcesso");
        progressDialog.show();
        search(numProcesso);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    popup_warning();
                }
            }
        }, 30000);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {
        if (result != null && !result.has("error")) {
            progressDialog.dismiss();
            try {
                nomeProduto.setText(result.getString("nomeComercial"));
                principioAtivo.setText(result.getString("principioAtivo"));

                JSONArray classesTerapeuticasArray = result.optJSONArray("classesTerapeuticas");
                if (classesTerapeuticasArray != null && classesTerapeuticasArray.length() > 0) {
                    String classeTerapeuticaString = classesTerapeuticasArray.getString(0);
                    classeTerapeutica.setText(classeTerapeuticaString);
                }

                JSONArray jsonApresentacoes = result.optJSONArray("apresentacoes");
                if (jsonApresentacoes != null && jsonApresentacoes.length() > 0) {
                    String destinacao = jsonApresentacoes.getJSONObject(0).getJSONArray("destinacao").getString(0);
                    String conservacaoString = jsonApresentacoes.getJSONObject(0).getJSONArray("conservacao").getString(0);
                    String viasAdm = jsonApresentacoes.getJSONObject(0).getJSONArray("viasAdministracao").getString(0);
                    String prescricaoString = jsonApresentacoes.getJSONObject(0).getJSONArray("restricaoPrescricao").getString(0);

                    destiny.setText(destinacao);
                    conservacao.setText(conservacaoString);
                    fabricante.setText(viasAdm);
                    prescricao.setText(prescricaoString);
                }

                JSONObject empresaObject = result.getJSONObject("empresa");
                String razaoSocialString = empresaObject.getString("razaoSocial");
                razaoSocial.setText(razaoSocialString);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
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

    @SuppressLint("SetTextI18n")
    public void popup_warning(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_warnings_layout, null);

        warningText = popupView.findViewById(R.id.warningText);
        warningText.setText("Serviço da anvisa fora do ar, por favor, tente mais tarde!");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(popupView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        buttonOk = popupView.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent it = new Intent(DetailsMedicineActivity.this, MenuActivity.class);
                startActivity(it);
            }
        });
    }
}