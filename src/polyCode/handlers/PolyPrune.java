package polyCode.handlers;

import java.util.ArrayList;

import polyCode.math.Distance;



public class PolyPrune {	
	ArrayList<int[]> input;
	ArrayList<Integer> seperator;
	ArrayList<int[]> output;
	ArrayList<Integer> outputSeperator;
	
	public static double error=30;
	
	public PolyPrune(ArrayList<int[]> input,ArrayList<Integer> seperator){
		this.seperator=seperator;
		this.input=input;
		prune();
	}
	
	public ArrayList<int[]> getGraph(){
		return output;
	}
	
	public ArrayList<Integer> getSeparator(){
		return outputSeperator;
	}
	
	public void prune(){
		output=new ArrayList<int[]>();
		outputSeperator=new  ArrayList<Integer>();
		addGraphSeperator();
		
		for(int i=0;i<seperator.size()-1;i++){
			output=merge(output,prune(seperator.get(i)+1,seperator.get(i+1)-1),0);
			addGraphSeperator();
		}
	}
	
	private ArrayList<int[]> prune(int first, int last){
		double max=0;
		int index=0;
		
		for(int i=first+1;i<last;i++){
			double dist=Distance.pointToLine(input.get(first), input.get(last), input.get(i), true);
			if(dist>max){
				index=i;
				max=dist;
			}
		}
		
		if(max>=error){
			ArrayList<int[]> left = prune(first,index);
			ArrayList<int[]> right = prune(index,last);
			return merge(left,right,1);
		}
		else return toFro(first,last);
	}
	
	
	private ArrayList<int[]> merge(ArrayList<int[]> a, ArrayList<int[]> b, int zamik){
		ArrayList<int[]> out=new ArrayList<int[]>();
		
		for(int i=0;i<a.size();i++){
			out.add(a.get(i));
		}
		for(int i=zamik;i<b.size();i++){
			out.add(b.get(i));
		}
		return out;
	}
	
	private ArrayList<int[]> toFro(int first, int next){
		ArrayList<int[]> out=new ArrayList<int[]>();
		out.add(input.get(first));
		out.add(input.get(next));
		return out;
	}
	
	private void addGraphSeperator(){
		int[] out=new int[2];
		out[0]=-1;
		out[1]=-1;
		output.add(out);
		outputSeperator.add(output.size()-1);
	}
	
	public void print(){
		System.out.println("\n");
		
		for(int i=0;i<output.size();i++){
			System.out.println("( "+output.get(i)[0]+", "+output.get(i)[1]+")");
		}
		System.out.println("\n");
	}
}

