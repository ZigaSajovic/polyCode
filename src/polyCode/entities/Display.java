package polyCode.entities;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

import polyCode.handlers.PolyPrune;
import polyCode.machine.Parser;
import polyCode.machine.Processor;
import polyCode.util.Util;

public class Display {
	public static JLabel info;
	JButton clearButton,compileButton,resetButton;
	static JCheckBox perserveInitialDrawing;
	public static JCheckBox drawVertices;
	static JCheckBox polyCode;
	public static JCheckBox autoEdit;
	private static JComboBox<Object> comboBox;
	static Poly poly;
	static JSlider error;
	JLabel sensitivity;
	boolean beenDone =false;
	static JTextArea jText;
	JTextField input;
	String welcome="polyCode version 1.0\n\nAI driven programming language, compiling images to code and code to images.\nDraw a simple polygon to test; polyCode will enforce symmetries allowed by the preservation of structure, employing them to use loops during compilation. Compiled function will be displayed on the terminal, in the form “objectN(posX, posY, rotDeg)”, where N stands for the object's ID. All such functions may be called on.\nObject drawn by the moving “poly”,  are compiled in the same manner after detachment by trail-less movement, or change of location (“setLoc(posX, posY)” call).\n\nBuilt-in commands:\n\npenDown() -> movement leaves a trail\npenUp() -> movement is trail-less\nmove(length) -> translates by length in current direction\nrotate(degrees) -> rotates \nrename(oldName, newName) -> renames a compiled function\nsetLoc(posX, posY) -> sets location\nsetRot(degree) -> sets rotation by degrees\nsetRot(vecX,vecY) -> sets rotation by direction vector\nsetColor(color) -> sets drawing color\nresetColor() -> resets to default color\nsetColorVertex(color) -> sets color of vertices\nprintAll() -> prints all compiled functions\nclear() -> clears the terminal\n%--------------------------------------------\n";	
	
	public static Poly getPoly(){
		return poly;
	}
	
	public static void printTerminal(String s){
		jText.append(s);
	}
	
	public static void clearTerminal(){
		jText.setText("");
	}
	
	ActionListener listener=new ActionListener(){	
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==clearButton) {
				poly.clear();
				beenDone=false;
			}
			if(e.getSource()==compileButton){				
				if(!poly.beenDrawn()) return;
				beenDone=true;
				poly.compile();
				poly.render();
				//if(getComboBox().getSelectedItem()=="Discrete")poly.render();
				//else if(getComboBox().getSelectedItem()=="Continuous") poly.drawSpline();
			}
		}
	};
	
	ChangeListener change=new ChangeListener() {
		
		public void stateChanged(ChangeEvent e) {
			int vrednost=error.getValue();
			PolyPrune.error=vrednost;
		}
	};
	
	KeyListener terminal=new KeyListener(){
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				String s=input.getText();
				printTerminal(s+"\n");
				Processor.execute(Parser.parseCommand(s));
				poly.render();
	      	    input.setText("");
	      	   }
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}};
	
	public void show(){
		JFrame frame=new JFrame("polyCode");
		Container content=frame.getContentPane();
		content.setLayout(new BorderLayout());
		
		
		
		poly=new Poly();
		jText=new JTextArea();
		jText.setEditable(false);
		input=new JTextField();
		
		jText.setWrapStyleWord(true);
		  jText.setLineWrap(true);
		
		JScrollPane scrollBar = new JScrollPane(jText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		DefaultCaret caret = (DefaultCaret)jText.getCaret();
		 caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JPanel rightPanel=new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(input,BorderLayout.SOUTH);
		input.addKeyListener(terminal);
		
		rightPanel.add(scrollBar,BorderLayout.CENTER);
		JSplitPane jSplit=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,poly, rightPanel);
		jSplit.setOneTouchExpandable(true);
		info=new JLabel();
		content.add(info, BorderLayout.SOUTH);
		content.add(jSplit,BorderLayout.CENTER);
		
		JPanel buttons= new JPanel();
		
		compileButton=new JButton("Compile");
		compileButton.addActionListener(listener);
		buttons.add(compileButton);
		
	/*	resetButton=new JButton("Reset");
		resetButton.addActionListener(listener);
		buttons.add(resetButton);*/
		
		clearButton=new JButton("Clear");
		clearButton.addActionListener(listener);
		buttons.add(clearButton);
		
		/*
		perserveInitialDrawing=new JCheckBox("Perserve initial drawing");
		perserveInitialDrawing.addActionListener(listener);
		buttons.add(perserveInitialDrawing);
		perserveInitialDrawing.setSelected(true);
		*/
		drawVertices=new JCheckBox("Draw vertices");
		drawVertices.addActionListener(listener);
		buttons.add(drawVertices);
		drawVertices.setSelected(true);
		
		/*autoEdit=new JCheckBox("AutoEdit");
		autoEdit.addActionListener(listener);
		buttons.add(autoEdit);
		autoEdit.setSelected(true);*/
		
		polyCode=new JCheckBox("polyCode");
		polyCode.addActionListener(listener);
		buttons.add(polyCode);
		polyCode.setSelected(true);
		
		
	/*	String[] comboString={"Discrete","Continuous"};
		setComboBox(new JComboBox<Object>(comboString));
		getComboBox().addActionListener(listener);
		buttons.add(getComboBox());*/
		
		sensitivity=new JLabel("Sensitivity");
		buttons.add(sensitivity);
		
		error=new JSlider();
		error.setMaximum(40);
		error.setMinimum(20);
		error.setValue(30);
		error.addChangeListener(change);
		buttons.add(error);
		
		content.add(buttons,BorderLayout.NORTH);
		frame.setSize(1100, 700);
		jSplit.setDividerLocation(Util.round(frame.getWidth()*6/9));
		Dimension minimumSize = new Dimension(Util.round(frame.getWidth()*3/9), 0);
		scrollBar.setMinimumSize(minimumSize);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		poly.initial();
		printTerminal(welcome);
		info.setText("X: / Y: /");
	}
	
	public static void main(String[] args){
		new Display().show();
	}

	public static JComboBox<Object> getComboBox() {
		return comboBox;
	}

	public static void setComboBox(JComboBox<Object> comboBox) {
		Display.comboBox = comboBox;
	}
}
