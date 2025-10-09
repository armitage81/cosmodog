package antonafanasjew.cosmodog.filesystem;

public class DefaultPathProvider implements PathProvider {

    private static final String USER_HOME = System.getProperty("user.home");
    private static final String GAME_SAVE_DIR = USER_HOME + "/cosmodog";

    public String gameSaveDir() {
        return GAME_SAVE_DIR;
    }

    public String scoreSavePath() {
        return gameSaveDir() + "/scorelist.bin";
    }

}
