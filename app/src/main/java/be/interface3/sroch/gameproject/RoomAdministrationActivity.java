package be.interface3.sroch.gameproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import be.interface3.sroch.gameproject.adapter.RequestingPlayerAdapter;
import be.interface3.sroch.gameproject.model.Room;

public class RoomAdministrationActivity extends AppCompatActivity {

    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_administration);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        room = new Room();
        room.setId(getIntent().getExtras().getLong(Utils.EXTRA_ID));
        RequestingPlayerFragment.getInstance().room = room;
        transaction.add(R.id.fragment, RequestingPlayerFragment.getInstance());
        transaction.commit();
    }

    public void doOnClick (View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.requesting_player:
                RequestingPlayerFragment.getInstance().room = room;
                transaction.replace(R.id.fragment, RequestingPlayerFragment.getInstance());
                break;
            case R.id.edit_room:
                EditRoomFragment.getInstance().room = room;
                transaction.replace(R.id.fragment, EditRoomFragment.getInstance());
                break;
        }
        transaction.commit();
    }
}
