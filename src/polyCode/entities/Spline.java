package polyCode.entities;

import java.util.ArrayList;

import polyCode.math.EquationSolver;

public class Spline {
	ArrayList<int[]> graph;
	ArrayList<Integer> separator;
	ArrayList<int[]> spline;
	ArrayList<Integer> splineSeperator;
	
	double[][][] equations;
	
	public Spline(ArrayList<int[]> graph,ArrayList<Integer> separator){
		this.graph=graph;
		this.separator=separator;
	}
	
	public void setUpEquations(int startSeperator, int endSeperator){
		int size=separator.get(endSeperator)-separator.get(startSeperator)-1;
		equations=new double[2][2*(size-1)][2*(size-1)+1];
		if(graph.get(separator.get(startSeperator)+1)[0]==graph.get(separator.get(endSeperator)-1)[0]&&graph.get(separator.get(startSeperator)+1)[1]==graph.get(separator.get(endSeperator)-1)[1]){
			equations[0][0][0]=1;
			equations[0][0][2*(size-1)-1]=1;
			equations[0][0][2*(size-1)]=2*graph.get(separator.get(startSeperator)+1)[0];
			equations[1][0][0]=1;
			equations[1][0][2*(size-1)-1]=1;
			equations[1][0][2*(size-1)]=2*graph.get(separator.get(startSeperator)+1)[1];
		}
		else{
			equations[0][0][0]=2;
			equations[0][0][1]=-1;
			equations[0][0][2*(size-1)]=graph.get(separator.get(startSeperator)+1)[0];
			equations[1][0][0]=2;
			equations[1][0][1]=-1;
			equations[1][0][2*(size-1)]=graph.get(separator.get(startSeperator)+1)[1];
		}
		int a=1;
		int b=0;
		int line=1;
		for(int i=1;i<size-1;i++){
			for(int j=0;j<2;++j){
				equations[j][line][a]=1;
				equations[j][line][a+1]=1;
				equations[j][line][2*(size-1)]=2*graph.get(separator.get(startSeperator)+1+i)[j];
				equations[j][line+1][b]=-1;
				equations[j][line+1][b+1]=2;
				equations[j][line+1][b+2]=-2;
				equations[j][line+1][b+3]=1;
				equations[j][line+1][2*(size-1)]=0;
			}
			line+=2;
			a+=2;
			b+=2;
		}
		for(int j=0;j<2;++j){
			if(graph.get(separator.get(startSeperator)+1)[0]==graph.get(separator.get(endSeperator)-1)[0]&&graph.get(separator.get(startSeperator)+1)[1]==graph.get(separator.get(endSeperator)-1)[1]){
				equations[j][line][0]=-2;
				equations[j][line][1]=1;
				equations[j][line][2*(size-1)-2]=-1;
				equations[j][line][2*(size-1)-1]=2;
				equations[j][line][2*(size-1)]=0;
			}
			else{
				equations[j][line][2*(size-1)-2]=-1;
				equations[j][line][2*(size-1)-1]=2;
				equations[j][line][2*(size-1)]=graph.get(separator.get(endSeperator)-1)[j];
			}
		}
	}
	
	public ArrayList<int[]> spline(){
		spline=new ArrayList<int[]>();
		splineSeperator=new ArrayList<Integer>();
		addSplineSeperator();
		for(int i=1;i<separator.size();i++){
			calculatPoints(i-1, i);
			addSplineSeperator();
		}
		return spline;
	}
	
	public void calculatPoints(int startIndex, int endIndex){
		spline.add(graph.get(separator.get(startIndex)+1));
		setUpEquations(startIndex, endIndex);
		int size=separator.get(endIndex)-separator.get(startIndex)-1;
		EquationSolver resevalec =new EquationSolver(equations[0], 2*(size-1), 2*(size-1));
		double[] resitevX=resevalec.solve();
		EquationSolver resevalecY =new EquationSolver(equations[1], 2*(size-1), 2*(size-1));
		double[] resitevY=resevalecY.solve();
		for(int i=0;i<(size-1);i++){
			int[] temp=new int[2];
			temp[0]=round(resitevX[2*i]);
			temp[1]=round(resitevY[2*i]);
			spline.add(temp);			
			int[] temp2=new int[2];
			temp2[0]=round(resitevX[2*i+1]);
			temp2[1]=round(resitevY[2*i+1]);
			spline.add(temp2);
			spline.add(graph.get(separator.get(startIndex)+i+2));
		}
	}
	
	private void addSplineSeperator(){
		int[] out=new int[2];
		out[0]=-1;
		out[1]=-1;
		this.spline.add(out);
		splineSeperator.add(spline.size()-1);
	}
	
	private int round(double x){
		double a=(int)x+0.5;
		if(x>a)return (int)x+1;
		else return (int)x;
	}
	
	public void print(){
		for(int i=0;i<spline.size();i++){
			System.out.println("( "+spline.get(i)[0]+" , "+spline.get(i)[1]+" )");
		}
	}
	
	public void printGraph(){
		for(int i=0;i<graph.size();i++){
			System.out.println("( "+graph.get(i)[0]+" , "+graph.get(i)[1]+" )");
		}
	}
}
