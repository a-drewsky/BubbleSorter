package sortPak;

public class SwapToken {
	
	private int car1Val;
	private int car2Val;
	private int phase;
	
	public SwapToken(int c1, int c2) {
		car1Val=c1;
		car2Val=c2;
		phase=1;
	}
	
	public int getCar1Space() {
		return car1Val;
	}
	
	public int getCar2Space() {
		return car2Val;
	}
	
	public int getPhase() {
		return phase;
	}
	
	public void setPhase(int n) {
		phase=n;
	}

}
