package polyCode.machine;

import java.awt.Color;

import polyCode.entities.Display;

public class Processor {
	
	public static void execute(Command c){
		if(c==null){
			return;
		}
		Command tmp;
		if(c.type.equals("resetColor")){
			if(c.vars==null||c.vars.size()==0){
				Display.getPoly().resetColor();
				}
			else Display.printTerminal("Error: command:! "+c.type+" ! takes no arguments\n");
		}
		else if(c.type.equals("setColorVertex")){
			if(c.vars!=null&&c.vars.size()==1){
				Color tmpC=Parser.getColor(c.vars.get(0).value);
				if(tmpC!=null)Display.getPoly().changeColorVertex(tmpC);
				else Display.printTerminal("Error: color :! "+c.vars.get(0).value+" is not a valid selection !\n");
			}
			else Display.printTerminal("Error: conflicting number of arguments with command :! "+c.type+" !\n");
		}
		else if(c.type.equals("setColor")){
			if(c.vars!=null&&c.vars.size()==1){
				Color tmpC=Parser.getColor(c.vars.get(0).value);
				if(tmpC!=null)Display.getPoly().changeColor(tmpC);
				else Display.printTerminal("Error: color :! "+c.vars.get(0).value+" is not a valid selection !\n");
			}
			else Display.printTerminal("Error: conflicting number of arguments with command :! "+c.type+" !\n");
		}
		
		else if(c.type.equals("rename")){
			if(c.vars!=null&&c.vars.size()==2){
				Command temp;
				if((temp=Display.getPoly().getFunction(c.vars.get(0).value))!=null){
					temp.type=new String(c.vars.get(1).value);
				}
				else Display.printTerminal("Error: trying to rename -> command:! "+c.vars.get(0).value+" ! not found\n");
			}
			else Display.printTerminal("Error: conflicting number of arguments with command :! "+c.type+" !\n");
		}
		else if(c.type.equals("clear")){
			if(c.vars==null){
				Display.clearTerminal();
			}
			else Display.printTerminal("Error: command:! "+c.type+" ! takes no arguments\n");
		}
		else if(c.type.equals("printAll")){
			if(c.vars==null||c.vars.size()==0){
				Display.getPoly().printFunctions();
			}
			else Display.printTerminal("Error: command:! "+c.type+" ! takes no arguments\n");
		}
		else if(c.type.equals("setLoc")){
			if(c.vars!=null&&c.vars.size()==2){
				Display.getPoly().setPolyLocation(Double.parseDouble(c.vars.get(0).value),Double.parseDouble(c.vars.get(1).value));
			}
			else Display.printTerminal("Error: conflicting number of arguments with command :! "+c.type+" !\n");
		}
		else if(c.type.equals("move")){
			if(c.vars!=null&&c.vars.size()==1){
				Display.getPoly().movePoly(Double.parseDouble(c.vars.get(0).value));
			}
			else Display.printTerminal("Error: conflicting number of arguments with command :! "+c.type+" !\n");
		}
		else if(c.type.equals("loop")){
			if(c.vars!=null&&c.vars.size()==1&&((int)Double.parseDouble(c.vars.get(0).value)==Double.parseDouble(c.vars.get(0).value))){
				for(int i=0;i<(int)Double.parseDouble(c.vars.get(0).value);i++){
					for(int j=0;j<c.subProgram.size();j++){
						execute(c.subProgram.get(j));
					}
				}
			}
			else Display.printTerminal("Error: conflicting number of arguments with command :! "+c.type+" !\n");
		}
		else if(c.type.equals("setRot")){
			if(c.vars!=null&&c.vars.size()==1){
				Display.getPoly().setRot(Double.parseDouble(c.vars.get(0).value));
			}
			else if(c.vars!=null&&c.vars.size()==2){
				Display.getPoly().setPolyRotation(Double.parseDouble(c.vars.get(0).value), Double.parseDouble(c.vars.get(1).value));
			}
			else Display.printTerminal("Error: conflicting number of arguments with command :! "+c.type+" !\n");
		}
		else if(c.type.equals("rotate")){
			if(c.vars!=null&&c.vars.size()==1){
				Display.getPoly().rotatePoly(Double.parseDouble(c.vars.get(0).value));
			}
			else Display.printTerminal("Error: conflicting number of arguments with command :! "+c.type+" !\n");
		}
		else if(c.type.equals("penDown")){
			if(c.vars==null){
				Display.getPoly().penDown();
			}
			else Display.printTerminal("Error: command:! "+c.type+" ! takes no arguments\n");
		}
		else if(c.type.equals("penUp")){
			if(c.vars==null){
				Display.getPoly().penUp();
			}
			else Display.printTerminal("Error: command:! "+c.type+" ! takes no arguments\n");
		}
		else if((tmp=Display.getPoly().getFunction(c.type))!=null){
			Display.getPoly().setCompile(false);
			if((c.vars==null&&tmp.vars==null)||(c.vars!=null&&tmp.vars!=null&&c.vars.size()==tmp.vars.size())){
				if(c.vars!=null)for(int j=0;j<c.vars.size();j++) tmp.vars.get(j).value=c.vars.get(j).value;
				for(int i=0;i<tmp.subProgram.size();i++) execute(tmp.subProgram.get(i));
			}
			else Display.printTerminal("Error: conflicting number of arguments with command :! "+c.type+" !\n");
		}
		else Display.printTerminal("Error: no such command:! "+c.type+" !\n");
	}
	 
}
