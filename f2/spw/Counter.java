package f2.spw;

public class Counter {
	private int c;

	public Counter(){
		this(0);
	}

	public Counter(int c){
		setCount(c);
	}

	public void count(){
		c++;
	}

	public void reset(){
		setCount(0);
	}

	public void setCount(int c){
		this.c = c;
	}

	public int getCount(){
		return c;
	}

	public int getSeconds(){
		return 1000/c;
	}

	public int getMinutes(){
		return 60000/c;
	}
}