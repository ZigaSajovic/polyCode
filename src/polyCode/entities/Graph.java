package polyCode.entities;

import java.util.ArrayList;

public class Graph {
	
	ArrayList<int[]> graph=new ArrayList<int[]>();
	double errorCurvature=20;
	
	ArrayList<int[]> input;
	ArrayList<Integer> seperator;
	
	
	public Graph(ArrayList<int[]> input){
		this.input=input;
		this.seperator=new ArrayList<Integer>();
		detect();
	}
	
	public Graph(ArrayList<int[]> input, double errorCurvature){
		this.input=input;
		this.errorCurvature=errorCurvature;
		this.seperator=new ArrayList<Integer>();
		detect();
	}
	
	private boolean dirShift(int i){
		int dy1=input.get(i)[1]-input.get(i-1)[1];
		int dx1=input.get(i)[0]-input.get(i-1)[0];
		int dy2=input.get(i+1)[1]-input.get(i)[1];
		int dx2=input.get(i+1)[0]-input.get(i)[0];
		if(dx1>=0&&dx2<0) return true;
		if(dx2>=0&&dx1<0) return true;
		if(dy1>=0&&dy2<0) return true;
		if(dy2>=0&&dy1<0) return true;
		return false;
	}
	
	
	private double curvature(int i){
		double fi1;
		double fi2;
		int dy1=input.get(i)[1]-input.get(i-1)[1];
		int dx1=input.get(i)[0]-input.get(i-1)[0];
		int dy2=input.get(i+1)[1]-input.get(i)[1];
		int dx2=input.get(i+1)[0]-input.get(i)[0];
		
		if(dx1==0){
			if(dy1>0)fi1=Math.toRadians(90);
			else fi1=Math.toRadians(-90);
		}
		else fi1=(dy1)/(dx1);
		if(dx2==0){
			if(dy2>0)fi2=Math.toRadians(90);
			else fi2=Math.toRadians(-90);
		}
		else fi2=(dy2)/(dx2);
		
		return Math.toDegrees(Math.atan(fi2)-Math.atan(fi1));
	}
	
	private void detect(){
		addGraphSeperator();
		addVertex(1);
		
		for(int i=2;i<input.size()-3;i++){
			if(input.get(i+1)[0]==-1){
				addVertex(i);
				addGraphSeperator();
				addVertex(i+2);
				i+=2;
			}
			if(dirShift(i)||Math.abs(curvature(i))>=errorCurvature){
				addVertex(i);
			}
		}
		
		addVertex(input.size()-2);
		addGraphSeperator();
	}
	
	private void addVertex(int i){
		int[] out=new int[2];
		out[0]=input.get(i)[0];
		out[1]=input.get(i)[1];
		graph.add(out);
	}
	
	private void addGraphSeperator(){
		int[] out=new int[2];
		out[0]=-1;
		out[1]=-1;
		graph.add(out);
		seperator.add(graph.size()-1);
	}
	
	public void print(){
		for(int i=0;i<graph.size();i++){
			System.out.println("( "+graph.get(i)[0]+" , "+graph.get(i)[0]+" )");
		}
	}
	
	public ArrayList<int[]> getGraph(){
		return graph;
	}
	
	public ArrayList<Integer> vrniSeperator(){
		return seperator;
	}
}
