package f2.spw;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class ScoreRecord {

	private ArrayList<PlayerRecord> playerList;

	public ScoreRecord(){
		playerList = new ArrayList<PlayerRecord>();
		loadRecord();
	}

	public void addRecord(Player v){
		PlayerRecord record = new PlayerRecord(
			v.getName(),
			v.score,
			v.stage
			);
		playerList.add(record);
		Collections.sort(playerList, new ScoreComparator());
		saveRecord();
	}

	public void removeRecord(Player v){
		Iterator<PlayerRecord> p_iter = playerList.iterator();
		while(p_iter.hasNext()){
			PlayerRecord p = p_iter.next();
			if(compareRecord(p,v)){
				p_iter.remove();
				saveRecord();
			}
		}
	}

	public boolean compareRecord(PlayerRecord r,Player v){
		if(r.getName().equals(v.getName()) && r.score == v.score
			&& r.stage == v.stage)
			return true;
		return false;
	}

	public void saveRecord(){
		try{
			FileOutputStream recordFile =new FileOutputStream(".score",false);
			ObjectOutputStream record = new ObjectOutputStream(recordFile);

			record.writeObject(playerList);

			record.close();
			printRecord();
			loadRecord();
		}		
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void loadRecord(){
		try{
			FileInputStream recordFile =new FileInputStream(".score");
			ObjectInputStream record = new ObjectInputStream(recordFile);

			playerList = (ArrayList<PlayerRecord>)record.readObject();

			record.close();
		}
		catch(FileNotFoundException e){
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void printRecord(){
		try{
			PrintWriter record = new PrintWriter("score.txt");

			record.println("------------------------------------");
			record.println("************ Space War! ************");
			record.println("------------------------------------");
			record.println("Name          Score           Stage");
			record.println("------------------------------------");
			for(PlayerRecord r: playerList){
				record.printf("%s %20d %18d %n",r.getName(),r.score,r.stage);
			}

			record.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public ArrayList<PlayerRecord> getRecord(){
		return playerList;
	}
}