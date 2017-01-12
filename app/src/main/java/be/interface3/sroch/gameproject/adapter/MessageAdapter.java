package be.interface3.sroch.gameproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import be.interface3.sroch.gameproject.R;
import be.interface3.sroch.gameproject.Utils;
import be.interface3.sroch.gameproject.model.Message;

/**
 * Created by s.roch on 11/10/2016.
 */
public class MessageAdapter extends BaseAdapter {

    TextView tv_messageadapter_author;
    TextView tv_messageadapter_content;

    List<Message> messages;
    Context context;

    public MessageAdapter (List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return messages.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_message, parent, false);
        }

        tv_messageadapter_author = (TextView) v.findViewById(R.id.author);
        tv_messageadapter_content = (TextView) v.findViewById(R.id.content);

        if (messages.get(position).getType().equals(Utils.IN_CHARACTER_MESSAGE)) {
            tv_messageadapter_author.setText(messages.get(position).getAlias().getName());
        } else {
            tv_messageadapter_author.setText(messages.get(position).getAuthor().getUsername());
            tv_messageadapter_content.setTextColor(R.color.lightgrey);
        }
        tv_messageadapter_content.setText(messages.get(position).getContent());

        return v;
    }
}
