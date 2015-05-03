package f2.spw;

import java.io.Serializable;

public class PlayerRecord implements Serializable{

	private String name = "Alex";

	protected long score = 0;
	protected int stage;
	
	public PlayerRecord(String name,long score,int stage) {
		this.name = name;
		this.score = score;
		this.stage = stage;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}