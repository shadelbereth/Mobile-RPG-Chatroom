package be.interface3.sroch.gameproject;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.interface3.sroch.gameproject.db.InsertDatabase;
import be.interface3.sroch.gameproject.model.User;

/**
 * Created by s.roch on 3/10/2016.
 */
public class SubscribeFragment extends Fragment {

    EditText et_subscribe_username;
    EditText et_subscribe_password;
    EditText et_subscribe_confirm_password;
    EditText et_subscribe_email;
    EditText et_subscribe_presentation;
    Button but_subscribe_subscribe;

    private static SubscribeFragment ourInstance = new SubscribeFragment();

    public static SubscribeFragment getInstance() {
        return ourInstance;
    }

    private SubscribeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.subscribe, container, false);
        et_subscribe_username = (EditText) v.findViewById(R.id.username);
        et_subscribe_password = (EditText) v.findViewById(R.id.password);
        et_subscribe_confirm_password = (EditText) v.findViewById(R.id.confirm_password);
        et_subscribe_email = (EditText) v.findViewById(R.id.email);
        et_subscribe_presentation = (EditText) v.findViewById(R.id.presentation);
        but_subscribe_subscribe = (Button) v.findViewById(R.id.subscribe);
        but_subscribe_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setUsername(et_subscribe_username.getText().toString());
                user.setPassword(et_subscribe_password.getText().toString());
                user.setEmail(et_subscribe_email.getText().toString());
                user.setPresentation(et_subscribe_presentation.getText().toString());
                if (!user.getUsername().equals("") && !user.getPassword().equals("") && !user.getEmail().equals("")) {
                    if (user.getPassword().equals(et_subscribe_confirm_password.getText().toString())) {
                        InsertDatabase insertDatabase = new InsertDatabase();
                        insertDatabase.execute(user);
                    } else {
                        Toast.makeText(getContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Form incomplete", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }
}
