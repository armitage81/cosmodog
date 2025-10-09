package antonafanasjew.cosmodog.filesystem;

public class CustomDecoratorForPathProvider implements PathProvider {

    private final PathProvider defaultDelegate;

    public CustomDecoratorForPathProvider(PathProvider defaultDelegate) {
        this.defaultDelegate = defaultDelegate;
    }

    @Override
    public String gameSaveDir() {
        String customSaveDir = System.getProperty("saveDir");
        if (customSaveDir != null && !customSaveDir.trim().isEmpty()) {
            return customSaveDir;
        } else {
            return defaultDelegate.gameSaveDir();
        }
    }

    @Override
    public String scoreSavePath() {
        return gameSaveDir() + "/scorelist.bin";
    }

}
