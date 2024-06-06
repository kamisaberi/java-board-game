import java.util.ArrayList;

public class Entity {

    public final String Tag;
    public final boolean Destroyable;

    public static ArrayList<Location> getFreeLocations(Object[][] Gametiles) {
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

    public boolean isDestroyable() {
        return Destroyable;
    }

    public Entity(String Tag,boolean Destroyable) {
        this.Tag = Tag;
        this.Destroyable = Destroyable;
    }
    public String setTag() {
        String tag = Tag;
        return tag;
    }
    public String getTag() {
        return Tag;
    }

    @Override
    public String toString() {
        return Tag;
    }
}
