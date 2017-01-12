package be.interface3.sroch.gameproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import be.interface3.sroch.gameproject.adapter.RoomAdapter;
import be.interface3.sroch.gameproject.db.CheckDatabase;
import be.interface3.sroch.gameproject.model.Room;

public class RoomListActivity extends AppCompatActivity implements CheckDatabase.CheckDBCallback {

    ListView lv_roomlist_list;
    ArrayList<Long> subscribedRooms;
    ArrayList<Room> visibleRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        lv_roomlist_list = (ListView) findViewById(R.id.room_list);

        subscribedRooms = new ArrayList<>();

        CheckDatabase checkDatabase = new CheckDatabase();
        checkDatabase.setCallback(this);
        checkDatabase.execute("reader");
    }

    public void doOnClick (View view) {
        switch (view.getId()) {
            case R.id.create:
                changeActivity(CreateRoomActivity.class);
                break;
        }
    }

    public void changeActivity (Class activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }

    @Override
    public void parseJson(JSONArray jsonArray) throws JSONException {
        if (jsonArray.getJSONObject(0).has("reader")) {
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).getInt("reader") == MainActivity.connectedUser.getId()) {
                    subscribedRooms.add((long) jsonArray.getJSONObject(i).getInt("room"));
                }
            }
            CheckDatabase checkDatabase = new CheckDatabase();
            checkDatabase.setCallback(this);
            checkDatabase.execute("room");
        } else if (jsonArray.getJSONObject(0).has("play_permission_mode")) {
            visibleRooms = new ArrayList<Room>();
            for (int i = 0; i < jsonArray.length(); i++) {
                if (!jsonArray.getJSONObject(i).getString("play_permission_mode").equals(Utils.PRIVATE_VISIBILITY) || subscribedRooms.contains(jsonArray.getJSONObject(i).getInt("id"))) {
                    Room room = new Room();
                    room.setId(jsonArray.getJSONObject(i).getInt("id"));
                    room.setTitle(jsonArray.getJSONObject(i).getString("title"));
                    room.setContext(jsonArray.getJSONObject(i).getString("context"));
                    room.setRule(jsonArray.getJSONObject(i).getString("rule"));
                    room.setVisibility(jsonArray.getJSONObject(i).getString("visibility"));
                    room.setPlayPermissionMode(jsonArray.getJSONObject(i).getString("play_permission_mode"));
                    visibleRooms.add(room);
                }
            }
            RoomAdapter adapter = new RoomAdapter(visibleRooms, subscribedRooms, getApplicationContext());
            lv_roomlist_list.setAdapter(adapter);
        }
    }
}
