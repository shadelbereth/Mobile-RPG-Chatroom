package be.interface3.sroch.gameproject.db;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by s.roch on 30/09/2016.
 */
public class CheckDatabase extends AsyncTask<String, String, JSONArray> {
    @Override
    protected JSONArray doInBackground(String[] params) {
        URL url = null;
        HttpURLConnection connection = null;

        JSONArray jsonObject = null;
        String json = "";

        try {
            url = new URL("http://10.0.2.2/android/select.php?table=" + params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            json = out.toString();
            reader.close();
            stream.close();
            jsonObject = new JSONArray(json);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        try{
            callback.parseJson(jsonArray);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    CheckDBCallback callback;

    public void setCallback(CheckDBCallback callback) {
        this.callback = callback;
    }

    public interface CheckDBCallback {
        void parseJson (JSONArray jsonArray) throws JSONException;
    }
}
