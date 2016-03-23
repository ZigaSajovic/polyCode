package polyCode.machine;

import java.util.ArrayList;

import polyCode.entities.PolyGraph;
import polyCode.math.Stat;
import polyCode.math.Vec;

public class Comparator {
	//dodaj gotove nesmisle, max varianc >x, itd...
	static double errorMean=50;
	static double errorVariance=50;
	
	public static boolean diff(PolyGraph a, PolyGraph b){
		if(a.getLengths().size()!=b.getLengths().size()) return false;
		ArrayList<Double> diff=new ArrayList<Double>();
		for(int i=0;i<a.getGraph().size();i++){
			diff.add(Vec.normInt(Vec.subInt(a.getGraph().get(i), b.getGraph().get(i))));
		}
		double mean=Stat.meanDouble(diff);
		double variance=Math.sqrt(Stat.momentDouble(diff, mean, 2));
		//System.out.println(mean+" , "+variance);
		return mean<=errorMean&&variance<=errorVariance;
	}

}
