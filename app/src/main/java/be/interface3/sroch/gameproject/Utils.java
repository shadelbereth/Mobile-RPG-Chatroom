package be.interface3.sroch.gameproject;

/**
 * Created by s.roch on 10/10/2016.
 */
public abstract class Utils {
    public static final String INSERT_URL = "http://10.0.2.2/android/insert.php?";
    public static final String DELETE_URL = "http://10.0.2.2/android/delete.php?";

    public static final String PUBLIC_VISIBILITY = "public";
    public static final String PROTECTED_VISIBILITY = "protected";
    public static final String PRIVATE_VISIBILITY = "private";

    public static final String AUTO_PLAY_PERMISSION = "automatic validation";
    public static final String MANUAL_PLAY_PERMISSION = "manual validation";

    public static final String OUT_OF_CHARACTER_MESSAGE = "message non RP";
    public static final String IN_CHARACTER_MESSAGE = "message RP";

    public static final int REQUEST_CODE_ADD_CHARACTER = 1;
    public static final int REQUEST_CODE_CREATE_CHARACTER = 2;
    public static final int REQUEST_CODE_CONFIRM = 3;
    public static final int REQUEST_CODE_ROOM_ADMIN = 4;

    public static final String EXTRA_QUESTION = "question";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_DESCR = "description";
}
