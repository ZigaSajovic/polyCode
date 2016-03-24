package polyCode.entities;

import java.awt.Color;
import java.util.ArrayList;

import polyCode.math.Stat;
import polyCode.math.Vec;

public class PolyGraph {
	
	ArrayList<double[]> unitVectors;
	ArrayList<Double> lengths;
	ArrayList<Double> angles;
	ArrayList<Color> colors;
	
	int[] start;
	boolean closed;
	
	int[] center;
	
	public PolyGraph(int[] start){
		this.start=start;
		this.angles=new ArrayList<Double>();
		unitVectors=new ArrayList<double[]>();
		lengths=new ArrayList<Double>();
		colors=new ArrayList<Color>();
	}
	
	public void addVec(double[] vec){
		unitVectors.add(vec);
	}
	
	public void addLength(double r){
		lengths.add(r);
	}
	public void addColor(Color c){
		colors.add(c);
	}
	
	public void uniformColor(Color c){
		if(colors==null)colors=new ArrayList<Color>();
		for(int i=0;i<lengths.size();i++) colors.add(c);
	}
	
	public boolean isClosed(){
		return closed;
	}
	
	public int[] getCenter(){
		return this.center;
	}
	
	public PolyGraph(ArrayList<Double> r, ArrayList<double[]> vec, int[] start, int[] center, boolean closed){
		this.center=center;
		this.unitVectors=vec;
		this.lengths=r;
		this.start=start;
		this.closed=closed;
		this.angles=new ArrayList<Double>();
		generateAngles();
		updateCenter();
	}
		
	public PolyGraph(ArrayList<int[]> graf,ArrayList<Integer> separator,int zacetniIndex, int koncniIndex){
		unitVectors=new ArrayList<double[]>();
		lengths=new ArrayList<Double>();
		angles=new ArrayList<Double>();
		int a=0;
		int b=graf.size()-1;
		if(separator!=null){
			a=separator.get(zacetniIndex)+1;
			b=separator.get(koncniIndex)-1;
		}
		start=new int[2];
		start[0]=graf.get(a)[0];
		start[1]=graf.get(a)[1];
		if(start[0]==graf.get(b)[0]&&start[1]==graf.get(b)[1]) closed=true;
		generateVectors(graf, a, b);
		generateAngles();
		center=Stat.meanInt(getGraph(), 2);
	}
	
	public PolyGraph update(ArrayList<int[]> vertex){
		unitVectors=new ArrayList<double[]>();
		lengths=new ArrayList<Double>();
		angles=new ArrayList<Double>();
		start[0]=vertex.get(0)[0];
		start[1]=vertex.get(0)[1];
		generateVectors(vertex, 0, vertex.size()-1);
		center=Stat.meanInt(vertex, 2);
		generateAngles();
		return this;
	}
	
	public ArrayList<double[]> getUnitVectors(){
		return unitVectors;
	}
	
	public ArrayList<Double> getLengths(){
		return lengths;
	}
	
	public ArrayList<Double> getAngles(){
		return angles;
	}
	
	public int[] getStart(){
		return  start;
	}
	
	public void updateCenter(){
		int[] dif=Vec.subInt(center,Stat.meanInt(getGraph(),2));
		start=Vec.sumInt(start, dif);
	}
	
	public void generateAngles(){
		angles=new ArrayList<Double>();
		for(int i=0;i<unitVectors.size()-1;i++){
			double deg=dot(unitVectors.get(i), unitVectors.get(i+1));
			if(unitVectors.get(i)[0]*unitVectors.get(i+1)[1]-unitVectors.get(i)[1]*unitVectors.get(i+1)[0]>0)deg*=-1;
			angles.add(deg);
		}
		if(closed){
			double deg=dot(unitVectors.get(0), unitVectors.get(unitVectors.size()-1));
			if(unitVectors.get(unitVectors.size()-1)[0]*unitVectors.get(0)[1]-unitVectors.get(unitVectors.size()-1)[1]*unitVectors.get(0)[0]>0)deg*=-1;
			angles.add(deg);
		}
	}
	
	
	private double dot(double[] vektor1, double[] vektor2){
		double out=0;
		for(int i=0;i<vektor1.length;i++) out+=vektor1[i]*vektor2[i];
		return Math.toDegrees(Math.acos(out));
	}
		
	private void generateVectors(ArrayList<int[]> graf,int zacetniIndex, int koncniIndex){
		for(int i=zacetniIndex;i<koncniIndex;i++){
			int x=graf.get(i+1)[0]-graf.get(i)[0];
			int y=	graf.get(i+1)[1]-graf.get(i)[1];
			double d=Math.sqrt(x*x+y*y);
			double[] out=new double[2];
			out[0]=x/d;
			out[1]=y/d;
			unitVectors.add(out);
			lengths.add(d);
		}
		
	}
	
	
	public ArrayList<int[]> getGraph(){
		ArrayList<int[]> out=new ArrayList<int[]>();
		out.add(start);
		for(int i=0;i<unitVectors.size();i++){
			out.add(Vec.sumInt(out.get(i),Vec.prod(unitVectors.get(i), lengths.get(i))));
		}
		/*System.out.println("Start");
		for(int i=0;i<out.size();i++){
			System.out.println("( "+out.get(i)[0]+" , "+out.get(i)[1]+" )");
		}
		
		System.out.println("End");
		*/
		return out;
	}
	
	public double[] getEndPointRelative(){
		double[] out=new double[2];
		for(int i=0;i<unitVectors.size();i++){
			out[0]+=unitVectors.get(i)[0]*lengths.get(i);
			out[1]+=unitVectors.get(i)[1]*lengths.get(i);
		}
		return out;
	}
	
	
	public void print(){
		System.out.println("Unit Vectors");
		for(int j=0;j<unitVectors.size();j++){
			System.out.println(unitVectors.get(j)[0]+" , "+unitVectors.get(j)[1]+"  Length : "+lengths.get(j));
			//System.out.println(unitVectors.get(j)[0]+" , "+unitVectors.get(j)[1]+"  Length : "+lengths.get(j)+"   Angle: "+angles.get(j));
		}
	}
}
