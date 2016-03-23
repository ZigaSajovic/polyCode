package polyCode.machine;

import java.util.ArrayList;

import polyCode.util.Util;


public class Compressor {
	static ArrayList<Command> prog;
	
	public static ArrayList<Command> compressLoop(ArrayList<Command> p){
		prog=Util.copyProg(p);
		for(int i=0;i<p.size();i++){
			
		}
		return null;
	}
	
	public static ArrayList<Command> compressLoops(ArrayList<Command> p){
		prog=p;
		while(looper());
		return prog;
	}
	
	public static boolean looper(){
		int[] temp=maxEqual();
		if(temp!=null){
			Command tempC=new Command("loop", new Variable(String.valueOf(temp[0]/temp[2]),"/"));
			tempC.subProgram=new ArrayList<Command>();
			tempC.subProgram.add(prog.get(temp[1]));
			prog.set(temp[1], tempC);
			//System.out.println(temp[0]+" , "+temp[1]+" , "+temp[2]);
			int j=temp[1]+1;
			for(int i=0;i<temp[0]-1;i++){
				if(i<temp[2]-1)tempC.subProgram.add(prog.get(j));
				prog.remove(j);
			}
			return true;
		}
		return false;
	}
	
	
	private static int[] maxEqual(){
		int[] max={0,-1,-1};
		for(int i=1;i<prog.size();i++){
			int[] temp=findEquals(i);
			if(temp==null) continue;
			if(max[0]<temp[0]) {
				max[0]=temp[0];
				max[1]=temp[1];
				max[2]=i;
			}
		}
		if(max[1]==-1)return null;
		return max;
	}
	
	private static int[] findEquals(int length){
		int max=0;
		int index=-1;
		for(int i=0;i<prog.size();i++){
			int count=1;
			for(int j=i+length;j<=prog.size()-length;j+=length){
				if(!equal(i,j,length))break;
				count+=1;
			}
			if((count-1)*(length)>max){
				max=(count)*(length);
				index=i;
			}
		}
		if(index==-1) return null;
		int[] out=new int[2];
		out[0]=max;
		out[1]=index;
		return out;
	}
	

	
	private static boolean equal(int a, int b, int length){
		for(int i=0;i<length;i++){
			if(!prog.get(a+i).equal(prog.get(b+i),1)) return false;
		}
		return true;
	}
}
