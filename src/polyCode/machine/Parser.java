package polyCode.machine;

import java.awt.Color;

import polyCode.entities.Display;

public class Parser {
	
	private static boolean testBrackets(String s){
		int a=0;
		int b=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='('){
				if(b==0&&a==0)a=1;
				else if((a==1))return false;
			}
			if(s.charAt(i)==')'){
				if(a==1&&b==0)b=1;
				else if((b==1))return false;
			}
		}
		if(a==1&&b==1)return true;
		else return false;
	}
	
	private static int countParameters(String s){
		int count=0;
		for(int i=s.lastIndexOf('(')+1;i<s.lastIndexOf(')');i++){
			if(s.charAt(i)==',') count++;
		}
		if(s.charAt(s.lastIndexOf('(')+1)!=')') count++;
		return count;
	}
	
	public static Command parseCommand(String s){
		if(!testBrackets(s)){
			Display.printTerminal("Error: could not parse command:! "+s+" !\n");
			return null;
		}
		int i=0;
		while(s.charAt(i)==' ')i++;
		String type=s.substring(i, (s.lastIndexOf('(')));
		int p=countParameters(s);
		String[] param=s.substring((s.lastIndexOf('(')+1), (s.lastIndexOf(')'))).split(",");
		Command out=new Command(type, null);
		for(int j=0;j<p;j++){
			out.addVar(new Variable((param[j]),"/"));
		}
		return out;
	}
	
	public static Color getColor(String s){
		if(s.toLowerCase().equals("red")) return Color.RED;
		else if(s.toLowerCase().equals("blue")) return Color.BLUE;
		else if(s.toLowerCase().equals("black")) return Color.BLACK;
		else if(s.toLowerCase().equals("cyan")) return Color.CYAN;
		else if(s.toLowerCase().equals("green")) return Color.GREEN;
		else if(s.toLowerCase().equals("magenta")) return Color.MAGENTA;
		else if(s.toLowerCase().equals("orange")) return Color.ORANGE;
		else if(s.toLowerCase().equals("pink")) return Color.PINK;
		else if(s.toLowerCase().equals("yellow")) return Color.YELLOW;
		return null;
	}

}
