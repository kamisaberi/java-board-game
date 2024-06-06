public class Location {
    int i = 0;
    int j = 0;

    public Location(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object obj) {
        Location location = (Location) obj;
        return this.i == location.i && this.j == location.j;
    }

    @Override
    public String toString() {
        return String.format("x:%d , y : %d", this.i, this.j);
    }
}
