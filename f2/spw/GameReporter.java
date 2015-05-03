package f2.spw;

import java.util.ArrayList;

public interface GameReporter {

	long getScore();
	int getStage();
	int getMaxHealth();
	int getHealth();
	ArrayList<PlayerRecord> getScoreRecord();
}
