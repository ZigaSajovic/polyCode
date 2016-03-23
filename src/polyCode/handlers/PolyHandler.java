package polyCode.handlers;

import java.util.ArrayList;

import polyCode.entities.PolyGraph;
import polyCode.math.Calculus;
import polyCode.math.Function;
import polyCode.math.FunctionExpression;
import polyCode.math.GradientDescent;
import polyCode.math.Stat;
import polyCode.math.Vec;
import polyCode.util.Util;

public class PolyHandler {
	ArrayList<String> rVariable;
	ArrayList<Double> rValues;
	ArrayList<String> fiVariable;
	ArrayList<Double> fiValues;
	ArrayList<Boolean> fiSign;
	ArrayList<Integer> signs;
	FunctionExpression[] equation;
	ArrayList<Integer> sign;
	PolyGraph poly;
	
	ArrayList<FunctionExpression[]> vertex;
	
	public PolyHandler(PolyGraph poly){
		rVariable=new ArrayList<String>();
		rValues=new ArrayList<Double>();
		fiVariable=new ArrayList<String>();
		fiValues=new ArrayList<Double>();
		fiSign=new ArrayList<Boolean>();
		signs=new ArrayList<Integer>();
		equation=new FunctionExpression[2];
		this.poly=poly;
		vertex=new ArrayList<FunctionExpression[]>();
		sign=new ArrayList<Integer>();
		setVar();
		setFunction();
	}
	
	public void print(){
		for(int i=0;i<rVariable.size();i++){
			System.out.println(rVariable.get(i)+" = "+rValues.get(i)+"  ; "+fiVariable.get(i)+" = "+fiValues.get(i)+"   sign: "+fiSign.get(i));
		}
	}
	
	public void prints(){
		for(int i=0;i<rVariable.size();i++){
			System.out.println(rVariable.get(i)+" = "+rValues.get(i)+"  ; "+fiVariable.get(i)+" = "+fiValues.get(i)+"   sign: "+signs.get(i));
			if(signs.get(i)==0) System.out.println(Math.cos(fiValues.get(i))+" , "+Math.sin(fiValues.get(i)));
			if(signs.get(i)==1) System.out.println("-"+Math.cos(fiValues.get(i))+" , "+Math.sin(fiValues.get(i)));
			if(signs.get(i)==2) System.out.println(Math.cos(fiValues.get(i))+" , -"+Math.sin(fiValues.get(i)));
			if(signs.get(i)==3) System.out.println("-"+Math.cos(fiValues.get(i))+" , -"+Math.sin(fiValues.get(i)));
		}
	}
	
	public void setVar(){
		ArrayList<ArrayList<Integer>> dist=Stat.getEquals(poly.getLengths(),50);
		ArrayList<ArrayList<Integer>> vec=Stat.getParallel(poly.getUnitVectors());
		ArrayList<double[]>vecMean=Stat.meansFromIndexVec(vec, poly.getUnitVectors(), 2);
		ArrayList<Double> distMean=Stat.meansFromIndex(dist, poly.getLengths());
		ArrayList<ArrayList<Integer>> negative=Util.getNegs(vecMean);
		
		for(int i=0;i<poly.getLengths().size();i++){
			int r=Util.locate(i,dist);
			rVariable.add("r"+Integer.toString(r));
			int fi=Util.locate(i, vec);
			int fiInNeg=Util.locate(fi, negative);
			
			fiVariable.add("fi"+Integer.toString(fiInNeg));
			if(negative!=null&&negative.get(fiInNeg).get(0)==fi)fiSign.add(false);
			else fiSign.add(true);
			double[] temp=vecMean.get(negative.get(fiInNeg).get(0));
			fiValues.add(Math.atan2(temp[1], temp[0]));
			rValues.add(distMean.get(r));
		}
		//print();
	}
	
	public void setFunction(){
		for(int i=0;i<rVariable.size();i++){
			FunctionExpression tempCos=new FunctionExpression(new Function("cos", fiVariable.get(i),0));
			FunctionExpression tempSin=new FunctionExpression(new Function("sin", fiVariable.get(i),0));
			FunctionExpression cosR=new FunctionExpression(new Function(rVariable.get(i), rVariable.get(i), 0));
			FunctionExpression sinR=new FunctionExpression(new Function(rVariable.get(i), rVariable.get(i), 0));
			tempCos=FunctionExpression.product(cosR, tempCos);
			tempSin=FunctionExpression.product(sinR, tempSin);
			if(fiSign.get(i)){
				tempCos=FunctionExpression.product(tempCos, -1.0);
				tempSin=FunctionExpression.product(tempSin, -1.0);
			}
			if(equation[0]==null){
				equation[1]=tempSin;
				equation[0]=tempCos;
			}
			else{
				equation[1]=FunctionExpression.sum(equation[1], tempSin);
				equation[0]=FunctionExpression.sum(equation[0], tempCos);
			}
			FunctionExpression[] out=new FunctionExpression[2];
			out[0]=equation[0];
			out[1]=equation[1];
			vertex.add(out);
		}
		//cos.printByLevel();
	}
	
	public double[] varianceLength(){
		PolyGraph poly=getPoly();
		ArrayList<int[]> a=poly.getGraph();
		double x=0;
		double y=0;
		for(int i=1;i<a.size();i++){
			x+=a.get(i)[0]-a.get(0)[0];
			y+=a.get(i)[1]-a.get(0)[1];
		}
		x/=(a.size()-1);
		y/=(a.size()-1);
		
		double X=0;
		double Y=0;
		for(int i=1;i<a.size();i++){
			X+=Math.pow((a.get(i)[0]-a.get(0)[0]-x),2);
			Y+=Math.pow((a.get(i)[1]-a.get(0)[1]-y),2);
		}
		
		X/=(a.size()-1);
		Y/=(a.size()-1);
		double[] out=new double[2];
		out[0]=X;
		out[1]=Y;
		return out;
	}
	
	public void test(){
		setComputer();
		FunctionExpression[] t=getMoment(1,null);
		FunctionExpression[] mean=getMoment(2,t);
		PolyGraph poly=getPoly();
		ArrayList<int[]> a=poly.getGraph();
		Util.printListVecInt(a);
		for(int i=0;i<poly.getLengths().size();i++){
			double x=poly.getLengths().get(i)*poly.getUnitVectors().get(i)[0];
			double y=poly.getLengths().get(i)*poly.getUnitVectors().get(i)[1];
			System.out.print("( "+x+" , "+y+" ) ,");
		}
		System.out.println();
		for(int i=0;i<vertex.size();i++){
			double x=Calculus.compute(vertex.get(i)[0]);
			double y=Calculus.compute(vertex.get(i)[1]);
			System.out.print("( "+x+" , "+y+" ) ,");
		}
		double x=0;
		double y=0;
		for(int i=1;i<a.size();i++){
			x+=a.get(i)[0]-a.get(0)[0];
			y+=a.get(i)[1]-a.get(0)[1];
		}
		x/=(a.size()-1);
		y/=(a.size()-1);
		
		double X=0;
		double Y=0;
		for(int i=1;i<a.size();i++){
			X+=Math.pow((a.get(i)[0]-a.get(0)[0]-x),2);
			Y+=Math.pow((a.get(i)[1]-a.get(0)[1]-y),2);
		}
		
		X/=(a.size()-1);
		Y/=(a.size()-1);
		
		System.out.println(a.size());
		System.out.println("( "+x+" , "+y+" )");
		System.out.println("( "+Calculus.compute(t[0])+" , "+Calculus.compute(t[1])+" )");
		System.out.println("( "+X+" , "+Y+" )");
		System.out.println("( "+Calculus.compute(mean[0])+" , "+Calculus.compute(mean[1])+" )");
	}
	
	public FunctionExpression[] getMoment(int n, FunctionExpression[] mean){
		FunctionExpression[] moment=new FunctionExpression[2];
		FunctionExpression tempSin=null;
		FunctionExpression tempCos=null;
		int x=vertex.size();
		for(int i=0;i<x;i++){
			FunctionExpression tC=null;
			FunctionExpression tS=null;
			if(mean==null){
				tC=FunctionExpression.exp(vertex.get(i)[0], n);
				tS=FunctionExpression.exp(vertex.get(i)[1], n);
			}
			else{
				tC=FunctionExpression.sub(vertex.get(i)[0], mean[0]);
				tS=FunctionExpression.sub(vertex.get(i)[1], mean[1]);
				tC=FunctionExpression.exp(tC, n);
				tS=FunctionExpression.exp(tS, n);
			}
			if(tempSin==null){
				tempSin=tS;
				tempCos=tC;
			}
			else{
				tempSin=FunctionExpression.sum(tempSin, tS);
				tempCos=FunctionExpression.sum(tempCos, tC);
			}
		}
		tempCos=FunctionExpression.div(tempCos, (vertex.size()));
		tempSin=FunctionExpression.div(tempSin, (vertex.size()));
		moment[0]=tempCos;
		moment[1]=tempSin;
		return moment;
	}
	
	public void setComputer(){
		ArrayList<String> tempVar=new ArrayList<String>();
		ArrayList<Double> tempVal=new ArrayList<Double>();
		for(int i=0;i<rVariable.size();i++){
			if(!Util.contains(rVariable.get(i), tempVar)){
				tempVar.add(rVariable.get(i));
				tempVal.add(rValues.get(i));
			}
		}
		for(int i=0;i<fiVariable.size();i++){
			if(!Util.contains(fiVariable.get(i), tempVar)){
				tempVar.add(fiVariable.get(i));
				tempVal.add(fiValues.get(i));
			}
		}
		Calculus.setVariables(tempVar);
		Calculus.setValues(tempVal);
		//System.out.println(Computer.compute(equation[0]));
	}
	
	
	public void optimize(){
		setComputer();
		//print();
		FunctionExpression toOptimize=FunctionExpression.sum(FunctionExpression.exp(equation[0], 2), FunctionExpression.exp(equation[1], 2));
		
		GradientDescent.descent(null, (toOptimize));
		
		for(int i=0;i<rVariable.size();i++){
			rValues.set(i, Calculus.getValue(rVariable.get(i)));
		}
		for(int i=0;i<fiVariable.size();i++){
			fiValues.set(i, Calculus.getValue(fiVariable.get(i)));
		}
	//	print();
	}
	
	public PolyGraph updatePoly(){
		optimize();
		return getPoly();
	}
	
	
	public PolyGraph getPoly(){
		ArrayList<double[]> tempVec=new ArrayList<double[]>();
		
		for(int i=0;i<fiValues.size();i++){
			double[] temp=new double[2];
			temp[0]=Math.cos(fiValues.get(i));
			temp[1]=Math.sin(fiValues.get(i));
			if(fiSign.get(i))temp=Vec.prod(temp, -1.0);
			tempVec.add(temp);
		}
		return new PolyGraph(rValues, tempVec, poly.getStart(),poly.getCenter(), poly.isClosed());
	}
}
