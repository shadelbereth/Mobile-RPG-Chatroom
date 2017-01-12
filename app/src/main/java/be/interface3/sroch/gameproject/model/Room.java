package be.interface3.sroch.gameproject.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by s.roch on 30/09/2016.
 */
public class Room extends DatabaseElement {
    long id;
    String title;
    String context;
    String rule;
    String visibility;
    String playPermissionMode;
    ArrayList<User> managers;
    ArrayList<Character> players;
    ArrayList<User> readers;
    ArrayList<Character> requestingPlayers;
    ArrayList<User> requestingReaders;
    ArrayList<Message> messages;

    long lastMessageNbr;
    long lastRequestNbr;

    public Room () {
        managers = new ArrayList<User>();
        players = new ArrayList<Character>();
        readers = new ArrayList<User>();
        requestingPlayers = new ArrayList<Character>();
        requestingReaders = new ArrayList<User>();
        messages = new ArrayList<Message>();
        lastMessageNbr = 0;
        lastRequestNbr = 0;
    }

    public Room(User creator) {
        this();

        managers.add(creator);
        readers.add(creator);
    }

    public void addManager (User manager) {
        managers.add(manager);
    }

    public void removeManager (User manager) {
        if (managers.size() > 1) {
            managers.remove(manager);
        }
    }

    public void addPlayer (Character player) {
        players.add(player);
    }

    public void removePlayer (Character player) {
        players.remove(player);
    }

    public void addReader (User reader) {
        readers.add(reader);
    }

    public void removeReader (User reader) {
        if (!managers.contains(reader)) {
            readers.remove(reader);
        }
    }

    public void addRequestingPlayer (Character player) {
        requestingPlayers.add(player);
    }

    public void removeRequestingPlayer (Character player) {
        requestingPlayers.remove(player);
    }

    public void addRequestingReader (User reader) {
        requestingReaders.add(reader);
    }

    public void removeRequestingReader (User reader) {
        requestingReaders.remove(reader);
    }

    public void addMessage (Message message) {
        messages.add(message);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPlayPermissionMode() {
        return playPermissionMode;
    }

    public void setPlayPermissionMode(String playPermissionMode) {
        this.playPermissionMode = playPermissionMode;
    }

    public ArrayList<User> getManagers() {
        return managers;
    }

    public ArrayList<Character> getPlayers() {
        return players;
    }

    public void setRequestingPlayers(ArrayList<Character> requestingPlayers) {
        this.requestingPlayers = requestingPlayers;
    }

    public ArrayList<User> getReaders() {
        return readers;
    }

    public ArrayList<Character> getRequestingPlayers() {
        return requestingPlayers;
    }

    public ArrayList<User> getRequestingReaders() {
        return requestingReaders;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public long getRequestNbr() {
        return getRequestingPlayers().size() + getRequestingReaders().size();
    }

    public boolean hasMessageNbrChange () {
        if (getMessages().size() != lastMessageNbr) {
            lastMessageNbr = getMessages().size();
            return true;
        }
        return false;
    }

    public boolean hasRequestNbrChange () {
        if (getRequestingPlayers().size() + getRequestingReaders().size() != lastRequestNbr) {
            lastRequestNbr = getRequestingPlayers().size() + getRequestingReaders().size();
            return true;
        }
        return false;
    }
}
