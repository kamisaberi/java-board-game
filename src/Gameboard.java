import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


interface Gameboard {

    Object[][] Gametiles = new Object[10][10];
    Random random = new Random(new Date().getTime());
    void print(Player player);

    ArrayList<Location> getFreeLocations ();
    public  Player whichPlayerTurn();
}
