package polyCode.machine;

import java.util.ArrayList;

import polyCode.entities.PolyGraph;

public class Compiler {
	static PolyGraph poly;
	
	public static void setPoly(PolyGraph p){
		poly=p;
	}
	
	public static Command compile(PolyGraph p, int index){
		setPoly(p);
		return compile(index);
	}
	
	public static Command compile(int index){
		Command c=new Command("object"+index,null);
		ArrayList<Command> prog=new ArrayList<Command>();
		Command setLoc=new Command("setLoc",null);
		Variable xPos=new Variable("/","xPos");
		Variable yPos=new Variable("/","yPos");
		Variable rotDeg=new Variable("/","rotDeg");
		setLoc.addVar(xPos);
		setLoc.addVar(yPos);
		c.addVar(xPos);
		c.addVar(yPos);
		c.addVar(rotDeg);
		Command setRot=new Command("setRot",null);
		setRot.addVar(rotDeg);
		prog.add(setLoc);
		prog.add(setRot);
		prog.add(new Command("penDown",null));
			for(int i=0;i<poly.getLengths().size();i++){
				prog.add(new Command("move", new Variable(String.valueOf(poly.getLengths().get(i)),"/")));
				if(i<poly.getAngles().size())prog.add(new Command("rotate", new Variable(String.valueOf(poly.getAngles().get(i)),"/")));
			}
			prog.add(new Command("penUp", null));
		c.setSubProgram(prog);
		return c;
	}
}
