package be.interface3.sroch.gameproject.db;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import be.interface3.sroch.gameproject.Utils;
import be.interface3.sroch.gameproject.model.Character;
import be.interface3.sroch.gameproject.model.DatabaseElement;
import be.interface3.sroch.gameproject.model.Message;
import be.interface3.sroch.gameproject.model.Room;
import be.interface3.sroch.gameproject.model.User;

/**
 * Created by s.roch on 30/09/2016.
 */
public class InsertDatabase extends AsyncTask<DatabaseElement, String, String> {
    @Override
    protected String doInBackground(DatabaseElement[] params) {
        String result = "";
        String url_path = Utils.INSERT_URL;

        if (params[0] instanceof User) {
            User newUser = (User) params[0];
            url_path += "table=user&username=" + newUser.getUsername() + "&password=" + newUser.getPassword() + "&email=" + newUser.getEmail() + "&presentation=" + newUser.getPresentation();
        } else if (params[0] instanceof Room) {
            Room newRoom = (Room) params[0];
            url_path += "table=room&title=" + newRoom.getTitle() + "&context=" + newRoom.getContext() + "&rule=" + newRoom.getRule() + "&visibility=" + newRoom.getVisibility() + "&play_permission_mode=" + newRoom.getPlayPermissionMode();
        } else if (params[0] instanceof Character) {
            Character newCharacter = (Character) params[0];
            User creator = (User) params[1];
            url_path += "table=playable_character&name=" + newCharacter.getName() + "&background=" + newCharacter.getBackground() + "&avatar_url=" + newCharacter.getAvatar_url() + "&user=" + creator.getId();
        } else if (params[0] instanceof Message) {
            Message newMessage = (Message) params[0];
            Room hostRoom = (Room) params[1];
            if (newMessage.getType().equals(Utils.IN_CHARACTER_MESSAGE)) {
                url_path += "table=message&author=" + newMessage.getAuthor().getId() + "&alias=" + newMessage.getAlias().getId() + "&room=" + hostRoom.getId() + "&post_date=" + newMessage.getPostDate() + "&type=" + newMessage.getType() + "&content=" + newMessage.getContent();
            } else {
                url_path += "table=message&author=" + newMessage.getAuthor().getId() + "&room=" + hostRoom.getId() + "&post_date=" + newMessage.getPostDate().getTime() + "&type=" + newMessage.getType() + "&content=" + newMessage.getContent();
            }
        }

        result = tryToInsert(url_path);
        if (params[0] instanceof Room && !result.equals("-1")) {
            Room newRoom = (Room) params[0];
            newRoom.setId(Long.parseLong(result));
            String result2;
            for (int i = 0; i < newRoom.getManagers().size(); i++) {
                String linked_path = Utils.INSERT_URL;
                linked_path+= "table=manager&room=" + newRoom.getId() + "&manager=" + newRoom.getManagers().get(i).getId();
                result2 = tryToInsert(linked_path);
                Log.e("add manager " + i, result2);
                Log.e("path " + i, linked_path);
            }
            for (int i = 0; i < newRoom.getReaders().size(); i++) {
                String linked_path = Utils.INSERT_URL;
                linked_path += "table=reader&room=" + newRoom.getId() + "&reader=" + newRoom.getReaders().get(i).getId();
                result2 = tryToInsert(linked_path);
                Log.e("add manager " + i, result2);
                Log.e("path " + i, linked_path);
            }
        }

        return result;
    }

    protected String tryToInsert (String url_path) {
        URL url = null;
        HttpURLConnection connection = null;
        String result = "";
        try {
            url = new URL(url_path);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            String urlString = uri.toASCIIString();
            Log.e("url", urlString);
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

    @Override
    protected void onPostExecute(String result) {
        if (!result.equals("-1")) {
            Log.e("insert status", "succeed");
            Log.e("id", result);
        } else {
            Log.e("insert status", "failed");
        }
    }
}
