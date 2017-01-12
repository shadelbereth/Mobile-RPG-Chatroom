package be.interface3.sroch.gameproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import be.interface3.sroch.gameproject.R;
import be.interface3.sroch.gameproject.model.Character;

/**
 * Created by s.roch on 17/10/2016.
 */
public class CharacterAdapter extends BaseAdapter {

    TextView tv_characteradapter_name;
    TextView tv_characteradapter_background;

    List<Character> characters;
    Context context;

    public CharacterAdapter (List<Character> characters, Context context) {
        this.characters = characters;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_character, parent, false);
        }

        tv_characteradapter_name = (TextView) v.findViewById(R.id.name);
        tv_characteradapter_background = (TextView) v.findViewById(R.id.background);

        tv_characteradapter_name.setText(characters.get(position).getName());
        tv_characteradapter_background.setText(characters.get(position).getBackground());

        return v;
    }
}
