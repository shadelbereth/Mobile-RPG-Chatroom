package be.interface3.sroch.gameproject.db;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import be.interface3.sroch.gameproject.Utils;

/**
 * Created by s.roch on 11/10/2016.
 */
public class UpdateDatabase extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        String url_path = "";
        String result = "";
        if (params[0].equals("reader") || params[0].equals("manager") || params[0].equals("player") || params[0].equals("requesting_reader") || params[0].equals("requesting_player")) {
            url_path = Utils.INSERT_URL + "table=" + params[0] + "&room=" + params[1] + "&" + params[0] + "=" + params[2];
        }
        Log.e("url", url_path);

        result = tryToInsert(url_path);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (!result.equals("-1") || result.equals("true")) {
            Log.e("update status", "succeed");
            Log.e("id", result);
        } else {
            Log.e("update status", "failed");
        }
    }

    protected String tryToInsert (String url_path) {
        URL url = null;
        HttpURLConnection connection = null;
        String result = "";
        try {
            url = new URL(url_path);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            String urlString = uri.toASCIIString();
            url = new URL(urlString);
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
            result = out.toString();
            result = result.replace("\"", "");
            reader.close();
            stream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }
}
