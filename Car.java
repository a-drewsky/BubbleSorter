package sortPak;

public class Car {

	//private int space;
	private int value;
	private int[] color = new int[3];
	private float x;
	private float y;
	private float angle;
	private int dir;
	private int rotPhase;
	
	public Car(int v, int r, int g, int b) {
		//space=s;
		value=v;
		color[0]=r;
		color[1]=g;
		color[2]=b;
		dir=0;
		angle=(float)3.14159;
		rotPhase=0;
	}
	
	/*public int getSpace() {
		return space;
	}*/
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int v) {
		value = v;
	}
	
	public int r() {
		return color[0];
	}
	
	public int g() {
		return color[1];
	}
	
	public int b() {
		return color[2];
	}
	
	public void setXY(float x_, float y_) {
		x=x_;
		y=y_;
	}
	
	public void updateXY(float x_, float y_) {
		x+=x_;
		y+=y_;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setAngle(float f) {
		angle=f;
	}
	
	public void updateAngle(float f) {
		angle+=f;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public int getDir() {
		return dir;
	}
	
	public void setDir(int n) {
		dir=n;
	}
	
	public int getRotPhase() {
		return rotPhase;
	}
	
	public void setRotPhase(int n) {
		rotPhase=n;
	}
	
}
