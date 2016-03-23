package polyCode.machine;

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
	
	public static void main(String[] args){
		String s="rotate(1,2)";
		Command c=parseCommand(s);
		if(c!=null)c.print(0);
		else System.out.println("opa");
	}

}
