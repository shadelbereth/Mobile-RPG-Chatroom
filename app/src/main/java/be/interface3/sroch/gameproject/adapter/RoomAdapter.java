package be.interface3.sroch.gameproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.interface3.sroch.gameproject.DisplayRoomActivity;
import be.interface3.sroch.gameproject.MainActivity;
import be.interface3.sroch.gameproject.R;
import be.interface3.sroch.gameproject.Utils;
import be.interface3.sroch.gameproject.db.InsertDatabase;
import be.interface3.sroch.gameproject.db.UpdateDatabase;
import be.interface3.sroch.gameproject.model.Room;

/**
 * Created by s.roch on 10/10/2016.
 */
public class RoomAdapter extends BaseAdapter {

    TextView tv_roomlist_title;
    TextView tv_roomlist_context;
    TextView tv_roomlist_rule;
    Button but_roomlist_join;
    Button but_roomlist_request;
    List<Room> roomList;
    ArrayList<Long> subscribedRooms;
    Context context;

    public RoomAdapter(List<Room> roomList, ArrayList<Long> subscribedRooms, Context context) {
        this.roomList = roomList;
        this.subscribedRooms = subscribedRooms;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.roomList.size();
    }

    @Override
    public Object getItem(int position) {
        return roomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return roomList.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_room, parent, false);
        }

        tv_roomlist_title = (TextView) v.findViewById(R.id.title);
        tv_roomlist_context = (TextView) v.findViewById(R.id.context);
        tv_roomlist_rule = (TextView) v.findViewById(R.id.rules);
        but_roomlist_join = (Button) v.findViewById(R.id.join);
        but_roomlist_request = (Button) v.findViewById(R.id.request_access);

        final Room room = roomList.get(position);

        tv_roomlist_title.setText(room.getTitle());
        tv_roomlist_context.setText(room.getContext());
        tv_roomlist_rule.setText(room.getRule());

        if (subscribedRooms.contains(room.getId())) {
            but_roomlist_join.setVisibility(View.VISIBLE);
            but_roomlist_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DisplayRoomActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("room", room.getId());
                    context.startActivity(intent);
                }
            });
        } else {
            but_roomlist_request.setVisibility(View.VISIBLE);
            if (room.getVisibility().equals(Utils.PUBLIC_VISIBILITY)) {
                but_roomlist_request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UpdateDatabase updateDatabase = new UpdateDatabase();
                        updateDatabase.execute("reader", String.valueOf(getItemId(position)), String.valueOf(MainActivity.connectedUser.getId()));
                        Intent intent = new Intent(context, DisplayRoomActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("room", room.getId());
                        context.startActivity(intent);
                    }
                });
            }
        }

        return v;
    }
}
