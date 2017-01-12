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
public class AddManagerFragment extends Fragment implements CheckDatabase.CheckDBCallback {

    ListView lv_addmanager_list;
    EditText et_addmanager_username;
    Button but_addmanager_add;

    private static AddManagerFragment ourInstance = new AddManagerFragment();

    public static AddManagerFragment getInstance() {
        return ourInstance;
    }

    private AddManagerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_manager, container, false);

        lv_addmanager_list = (ListView) v.findViewById(R.id.manager_list);
        et_addmanager_username = (EditText) v.findViewById(R.id.add_manager_text);
        but_addmanager_add = (Button) v.findViewById(R.id.add_manager);

        String[] listData = new String[] {MainActivity.connectedUser.getUsername()};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.list_user, R.id.username, listData);
        lv_addmanager_list.setAdapter(adapter);

        but_addmanager_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckDatabase checkDatabase = new CheckDatabase();
                checkDatabase.setCallback(AddManagerFragment.this);
                checkDatabase.execute("user");
            }
        });

        return v;
    }

    protected void addManager (String username) {
        boolean alreadyExists = false;
        String[] list = new String[lv_addmanager_list.getAdapter().getCount() + 1];
        for (int i = 0; i < lv_addmanager_list.getAdapter().getCount(); i++) {
            if (lv_addmanager_list.getAdapter().getItem(i).toString().equals(username)) {
                alreadyExists = true;
            }
            list[i] = lv_addmanager_list.getAdapter().getItem(i).toString();
        }
        if (!alreadyExists) {
            list[lv_addmanager_list.getAdapter().getCount()] = username;
            et_addmanager_username.setText("");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.list_user, R.id.username, list);
            lv_addmanager_list.setAdapter(adapter);
            boolean found = false;
            String[] readerList = new String[AddReaderFragment.getInstance().lv_addreader_list.getAdapter().getCount() + 1];
            for (int i = 0; i < AddReaderFragment.getInstance().lv_addreader_list.getAdapter().getCount(); i++) {
                if (AddReaderFragment.getInstance().lv_addreader_list.getAdapter().getItem(i).toString().equals(username)) {
                    found = true;
                }
                readerList[i] = AddReaderFragment.getInstance().lv_addreader_list.getAdapter().getItem(i).toString();
            }
            if (! found) {
                readerList[AddReaderFragment.getInstance().lv_addreader_list.getAdapter().getCount()] = username;
                ArrayAdapter<String> readerAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_user, R.id.username, readerList);
                AddReaderFragment.getInstance().lv_addreader_list.setAdapter(readerAdapter);
            }
        } else {
            Toast.makeText(getContext(), "User is already manager", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void parseJson(JSONArray jsonArray) throws JSONException {
        boolean found = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("username").equals(et_addmanager_username.getText().toString())) {
                found = true;
                addManager(jsonArray.getJSONObject(i).getString("username"));
            }
        }
        if (!found) {
            Toast.makeText(getContext(), "Inexistent username", Toast.LENGTH_SHORT).show();
        }
    }
}
