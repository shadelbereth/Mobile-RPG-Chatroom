package be.interface3.sroch.gameproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import be.interface3.sroch.gameproject.R;
import be.interface3.sroch.gameproject.RequestingPlayerFragment;
import be.interface3.sroch.gameproject.db.CheckDatabase;
import be.interface3.sroch.gameproject.db.DeleteDatabase;
import be.interface3.sroch.gameproject.db.InsertDatabase;
import be.interface3.sroch.gameproject.db.UpdateDatabase;
import be.interface3.sroch.gameproject.model.Character;
import be.interface3.sroch.gameproject.model.Room;
import be.interface3.sroch.gameproject.model.User;

/**
 * Created by s.roch on 18/10/2016.
 */
public class RequestingPlayerAdapter extends BaseAdapter implements CheckDatabase.CheckDBCallback {

    TextView tv_requestingplayer_name;
    TextView tv_requesting_player_background;
    TextView tv_requestingplayer_username;
    Button but_requestingplayer_validate;
    Button but_requestingplayer_reject;

    ArrayList<Character> characters;
    ArrayList<Long> characterIdToRemove;
    long roomId;
    Context context;

    public RequestingPlayerAdapter (Room room, Context context) {
        this.characters = room.getRequestingPlayers();
        this.roomId = room.getId();
        this.context = context;
        characterIdToRemove = new ArrayList<Long>();
    }

    @Override
    public int getCount() {
        return characters.size();
    }

    @Override
    public Object getItem(int position) {
        return characters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return characters.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_requesting_player, parent, false);

            tv_requestingplayer_name = (TextView) v.findViewById(R.id.name);
            tv_requesting_player_background = (TextView) v.findViewById(R.id.background);
            tv_requestingplayer_username = (TextView) v.findViewById(R.id.player_username);
            but_requestingplayer_validate = (Button) v.findViewById(R.id.validate);
            but_requestingplayer_reject = (Button) v.findViewById(R.id.reject);

            tv_requestingplayer_name.setText(characters.get(position).getName());
            tv_requesting_player_background.setText(characters.get(position).getBackground());
            tv_requestingplayer_username.setText(characters.get(position).getUser().getUsername());

            but_requestingplayer_validate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateDatabase updateDatabase = new UpdateDatabase();
                    updateDatabase.execute("player", String.valueOf(roomId), String.valueOf(characters.get(position).getId()));
                    characterIdToRemove.add(characters.get(position).getId());
                    CheckDatabase checkDatabase = new CheckDatabase();
                    checkDatabase.setCallback(RequestingPlayerAdapter.this);
                    checkDatabase.execute("requesting_player");
                }
            });

            but_requestingplayer_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    characterIdToRemove.add(characters.get(position).getId());
                    CheckDatabase checkDatabase = new CheckDatabase();
                    checkDatabase.setCallback(RequestingPlayerAdapter.this);
                    checkDatabase.execute("requesting_player");
                }
            });
        }
        return v;
    }

    @Override
    public void parseJson(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getInt("room") == roomId) {
                long found = -1;
                for (int j = 0; j < characterIdToRemove.size(); j++) {
                    if (jsonArray.getJSONObject(i).getInt("requesting_player") == characterIdToRemove.get(j)) {
                        found = characterIdToRemove.get(j);
                        DeleteDatabase deleteDatabase = new DeleteDatabase();
                        deleteDatabase.execute("requesting_player", String.valueOf(jsonArray.getJSONObject(i).getInt("id")));
                    }
                }
                if (found != -1) {
                    Character characterToRemove = null;
                    for (int j = 0; j < characters.size(); j++) {
                        if (characters.get(j).getId() == found) {
                            characterToRemove = characters.get(j);
                        }
                    }
                    if (characterToRemove != null) {
                        characters.remove(characterToRemove);
                        characterIdToRemove.remove(found);
                    }
                }
            }
        }
        RequestingPlayerFragment.getInstance().refreshList(characters);
    }
}
