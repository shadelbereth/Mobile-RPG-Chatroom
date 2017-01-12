package be.interface3.sroch.gameproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import be.interface3.sroch.gameproject.adapter.MessageAdapter;
import be.interface3.sroch.gameproject.db.CheckDatabase;
import be.interface3.sroch.gameproject.db.InsertDatabase;
import be.interface3.sroch.gameproject.db.UpdateDatabase;
import be.interface3.sroch.gameproject.model.Character;
import be.interface3.sroch.gameproject.model.Message;
import be.interface3.sroch.gameproject.model.Room;
import be.interface3.sroch.gameproject.model.User;

public class DisplayRoomActivity extends AppCompatActivity implements CheckDatabase.CheckDBCallback, UpdateManager.Updater {
    TextView tv_displayroom_title;
    EditText et_displayroom_write_message;
    ListView lv_displayroom_list;
    Button but_displayroom_request;

    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_room);

        tv_displayroom_title = (TextView) findViewById(R.id.title);
        et_displayroom_write_message = (EditText) findViewById(R.id.write_message);
        lv_displayroom_list = (ListView) findViewById(R.id.message_list);
        but_displayroom_request = (Button) findViewById(R.id.treat_request);
        but_displayroom_request.setText("Administrate");

        CheckDatabase checkDatabase = new CheckDatabase();
        checkDatabase.setCallback(this);
        checkDatabase.execute("room");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateManager updateManager = new UpdateManager();
                updateManager.setCallback(DisplayRoomActivity.this);
                updateManager.execute();
            }
        }, 2000, 2000);
    }

    public void doOnClick (View v) {
        switch (v.getId()) {
            case R.id.send:
                if (!et_displayroom_write_message.getText().toString().equals("")) {
                    Message message = new Message();
                    message.setAuthor(MainActivity.connectedUser);
                    message.setContent(et_displayroom_write_message.getText().toString());
                    message.setType(Utils.OUT_OF_CHARACTER_MESSAGE);
                    message.setPostDate(new Date(System.currentTimeMillis()));
                    InsertDatabase insertDatabase = new InsertDatabase();
                    insertDatabase.execute(message, room);
                    et_displayroom_write_message.setText("");
                }
                break;
            case R.id.add_character:
                startNewActivityForResult(AddCharacterActivity.class, Utils.REQUEST_CODE_ADD_CHARACTER);
                break;
            case R.id.treat_request:
                startNewActivityForResult(RoomAdministrationActivity.class, Utils.REQUEST_CODE_ROOM_ADMIN, room.getId());
                break;
        }
    }

    protected void startNewActivityForResult (Class activity, int code) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivityForResult(intent, code);
    }

    protected void startNewActivityForResult (Class activity, int code, long id) {
        Intent intent = new Intent(getApplicationContext(), activity);
        intent.putExtra(Utils.EXTRA_ID, id);
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Utils.REQUEST_CODE_ADD_CHARACTER:
                if (resultCode == RESULT_OK) {
                    boolean found = false;
                    int i = 0;
                    while ((i < room.getPlayers().size() || i < room.getRequestingPlayers().size()) && !found) {
                        if (i < room.getPlayers().size()) {
                            if (room.getPlayers().get(i).getId() == data.getExtras().getLong(Utils.EXTRA_ID)) {
                                found = true;
                            }
                        }
                        if (i < room.getRequestingPlayers().size()) {
                            if (room.getRequestingPlayers().get(i).getId() == data.getExtras().getLong(Utils.EXTRA_ID)) {
                                found = true;
                            }
                        }
                        i++;
                    }
                    if (!found) {
                        Character newPlayer = new Character();
                        newPlayer.setId(data.getExtras().getLong(Utils.EXTRA_ID));
                        if (room.getPlayPermissionMode().equals(Utils.AUTO_PLAY_PERMISSION)) {
                            room.addPlayer(newPlayer);
                            UpdateDatabase updateDatabase = new UpdateDatabase();
                            updateDatabase.execute("player", String.valueOf(room.getId()), String.valueOf(newPlayer.getId()));
                        } else {
                            room.addRequestingPlayer(newPlayer);
                            UpdateDatabase updateDatabase = new UpdateDatabase();
                            updateDatabase.execute("requesting_player", String.valueOf(room.getId()), String.valueOf(newPlayer.getId()));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Character already in the room or requesting", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void parseJson(JSONArray jsonArray) throws JSONException {
        if (jsonArray.length() > 0) {
            if (jsonArray.getJSONObject(0).has("play_permission_mode")) {
                room = new Room();
                room.setId(getIntent().getExtras().getLong("room", -1));
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getJSONObject(i).getInt("id") == room.getId()) {
                        room.setTitle(jsonArray.getJSONObject(i).getString("title"));
                        room.setContext(jsonArray.getJSONObject(i).getString("context"));
                        room.setRule(jsonArray.getJSONObject(i).getString("rule"));
                        room.setVisibility(jsonArray.getJSONObject(i).getString("visibility"));
                        room.setPlayPermissionMode(jsonArray.getJSONObject(i).getString("play_permission_mode"));
                    }
                }
                tv_displayroom_title.setText(room.getTitle());
                CheckDatabase checkDatabase = new CheckDatabase();
                checkDatabase.setCallback(this);
                checkDatabase.execute("message");
                CheckDatabase checkDatabase2 = new CheckDatabase();
                checkDatabase2.setCallback(this);
                checkDatabase2.execute("player");
                CheckDatabase checkDatabase3 = new CheckDatabase();
                checkDatabase3.setCallback(this);
                checkDatabase3.execute("requesting_player");
            } else if (jsonArray.getJSONObject(0).has("post_date")) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getJSONObject(i).getInt("room") == room.getId()) {
                        boolean found = false;
                        for (int j = 0; j < room.getMessages().size(); j++) {
                            if (jsonArray.getJSONObject(i).getInt("id") == room.getMessages().get(j).getId()) {
                                found = true;
                            }
                        }
                        if (!found) {
                            Message newMessage = new Message();
                            newMessage.setId(jsonArray.getJSONObject(i).getInt("id"));
                            newMessage.setAuthor(new User());
                            newMessage.getAuthor().setId(jsonArray.getJSONObject(i).getInt("author"));
                            newMessage.getAuthor().setUsername("");
                            newMessage.setPostDate(new Date(jsonArray.getJSONObject(i).getInt("post_date")));
                            newMessage.setType(jsonArray.getJSONObject(i).getString("type"));
                            newMessage.setContent(jsonArray.getJSONObject(i).getString("content"));
                            if (newMessage.getType().equals(Utils.IN_CHARACTER_MESSAGE)) {
                                newMessage.setAlias(new Character());
                                newMessage.getAlias().setId(jsonArray.getJSONObject(i).getInt("alias"));
                                newMessage.getAlias().setName("");
                            }
                            room.addMessage(newMessage);
                        }
                    }
                }
                if (room.hasMessageNbrChange()) {
                    CheckDatabase checkDatabase = new CheckDatabase();
                    checkDatabase.setCallback(this);
                    checkDatabase.execute("user");
                }
            } else if (jsonArray.getJSONObject(0).has("username")) {
                for (int i = 0; i < room.getMessages().size(); i++) {
                    if (room.getMessages().get(i).getAuthor().getUsername().equals("")) {
                        for (int j = 0; j < jsonArray.length(); j++) {
                            if (room.getMessages().get(i).getAuthor().getId() == jsonArray.getJSONObject(j).getInt("id")) {
                                room.getMessages().get(i).getAuthor().setUsername(jsonArray.getJSONObject(j).getString("username"));
                                room.getMessages().get(i).getAuthor().setPresentation(jsonArray.getJSONObject(j).getString("presentation"));
                            }
                        }
                    }
                }
                CheckDatabase checkDatabase = new CheckDatabase();
                checkDatabase.setCallback(this);
                checkDatabase.execute("playable_character");
            } else if (jsonArray.getJSONObject(0).has("background")) {
                for (int i = 0; i < room.getMessages().size(); i++) {
                    if (room.getMessages().get(i).getType().equals(Utils.IN_CHARACTER_MESSAGE) && room.getMessages().get(i).getAlias().getName().equals("")) {
                        for (int j = 0; j < jsonArray.length(); j++) {
                            if (room.getMessages().get(i).getAlias().getId() == jsonArray.getJSONObject(j).getInt("id")) {
                                room.getMessages().get(i).getAlias().setName(jsonArray.getJSONObject(j).getString("name"));
                                room.getMessages().get(i).getAlias().setBackground(jsonArray.getJSONObject(j).getString("background"));
                            }
                        }
                    }
                }
                MessageAdapter adapter = new MessageAdapter(room.getMessages(), getApplicationContext());
                lv_displayroom_list.setAdapter(adapter);
            } else if (jsonArray.getJSONObject(0).has("player")) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getJSONObject(i).getInt("room") == room.getId()) {
                        boolean found = false;
                        for (int j = 0; j < room.getPlayers().size(); j++) {
                            if (jsonArray.getJSONObject(i).getInt("player") == room.getPlayers().get(j).getId()) {
                                found = true;
                            }
                        }
                        if (!found) {
                            Character character = new Character();
                            character.setId(jsonArray.getJSONObject(i).getInt("player"));
                            room.addPlayer(character);
                        }
                    }
                }
            } else if (jsonArray.getJSONObject(0).has("requesting_player")) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getJSONObject(i).getInt("room") == room.getId()) {
                        boolean found = false;
                        for (int j = 0; j < room.getRequestingPlayers().size(); j++) {
                            if (jsonArray.getJSONObject(i).getInt("requesting_player") == room.getRequestingPlayers().get(j).getId()) {
                                found = true;
                            }
                        }
                        if (!found) {
                            Character character = new Character();
                            character.setId(jsonArray.getJSONObject(i).getInt("requesting_player"));
                            room.addRequestingPlayer(character);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateRequestNbr() {
        if (room.hasRequestNbrChange()) {
            if (room.getRequestNbr() == 0) {
                but_displayroom_request.setText("Administrate");
            } else if (room.getRequestNbr() == 1) {
                but_displayroom_request.setText("Administrate (" + room.getRequestNbr() + " request pending)");
            } else {
                but_displayroom_request.setText("Administrate (" + room.getRequestNbr() + " requests pending)");
            }
        }
    }

    @Override
    public void lookForNewMessage() {
        CheckDatabase checkDatabase = new CheckDatabase();
        checkDatabase.setCallback(this);
        checkDatabase.execute("message");
    }
}
