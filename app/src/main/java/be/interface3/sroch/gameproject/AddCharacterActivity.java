package be.interface3.sroch.gameproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import be.interface3.sroch.gameproject.adapter.CharacterAdapter;
import be.interface3.sroch.gameproject.db.CheckDatabase;
import be.interface3.sroch.gameproject.model.Character;

public class AddCharacterActivity extends AppCompatActivity implements CheckDatabase.CheckDBCallback {

    ListView lv_addcharacter_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_character);

        lv_addcharacter_list = (ListView) findViewById(R.id.character_list);
        lv_addcharacter_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
                intent.putExtra(Utils.EXTRA_QUESTION, "Do you want to request play permission for the following character?");
                intent.putExtra(Utils.EXTRA_ID, id);
                Character character = (Character) parent.getAdapter().getItem(position);
                intent.putExtra(Utils.EXTRA_NAME, character.getName());
                intent.putExtra(Utils.EXTRA_DESCR, character.getBackground());
                startActivityForResult(intent, Utils.REQUEST_CODE_CONFIRM);
            }
        });

        CheckDatabase checkDatabase = new CheckDatabase();
        checkDatabase.setCallback(this);
        checkDatabase.execute("playable_character");
    }

    public void doOnClick (View v) {
        switch (v.getId()) {
            case R.id.create_character:
                Intent intent = new Intent(getApplicationContext(), CreateCharacterActivity.class);
                startActivityForResult(intent, Utils.REQUEST_CODE_CREATE_CHARACTER);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Utils.REQUEST_CODE_CREATE_CHARACTER:
                if (resultCode == RESULT_OK) {
                    CheckDatabase checkDatabase = new CheckDatabase();
                    checkDatabase.setCallback(this);
                    checkDatabase.execute("playable_character");
                }
                break;
            case Utils.REQUEST_CODE_CONFIRM:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent();
                    intent.putExtra(Utils.EXTRA_ID, data.getExtras().getLong(Utils.EXTRA_ID));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void parseJson(JSONArray jsonArray) throws JSONException {
        ArrayList<Character> characters = new ArrayList<Character>();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getInt("user") == MainActivity.connectedUser.getId()) {
                boolean found = false;
                for (int j = 0; j < characters.size(); j++) {
                    if (jsonArray.getJSONObject(i).getInt("id") == characters.get(j).getId()) {
                        found = true;
                    }
                }
                if (!found) {
                    Character character = new Character();
                    character.setId(jsonArray.getJSONObject(i).getInt("id"));
                    character.setName(jsonArray.getJSONObject(i).getString("name"));
                    character.setBackground(jsonArray.getJSONObject(i).getString("background"));
                    character.setAvatar_url(jsonArray.getJSONObject(i).getString("avatar_url"));
                    characters.add(character);
                }
            }
        }
        CharacterAdapter adapter = new CharacterAdapter(characters, getApplicationContext());
        lv_addcharacter_list.setAdapter(adapter);
    }
}
