package polyCode.handlers;

import java.util.ArrayList;

public class Grid {
	
	static ArrayList<int[]> vertex;
	
	static double error=20;
	
	public static void align(){
		align(0);
		align(1);
	}
	
	public static ArrayList<int[]> align(ArrayList<int[]> input){
		vertex=input;
		align(0);
		align(1);
		return vertex;
	}

	
	public static void setVertex(ArrayList<int[]> vertexIn){
		vertex=vertexIn;
	}
	
	
	private static boolean beenDone(ArrayList<Integer> obdelani,int index){
		for(int i=0;i<obdelani.size();i++){
			if(obdelani.get(i)==index) return true;
		}
		return false;
	}
	
	public static void print(){
		System.out.println("\n");
		
		for(int i=0;i<vertex.size();i++){
			System.out.println("( "+vertex.get(i)[0]+", "+vertex.get(i)[1]+")");
		}
		System.out.println("\n");
	}
	
	private static void align(int Index){
		ArrayList<Integer> beenDone=new ArrayList<Integer>();
		for(int i=0;i<vertex.size();i++){
			if(vertex.get(i)[0]==-1||beenDone(beenDone,i)) continue;
			ArrayList<Integer> indexi=new ArrayList<Integer>();
			int sum=vertex.get(i)[Index];
			int count=1;
			beenDone.add(i);
			indexi.add(i);
			for(int j=0;j<vertex.size();j++){
				if(i==j||vertex.get(j)[0]==-1)continue;
				if(Math.abs(vertex.get(i)[Index]- vertex.get(j)[Index])<error){
					indexi.add(j);
					beenDone.add(j);
					sum+=vertex.get(j)[Index];
					count+=1;
				}
			}
			sum/=count;
			for(int j=0;j<indexi.size();j++){
				int[]temp=new int[2];
				temp[Index]=sum;
				int index=indexi.get(j);
				if(Index==1)temp[0]=vertex.get(index)[0];
				else temp[1]=vertex.get(index)[1];
				vertex.set(index, temp);
			}
		}
	}
	
	
	public static ArrayList<int[]> getVertex(){
		return vertex;
	}
}
