import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Portal extends Entity {


    public static ArrayList<Portal> portals = new ArrayList<>();


    public Portal getAnotherPortal(Object[][] Gametiles) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (Gametiles[i][j] instanceof Portal && (((Portal) Gametiles[i][j]).x != this.x || ((Portal) Gametiles[i][j]).y != this.y)) {
                    return (Portal) Gametiles[i][j];
                }
            }
        }
        return null;

    }

    public ArrayList<Location> getFreeLocationAroundPortal(Object[][] Gametiles) {
        ArrayList<Location> locations = new ArrayList<>();
        for (int i = Math.max(0, this.y - 1); i <= Math.min(9,this.y + 1); i++) {
            for (int j = Math.max(0, this.x - 1); j <= Math.min(9,this.x + 1); j++) {
                if (Gametiles[i][j] == null) {
                    locations.add(new Location(i, j));
                }
            }

        }
        return  locations;
    }


    int x;
    int y;

    Random random = new Random();

    public Portal(int x, int y, String Tag) {
        super(Tag, false);
        this.x = x;
        this.y = y;
    }

    public void reSpawn(Object[][] Gametiles) {
        int emptyTiles = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (Gametiles[i][j] == null) {
                    emptyTiles++;
                }
            }
        }

        int numObstacles = emptyTiles / 2;

        for (int i = 0; i < numObstacles * 0.2; i++) {
            int randomRow = random.nextInt(10);
            int randomCol = random.nextInt(10);
            if (Gametiles[randomRow][randomCol] == null) {
                Gametiles[randomRow][randomCol] = new Portal(randomRow, randomCol, "PRT");
                Gametiles[x][y] = null;
                this.x = randomRow;
                this.y = randomCol;
                break;
            }
        }
    }

}