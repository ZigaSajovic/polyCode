package polyCode.util;

import java.util.ArrayList;

import polyCode.machine.Command;
import polyCode.machine.Variable;
import polyCode.math.Vec;

public class Util {
	
	public static int round(double x){
		int out=(int)x;
		if(x-out<0.5)return out;
		else return out+1;
	}
	
	public static ArrayList<Double> copy(ArrayList<Double> a){
		ArrayList<Double> out=new ArrayList<Double>();
		for(int i=0;i<a.size();i++){
			out.add(a.get(i));
		}
		return out;
	}
	
	public static ArrayList<ArrayList<Integer>>getSym(ArrayList<double[]>means, char c){
		if(c!='x'&&c!='y') return null;
		ArrayList<ArrayList<Integer>> base=new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<means.size();i++){
			if(containsMul(i, base)) continue;
			ArrayList<Integer> temp=new ArrayList<Integer>();
			temp.add(i);
			double min=-1;
			int index=-1;
			for(int j=0;j<means.size();j++){
				if(containsMul(j, base)) continue;
				if(i==j)continue;
				double[] tmp={means.get(j)[0],means.get(j)[1]};
				if(c=='x') tmp[0]=-tmp[0];
				else if(c=='y') tmp[1]=-tmp[1];
				double dot=Vec.dotProd(means.get(i), tmp);
				if(dot>=0.99&&dot>min){
					min=dot;
					index=j;
				}
			}
			if(index!=-1){
				temp.add(index);
			}
			base.add(temp);
		}
		return base;
	}
	
	public static ArrayList<ArrayList<Integer>>getNegs(ArrayList<double[]>means){
		ArrayList<ArrayList<Integer>> base=new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<means.size();i++){
			if(containsMul(i, base)) continue;
			ArrayList<Integer> temp=new ArrayList<Integer>();
			temp.add(i);
			double min=1;
			int index=-1;
			for(int j=0;j<means.size();j++){
				if(containsMul(j, base)) continue;
				if(i==j)continue;
				double dot=Vec.dotProd(means.get(i), means.get(j));
				if(dot<=-0.99&&dot<min){
					min=dot;
					index=j;
				}
			}
			if(index!=-1){
				temp.add(index);
			}
			base.add(temp);
		}
		return base;
	}
	
	public static boolean contains(int n,ArrayList<Integer> a){
		for(int i=0;i<a.size();i++){
			if(a.get(i)==n) return true;
		}
		return false;
	}
	
	public static boolean contains(String n,ArrayList<String> a){
		for(int i=0;i<a.size();i++){
			if(a.get(i).equals(n)) return true;
		}
		return false;
	}
	
	public static boolean containsMul(int n,ArrayList<ArrayList<Integer>> a){
		for(int i=0;i<a.size();i++){
			if(contains(n,a.get(i))) return true;
		}
		return false;
	}
	
	public static double[] intToDoubleVec(int[] input, int size){
		double[] temp=new double[size];
		for(int i=0;i<size;i++){
			temp[i]=(double)input[i];
		}
		return temp;
	}
	
	public static int locate(int n,ArrayList<ArrayList<Integer>> a){
		for(int i=0;i<a.size();i++){
			if(contains(n,a.get(i))) return i;
		}
		return -1;
	}
	
	public static ArrayList<double[]> copyVec(ArrayList<double[]> a){
		ArrayList<double[]> out=new ArrayList<double[]>();
		for(int i=0;i<a.size();i++){
			double[] temp=new double[2];
			temp[0]=a.get(i)[0];
			temp[1]=a.get(i)[1];
			out.add(temp);
		}
		return out;
	}
	
	public static double[] copyTab(double[] a){
		double[] temp=new double[a.length];
		for(int i=0;i<a.length;i++){
			temp[i]=a[i];
		}
		return temp;
	}
	
	public static int[] copyTab(int[] a){
		int[] temp=new int[a.length];
		for(int i=0;i<a.length;i++){
			temp[i]=a[i];
		}
		return temp;
	}
	
	public static ArrayList<Command> copyProg(ArrayList<Command> a){
		ArrayList<Command> out=new ArrayList<Command>();
		for(int i=0;i<a.size();i++){
			Command c=new Command(a.get(i).getType(), null);
			c.setVars(copyVars(a.get(i).getVars()));
			out.add(c);
		}
		return out;
	}
	
	public static ArrayList<Variable> copyVars(ArrayList<Variable> v){
		if(v==null) return null;
		ArrayList<Variable> out=new ArrayList<Variable>();
		for(int i=0;i<v.size();i++){
			out.add(v.get(i));
		}
		return out;
	}
	
	public static void printListInt(ArrayList<Integer> a){
		for(int i=0;i<a.size();i++){
			System.out.print(a.get(i));
			if(i!=a.size()-1)System.out.print(", ");
			else System.out.println();
		}
	}
	
	public static void printListString(ArrayList<String> a){
		for(int i=0;i<a.size();i++){
			System.out.print(a.get(i));
			if(i!=a.size()-1)System.out.print(", ");
			else System.out.println();
		}
	}
	
	public static void printListDouble(ArrayList<Double> a){
		for(int i=0;i<a.size();i++){
			System.out.print(a.get(i));
			if(i!=a.size()-1)System.out.print(", ");
			else System.out.println();
		}
		
	}
	
	public static void printListIntMul(ArrayList<ArrayList<Integer>> list){
		for(int i=0;i<list.size();i++){
			printListInt(list.get(i));
		}
	}
	
	public static void printListVec(ArrayList<double[]> a){
		for(int i=0;i<a.size();i++){
			System.out.print("( "+a.get(i)[0]+" , "+a.get(i)[1]+" )");
			if(i!=a.size()-1)System.out.print(", ");
			else System.out.println();
		}
	}
	
	public static void printListVecInt(ArrayList<int[]> a){
		for(int i=0;i<a.size();i++){
			System.out.print("( "+a.get(i)[0]+" , "+a.get(i)[1]+" )");
			if(i!=a.size()-1)System.out.print(", ");
			else System.out.println();
		}
	}
	
	public static int[] doubleToInt(double[] in){
		int[] out=new int[in.length];
		for(int i=0;i<in.length;i++)out[i]=(int)in[i];
		return out;
	}
	
	public static double[] intToDouble(int[] in){
		double[] out=new double[in.length];
		for(int i=0;i<in.length;i++)out[i]=(double)in[i];
		return out;
	}
}
