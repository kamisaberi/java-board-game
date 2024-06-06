import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Main implements Gameboard {


    //CONSTANTS
    public static final float WALL_PERCENT = 0.1f;
    public static final float TRAP_PERCENT = 0.1f;
    public static final String WHITE_BACKGROUND = "\033[0;107m";
    public static final String WHITE = "\u001b[0m";
    public static final String YELLOW = "\033[0;93m";
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\033[1;90m";
    public static final String RED = "\033[0;91m";
    public static final String GREEN = "\u001b[32m";
    public static final String BLUE = "\u001b[34m";
    public static final boolean DARK_MODE_ACTIVATED = false;

    public static final int DARK_MODE_DISPLAY_RADIUS = 4;
    public static final String DARK_MODE_DISPLAY_SIGN = " ??? ║";
    public static final boolean USE_PORTALS = true;
    public static final boolean USE_SPINNER = true;
    public static final boolean DEBUG_MODE = true;
    //VARIABLES
    Random random = new Random(new Date().getTime());
    Player Player1;
    Player Player2;
    int Turn = 1;

    @Override
    public ArrayList<Location> getFreeLocations() {
        ArrayList<Location> locations = new ArrayList<>();
        for (int i = 0; i < Gametiles.length; i++) {
            for (int j = 0; j < Gametiles[i].length; j++) {
                if (Gametiles[i][j] == null) {
                    locations.add(new Location(i, j));
                }
            }
        }
        return locations;
    }

    public Player whichPlayerTurn() {
        if (this.Turn % 2 == 1) {
            return Player1;
        } else {
            return Player2;
        }
    }


    @Override
    public void print(Player player) {

        System.out.print("\033[H\033[2J");
        System.out.flush();
        Player playerShouldMove = whichPlayerTurn();
        int startXToShow = 0;
        int startYToShow = 0;
        int endXToShow = 9;
        int endYToShow = 9;
        if (DARK_MODE_ACTIVATED) {
            int x = playerShouldMove.getX();
            int y = playerShouldMove.getY();
            startXToShow = Math.max(0, x - DARK_MODE_DISPLAY_RADIUS);
            startYToShow = Math.max(0, y - DARK_MODE_DISPLAY_RADIUS);
            endXToShow = Math.min(9, x + DARK_MODE_DISPLAY_RADIUS);
            endYToShow = Math.min(9, y + DARK_MODE_DISPLAY_RADIUS);
        }


        System.out.println("╔═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╗");
        for (int i = 0; i < Gametiles.length; i++) {
            System.out.print("║");
            for (int j = 0; j < Gametiles[i].length; j++) {
                String toPrint = "";
                if (Gametiles[i][j] == null) {
                    toPrint = "     ║";
                } else {
                    boolean inRange = i >= startYToShow && i <= endYToShow && j >= startXToShow && j <= endXToShow;
                    switch (Gametiles[i][j].getClass().getSimpleName()) {
                        case "Player":
                            toPrint = " " + WHITE_BACKGROUND + BLACK + Gametiles[i][j] + RESET + " ║";
                            break;
                        case "Treasure":
                            toPrint = inRange ? " " + GREEN + Gametiles[i][j] + RESET + " ║" : DARK_MODE_DISPLAY_SIGN;
                            break;
                        case "Trap":
                            toPrint = inRange ? " " + RED + Gametiles[i][j] + RESET + " ║" : DARK_MODE_DISPLAY_SIGN;
                            break;
                        case "Wall":
                            toPrint = inRange ? " " + YELLOW + Gametiles[i][j] + RESET + " ║" : DARK_MODE_DISPLAY_SIGN;
                            break;
                        case "Portal":
                            toPrint = inRange ? " " + BLUE + Gametiles[i][j] + RESET + " ║" : DARK_MODE_DISPLAY_SIGN;
                            break;
                        case "Spinner":
                            toPrint = inRange ? " " + WHITE + Gametiles[i][j] + RESET + " ║" : DARK_MODE_DISPLAY_SIGN;
                            break;
                    }
                }
                System.out.print(toPrint);
            }
            System.out.println();
            if (i < Gametiles.length - 1) {
                System.out.println("╠═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╣");
            }
        }
        System.out.println("╚═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╝");
        System.out.println("PL1" + " Score: " + Player1.getScore() + " | " + "PL2" + " Score: " + Player2.getScore());
        System.out.println("PL1" + " HP: " + Player1.getHP() + " | " + "PL2" + " HP: " + Player2.getHP());
        System.out.println("PL1" + " Abilities -> " + Player1.getAbilities() + "\nPL2" + " Abilities -> " + Player2.getAbilities());
        System.out.println("------------- Turn: " + Turn + " " + player + "'s Turn , Choose an action -------------");
        System.out.println("U. Move Up - L. Move Left - D. Move Down - R. Move Right - DE. Destruction - LJ. Long Jump - ST. Spawn Trap");
        System.out.println("in order to use your abilities first Enter the Direction key then the key representing the ability (e.g., DT)");
    }


    public void spawnPlayers() {
        Player1 = new Player(9, 0, "PL1", false, 0, 5, 6, 3, 3);
        Player2 = new Player(0, 9, "PL2", false, 0, 5, 6, 3, 3);
        Gametiles[0][9] = Player1;
        Gametiles[9][0] = Player2;

    }

    public void spawnTreasure() {
        // Spawn treasure
        ArrayList<Location> locations = getFreeLocations();
        int rndInd = random.nextInt(locations.size());
        Gametiles[locations.get(rndInd).i][locations.get(rndInd).j] = new Treasure(locations.get(rndInd).j, locations.get(rndInd).i, "TSR", false);
    }

    public void spawnWalls() {
        int wallCount = 0;
        do {
            ArrayList<Location> locations = getFreeLocations();
            int rndInd = random.nextInt(locations.size());
            String trapType = Wall.getRandomWallType();
            Gametiles[locations.get(rndInd).i][locations.get(rndInd).j] = new Wall(locations.get(rndInd).j, locations.get(rndInd).i, trapType, false);
            wallCount++;
        } while (!((float) wallCount / 100 >= WALL_PERCENT));
    }

    public void spawnTraps() {
        int trapCount = 0;
        do {
            ArrayList<Location> locations = getFreeLocations();
            int rndInd = random.nextInt(locations.size());
            String trapType = Trap.getRandomTrapType();
            Gametiles[locations.get(rndInd).i][locations.get(rndInd).j] = new Trap(locations.get(rndInd).j, locations.get(rndInd).i, trapType, false);
            trapCount++;
        } while (!((float) trapCount / 100 >= TRAP_PERCENT));
    }

    public void spawnPortals() {
        if (!USE_PORTALS)
            return;
        ArrayList<Location> locations = getFreeLocations();
        int rndInd = random.nextInt(locations.size());
        Gametiles[locations.get(rndInd).i][locations.get(rndInd).j] = new Portal(locations.get(rndInd).j, locations.get(rndInd).i, "PRT");

        if (!DEBUG_MODE) {
            locations = getFreeLocations();
            rndInd = random.nextInt(locations.size());
            Gametiles[locations.get(rndInd).i][locations.get(rndInd).j] = new Portal(locations.get(rndInd).j, locations.get(rndInd).i, "PRT");
        } else {
            Gametiles[8][0] = new Portal(0, 8, "PRT");
        }
    }

    public void spawnSpinner() {
        if (!USE_SPINNER) {
            return;
        }
        // Spawn Spinner
        if (!DEBUG_MODE) {
            ArrayList<Location> locations = getFreeLocations();
            int rndInd = random.nextInt(locations.size());
            Gametiles[locations.get(rndInd).i][locations.get(rndInd).j] = new Spinner(locations.get(rndInd).j, locations.get(rndInd).i, "SPN", false);
        } else {
            Gametiles[1][9] = new Spinner(9, 1, "SPN", false);
        }
    }


    public void spawnForDebug() {
        if (!DEBUG_MODE)
            return;
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new Location(0, 8));
        locations.add(new Location(1, 8));
        locations.add(new Location(1, 9));
        locations.add(new Location(8, 0));
        locations.add(new Location(8, 1));
        locations.add(new Location(9, 1));
        for (Location location : locations) {
            if (Gametiles[location.i][location.j] == null) {
                int tp = random.nextInt(2);
                if (tp == 0) {
                    String type = Trap.getRandomTrapType();
                    Gametiles[location.i][location.j] = new Trap(location.j, location.i, type, false);
                } else {
                    String type = Wall.getRandomWallType();
                    Gametiles[location.i][location.j] = new Wall(location.j, location.i, type, false);
                }
            }
        }
    }


    public static void main(String[] args) {

        new Main();

    }

    Main() {

        boolean GameOver = false;
        spawnPlayers();
        spawnTreasure();
        spawnTraps();
        spawnWalls();
        spawnPortals();
        spawnSpinner();
        spawnForDebug();


        while (!GameOver) {

            if (Turn % 2 == 1) { // PL1 turn

                System.out.println(Player2.getLastTurnAction());
                print(Player1);
                Player1.play(Gametiles);
                Turn++;

            } else if (Turn % 2 == 0) { // PL2 Turn

                System.out.println(Player1.getLastTurnAction());
                print(Player2);
                Player2.play(Gametiles);
                Turn++;
            }

            // End game condition
            if (Player1.getHP() <= 0 || Player2.getHP() <= 0 || Player1.getScore() >= 100 || Player2.getScore() >= 100) {
                GameOver = true;
                if (Player1.getHP() <= 0) {
                    System.out.println("PL1 died. PL2 Victory.");
                    break;
                } else if (Player1.getScore() >= 100) {
                    System.out.println("PL1 reached +100 scores. PL1 Victory");
                    break;
                }
                if (Player2.getHP() <= 0) {
                    System.out.println("PL2 died. PL1 Victory.");
                    break;
                } else if (Player2.getScore() >= 100) {
                    System.out.println("PL2 reached +100 scores. PL2 Victory");
                    break;
                }
            }
        }
    }
}