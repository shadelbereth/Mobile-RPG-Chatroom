package be.interface3.sroch.gameproject;

import android.os.AsyncTask;

import java.util.TimerTask;

import be.interface3.sroch.gameproject.db.CheckDatabase;
import be.interface3.sroch.gameproject.db.UpdateDatabase;
import be.interface3.sroch.gameproject.model.Room;

/**
 * Created by s.roch on 18/10/2016.
 */
public class UpdateManager extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        callback.updateRequestNbr();
        callback.lookForNewMessage();
    }

    Updater callback;

    public void setCallback(Updater callback) {
        this.callback = callback;
    }

    public interface Updater {
        void updateRequestNbr();
        void lookForNewMessage();
    }
}
