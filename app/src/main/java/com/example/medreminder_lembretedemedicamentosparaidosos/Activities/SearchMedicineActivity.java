package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Adapter;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_medicine);

        editTextSearch = findViewById(R.id.editTextSearch);
        recycleViewMedicineItem = findViewById(R.id.recycleViewMedicineItem);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                performSearch(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
}