package be.interface3.sroch.gameproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import be.interface3.sroch.gameproject.db.CheckDatabase;
import be.interface3.sroch.gameproject.db.InsertDatabase;
import be.interface3.sroch.gameproject.model.Room;
import be.interface3.sroch.gameproject.model.User;

public class CreateRoomActivity extends AppCompatActivity implements CheckDatabase.CheckDBCallback {

    EditText et_createroom_title;
    EditText et_createroom_context;
    EditText et_createroom_rule;
    RadioGroup rg_createroom_visibility;
    RadioGroup rg_createroom_play_permission;
    Room newRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        et_createroom_title = (EditText) findViewById(R.id.title);
        et_createroom_context = (EditText) findViewById(R.id.context);
        et_createroom_rule = (EditText) findViewById(R.id.rules);
        rg_createroom_visibility = (RadioGroup) findViewById(R.id.visibility);
        rg_createroom_play_permission = (RadioGroup) findViewById(R.id.play_permission);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.manager, AddManagerFragment.getInstance());
        transaction.add(R.id.reader, AddReaderFragment.getInstance());
        transaction.commit();
    }

    public void doOnClick(View view) {
        switch (view.getId()) {
            case R.id.validate:
                newRoom = new Room();
                newRoom.setTitle(et_createroom_title.getText().toString());
                newRoom.setContext(et_createroom_context.getText().toString());
                newRoom.setRule(et_createroom_rule.getText().toString());
                if (!newRoom.getTitle().equals("") && !newRoom.getContext().equals("")) {
                    switch (rg_createroom_visibility.getCheckedRadioButtonId()) {
                        case R.id.visibility_public:
                            newRoom.setVisibility(Utils.PUBLIC_VISIBILITY);
                            break;
                        case R.id.visibility_protected:
                            newRoom.setVisibility(Utils.PROTECTED_VISIBILITY);
                            break;
                        case R.id.visibility_private:
                            newRoom.setVisibility(Utils.PRIVATE_VISIBILITY);
                            break;
                    }
                    switch (rg_createroom_play_permission.getCheckedRadioButtonId()) {
                        case R.id.play_public:
                            newRoom.setPlayPermissionMode(Utils.AUTO_PLAY_PERMISSION);
                            break;
                        case R.id.play_private:
                            newRoom.setPlayPermissionMode(Utils.MANUAL_PLAY_PERMISSION);
                            break;
                    }
                    for (int i = 0; i < AddManagerFragment.getInstance().lv_addmanager_list.getAdapter().getCount(); i++) {
                        User manager = new User();
                        manager.setUsername(AddManagerFragment.getInstance().lv_addmanager_list.getAdapter().getItem(i).toString());
                        newRoom.addManager(manager);
                    }
                    for (int i = 0; i < AddReaderFragment.getInstance().lv_addreader_list.getAdapter().getCount(); i++) {
                        User reader = new User();
                        reader.setUsername(AddReaderFragment.getInstance().lv_addreader_list.getAdapter().getItem(i).toString());
                        newRoom.addReader(reader);
                    }
                    CheckDatabase checkDatabase = new CheckDatabase();
                    checkDatabase.setCallback(this);
                    checkDatabase.execute("user");
                } else {
                    Toast.makeText(getApplicationContext(), "Form incomplete", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void parseJson(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < newRoom.getReaders().size(); i++) {
            for (int j = 0; j < jsonArray.length(); j++) {
                if (jsonArray.getJSONObject(j).getString("username").equals(newRoom.getReaders().get(i).getUsername())) {
                    newRoom.getReaders().get(i).setId(jsonArray.getJSONObject(j).getInt("id"));
                }
                if (i < newRoom.getManagers().size() && jsonArray.getJSONObject(j).getString("username").equals(newRoom.getManagers().get(i).getUsername())) {
                    newRoom.getManagers().get(i).setId(jsonArray.getJSONObject(j).getInt("id"));
                }
            }
        }
        InsertDatabase insertDatabase = new InsertDatabase();
        insertDatabase.execute(newRoom);
        Intent intent = new Intent(getApplicationContext(), RoomListActivity.class);
        startActivity(intent);
    }
}
