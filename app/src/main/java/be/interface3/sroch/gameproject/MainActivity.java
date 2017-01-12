package be.interface3.sroch.gameproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONException;

import be.interface3.sroch.gameproject.model.User;

public class MainActivity extends AppCompatActivity {

    public static User connectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.form, LogInFragment.getInstance());
        transaction.commit();
    }

    public void doOnClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                showLogInFragment();
                break;
            case R.id.subscribe:
                showSubscribeFragment();
                break;
        }
    }

    private void showSubscribeFragment () {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.form, SubscribeFragment.getInstance());
        transaction.commit();
    }

    private void showLogInFragment () {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.form, LogInFragment.getInstance());
        transaction.commit();
    }
}
