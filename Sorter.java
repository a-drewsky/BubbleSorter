package sortPak;

import java.util.*;

import processing.core.*;

public class Sorter extends PApplet{
	
	//number of cars
	static int size = 10;
	static Car[] Cars = new Car[size];
	
	//car spot locations
	static int[] spotsX = new int[size];
	static int spotsY = 50;
	
	//Moving distance
	static int yTemp1 = 160;
	static int yTemp2 = 200;
	static int dist1 = yTemp1-spotsY;
	static int dist2 = yTemp2-spotsY;
	
	//move speed
	static float speed=10;
	static float rotSpeed=16; //lower is faster

	//Value min and max
	static int min = 0;
	static int max = 100;
	boolean allowDuplicates = false;
	
	//spot specifications
	static PVector carSize = new PVector(20,30);
	static int buffer = 10;
	static int spotSize=80; //spot height
	int len; //width-buffer
	int iter; //spot length

	ArrayList<SwapToken> tokens = new ArrayList<SwapToken>();
	static float rotRadius = 5;
	
	//Bubble Sort
	int bubbleIndex=0;
	
	//shuffle
	int shuffleAmount=5;
	int shuffles=0;	
	
	//functionality
	boolean operating=false;
	boolean sort=false;
	boolean shuffle=false;
	int message=0;
	int messageTime=0;
	int fadeTime=30;
	
	//slider
	int sliderSize=100;
	int slider=5;
	
	//background
	ArrayList<Integer> specX = new ArrayList<Integer>();
	ArrayList<Integer> specY = new ArrayList<Integer>();
	ArrayList<Integer> specW = new ArrayList<Integer>();
	ArrayList<Integer> specH = new ArrayList<Integer>();
	ArrayList<Integer> specC = new ArrayList<Integer>();
	
	public static void main(String[] args){
		PApplet.main("sortPak.Sorter");
	}
	
	public void settings() {
		size(600,300, JAVA2D);
	}
	
	//Processing function: runs once at start
	public void setup() {
		len=width-(buffer*2);
		iter=len/size;
		
		//tokens.add(new SwapToken(2,5)); temporary
		
		//initialize cars
		for(int i=0; i<size; i++) {
			int v= (int) random(min,max);
			if(allowDuplicates==false) {
				while(valueTaken(v,i)) v = (int) random(min,max);
			}
			int r = (int)map(v,min,max,50,5);
			int g = (int)map(v,min,max,100,250);
			int b=210;
			Cars[i] = new Car(v,r,g,b);
			spotsX[i] = (buffer+iter/2)+i*iter;
			Cars[i].setXY(spotsX[i], spotsY);
			
		}
		rectMode(CENTER);
		textAlign(CENTER);
		loadSpecs();
		printVals();
	}
	
	//Processing function: runs every frame
	public void draw() {
		background(100);
		showSpecs();
		showLot();
		showCars();
		showUI();
		showMessage();
		
		if(sort && tokens.size()==0) bubbleSort(); //Bubble Sort
		
		if(shuffle && tokens.size()==0) {
			if(shuffles<shuffleAmount)shuffle();
			else {
				message=2;
				messageTime=fadeTime;
				shuffle=false;
				operating=false;
			}
		}
		
		for(int i=0; i<tokens.size(); i++) {
			SwapToken t = tokens.get(i);
			swap(t);
		}
	}
	
	public void mousePressed() {
		if(mouseX>buffer && mouseX<100+buffer 
		&& mouseY>height-buffer-40 && mouseY<height-buffer && !operating) {
			shuffles=0;
			operating=true;
			shuffle=true;
		}
		if(mouseX>200+buffer*7 && mouseX<300+buffer*7
		&& mouseY>height-buffer-40 && mouseY<height-buffer && !operating) {
			operating=true;
			sort=true;
		}
		if(mouseX>100+buffer*4 && mouseX<200+buffer*4
		&& mouseY>height-buffer-40 && mouseY<height-buffer && !operating) {
			randomizeValues();
		}
	}
	
	public void loadSpecs() {
		int dist1 = (int) random(5,20);
		int dist2 = (int) random(5,20);
		int chance;
		int bump1;
		int bump2;
		for(int i=0; i<width; i+=dist1) {
			for(int j=0; j<height; j+=dist2) {
				chance = (int) random(1,10);
				bump1 = (int) random(-5,5);
				bump2 = (int) random(-5,5);
				specW.add((int) random(1,4));
				specH.add((int) random(1,4));
				specX.add(i+bump1);
				specY.add(j+bump2);
				
				//draw spec
				specC.add((int)map(chance,1,10,50,150));
				
				dist2 = (int) random(2,20);
			}
			dist1 = (int) random(2,20);
		}
	}
	
	public void showSpecs() {
		for(int i=0; i<specX.size(); i++) {
			fill(specC.get(i));
			rect(specX.get(i),specY.get(i),specW.get(i),specH.get(i));
		}
	}
	
	public void showMessage() {
		textSize(25);
		if(messageTime==0)message=0;
		if(message==1) {
			fill(25,map(messageTime,0,fadeTime,0,250));
			text("COMPLETE",width/2,height/2);
			messageTime--;
		}
		if(message==2) {
			fill(25,map(messageTime,0,fadeTime,0,250));
			text("Shuffle Done",width/2,height/2);
			messageTime--;
		}
	}
	
	public void showUI() {
		textSize(14);
		textLeading(12);
		noStroke();

		//shuffle button
		fill(250,150,150);
		if(mouseX>buffer && mouseX<100+buffer 
		&& mouseY>height-buffer-40 && mouseY<height-buffer) {
			fill(250,200,200);
			if(mousePressed)fill(250,100,100);
		}
		rect(50+buffer,height-buffer-20,100,40);
		fill(25);
		text("Shuffle",50+buffer,height-buffer-20);
		
		//randomize button
		fill(250,150,150);
		if(mouseX>100+buffer*4 && mouseX<200+buffer*4 
		&& mouseY>height-buffer-40 && mouseY<height-buffer) {
			fill(250,200,200);
			if(mousePressed)fill(250,100,100);
		}
		rect(150+buffer*4,height-buffer-20,100,40);
		fill(25);
		text("Randomize\r\nValues",150+buffer*4,height-buffer-20);
		
		//sort button
		fill(250,150,150);
		if(mouseX>200+buffer*7 && mouseX<300+buffer*7
		&& mouseY>height-buffer-40 && mouseY<height-buffer) {
			fill(250,200,200);
			if(mousePressed)fill(250,100,100);
		}
		rect(250+buffer*7,height-buffer-20,100,40);
		fill(25);
		text("Bubble Sort",250+buffer*7,height-buffer-20);
		
		//Speed slider
		fill(200);
		text("Speed",300+buffer*10+sliderSize/2,height-buffer-30);
		stroke(240);
		strokeWeight(4);
		line(300+buffer*10,height-buffer-20,300+buffer*10+sliderSize, height-buffer-20);
		noStroke();
		fill(100);
		rect(map(slider,0,10,300+buffer*10,300+buffer*10+sliderSize),height-buffer-20,14,10);
		fill(25);
		rect(map(slider,0,10,300+buffer*10,300+buffer*10+sliderSize),height-buffer-20,10,10);
		
		if(mouseX>300+buffer*10 && mouseX<300+buffer*10+sliderSize && mousePressed
		&& mouseY>height-buffer-30 && mouseY<height-buffer-10 && !operating) {
			slider=(int)map(mouseX,300+buffer*10,300+buffer*10+sliderSize,1,10);
			speed=slider*3;
		}
	}
	
	public boolean sorted() {
		for(int i=0; i<Cars.length-1; i++) {
			if(Cars[i].getValue()>Cars[i+1].getValue()) return false;
		}
		return true;
	}
	
	public void randomizeValues() {
		for(int i=0; i<size; i++) {
			int v= (int) random(min,max);
			if(allowDuplicates==false) {
				while(valueTaken(v,i)) v = (int) random(min,max);
			}
			int r = (int)map(v,min,max,50,5);
			int g = (int)map(v,min,max,100,250);
			int b=210;
			Cars[i] = new Car(v,r,g,b);
			spotsX[i] = (buffer+iter/2)+i*iter;
			Cars[i].setXY(spotsX[i], spotsY);
			
		}
	}
	
	public void shuffle() {
		int a = (int)random(0,size);
		int b = (int)random(0,size);
		while(a==b) b = (int)random(0,size);
		
		int c = (int)random(0,size);
		while(c==b || c==a) c = (int)random(0,size);
		int d = (int)random(0,size);
		while(d==c || d==b || d==a) d = (int)random(0,size);
		
		if(a>b) {
			int temp=a;
			a=b;
			b=temp;
		}
		if(c>d) {
			int temp=c;
			c=d;
			d=temp;
		}
		shuffles++;
		tokens.add(new SwapToken(a,b));
		tokens.add(new SwapToken(c,d));
	}
	
	//sorting algorithms
	public void bubbleSort() {
		if(bubbleIndex==Cars.length-1) bubbleIndex=0;
		for(int i=bubbleIndex; i<Cars.length-1; i++) {
			if (Cars[i].getValue() > Cars[i+1].getValue()){
                tokens.add(new SwapToken(i,i+1));
                bubbleIndex=i+1;
                return;
            }
		}
		if(bubbleIndex==0) {
			message=1;
			messageTime=fadeTime;
			operating=false;
			sort=false;
		}else {
			bubbleIndex=0;
			bubbleSort();
		}
	}
	
	
	
	//sorting algorithms
	
	public void showLot() {
		strokeWeight(4);
		stroke(200);
		line(buffer,buffer,width-buffer,buffer);
		
		for(int i=0; i<=size+1; i++) {
			int x= buffer+i*iter;
			line(x,buffer,x,buffer+spotSize);
		}
	}
	
	public void showCars() {
		strokeWeight(1);
		textSize(10);
		stroke(25);
		for(Car c: Cars) {
			fill(c.r(),c.g(),c.b());
			
			if(c.getDir()==1) {
				pushMatrix();
				if(c.getRotPhase()==1) {
					translate(c.getX()+rotRadius, c.getY());
					rotate(c.getAngle());
					
					rect(rotRadius,carSize.y/2,carSize.x, carSize.y/3,3);
					rect(rotRadius,-carSize.y/2,carSize.x, carSize.y/3,3);
					rect(rotRadius,0,carSize.x, carSize.y,5);
					fill(0);
					pushMatrix();
					translate(rotRadius,0);
					rotate(PI);
					text(c.getValue(),0,0);
					popMatrix();
				}
				if(c.getRotPhase()==2) {
					translate(c.getX()+rotRadius, c.getY()-2*rotRadius);
					rotate(c.getAngle());
					
					rect(-rotRadius,carSize.y/2,carSize.x, carSize.y/3,3);
					rect(-rotRadius,-carSize.y/2,carSize.x, carSize.y/3,3);
					rect(-rotRadius,0,carSize.x, carSize.y,5);
					fill(0);
					pushMatrix();
					translate(-rotRadius,0);
					rotate(PI);
					text(c.getValue(),0,0);
					popMatrix();
				}
				popMatrix();
			}
			if(c.getDir()==-1) {
				pushMatrix();
				if(c.getRotPhase()==1) {
					translate(c.getX()-rotRadius, c.getY());
					rotate(c.getAngle());
					
					rect(-rotRadius,carSize.y/2,carSize.x, carSize.y/3,3);
					rect(-rotRadius,-carSize.y/2,carSize.x, carSize.y/3,3);
					rect(-rotRadius,0,carSize.x, carSize.y,5);
					fill(0);
					pushMatrix();
					translate(-rotRadius,0);
					rotate(PI);
					text(c.getValue(),0,0);
					popMatrix();
				}
				if(c.getRotPhase()==2) {
					translate(c.getX()-rotRadius, c.getY()-2*rotRadius);
					rotate(c.getAngle());
					
					rect(rotRadius,carSize.y/2,carSize.x, carSize.y/3,3);
					rect(rotRadius,-carSize.y/2,carSize.x, carSize.y/3,3);
					rect(rotRadius,0,carSize.x, carSize.y,5);
					fill(0);
					pushMatrix();
					translate(rotRadius,0);
					rotate(PI);
					text(c.getValue(),0,0);
					popMatrix();
				}
				popMatrix();
			}
			
			if(c.getDir()==0) {
				rect(c.getX(),c.getY()-carSize.y/2,carSize.x, carSize.y/3,3);
				rect(c.getX(),c.getY()+carSize.y/2,carSize.x, carSize.y/3,3);
				rect(c.getX(),c.getY(),carSize.x, carSize.y,5);
				fill(0);
				text(c.getValue(),c.getX(),c.getY());
			}
		}
	}
	
	
	public void swap(SwapToken tok) {
		int p = tok.getPhase();
		Car carA=Cars[tok.getCar1Space()];
		Car carB = Cars[tok.getCar2Space()];
		float xa=spotsX[tok.getCar1Space()];
		float xb = spotsX[tok.getCar2Space()];
		int aDir;
		int bDir;
		if(xa>xb) aDir=-1;
		else aDir=1;
		
		bDir=-1*aDir;
		carA.setDir(aDir);
		carB.setDir(bDir);
		
		float easeA = abs(carA.getY()-spotsY-(dist1/2));
		float easeB = abs(carB.getY()-spotsY-(dist2/2));
		
		if(p==1) {
			carA.setRotPhase(1);
			carB.setRotPhase(1);
		if(carA.getY()<yTemp1) carA.updateXY(0, speed-map(easeA,0,dist1-spotsY,0,speed));
		if(carB.getY()<yTemp2) carB.updateXY(0, speed-map(easeB,0,dist2-spotsY,0,speed));
		if(carA.getY()>=yTemp1 && carB.getY()>=yTemp2) tok.setPhase(2);
		}
		
		if(p==2) {
			if(aDir==1) {
				if(carA.getAngle()<95*PI/64) {
				carA.updateAngle(PI/rotSpeed);
				carB.updateAngle(-PI/rotSpeed);
				}else {
					tok.setPhase(3);
				}
			}else {
				if(carA.getAngle()<95*PI/64) {
				carA.updateAngle(-PI/rotSpeed);
				carB.updateAngle(PI/rotSpeed);
				}else {
					tok.setPhase(3);
				}
			}
		}
		
		if(p==3) {
			if(aDir==1) {
				if(carA.getX()<xb-rotRadius*2) {
					carA.updateXY(speed/2, 0);
					carB.updateXY(-speed/2, 0);
				}else {
					carA.setXY(xb-rotRadius*2, carA.getY());
					carB.setXY(xa+rotRadius*2, carB.getY());
					tok.setPhase(4);
				}
			}else {
				if(carB.getX()<xa-rotRadius*2) {
					carB.updateXY(speed/2, 0);
					carA.updateXY(-speed/2, 0);
				}else {
					carA.setXY(xb+rotRadius*2, carA.getY());
					carB.setXY(xa-rotRadius*2, carB.getY());
					tok.setPhase(4);
				}
			}
		}
		
		if(p==4) {
			carA.setRotPhase(2);
			carB.setRotPhase(2);
			if(aDir==1) {
				if(carA.getAngle()>PI) {
				carA.updateAngle(-PI/rotSpeed);
				carB.updateAngle(PI/rotSpeed);
				}else {
					tok.setPhase(5);
				}
			}else {
				if(carA.getAngle()>PI) {
				carA.updateAngle(PI/rotSpeed);
				carB.updateAngle(-PI/rotSpeed);
				}else {
					tok.setPhase(5);
				}
			}
		}
		
		if(p==5) {
			if(carA.getY()>spotsY+2*rotRadius) carA.updateXY(0, -speed+map(easeA,0,dist1-spotsY,0,speed));
			if(carB.getY()>spotsY+2*rotRadius) carB.updateXY(0, -speed+map(easeB,0,dist2-spotsY,0,speed));
			if(carA.getY()<=spotsY+2*rotRadius && carB.getY()<=spotsY+2*rotRadius) {
				
				carA.setXY(xb, spotsY);
				carB.setXY(xa, spotsY);
				carA.setRotPhase(0);
				carB.setRotPhase(0);
				carA.setDir(0);
				carB.setDir(0);
				
				Car temp = carA;
				Cars[tok.getCar1Space()] = carB;
				Cars[tok.getCar2Space()] = temp;
				tokens.remove(tok);
				printVals();
				
			}
		}
		
	}
	
	
	public void printVals() {
		System.out.println();
		for(int i=0; i<Cars.length; i++) {
			System.out.print(Cars[i].getValue()+", ");
		}
	}
	
	public boolean valueTaken(int v, int index) {
		for(int i=0; i<index; i++) {
			if(Cars[i].getValue()==v)return true;
		}
		return false;
	}

}
