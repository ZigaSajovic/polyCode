package polyCode.machine;

import java.util.ArrayList;

import polyCode.entities.Display;
import polyCode.util.Util;


public class Command {
	String type;
	double[] parameters=null;
	ArrayList<Command> subProgram=null;
	ArrayList<String> param=null;
	ArrayList<Variable> vars=null;
	
	public void addVar(Variable v){
		if(vars==null)vars=new ArrayList<Variable>();
		vars.add(v);
	}
	
	public void setSubProgram(ArrayList<Command> subProg){
		subProgram=subProg;
	}
	
	public void addToSubProgram(Command c){
		if(subProgram==null)subProgram=new ArrayList<Command>();
		subProgram.add(c);
	}
	
	public String getType() {
		return type;
	}
	
	public void addParameterVar(String s){
		if(param==null)param=new ArrayList<String>();
		param.add(s);
	}
	
	public void setParameters(double[] p){
		parameters=p;
	}
	
	public void setVarParameters(ArrayList<String> s){
		param=s;
	}

	public double[] getParameters() {
		return parameters;
	}
	
	public Command(String type,Variable var){
		this.type=type;
		if(var!=null)addVar(var);
	}
	
	public void setVars(ArrayList<Variable> vars){
		this.vars=vars;
	}
	
	public ArrayList<Variable> getVars(){
		return vars;
	}
	
	public boolean equal(Command c, double error){
		if(!c.getType().equals(type)) return false;
		if(c.vars.size()!=vars.size()) return false;
		for(int i=0;i<vars.size();i++){
			if(Math.abs(Double.parseDouble(c.vars.get(i).value)-Double.parseDouble(vars.get(i).value))>error)return false;
			if(!c.vars.get(i).name.equals(vars.get(i).name)) return false;
		}
		if(c.subProgram!=null&&subProgram!=null){
			if(c.subProgram.size()!=subProgram.size()) return false;
			for(int i=0;i<subProgram.size();i++){
				if(!subProgram.get(i).equal(c.subProgram.get(i), 0)) return false;
			}
		}
		if(c.subProgram==null&&subProgram==null) return true;
		else return false;
	}
	
	public ArrayList<Command> getSubProgram(){
		return subProgram;
	}
	
	public void printAll(int level){
		for(int i=0;i<4*level;i++)Display.printTerminal(" ");
		
		Display.printTerminal(type+"(");
		if(vars!=null){
			for(int i=0;i<vars.size();i++){
				if(vars.get(i).name.equals("/")) Display.printTerminal(""+Util.round(Double.parseDouble(vars.get(i).value)));
				else Display.printTerminal(vars.get(i).name);
				if(i<vars.size()-1) Display.printTerminal(", ");		
			}
		}
		Display.printTerminal(")");
		if(subProgram!=null){
			Display.printTerminal("{\n");
			for(int i=0;i<subProgram.size();i++) subProgram.get(i).printAll(level+1);
			for(int i=0;i<4*level;i++)Display.printTerminal(" ");
			Display.printTerminal("}");
		}
		else Display.printTerminal(";");
		Display.printTerminal("\n");
		
	}
	
	public void print(int level){
		for(int i=0;i<4*level;i++)System.out.print(" ");
		
		System.out.print(type+"(");
		if(vars!=null){
			for(int i=0;i<vars.size();i++){
				if(vars.get(i).name.equals("/")) System.out.print(""+Util.round(Double.parseDouble(vars.get(i).value)));
				else System.out.print(vars.get(i).name);
				if(i<vars.size()-1) System.out.print(", ");		
			}
		}
		System.out.print(")");
		if(subProgram!=null){
			System.out.print("{\n");
			for(int i=0;i<subProgram.size();i++) subProgram.get(i).print(level+1);
			for(int i=0;i<4*level;i++)System.out.print(" ");
			System.out.println("}");
		}
		else System.out.print(";");
		System.out.println("\n");
		
	}
	
}
