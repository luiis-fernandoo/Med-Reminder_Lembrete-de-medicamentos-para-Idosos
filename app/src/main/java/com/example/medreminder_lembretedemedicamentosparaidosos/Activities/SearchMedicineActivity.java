package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.Adapter.SearchAdapter;
import com.example.medreminder_lembretedemedicamentosparaidosos.MyAsync.MyAsyncTask;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchMedicineActivity extends AppCompatActivity implements MyAsyncTask.AsyncTaskListener{

    EditText editTextSearch;
    RecyclerView recycleViewMedicineItem;
    TextView searchText, warningText;
    private LinearLayout buttonOk;
    private Dialog progressDialog;
    private Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_medicine);

        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);

        recycleViewMedicineItem = findViewById(R.id.recycleViewMedicineItem);

        searchText = findViewById(R.id.searchText);
        searchText.setText(R.string.Search_for_medicines);

        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_loading);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editTextSearch.getText().toString();
                performSearch(query);
                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            popup_warning(view);
                        }
                    }
                }, 30000);
            }
        });
    }
    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {

        if (result != null) {
            if(result.has("content")){
                try {
                    JSONArray results = result.getJSONArray("content");
                    if (results.length() > 0) {
                        progressDialog.dismiss();
                        SearchAdapter searchAdapter = new SearchAdapter(this, results);
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        recycleViewMedicineItem.removeAllViews();
                        recycleViewMedicineItem.setLayoutManager(layoutManager);
                        recycleViewMedicineItem.setAdapter(searchAdapter);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else{
                Toast.makeText(this, "Carregando...", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Não há medicamentos com esse nome, tente outro!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onTaskError(String error) {
    }
    private void performSearch(String query){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!TextUtils.isEmpty(query)){
                    String url = "https://consultas.anvisa.gov.br/api/consulta/bulario?filter%5BnomeProduto%5D="+query+"&page=1";
                    MyAsyncTask myAsyncTaskSearch = new MyAsyncTask(SearchMedicineActivity.this, url);
                    myAsyncTaskSearch.execute();
                }
            }
        }).start();
    }

    @SuppressLint("SetTextI18n")
    public void popup_warning(View view){
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
            }
        });
    }
}