
import java.util.Random;

public class Spinner extends Entity {

    int x;
    int y;


    public static final int RANDOM_TYPE_GIVE_ABILITY = 0;
    public static final int RANDOM_TYPE_GO_BACK_TO_START = 1;
    public static final int RANDOM_TYPE_GO_BACK_ENEMY_TO_START = 2;
    public static final int RANDOM_TYPE_CREATE_3_TNT = 3;
    public static final int RANDOM_TYPE_DESTROY_3_TRAPS = 4;


    Random random = new Random();

    public Spinner(int x, int y, String Tag, boolean Destroyable) {
        super(Tag, Destroyable);
        this.x = x;
        this.y = y;
    }


    public void reSpawn(Object[][] Gametiles) {
        while (true) {
            int randomx = random.nextInt(10);
            int randomy = random.nextInt(10);
            if (Gametiles[randomx][randomy] == null) {
                Gametiles[randomx][randomy] = new Spinner(randomx, randomy, "SPN", false);
                Gametiles[x][y] = null;
                this.x = randomx;
                this.y = randomy;
                break;
            }
        }
    }
}
