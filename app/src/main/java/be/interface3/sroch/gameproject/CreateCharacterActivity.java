package be.interface3.sroch.gameproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import be.interface3.sroch.gameproject.db.InsertDatabase;
import be.interface3.sroch.gameproject.model.Character;

public class CreateCharacterActivity extends AppCompatActivity {

    EditText et_createcharacter_name;
    EditText et_createcharacter_background;
    EditText et_createcharacter_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_character);

        et_createcharacter_name = (EditText) findViewById(R.id.name);
        et_createcharacter_background = (EditText) findViewById(R.id.background);
        et_createcharacter_avatar = (EditText) findViewById(R.id.avatar);
    }

    public void doOnClick (View v) {
        switch (v.getId()) {
            case R.id.validate:
                Character newCharacter = new Character();
                newCharacter.setName(et_createcharacter_name.getText().toString());
                newCharacter.setBackground(et_createcharacter_background.getText().toString());
                newCharacter.setAvatar_url(et_createcharacter_avatar.getText().toString());
                if (!newCharacter.getName().equals("") && !newCharacter.getBackground().equals("")) {
                    if (newCharacter.getAvatar_url().equals("") || Patterns.WEB_URL.matcher(newCharacter.getAvatar_url()).matches()) {
                        InsertDatabase insertDatabase = new InsertDatabase();
                        insertDatabase.execute(newCharacter, MainActivity.connectedUser);
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid url", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Missing fields", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
