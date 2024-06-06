import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Trap extends Entity {

    public static ArrayList<Trap> traps = new ArrayList<>();
    public static HashMap<String, BlockEffect> blockEffects = new HashMap<>();

    static {
        blockEffects.put("MST", new BlockEffect("Mouse Trap", -1, -5));
        blockEffects.put("BMB", new BlockEffect("Bomb", -2, -10));
        blockEffects.put("TNT", new BlockEffect("TNT", -3, -15));
    }


    public static ArrayList<Trap> getTraps(Object[][] Gametiles) {
        ArrayList<Trap> traps = new ArrayList<>();
        for (int i = 0; i < Gametiles.length; i++) {
            for (int j = 0; j < Gametiles[i].length; j++) {
                if (Gametiles[i][j] instanceof Trap) {
                    traps.add((Trap) Gametiles[i][j]);
                }
            }
        }
        return traps;
    }

    public static void spawnTnTTraps(Object[][] Gametiles, int count) {
        int trapCount = 0;
        do {
            ArrayList<Location> locations = Trap.getFreeLocations(Gametiles);
            int rndInd = new Random().nextInt(locations.size());
            Gametiles[locations.get(rndInd).i][locations.get(rndInd).j] = new Trap(locations.get(rndInd).i, locations.get(rndInd).j, "TNT", false);
            trapCount++;
        } while (trapCount < count);
    }

    public static void destroyNTraps(Object[][] Gametiles, int count) {

        for (int i = 0; i <= count; i++) {
            ArrayList<Trap> traps = getTraps(Gametiles);
            int rnd = new Random().nextInt(traps.size());
            Gametiles[traps.get(rnd).y][traps.get(rnd).x] = null;
        }
    }

    int x;
    int y;

    Random random = new Random();
    Scanner input = new Scanner(System.in);

    public Trap(int x, int y, String Tag, boolean Destroyable) {
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


        for (int i = 0; i < numObstacles * 0.3; i++) {
            int randomRow = random.nextInt(10);
            int randomCol = random.nextInt(10);
            if (Gametiles[randomRow][randomCol] == null) {
                String trapType = getRandomTrapType();
                Boolean destroyable = CanBeDestroyed(trapType);
                Gametiles[randomRow][randomCol] = new Trap(randomRow, randomCol, trapType, destroyable);
                Gametiles[x][y] = null;
                this.x = randomRow;
                this.y = randomCol;
                break;
            }
        }
    }

    public static String getRandomTrapType() {
        int random = new Random().nextInt(3);
        switch (random) {
            case 0:
                return "MST";
            case 1:
                return "BMB";
            case 2:
                return "TNT";
            default:
                throw new IllegalStateException("" + random);
        }
    }

    public static Boolean CanBeDestroyed(String trapType) {
        if (trapType.equals("MST") || trapType.equals("BMB")) {
            return true;
        } else {
            return false;
        }
    }


}


//    public enum TrapType {
//        MST, BMB, TNT
//    }

//    public class Trap {
//        private TrapType type;
//        private int damage;
//        private int score;
//
//        public Trap(TrapType type) {
//            this.type = type;
//            switch (type) {
//                case MST:
//                    damage = 1;
//                    score = 5;
//                    break;
//                case BMB:
//                    damage = 2;
//                    score = 10;
//                    break;
//                case TNT:
//                    damage = 3;
//                    score = 15;
//                    break;
//            }
//        }
//
//        public int getDamage() {
//            return damage;
//        }
//
//        public int getScore() {
//            return score;
//        }
//    }
//
//    public enum WallType {
//        BWL, UWL
//    }
//
//    public class Wall {
//        private WallType type;
//
//        public Wall(WallType type) {
//            this.type = type;
//        }
//
//        public boolean isBreakable() {
//            return type == WallType.BWL;
//        }
//    }
//}
