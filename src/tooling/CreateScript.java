package tooling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CreateScript {

    public static List<String> writingPaths = new ArrayList<>();

    static {

        writingPaths.add("dyinghints/dyinghints");

        writingPaths.add("gamelogs/hints/001");
        writingPaths.add("gamelogs/hints/002");
        writingPaths.add("gamelogs/hints/003");
        writingPaths.add("gamelogs/hints/004");
        writingPaths.add("gamelogs/hints/005");
        writingPaths.add("gamelogs/hints/006");
        writingPaths.add("gamelogs/hints/007");
        writingPaths.add("gamelogs/hints/008");
        writingPaths.add("gamelogs/hints/009");
        writingPaths.add("gamelogs/hints/010");

        writingPaths.add("intro/intro");
        writingPaths.add("gamelogs/cutscenes/001.meetingalisa");
        writingPaths.add("gamelogs/cutscenes/002.silence");
        writingPaths.add("gamelogs/cutscenes/003.ghosttown");

        writingPaths.add("gamelogs/dronespecs/001");
        writingPaths.add("gamelogs/dronespecs/002");
        writingPaths.add("gamelogs/dronespecs/003");
        writingPaths.add("gamelogs/dronespecs/004");
        writingPaths.add("gamelogs/dronespecs/005");
        writingPaths.add("gamelogs/dronespecs/006");
        writingPaths.add("gamelogs/dronespecs/007");

        writingPaths.add("gamelogs/unsorted/aliensequencepart1");
        writingPaths.add("gamelogs/unsorted/aliensequencepart2");
        writingPaths.add("gamelogs/unsorted/containers");
        writingPaths.add("gamelogs/unsorted/cornerstones");
        writingPaths.add("gamelogs/unsorted/dreamweaversstash");
        writingPaths.add("gamelogs/unsorted/encounterwithsuf");
        writingPaths.add("gamelogs/unsorted/flowersandmines");
        writingPaths.add("gamelogs/unsorted/guardiandeactivation");
        writingPaths.add("gamelogs/unsorted/isolatedmonolith");
        writingPaths.add("gamelogs/unsorted/murdercase");
        writingPaths.add("gamelogs/unsorted/openedgate");
        writingPaths.add("gamelogs/unsorted/poison");
        writingPaths.add("gamelogs/unsorted/portals");
        writingPaths.add("gamelogs/unsorted/riddletemples");
        writingPaths.add("gamelogs/unsorted/sightradius");
        writingPaths.add("gamelogs/unsorted/solarcells");
        writingPaths.add("gamelogs/unsorted/southlab");
        writingPaths.add("gamelogs/unsorted/stealth");
        writingPaths.add("gamelogs/unsorted/trappedinacave");
        writingPaths.add("gamelogs/unsorted/wormwarning");

        writingPaths.add("gamelogs/writersblock/001");
        writingPaths.add("gamelogs/writersblock/002");
        writingPaths.add("gamelogs/writersblock/003");
        writingPaths.add("gamelogs/writersblock/004");
        writingPaths.add("gamelogs/writersblock/005");
        writingPaths.add("gamelogs/writersblock/006");

        writingPaths.add("gamelogs/montanajames/001");
        writingPaths.add("gamelogs/montanajames/002");
        writingPaths.add("gamelogs/montanajames/003");
        writingPaths.add("gamelogs/montanajames/004");
        writingPaths.add("gamelogs/montanajames/005");
        writingPaths.add("gamelogs/montanajames/006");
        writingPaths.add("gamelogs/montanajames/007");

        writingPaths.add("gamelogs/amurderingpoet/001");
        writingPaths.add("gamelogs/amurderingpoet/002");
        writingPaths.add("gamelogs/amurderingpoet/003");
        writingPaths.add("gamelogs/amurderingpoet/004");
        writingPaths.add("gamelogs/amurderingpoet/005");
        writingPaths.add("gamelogs/amurderingpoet/006");
        writingPaths.add("gamelogs/amurderingpoet/007");
        writingPaths.add("gamelogs/amurderingpoet/008");

        writingPaths.add("gamelogs/fathersfarewell/001");
        writingPaths.add("gamelogs/fathersfarewell/002");
        writingPaths.add("gamelogs/fathersfarewell/003");
        writingPaths.add("gamelogs/fathersfarewell/004");
        writingPaths.add("gamelogs/fathersfarewell/005");
        writingPaths.add("gamelogs/fathersfarewell/006");

        writingPaths.add("gamelogs/smileofthegoddess/001");
        writingPaths.add("gamelogs/smileofthegoddess/002");
        writingPaths.add("gamelogs/smileofthegoddess/003");
        writingPaths.add("gamelogs/smileofthegoddess/004");

        writingPaths.add("gamelogs/pinky/001");
        writingPaths.add("gamelogs/pinky/002");
        writingPaths.add("gamelogs/pinky/003");
        writingPaths.add("gamelogs/pinky/004");
        writingPaths.add("gamelogs/pinky/005");
        writingPaths.add("gamelogs/pinky/006");
        writingPaths.add("gamelogs/pinky/007");
        writingPaths.add("gamelogs/pinky/008");
        writingPaths.add("gamelogs/pinky/009");

        writingPaths.add("gamelogs/privatehiggs/001");
        writingPaths.add("gamelogs/privatehiggs/002");
        writingPaths.add("gamelogs/privatehiggs/003");
        writingPaths.add("gamelogs/privatehiggs/004");
        writingPaths.add("gamelogs/privatehiggs/005");
        writingPaths.add("gamelogs/privatehiggs/006");
        writingPaths.add("gamelogs/privatehiggs/007");
        writingPaths.add("gamelogs/privatehiggs/008");
        writingPaths.add("gamelogs/privatehiggs/009");
        writingPaths.add("gamelogs/privatehiggs/010");

        writingPaths.add("gamelogs/memories/memory001");
        writingPaths.add("gamelogs/memories/memory002");
        writingPaths.add("gamelogs/maryharper/001");
        writingPaths.add("gamelogs/memories/memory003");
        writingPaths.add("gamelogs/memories/memory004");
        writingPaths.add("gamelogs/memories/memory005");
        writingPaths.add("gamelogs/maryharper/002");
        writingPaths.add("gamelogs/memories/memory006");
        writingPaths.add("gamelogs/memories/memory007");
        writingPaths.add("gamelogs/maryharper/003");
        writingPaths.add("gamelogs/memories/memory008");
        writingPaths.add("gamelogs/memories/memory009");
        writingPaths.add("gamelogs/memories/memory010");
        writingPaths.add("gamelogs/maryharper/004");
        writingPaths.add("gamelogs/memories/memory011");
        writingPaths.add("gamelogs/memories/memory012");
        writingPaths.add("gamelogs/maryharper/005");
        writingPaths.add("gamelogs/memories/memory013");
        writingPaths.add("gamelogs/maryharper/006");
        writingPaths.add("gamelogs/memories/memory014");
        writingPaths.add("gamelogs/maryharper/007");
        writingPaths.add("gamelogs/memories/memory015");
        writingPaths.add("gamelogs/maryharper/008");
        writingPaths.add("gamelogs/memories/memory016");
        writingPaths.add("gamelogs/maryharper/009");
        writingPaths.add("gamelogs/memories/memory017");
        writingPaths.add("gamelogs/maryharper/010");
        writingPaths.add("gamelogs/memories/memory018");
        writingPaths.add("gamelogs/maryharper/011");
        writingPaths.add("gamelogs/memories/memory019");
        writingPaths.add("gamelogs/maryharper/012");
        writingPaths.add("gamelogs/memories/memory020");
        writingPaths.add("gamelogs/cutscenes/004.enteringthebase");
        writingPaths.add("gamelogs/cutscenes/005.entering6000infobitsbarrier");
        writingPaths.add("gamelogs/aliennomads/001");
        writingPaths.add("gamelogs/aliennomads/002");
        writingPaths.add("gamelogs/aliennomads/003");
        writingPaths.add("gamelogs/aliennomads/004");
        writingPaths.add("gamelogs/aliennomads/005");
        writingPaths.add("gamelogs/aliennomads/006");
        writingPaths.add("gamelogs/aliennomads/007");
        writingPaths.add("gamelogs/aliennomads/008");
        writingPaths.add("gamelogs/aliennomads/009");
        writingPaths.add("gamelogs/cutscenes/006.decision");
        writingPaths.add("outro/outro_a");
        writingPaths.add("outro/outro_b");

    }


    public static void main(String[] args) throws IOException {
        List<Path> textFiles = new ArrayList<>();

        writingPaths.forEach(e -> textFiles.add(Path.of("data/writing/" + e)));

        List<String> allLines = new ArrayList<>();

        for (Path e : textFiles) {
            allLines.addAll(Files.readAllLines(e));
            allLines.add("");
            allLines.add("");
            allLines.add("");
            allLines.add("");
            allLines.add("");
        }


        allLines = allLines.stream().map(String::trim).toList();

        String text = String.join("\r\n", allLines);

        /*
        text = text.replaceAll("\r\n", "\n");

        text = text.replaceAll("<font:speech>[ \t]*", "");
        text = text.replaceAll("<font:speech>", "");
        text = text.replaceAll("<font:narration>[ \t]*", "");
        text = text.replaceAll("(-----+)\n", "\n\n\n\n");


        text = text.replaceAll("([^\\n])\n{1}([^\\n])", "$1 $2");
        text = text.replaceAll("([^\n])\n{2}([^\n])", "$1\n$2");
        text = text.replaceAll("([^\n])\n{3}([^\n])", "$1\n\n$2");
        text = text.replaceAll("([^\n])\n{4}([^\n])", "$1\n\n\n$2");
        */

        System.out.println(text);

    }

}
