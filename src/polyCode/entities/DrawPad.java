package polyCode.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
 

import java.util.ArrayList;

import javax.swing.JComponent;

import polyCode.handlers.Grid;
import polyCode.handlers.Linker;
import polyCode.handlers.PolyPrune;
public class DrawPad extends JComponent{
	private static final long serialVersionUID = 1L;
	Image image;
	protected static Graphics2D g2;
	
	ArrayList<int[]> vertex=new ArrayList<int[]>();
	protected ArrayList<int[]> graph;
	static ArrayList<int[]> spline;
	ArrayList<Integer> seperator;
		
	
	private int currentX, currentY, oldX, oldY;
	
	
	public DrawPad(){
		setDoubleBuffered(false);
		addMouseListener(new MouseAdapter(){

			@Override
			public void mousePressed(MouseEvent e) {
				oldX=e.getX();
				oldY=e.getY();
				addVertex(oldX, oldY);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				addVertex(-1,-1);
				
			}
		});
		addMouseMotionListener(new MouseMotionAdapter(){
			
			

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseMoved(e);
				Display.info.setText("X: "+e.getX()+" Y: "+e.getY());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				currentX=e.getX();
				currentY=e.getY();
				
				if(g2!=null){
					Display.info.setText("X: "+e.getX()+" Y: "+e.getY());
					g2.drawLine(oldX,oldY,currentX,currentY);
					repaint();
					oldX=currentX;
					oldY=currentY;
					addVertex(oldX, oldY);
				}
			}		
		});
	}
	
	
	protected void paintComponent(Graphics g){
		if(image==null){
			image=createImage(getSize().width,getSize().height);
			g2=(Graphics2D) image.getGraphics();
			g2.setStroke(new BasicStroke(2));
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		
		g.drawImage(image, 0, 0, null);
	}
	

	private void addVertex(int x, int y){
		int[] temp=new int[2];
		temp[0]=x;
		temp[1]=y;
		vertex.add(temp);
	}
	
	private int round(double x){
		double a=(int)x+0.5;
		
		if(x>a)return (int)x+1;
		else return (int)x;
	}
	
	public void drawSpline(){
		drawSpline(null);
	}
	
	public void drawSpline(Color c){
		Color temp=g2.getColor();
		if(c!=null)g2.setColor(c);
		g2.setStroke(new BasicStroke(3));
		for(int i=1;i<spline.size()-2;i++){
			if(spline.get(i+1)[0]==-1){
				i+=1;
				continue;
			}
			int xS=spline.get(i)[0];
			int yS=spline.get(i)[1];
			for(double t=0.1;t<=1;t+=0.1){
				int xT=round((1-t)*(1-t)*(1-t)*spline.get(i)[0]+3*t*(1-t)*(1-t)*spline.get(i+1)[0]+3*(1-t)*t*t*spline.get(i+2)[0] + t*t*t*spline.get(i+3)[0]);
				int yT=round((1-t)*(1-t)*(1-t)*spline.get(i)[1]+3*t*(1-t)*(1-t)*spline.get(i+1)[1]+3*(1-t)*t*t*spline.get(i+2)[1] + t*t*t*spline.get(i+3)[1]);
				g2.drawLine(xS,yS,xT,yT);
				xS=xT;
				yS=yT;
			}
			i+=2;
		}
		
		repaint();
		g2.setStroke(new BasicStroke(2));
		g2.setColor(temp);
	}
	
	public void reset(){
		clearScreen();
		drawGraph(vertex, Color.black);
	}
	
	
	public void drawVertices(Color c){
		drawVertices(this.graph,c);
	}
	
	public void drawVertices(ArrayList<int[]> vertices,Color c){
		Color temp=g2.getColor();
		if(c!=null)g2.setColor(c);
		
		for(int i=0;i<vertices.size();i++){
			if(vertices.get(i)[0]==-1){
				continue;
			}
			
			int[] q=vertices.get(i);
			g2.drawOval(q[0]-5, q[1]-5, 10, 10);
		}
		
		g2.setColor(temp);
	}
	
	public void drawInitialDrawing(){
		drawGraph(vertex,Color.black);
	}

	
	public void drawGraph(ArrayList<int[]> in, Color c){
		Color temp=g2.getColor();
		if(c!=null)g2.setPaint(c);
		for(int i=2;i<in.size();++i){
			int[] t=in.get(i);
			int[]t2=in.get(i-1);
			if(t[0]==-1&&t[1]==-1){
				i+=1;
				continue;
			}
			if(t2[0]==-1&&t[1]==-1){
				i+=1;
				continue;
			}
			
			g2.drawLine(t2[0], t2[1], t[0], t[1]);
		}
		
		g2.setPaint(temp);
		repaint();
	}
	
	public void drawPolyGraph(){
		drawGraph(graph, Color.red);
	}

	public void compile(){
		Graph initialGraph=new Graph(vertex);
		PolyPrune prune=new PolyPrune(initialGraph.getGraph(),initialGraph.vrniSeperator());
		this.graph = prune.getGraph();
		this.seperator=prune.getSeparator();

		Linker link=new Linker(graph, seperator);
		graph=link.getGraph();
		
		Grid.align(graph);
		graph=Grid.getVertex();
		
		Spline makeSpline=new Spline(graph, seperator);
		spline=makeSpline.spline();
		
	}
	
	public void clear(){
		vertex.clear();
		clearScreen();
		repaint();
	}
	
	public void clearScreen(){
		g2.setPaint(Color.white);
		g2.fillRect(0, 0, getSize().width, getSize().height);
		g2.setPaint(Color.black);
		repaint();
	}
}
