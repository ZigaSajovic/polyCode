package polyCode.handlers;

import java.util.ArrayList;

public class Linker {
	ArrayList<int[]> graph;
	ArrayList<Integer> seperator;
	
	double error=20;
	
	public Linker(ArrayList<int[]> graf, ArrayList<Integer> seperator){
		this.graph=graf;
		this.seperator=seperator;
	}
	
	private double dist(int[]a, int[] b){
		return Math.sqrt((b[0]-a[0])*(b[0]-a[0]) + (b[1]-a[1])*(b[1]-a[1]));
	}
	
	private int round(double x){
		double a=(int)x+0.5;
		if(x>a)return (int)x+1;
		else return (int)x;
	}
	
	private int[] avg(int[]a, int[]b){
		int[]t=new int[2];
		t[0]=round((a[0]+b[0])/2);
		t[1]=round((a[1]+b[1])/2);
		return t;
	}
	
	private void link(int a, int b){
		if(dist(graph.get(a), graph.get(b))<error){
			int[] avg=avg(graph.get(a),graph.get(b));
			graph.set(a, avg);
			graph.set(b, avg);
		}
	}
	
	
	private void cycle(){
		for(int i=1;i<seperator.size();i++){
			link(seperator.get(i-1)+1,seperator.get(i)-1);
		}
	}
	
	private boolean beenDone(ArrayList<Integer> obdelani,int index){
		for(int i=0;i<obdelani.size();i++){
			if(obdelani.get(i)==index) return true;
		}
		return false;
	}
	
	private void cyclesInGraph(){
		ArrayList<Integer> obdelani=new ArrayList<Integer>();
		for(int i=0;i<graph.size();i++){
			ArrayList<Integer> indexi=new ArrayList<Integer>();
			if(beenDone(obdelani,i)||graph.get(i)[0]==-1) continue;
			int sumX=graph.get(i)[0];
			int sumY=graph.get(i)[1];
			int count=1;
			obdelani.add(i);
			indexi.add(i);
			for(int j=0;j<graph.size();j++){
				if(i==j||graph.get(i)[0]==-1)continue;
				if(dist(graph.get(i), graph.get(j))<error){
					indexi.add(j);
					obdelani.add(j);
					sumX+=graph.get(j)[0];
					sumY+=graph.get(j)[1];
					count+=1;
				}
			}
			sumX/=count;
			sumY/=count;
			for(int j=0;j<indexi.size();j++){
				int[]temp=new int[2];
				temp[0]=sumX;
				temp[1]=sumY;
				int index=indexi.get(j);
				graph.set(index, temp);
			}
		}
	}
	
	public ArrayList<int[]> getGraph(){
		cycle();
		cyclesInGraph();
		return graph;
	}
}
