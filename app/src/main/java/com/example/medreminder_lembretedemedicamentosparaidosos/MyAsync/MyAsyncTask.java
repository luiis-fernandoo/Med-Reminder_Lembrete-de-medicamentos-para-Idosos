package com.example.medreminder_lembretedemedicamentosparaidosos.MyAsync;

import android.os.AsyncTask;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MyAsyncTask extends AsyncTask<Void, Void, String> {
    private AsyncTaskListener listener;
    private String url, medicine;
    public MyAsyncTask(AsyncTaskListener listener, String medicine) {
        this.listener = listener;
        this.medicine = medicine;
    }

    @Override
    protected String doInBackground(Void... voids) {
        this.url = "https://consultas.anvisa.gov.br/api/consulta/bulario?filter%5BnomeProduto%5D="+this.medicine+"&page=1";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(this.url)
                .get()
                .addHeader("accept", "*/*")
                .addHeader("accept", "application/json, text/plain, */*")
                .addHeader("accept-language", "pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7")
                .addHeader("authorization", "Guest")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (IOException e) {
            return "Erro de rede: " + e.getMessage();
        }
    }
    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject json = new JSONObject(result);
            if (listener != null) {
                listener.onTaskComplete(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onTaskError(e.getMessage());
            }
        }
    }
    public interface AsyncTaskListener {
        void onTaskComplete(JSONObject result) throws JSONException;
        void onTaskError(String error);
    }
}
