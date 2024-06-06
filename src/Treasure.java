
import java.util.ArrayList;
import java.util.Random;

public class Treasure extends Entity {


    int x;
    int y;

    Random random = new Random();

    public Treasure(int x, int y, String Tag, boolean Destroyable) {
        super(Tag, Destroyable);
        this.x = x;
        this.y = y;
    }

    public static void reSpawn(Object[][] Gametiles) {

        ArrayList<Location> locations = Treasure.getFreeLocations(Gametiles);
        int rndInd = new Random().nextInt(locations.size());
        Gametiles[locations.get(rndInd).j][locations.get(rndInd).i] = new Treasure(locations.get(rndInd).i, locations.get(rndInd).j, "TSR", false);

    }
}
