import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Player extends Entity {


    Scanner input = new Scanner(System.in);

    public Player(int x, int y, String Tag, boolean Destroyable, int score, int HP, int LongJumpCount, int DestructionCount, int SpawnTrapCount) {
        super(Tag, Destroyable);
        this.x = x;
        this.y = y;
        this.originX = x;
        this.originY = y;
        this.score = score;
        this.HP = HP;
        this.LongJumpCount = LongJumpCount;
        this.DestructionCount = DestructionCount;
        this.SpawnTrapCount = SpawnTrapCount;
    }

    private int originX;
    private int originY;
    private int x;
    private int y;
    private int score;
    private int HP;
    private int LongJumpCount;
    private int DestructionCount;
    private int SpawnTrapCount;
    private String LastTurnAction;

    public String getLastTurnAction() {
        if (LastTurnAction == null) return "Game has just Started.";
        else return LastTurnAction;
    }

    public void setLastTurnAction(String lastTurnAction) {
        LastTurnAction = lastTurnAction;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getLongJumpCount() {
        return LongJumpCount;
    }

    public void setLongJumpCount(int longJumpCount) {
        LongJumpCount = longJumpCount;
    }

    public int getDestructionCount() {
        return DestructionCount;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setDestructionCount(int destructionCount) {
        DestructionCount = destructionCount;
    }

    public int getSpawnTrapCount() {
        return SpawnTrapCount;
    }

    public void setSpawnTrapCount(int spawnTrapCount) {
        SpawnTrapCount = spawnTrapCount;
    }

    public String getAbilities() {
        return "Destruction: " + getDestructionCount() + " | Long Jump: " + getLongJumpCount() + " | Spawn Trap: " + getSpawnTrapCount();
    }

    public Player getAnotherPlayer(Object[][] Gametiles) {
        for (int i = 0; i < Gametiles.length; i++) {
            for (int j = 0; j < Gametiles[i].length; j++) {
                if (Gametiles[i][j] instanceof Player && !((Player) Gametiles[i][j]).Tag.equals(this.Tag)) {
                    return (Player) Gametiles[i][j];
                }
            }
        }
        return null;
    }

    public void move(Object[][] Gametiles, String direction, String action) {
        String lastAction = "";
        Location location = getNextLocation(direction, action);
//        System.out.println(location);
        lastAction = this + " moved from [" + (x) + "][" + y + "] to [" + location.j + "][" + location.i + "].";
        Entity entity = getNextLocationSpot(Gametiles, direction, action);
        if (entity instanceof Trap) {
            BlockEffect blockEffect = Trap.blockEffects.get(((Trap) entity).Tag);
            HP += blockEffect.HP;
            score += blockEffect.score;
            lastAction = this + " moved from [" + x + "][" + (y) + "] to [" + location.j + "][" + location.i + "] stepped on a " + blockEffect.title + " and lost " + Math.abs(blockEffect.HP) + " life and " + Math.abs(blockEffect.score) + " scores. ";
        } else if (entity instanceof Treasure) {
            score += 10;
            Treasure.reSpawn(Gametiles);
            lastAction = this + " moved from [" + x + "][" + (y) + "] to [" + location.j + "][" + location.i + "] picked up treasure and obtained +10 Scores. ";
        } else if (entity instanceof Wall) {
            if (action.equals("D")) {
                Wall wall = (Wall) entity;
                if (wall.Tag.equals("BWL")) {
                    Gametiles[location.i][location.j] = null;
                    location.i = this.y;
                    location.j = this.x;
                    lastAction = this + " moved from [" + x + "][" + (y) + "] to [" + location.j + "][" + location.i + "] destroyed wall. ";
                } else if (wall.Tag.equals("UWL")) {
                    System.out.println("Cant break Unbreakable Wall.Choose another choice");
                }
            } else if (action.isEmpty()) {
                System.out.println("Cannot move over wall.Choose another choice");
            }
        } else if (entity instanceof Spinner) {
            System.out.println("SPINNER");
            int rnd = new Random().nextInt(5);
            switch (rnd) {
                case Spinner.RANDOM_TYPE_GIVE_ABILITY:
                    rnd = new Random().nextInt(3);
                    if (rnd == 0){
                        LongJumpCount++;
                        lastAction = "//TODO create message  got 1 free long jump";
                    }
                    else if (rnd == 1){
                        DestructionCount++;
                        lastAction = "//TODO create message got 1 free desctuction ";
                    }
                    else{
                        SpawnTrapCount++;
                        lastAction = "//TODO create message got 1 spawn";
                    }
                    break;

                case Spinner.RANDOM_TYPE_GO_BACK_TO_START:
                    location.i = this.originX;
                    location.j = this.originY;
                    lastAction = "//TODO player returned to start location";
                    break;
                case Spinner.RANDOM_TYPE_GO_BACK_ENEMY_TO_START:
                    Player pl = getAnotherPlayer(Gametiles);
                    pl.x = pl.originX;
                    pl.y = pl.originY;
                    lastAction = "//TODO enemy returned to start location";
                    break;
                case Spinner.RANDOM_TYPE_CREATE_3_TNT:
                    Trap.spawnTnTTraps(Gametiles, 3);
                    lastAction = "//TODO create 3 trap message";
                    break;
                case Spinner.RANDOM_TYPE_DESTROY_3_TRAPS:
                    Trap.destroyNTraps(Gametiles, 3);
                    lastAction = "//TODO destroy 3 trap message";
                    break;
            }
        } else if (entity instanceof Portal) {
            Portal portal = ((Portal) entity).getAnotherPortal(Gametiles);
            ArrayList<Location> locations = portal.getFreeLocationAroundPortal(Gametiles);
            if (locations.isEmpty()) {
                location.i = this.y;
                location.j = this.x;
                lastAction = "//TODO cant use portal beacause target portal doesnt have free spot ";
            } else {
                int rnd = new Random().nextInt(locations.size());
                Location location1 = locations.get(rnd);
                location.i = location1.i;
                location.j = location1.j;
                lastAction = "//TODO portal used message";
            }
        } else if (entity == null) {
            if (action.equals("S")) {
                String trapType = Trap.getRandomTrapType();
                Gametiles[location.i][location.j] = new Trap(location.j, location.i, trapType, true);
                location.i = this.y;
                location.j = this.x;
                lastAction = this + " moved from [" + x + "][" + (y) + "] to [" + location.j + "][" + location.i + "] created trap. ";
            }
        }
        System.out.println(location);
        Gametiles[location.i][location.j] = this;
        Gametiles[y][x] = null;
        x = location.j;
        y = location.i;
        setLastTurnAction(lastAction);
    }


    private HashMap<String, Integer> createMapFromActions() {
        HashMap<String, Integer> actions = new HashMap<>();
        actions.put("D", DestructionCount);
        actions.put("L", LongJumpCount);
        actions.put("S", SpawnTrapCount);
        return actions;
    }


    private Location getNextLocation(String direction, String action) {
        return switch (direction) {
            case "D" -> action.equals("L") ? new Location(y + 2, x) : new Location(y + 1, x);
            case "U" -> action.equals("L") ? new Location(y - 2, x) : new Location(y - 1, x);
            case "L" -> action.equals("L") ? new Location(y, x - 2) : new Location(y, x - 1);
            case "R" -> action.equals("L") ? new Location(y, x + 2) : new Location(y, x + 1);
            default -> null;
        };

    }

    private boolean isNextLocationValid(String direction, String action) {
        Location location = getNextLocation(direction, action);
        if (location == null) return false;
        if (location.i < 0 || location.i > 9) return false;
        if (location.j < 0 || location.j > 9) return false;
        return true;
    }

    private Entity getNextLocationSpot(Object[][] GameTiles, String direction, String action) {
        Location location = getNextLocation(direction, action);
        if (GameTiles[location.i][location.j] != null) return (Entity) GameTiles[location.i][location.j];
        return null;
    }

    private boolean canAct(Object[][] GameTiles, String direction, String action) {
        if (createMapFromActions().get(action) <= 0) return false;
        Entity entity = getNextLocationSpot(GameTiles, direction, action);
        if (action.equals("D") && (entity == null || !entity.isDestroyable())) return false;
        if (entity != null && action.equals("S")) return false;
        return true;
    }

    private boolean canMove(Object[][] GameTiles, String direction, String action) {
        if (!isNextLocationValid(direction, action)) return false;
        if (getNextLocationSpot(GameTiles, direction, action) instanceof Wall) return false;
        return true;
    }

    private boolean isCommandValid(Object[][] GameTiles, String command) {
        if (command.length() > 2) return false;
        if ("ULDR".indexOf(command.charAt(0)) == -1) return false;
        if (command.length() == 1 && !canMove(GameTiles, command.substring(0, 1), "")) return false;
        if (command.length() == 2 && "DLS".indexOf(command.charAt(1)) == -1) return false;
        if (command.length() == 2 && !canAct(GameTiles, command.substring(0, 1), command.substring(1, 2))) return false;
        return true;
    }

    public void play(Object[][] GameTiles) {

        while (true) {
            String command = input.nextLine().toUpperCase().trim();
            if (!isCommandValid(GameTiles, command)) continue;
            move(GameTiles, command.substring(0, 1), command.length() == 1 ? "" : command.substring(1, 2));
//            move(GameTiles, command);
            break;
        }

    }

}

