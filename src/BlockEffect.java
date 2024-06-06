import java.lang.management.ThreadInfo;

public class BlockEffect {
    public String title;
    public int HP = 0;
    public int score = 0;

    public BlockEffect(String title, int HP, int score) {
        this.title = title;
        this.HP = HP;
        this.score = score;
    }
}
