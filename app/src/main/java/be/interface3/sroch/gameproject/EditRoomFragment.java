package be.interface3.sroch.gameproject;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.interface3.sroch.gameproject.model.Room;

/**
 * Created by s.roch on 25/10/2016.
 */
public class EditRoomFragment extends Fragment {

    public Room room;

    private static EditRoomFragment ourInstance = new EditRoomFragment();

    public static EditRoomFragment getInstance() {
        return ourInstance;
    }

    private EditRoomFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_room, container, false);
        return v;
    }
}
