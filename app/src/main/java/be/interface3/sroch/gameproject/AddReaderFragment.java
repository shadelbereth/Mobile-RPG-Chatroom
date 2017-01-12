package be.interface3.sroch.gameproject;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import be.interface3.sroch.gameproject.db.CheckDatabase;

/**
 * Created by s.roch on 4/10/2016.
 */
public class AddReaderFragment extends Fragment implements CheckDatabase.CheckDBCallback {

    ListView lv_addreader_list;
    EditText et_addreader_text;
    Button but_addreader_add;

    private static AddReaderFragment ourInstance = new AddReaderFragment();

    public static AddReaderFragment getInstance() {
        return ourInstance;
    }

    private AddReaderFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_reader, container, false);

        lv_addreader_list = (ListView) v.findViewById(R.id.list);
        et_addreader_text = (EditText) v.findViewById(R.id.reader_text);
        but_addreader_add = (Button) v.findViewById(R.id.add_reader);

        String[] list = new String[] {MainActivity.connectedUser.getUsername()};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.list_user, R.id.username, list);
        lv_addreader_list.setAdapter(adapter);

        but_addreader_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckDatabase checkDatabase = new CheckDatabase();
                checkDatabase.setCallback(AddReaderFragment.this);
                checkDatabase.execute("user");
            }
        });

        return v;
    }

    protected void addManager (String username) {
        boolean alreadyExists = false;
        String[] list = new String[lv_addreader_list.getAdapter().getCount() + 1];
        for (int i = 0; i < lv_addreader_list.getAdapter().getCount(); i++) {
            if (lv_addreader_list.getAdapter().getItem(i).toString().equals(username)) {
                alreadyExists = true;
            }
            list[i] = lv_addreader_list.getAdapter().getItem(i).toString();
        }
        if (!alreadyExists) {
            list[lv_addreader_list.getAdapter().getCount()] = username;
            et_addreader_text.setText("");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.list_user, R.id.username, list);
            lv_addreader_list.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), "User is already manager", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void parseJson(JSONArray jsonArray) throws JSONException {
        boolean found = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("username").equals(et_addreader_text.getText().toString())) {
                found = true;
                addManager(jsonArray.getJSONObject(i).getString("username"));
            }
        }
        if (!found) {
            Toast.makeText(getContext(), "Inexistent username", Toast.LENGTH_SHORT).show();
        }
    }
}
