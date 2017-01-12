package be.interface3.sroch.gameproject;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import be.interface3.sroch.gameproject.db.CheckDatabase;
import be.interface3.sroch.gameproject.model.User;

/**
 * Created by s.roch on 3/10/2016.
 */
public class LogInFragment extends Fragment implements CheckDatabase.CheckDBCallback {
    EditText et_login_username;
    EditText et_login_password;
    Button but_login_login;
    User user;

    private static LogInFragment ourInstance = new LogInFragment();

    public static LogInFragment getInstance() {
        return ourInstance;
    }

    private LogInFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login, container, false);
        et_login_username = (EditText) v.findViewById(R.id.username);
        et_login_password = (EditText) v.findViewById(R.id.password);
        but_login_login = (Button) v.findViewById(R.id.login);
        but_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User();
                user.setUsername(et_login_username.getText().toString());
                user.setPassword(et_login_password.getText().toString());
                CheckDatabase checkDatabase = new CheckDatabase();
                checkDatabase.setCallback(LogInFragment.this);
                checkDatabase.execute("user");
            }
        });
        return v;
    }

    @Override
    public void parseJson(JSONArray jsonArray) throws JSONException {
        boolean found = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("username").equals(user.getUsername()) && jsonArray.getJSONObject(i).getString("password").equals(user.getPassword())) {
                found = true;
                user.setId(jsonArray.getJSONObject(i).getInt("id"));
                user.setEmail(jsonArray.getJSONObject(i).getString("email"));
                user.setPresentation(jsonArray.getJSONObject(i).getString("presentation"));
                MainActivity.connectedUser = user;
                Intent intent = new Intent(getContext(), RoomListActivity.class);
                startActivity(intent);
            }
        }
        if (!found) {
            Toast.makeText(getContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
