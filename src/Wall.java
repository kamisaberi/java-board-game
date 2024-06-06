import java.util.ArrayList;
import java.util.Random;

public class Wall extends Entity {

    public static ArrayList<Wall> walls = new ArrayList<>();
    int x;
    int y;

    Random random = new Random();

    public Wall(int x, int y, String Tag, boolean Destroyable) {
        super(Tag, Destroyable);
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
                String wallType = getRandomWallType();
                Boolean destroyable = CanBeDestroyed(wallType);
                Gametiles[randomRow][randomCol] = new Wall(randomRow, randomCol, wallType, destroyable);
                Gametiles[x][y] = null;
                this.x = randomRow;
                this.y = randomCol;
                break;
            }
        }
    }

    public static String getRandomWallType() {
        int random = new Random().nextInt(2);
        switch (random) {
            case 0:
                return "BWL";
            case 1:
                return "UWL";
            default:
                throw new IllegalStateException("Unexpected value: " + random);
        }
    }

    public static Boolean CanBeDestroyed(String wallType) {
        if (wallType.equals("BWL")) {
            return true;
        } else {
            return false;
        }
    }
}