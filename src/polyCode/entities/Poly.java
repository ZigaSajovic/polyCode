package polyCode.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import polyCode.handlers.PolyHandler;
import polyCode.machine.Command;
import polyCode.machine.Comparator;
import polyCode.machine.Compiler;
import polyCode.machine.Compressor;
import polyCode.math.Matrix;
import polyCode.math.Vec;
import polyCode.util.Util;

public class Poly extends DrawPad{
	private static final long serialVersionUID = 1L;
	boolean penDown=false;
	boolean seen=true;
	boolean beenPlaced=false;
	
	boolean compile=true;
	
	Color draw=Color.RED;
	Color vertices=Color.BLUE;

	PolyGraph current=null;
	ArrayList<PolyGraph> polysToDraw=new ArrayList<PolyGraph>();
	ArrayList<PolyGraph> toCompile=new ArrayList<PolyGraph>();
	ArrayList<Command> functions=new ArrayList<Command>();
	int functionCount=0;
	
	private static int[] location;
	static double[] unitDirVec;
	int[][] polyX={{-15,15},{0,0},{-15,-15},{30,0}};
	
	
	public Poly(){
		super();
		unitDirVec=new double[2];
		unitDirVec[0]=1;
		unitDirVec[1]=0;
		
	}
	public void initial(){
		location=new int[2];
		setPolyLocation((int)(getSize().width/2),(int)(getSize().height/2));
		int sumx=0;
		int sumy=0;
		for(int i=0;i<polyX.length;i++){
			sumx+=polyX[i][0];
			sumy+=polyX[i][1];
		}
		sumx/=polyX.length;
		sumy/=polyX.length;
		for(int i=0;i<polyX.length;i++){
			polyX[i][0]-=sumx;
			polyX[i][1]-=sumy;
		}
		if(image==null){
			image=createImage(getSize().width,getSize().height);
			g2=(Graphics2D) image.getGraphics();
			g2.setStroke(new BasicStroke(2));
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		render();
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	public void changeColor(Color c){
		draw=c;
	}
	
	public void changeColorVertex(Color c){
		vertices=c;
	}
	
	public void resetColor(){
		draw=Color.RED;
		vertices=Color.BLUE;
	}
	
	public boolean beenDrawn(){
		return vertex.size()!=0;
	}
	
	public void printGraph(){
		System.out.println("Print graf");
		for(int i=0;i<graph.size();i++){
			System.out.println("( "+graph.get(i)[0]+" , "+graph.get(i)[1]+" )");
		}
		System.out.println("End print graf");
	}
	
	public void printSeperator(){
		System.out.println("Print seperator");
		for(int i=0;i<seperator.size();i++){
			System.out.println("( "+seperator.get(i)+" )");
		}
		System.out.println("End print seperator");
	}
	
	public void compile(){
		super.compile();
		generatePolys();
		if(Display.polyCode.isSelected()){
			optimizePoly();
			updateGraph();			
		}
		compilePolysToFunctions(true);
		seperator.clear();
		graph.clear();
		polysToDraw.addAll(toCompile);
		toCompile.clear();
		vertex.clear();
	}
	
	public void printFunctions(){
		if(functions==null) return;
		Display.printTerminal("%--------------------------------------------\n");
		for(int i=0;i<functions.size();i++){
			functions.get(i).printAll(0);
			Display.printTerminal("%--------------------------------------------\n");
		}
	}
	
	public Command getFunction(String s){
		for(int i=0;i<functions.size();i++){
			if(functions.get(i).getType().equals(s)) return functions.get(i);
		}
		return null;
	}
	
	public static ArrayList<int[]> getSpline(){
		return spline;
	}
	
	public void addFunction(Command c){
		functions.add(c);
		functionCount++;
	}
	
	public void setPolyLocation(double x, double y){
		if(current!=null) detachPoly();
		location[0]=(int)x;
		location[1]=(int)y;
		locationCheck();
	}

	
	private void compilePolyToFunction(PolyGraph poly, boolean print){
		Command temp=Compiler.compile(poly,functionCount);
		ArrayList<Command> prog=temp.getSubProgram();
		prog=Compressor.compressLoops(prog);
		temp.setSubProgram(prog);
		functionCount++;
		functions.add(temp);
		if(print) {
			temp.printAll(0);
			Display.printTerminal("%--------------------------------------------\n");
		}
	}
	
	private void compilePolysToFunctions(boolean print){
		if(print) Display.printTerminal("%--------------------------------------------\n");
		for(int i=0;i<toCompile.size();i++){
			compilePolyToFunction(toCompile.get(i), print);
		}
	}
	
	public void updateGraph(){
		ArrayList<int[]> out=new ArrayList<int[]>();
		for(int i=0;i<toCompile.size();i++){
			ArrayList<int[]> temp=toCompile.get(i).getGraph();
			int[] sep=new int[2];
			sep[0]=-1;
			sep[1]=-1;
			out.add(sep);
			for(int j=0;j<temp.size();j++)out.add(temp.get(j));
		}
		graph=out;
	}
	
	public void optimizePoly(){
		for(int i=0;i<toCompile.size();i++){
			if(!toCompile.get(i).closed){
				double[] tmp=Vec.prod(toCompile.get(i).getEndPointRelative(),-1);
				toCompile.get(i).addLength(Vec.normDouble(tmp));
				toCompile.get(i).addVec(Vec.normalize(tmp, 2));
			}
			PolyHandler temp=new PolyHandler(toCompile.get(i));
			PolyGraph tempPoly=new PolyGraph(toCompile.get(i).getLengths(), toCompile.get(i).getUnitVectors(), toCompile.get(i).getStart(), toCompile.get(i).getCenter(), toCompile.get(i).isClosed());
			PolyGraph tempPoly2=temp.updatePoly();
			if(Comparator.diff(tempPoly, tempPoly2)){
				if(!toCompile.get(i).closed){
					tempPoly2.lengths.remove(tempPoly2.lengths.size()-1);
					tempPoly2.unitVectors.remove(tempPoly2.unitVectors.size()-1);
				}
				tempPoly2.updateCenter();
				toCompile.set(i,tempPoly2);
				toCompile.get(i).generateAngles();
			}
			toCompile.get(i).uniformColor(draw);
		}
	}
	
	public void generatePolys(){
		toCompile=new ArrayList<PolyGraph>();
		for(int i=0;i<seperator.size()-1;i++){
			toCompile.add(new PolyGraph(graph, seperator,i,i+1));
		}
	}
	
	public void drawPolys(){
		for(int i=0;i<polysToDraw.size();i++){
			drawPolyObject(polysToDraw.get(i));
		}
	}
	
	public void drawPolyObject(PolyGraph a){
		double[] x=new double[2];
		Color temp=g2.getColor();
		g2.setColor(draw);
		x[0]=a.start[0];
		x[1]=a.start[1];
		for(int i=0;i<a.lengths.size();i++){
			if(a.colors!=null&&a.colors.size()==a.lengths.size()){
				g2.setColor(a.colors.get(i));
			}
			double[] y=Vec.sumD(x, Vec.prod(a.unitVectors.get(i), a.lengths.get(i)));
			g2.drawLine((int)x[0], (int)x[1], (int)y[0], (int)y[1]);
			x=y;
		}
		g2.setColor(temp);
	}
	
	
	public void drawPolysVertex(){
		for(int i=0;i<polysToDraw.size();i++){
			drawPolyVertex(polysToDraw.get(i));
		}
	}
	
	public void drawPoly(){
		int[] x = new int[polyX.length];
		int[] y = new int[polyX.length];
		for(int i=0;i<polyX.length;i++){
			x[i]=polyX[i][0]+location[0];
			y[i]=polyX[i][1]+location[1];
		}
		g2.fillPolygon(x, y, polyX.length);
	}
	
	public void render(){
		clearScreen();
		drawPolys();
		if(Display.drawVertices.isSelected()) drawPolysVertex();
		if(current!=null) {
			drawPolyObject(current);
			if(Display.drawVertices.isSelected()) drawPolyVertex(current);
		}
		if(vertex.size()!=0)drawInitialDrawing();
		drawPoly();
		repaint();
	}
	
	public void drawPolyVertex(PolyGraph poly){
		double[] x=new double[2];
		Color temp=g2.getColor();
		g2.setColor(vertices);
		x[0]=poly.start[0];
		x[1]=poly.start[1];
		g2.drawOval(Util.round(x[0])-5, Util.round(x[1])-5, 10, 10);
		int n=poly.lengths.size();
		if(poly.closed)n-=1;
		for(int i=0;i<n;i++){
			x=Vec.sumD(x, Vec.prod(poly.unitVectors.get(i), poly.lengths.get(i)));
			g2.drawOval(Util.round(x[0])-5, Util.round(x[1])-5, 10, 10);
		}
		g2.setColor(temp);
	}
	
	public void penDown(){
		penDown=true;
	}
	
	public void penUp(){
		penDown=false;
	}
	
	public void viden(){
		seen=true;
	}
	
	public void neViden(){
		seen=false;
	}
	
	
	public void setLoc(int x, int y){
		if(current!=null) detachPoly();
		location[0]=x;
		location[1]=y;
		locationCheck();
	}
	
	public void locationCheck(){
		if(location[0]<30)location[0]=30;
		else if(location[0]-30>getWidth())location[0]=getWidth()-30;
		if(location[1]<30)location[1]=30;
		else if(location[1]-30>getHeight())location[1]=getHeight()-30;
	}
	
	
	public void rotatePoly(double kot){
		Matrix.setRotMatrixDeg(kot);
		unitDirVec=Matrix.prodDouble(unitDirVec);
		
		for(int i=0;i<polyX.length;i++){
			polyX[i]=Matrix.prodInt(polyX[i]);
		}
	}
	
	
	public void setRot(double deg){
		double[] tempDir=new double[2];
		tempDir[0]=Math.cos(Math.toRadians(deg));
		tempDir[1]=Math.sin(Math.toRadians(deg));
		double degree=Math.toDegrees(Math.acos(Vec.dotProd(tempDir, unitDirVec))); 
		if(unitDirVec[0]*tempDir[1]-unitDirVec[1]*tempDir[0]<0)degree*=-1;
		rotatePoly(degree);
	}
	
	public void setRot(double[] temp){
		double degree=Math.toDegrees(Math.acos(Vec.dotProd(temp, unitDirVec))); 
		if(unitDirVec[0]*temp[1]-unitDirVec[1]*temp[0]>0)degree*=-1;
		rotatePoly(degree);
	}
	
	public void setPolyRotation(double x, double y){
		double[] tmp={x,y};
		setRot(Vec.normalize(tmp,2));
	}
	
	public void movePoly(double length){	
		int newX=location[0]+ (int)(unitDirVec[0]*length);
		int newY=location[1]+ (int)(unitDirVec[1]*length);
		if(newX<30)newX=30;
		else if(newX-30>getWidth())newX=getWidth()-30;
		if(newY<30)newY=30;
		else if(newY-30>getHeight())newY=getHeight()-30;
		double dist=Math.sqrt(Math.pow((newX-location[0]), 2)+Math.pow((newY-location[1]), 2));
		if(penDown){
			if(current==null) createPoly();
			current.lengths.add(dist);
			current.colors.add(draw);
			current.unitVectors.add(Util.copyTab(unitDirVec));
		}
		else if(!penDown&&current!=null)detachPoly();
		location[0]=newX;
		location[1]=newY;
	}
	
	public void setCompile(boolean setting){
		compile=setting;
	}
	
	public void moveRotate(double dolzina, double kot){
		rotatePoly(kot);
		movePoly(dolzina);
	}
	
	public int[] getPolyLocation() {
		return location;
	}
	public void setPolyLocation(int[] location) {
		if(current!=null) detachPoly();
		Poly.location = location;
		locationCheck();
	}
	
	public void detachPoly(){
		polysToDraw.add(current);
		if(current.start[0]==location[0]&&current.start[1]==location[1])current.closed=true;
		current.generateAngles();
		if(compile){
			Display.printTerminal("%--------------------------------------------\n");
			compilePolyToFunction(current, true);
		}
		else compile=true;
		current=null;
	}
	
	public double getPolyRotation(){
		return Math.toDegrees(Math.acos(unitDirVec[0]));
	}
	
	public void createPoly(){
		current=new PolyGraph(Util.copyTab(location));
	}
	
	public void clear(){
		vertex.clear();
		toCompile.clear();
		polysToDraw.clear();
		current=null;
		clearScreen();
		render();
	}
}
