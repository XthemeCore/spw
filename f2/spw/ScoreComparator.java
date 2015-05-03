package f2.spw;

import java.util.Comparator;

public class ScoreComparator implements Comparator<PlayerRecord> {
    @Override
    public int compare(PlayerRecord r1, PlayerRecord r2) {
        return (int)(r2.score - r1.score);
    }
}