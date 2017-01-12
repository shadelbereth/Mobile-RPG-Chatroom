package be.interface3.sroch.gameproject;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import be.interface3.sroch.gameproject.adapter.RequestingPlayerAdapter;
import be.interface3.sroch.gameproject.db.CheckDatabase;
import be.interface3.sroch.gameproject.model.Character;
import be.interface3.sroch.gameproject.model.Room;
import be.interface3.sroch.gameproject.model.User;

/**
 * Created by s.roch on 18/10/2016.
 */
public class RequestingPlayerFragment extends Fragment implements CheckDatabase.CheckDBCallback {

    Room room;
    ListView lv_requestingplayer_list;

    private static RequestingPlayerFragment ourInstance = new RequestingPlayerFragment();

    public static RequestingPlayerFragment getInstance() {
        return ourInstance;
    }

    private RequestingPlayerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_requesting_player, container, false);

        lv_requestingplayer_list = (ListView) v.findViewById(R.id.requesting_player_list);

        CheckDatabase checkDatabase = new CheckDatabase();
        checkDatabase.setCallback(this);
        checkDatabase.execute("requesting_player");
        return v;
    }

    public void refreshList(ArrayList<Character> characters) {
        room.setRequestingPlayers(characters);
        RequestingPlayerAdapter adapter = new RequestingPlayerAdapter(room, getContext());
        lv_requestingplayer_list.setAdapter(adapter);
    }

    @Override
    public void parseJson(JSONArray jsonArray) throws JSONException {
        if (jsonArray.length() > 0) {
            if (jsonArray.getJSONObject(0).has("requesting_player")) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getJSONObject(i).getInt("room") == room.getId()) {
                        boolean found = false;
                        for (int j = 0; j < room.getRequestingPlayers().size(); i++) {
                            if (jsonArray.getJSONObject(i).getInt("requesting_player") == room.getRequestingPlayers().get(j).getId()) {
                                found = true;
                            }
                        }
                        if (!found) {
                            Character requestingPlayer = new Character();
                            requestingPlayer.setId(jsonArray.getJSONObject(i).getInt("requesting_player"));
                            requestingPlayer.setName("");
                            room.addRequestingPlayer(requestingPlayer);
                        }
                    }
                }
                CheckDatabase checkDatabase = new CheckDatabase();
                checkDatabase.setCallback(this);
                checkDatabase.execute("playable_character");
            } else if (jsonArray.getJSONObject(0).has("background")) {
                for (int i = 0; i < room.getRequestingPlayers().size(); i++) {
                    if (room.getRequestingPlayers().get(i).getName().equals("")) {
                        for (int j = 0; j < jsonArray.length(); j++) {
                            if (room.getRequestingPlayers().get(i).getId() == jsonArray.getJSONObject(j).getInt("id")) {
                                room.getRequestingPlayers().get(i).setName(jsonArray.getJSONObject(j).getString("name"));
                                room.getRequestingPlayers().get(i).setBackground(jsonArray.getJSONObject(j).getString("background"));
                                room.getRequestingPlayers().get(i).setUser(new User());
                                room.getRequestingPlayers().get(i).getUser().setId(jsonArray.getJSONObject(j).getInt("user"));
                                room.getRequestingPlayers().get(i).getUser().setUsername("");
                            }
                        }
                    }
                }
                CheckDatabase checkDatabase = new CheckDatabase();
                checkDatabase.setCallback(this);
                checkDatabase.execute("user");
            } else if (jsonArray.getJSONObject(0).has("username")) {
                for (int i = 0; i < room.getRequestingPlayers().size(); i++) {
                    if (room.getRequestingPlayers().get(i).getUser().getUsername().equals("")) {
                        for (int j = 0; j < jsonArray.length(); j++) {
                            if (room.getRequestingPlayers().get(i).getUser().getId() == jsonArray.getJSONObject(j).getInt("id")) {
                                room.getRequestingPlayers().get(i).getUser().setUsername(jsonArray.getJSONObject(j).getString("username"));
                            }
                        }
                    }
                }
                RequestingPlayerAdapter adapter = new RequestingPlayerAdapter(room, getContext());
                lv_requestingplayer_list.setAdapter(adapter);
            }
        }
    }
}
