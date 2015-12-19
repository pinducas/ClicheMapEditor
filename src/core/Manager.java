package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Manager {
	private final int LEFT = 0,RIGHT = 1,UP = 2,DOWN = 3,MOUSE = 4, A = 5, S = 6,D = 7, W = 8;
	
	private boolean[] pressing;
	public boolean pressedEnter;
	
	private boolean showPlatforms;
	
	public int [][]map;
	
	private ArrayList<Platform> platforms;
	
	private int boxtool;
	
	private int tileWidth;
	private int tileHeight;
	
	private int numPlatform;
	
	private int ribbon;
	
	private BufferedImage []tiles;
	
	private TextBox boxWidth;
	private TextBox boxHeight;
	
	private Loader loader;
	
	private Camera camera;
	
	private JLabel dimensions;
	private JLabel x;
	
	private JPanel panel;
	
	private JButton tileMode;
	private JButton boxMode;
	private JButton save;
	private JButton load;
	private JButton createbox;
	private JButton deletebox;
	private JButton movebox;
	private JButton changebox;
	private JButton selectbox;
	
	private JButton up;
	private JButton down;
	private int positiony;
	
	private JFileChooser explorer;
	
	private Point mousePosition;
	
	private Platform selectedPlatform;
	
	
	public Manager(JPanel panel){
		this.panel = panel;
		map = new int[9][12];
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[0].length; j++){
				map[i][j] = 0;
			}
		}
		

		
		tiles = new BufferedImage[16*16];
		
		loader = new Loader();

		BufferedImage sheet = loader.loadImage("Tiles.png");		
		
		int current = 0;
		for(int line = 0; line < 16; line++){
			for(int col = 0; col < 16; col++){
				tiles[current] = sheet.getSubimage(32*col, 32*line, 32, 32);
				current++;
			}
		}
		
		tileWidth = tiles[0].getWidth();
		tileHeight = tiles[0].getHeight();
		
		tileWidth = tileHeight = 72;
		
		load = new JButton("Load Map");
		load.setBounds(820, 10, 120,30);
		panel.add(load);
		save = new JButton("Save Map");
		save.setBounds(975, 10, 120,30);
		panel.add(save);
		
		JLabel temp = new JLabel("___________________________________________________");
		temp.setBounds(800,20,500,50);
		panel.add(temp);
		
		
		
		boxWidth = new SizeBox(panel, this,900, 550, 40,30, ""+map[0].length,0);
		boxHeight = new SizeBox(panel, this,960, 550, 40,30, ""+map.length,1);
		
		
		x = new JLabel("X");
		x.setBounds(945,550,40,30);
		panel.add(x);
		
		pressing = new boolean[9];
		for(int i = 0; i < pressing.length; i++)
			pressing[i] = false;
		
		camera = new Camera(0, 0, 780, 500);
		
		ribbon = -1;
		
		dimensions = new JLabel("Map size ");
		dimensions.setBounds(820,545,250,40);
		panel.add(dimensions);
		
		tileMode = new JButton("Tile Mode");
		tileMode.setBounds(10,540,130,40);
		panel.add(tileMode);
		
		boxMode = new JButton("Box Mode");
		boxMode.setBounds(160,540,130,40);
		panel.add(boxMode);
		
		numPlatform = 0;
		platforms = new ArrayList<Platform>();
		
		positiony = 0;
		
		up = new JButton("^");
		down = new JButton("v");
		up.setBounds(1045,268,50,115);
		down.setBounds(1045,392,50,115);
		
		panel.add(up);
		panel.add(down);
		
		boxtool = 3;
		
		createbox = new JButton("Create Box");
		deletebox = new JButton("Delete Box");
		movebox = new JButton("Move Box");
		changebox = new JButton("Define Box");
		selectbox = new JButton("Select Box");
		createbox.setBounds(820, 80, 120,30);
		deletebox.setBounds(975, 80, 120,30);
		movebox.setBounds(820, 125, 120,30);
		changebox.setBounds(975, 125, 120,30);
		selectbox.setBounds(820, 170, 120,30);
		
		panel.add(createbox);
		panel.add(deletebox);
		panel.add(movebox);
		panel.add(changebox);
		panel.add(selectbox);
		
		temp = new JLabel("Platforms");
		temp.setBounds(820,40,500,50);
		panel.add(temp);
		
		temp = new JLabel("___________________________________________________");
		temp.setBounds(800,180,500,50);
		panel.add(temp);
		
		
		mousePosition = new Point(0, 0);
		
		String [] options = {"New Map","Load Map","Cancel"};
		
		int resp = JOptionPane.showOptionDialog(null,"Choose something","Map editor",JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,null,options, options[0]);
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".map FILES", "map");
		explorer = new JFileChooser();
		explorer.setFileFilter(filter);
		
		selectedPlatform = null;
		
		if(resp == 2)System.exit(0);
		if(resp == 1){
			loadMap();		
		}
		
	}
	
	public void update(){
		if(load.getModel().isPressed())loadMap();
		if(save.getModel().isPressed())saveMap();
		if(boxMode.getModel().isPressed() && !showPlatforms)showPlatforms = true;
		if(tileMode.getModel().isArmed() && showPlatforms)showPlatforms = false;
		if(up.getModel().isPressed() && positiony > 0)positiony -= 10;
		if(down.getModel().isPressed())positiony += 10;
		if(createbox.getModel().isPressed())boxtool = 0;
		if(deletebox.getModel().isPressed())boxtool = 1;
		if(movebox.getModel().isPressed())boxtool = -1;
		if(changebox.getModel().isPressed())boxtool = 2;
		if(selectbox.getModel().isPressed())boxtool = 3;
		
		for(Platform p:platforms)p.update(camera);
		
		
		
		
		if(pressing[MOUSE]){
			int x,y;
			if(mousePosition.x > 800 && mousePosition.y> 268){		
				x = (int)(mousePosition.x-800);
				y = (int)(mousePosition.y-268);
				if(x > 0 && x < 240 && y > 0 && y < 240)
					copyRibbon(x/120,(y+positiony)/120);
			}
			else if(!showPlatforms){
				panel.requestFocus();
				
				x = mouseMapPos().x;
				y = mouseMapPos().y;
				if(x >= 0 && y>=0 && x < camera.getX()+camera.getW()-10 && y< -camera.getY()+camera.getH()-10)
					changeTile(x/tileWidth,y/tileHeight);
			}
			else if(showPlatforms){
				panel.requestFocus();

				if(selectedPlatform == null){
					Rectangle m = new Rectangle(mouseMapPos().x-2-(int)camera.getX(),
							500-mouseMapPos().y-2-(int)camera.getY(),4,4);
					for(Platform p:platforms){
						if(p.getRect(camera).intersects(m)){
							selectedPlatform = p;
							System.out.println("YUP");
							break;
						}
					}
				}
				
				if(boxtool == -1){
					if(selectedPlatform != null){
						selectedPlatform.x = mouseMapPos().x;
						selectedPlatform.y = mouseMapPos().y;
					}
				}
				if(boxtool == 2){
					if(selectedPlatform != null){
						String resp = "";
						try{
							resp = JOptionPane.showInputDialog("Current platform values",(int)(selectedPlatform.x-selectedPlatform.width/2)+"x"+
						(int)(selectedPlatform.y - selectedPlatform.height/2)+"x"+selectedPlatform.width+"x"+selectedPlatform.height);
						}catch(Exception e){
							selectedPlatform = null;
							return;
						}
						if(resp == null){
							selectedPlatform = null;
							return;
						}
						if(resp.length() < 7){
							selectedPlatform = null;
							return;
						}
						try{
							String []temp = resp.split("x");
							if(temp.length < 4)return;
							int w =  Integer.parseInt(temp[2]);
							int h =  Integer.parseInt(temp[3]);

							int tx = (int) (Integer.parseInt(temp[0])+w/2);
							int ty = (int)(Integer.parseInt(temp[1])+h/2);
							
							if(tx < 0 || ty < 0 || tx > (map[0].length-1)*tileWidth || ty > (map.length-1)*tileHeight||
									w < tileWidth || h < tileHeight || w > (map[0].length-1)*tileWidth ||
									 h > (map.length-1)*tileHeight){
								return;
							}
							
							selectedPlatform.x = tx;
							selectedPlatform.y = ty;
							selectedPlatform.width = w;
							selectedPlatform.height = h;
							
							
						}catch(Exception e){
							selectedPlatform = null;
							return;
						}
						
					}	
				}
				if(boxtool == 0){
					String resp = "";
					try{
						resp = JOptionPane.showInputDialog("New tile dimensions",tileWidth+"x"+tileHeight);
					}catch(Exception e){
						boxtool = 3;
						return;
					}
					
					try{
						String []temp = resp.split("x");
						if(temp.length < 2)return;
						int w =  Integer.parseInt(temp[0]);
						int h =  Integer.parseInt(temp[1]);
						
						if(w < tileWidth || h < tileHeight || w > (map[0].length-1)*tileWidth ||
								 h > (map.length-1)*tileHeight){
							boxtool = 3;
							return;
						}
						
						Platform p = new Platform(mouseMapPos().x, mouseMapPos().y, w, h, 1);
						platforms.add(p);
						numPlatform++;
						selectedPlatform = p;
						selectedPlatform.width = w;
						selectedPlatform.height = h;
						
						
					}catch(Exception e){
						selectedPlatform = null;
						return;
					}
				}
				if(boxtool == 1){
					if(selectedPlatform != null){
						platforms.remove(selectedPlatform);
						selectedPlatform = null;
						numPlatform --;
					}
				}
			}
		}
		
		
		
		boxWidth.update();
		boxHeight.update();
	}
	
	public Point mouseMapPos(){
		return new Point((int)(camera.getX()+mousePosition.x-10),(int)(510-mousePosition.y-camera.getY()));
	}
	
	public void draw(Graphics2D gm,Graphics2D gt){
		for(int line = 0; line < map.length; line++){
			for(int col = 0; col < map[0].length; col++){
				if(map[line][col] == -1)continue;
				gm.drawImage(tiles[map[line][col]],(int)(col*tileWidth-camera.getX()),
						(int)(500-tileHeight-line * tileHeight-camera.getY()),tileWidth,tileHeight,null);
				
			}
		}
		
		int currentLine = -1;
		for(int i = 0; i < tiles.length; i++){
			if(i%2 == 0)currentLine++;
			gt.drawImage(tiles[i],10+(i%2)*120,10+currentLine*120-positiony,100,100,null);
			if(i == ribbon){
				gt.setColor(new Color(0,0,255,100));
				gt.fillRect(10+(i%2)*120-2,10+currentLine*120-2-positiony,104,104);
			}
		}
		
		if(showPlatforms)
			for(Platform p:platforms)p.draw(gm,camera);
		
		gm.setColor(Color.BLACK);
		gm.fillRect(mouseMapPos().x-10-(int)camera.getX(),500-mouseMapPos().y+2-(int)camera.getY(),6,2);
		gm.fillRect(mouseMapPos().x+8-(int)camera.getX(),500-mouseMapPos().y+2-(int)camera.getY(),6,2);
		gm.fillRect(mouseMapPos().x-(int)camera.getX(),500-mouseMapPos().y+10-(int)camera.getY(),2,6);
		gm.fillRect(mouseMapPos().x-(int)camera.getX(),500-mouseMapPos().y-10-(int)camera.getY(),2,6);

	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_D && !pressing[D]){
			if(camera.getX() < tileWidth * (map[0].length-1) - camera.getW())
				camera.translate(tileWidth/2f, 0);	
			pressing[D] = true;
		}
		if(k == KeyEvent.VK_A && !pressing[A]){
			if(camera.getX() > 0)
				camera.translate(-tileWidth/2f, 0);	
			pressing[A] = true;
		}
		if(k == KeyEvent.VK_W && !pressing[W]){
			if(camera.getY() > -tileHeight*(map.length-1)+camera.getH())
			camera.translate(0,-tileWidth/2f);	
			pressing[W] = true;
		}
		if(k == KeyEvent.VK_S && !pressing[S]){
			if(camera.getY() < 0)
				camera.translate(0, tileWidth/2f);
			pressing[S] = true;
		}
		if(k == KeyEvent.VK_ENTER && !pressedEnter){
			pressedEnter = true;
		}
		if(k == KeyEvent.VK_DELETE){
			ribbon = 0;
		}
		
		if(k == KeyEvent.VK_LEFT && !pressing[LEFT]){
			pressing[LEFT] = true;
			if(selectedPlatform != null){
				if((selectedPlatform.x-selectedPlatform.width/2)%tileWidth != 0){
					selectedPlatform.x -= (selectedPlatform.x-selectedPlatform.width/2)%tileWidth;
				}
				else{
					selectedPlatform.x -= tileWidth;
				}
			}
		}
		if(k == KeyEvent.VK_RIGHT && !pressing[RIGHT]){
			pressing[RIGHT] = true;	
			if(selectedPlatform != null){
				if((selectedPlatform.x-selectedPlatform.width/2)%tileWidth != 0){
					selectedPlatform.x -= (selectedPlatform.x-selectedPlatform.width/2)%tileWidth;
				}
				else{
					selectedPlatform.x += tileWidth;
				}
			}
			//selectedPlatform.x = 0;
		}
		if(k == KeyEvent.VK_UP && !pressing[UP]){
			pressing[UP] = true;
			
			if(selectedPlatform != null){
				if((selectedPlatform.y-selectedPlatform.height/2)%tileHeight != 0){
					selectedPlatform.y -= (selectedPlatform.y-selectedPlatform.height/2)%tileHeight;
				}
				else{
					selectedPlatform.y += tileHeight;
				}
			}
			
		}
		if(k == KeyEvent.VK_DOWN && !pressing[DOWN]){
			pressing[DOWN] = true;
			if(selectedPlatform != null){
				if((selectedPlatform.y-selectedPlatform.height/2)%tileHeight != 0){
					selectedPlatform.y -= (selectedPlatform.y-selectedPlatform.height/2)%tileHeight;
				}
				else{
					selectedPlatform.y -= tileHeight;
				}
			}
		}
		
	}
	
	
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_D && pressing[D]){
			pressing[D] = false;
		}
		if(k == KeyEvent.VK_A && pressing[A]){
			pressing[A] = false;
		}
		if(k == KeyEvent.VK_W && pressing[W]){
			pressing[W] = false;
		}
		if(k == KeyEvent.VK_S && pressing[S]){
			pressing[S] = false;
		}
		if(k == KeyEvent.VK_ENTER && pressedEnter){
			pressedEnter = false;
		}
		if(k == KeyEvent.VK_LEFT && pressing[LEFT]){
			pressing[LEFT] = false;
		}
		if(k == KeyEvent.VK_RIGHT && pressing[RIGHT]){
			pressing[RIGHT] = false;
		}
		if(k == KeyEvent.VK_UP && pressing[UP]){
			pressing[UP] = false;
		}
		if(k == KeyEvent.VK_DOWN && pressing[DOWN]){
			pressing[DOWN] = false;
		}
		
		
	}
	
	public void mousePressed(MouseEvent e) {
		if(!pressing[MOUSE])selectedPlatform = null;
		pressing[MOUSE] = true;	
		
	}

	public void mouseReleased(MouseEvent e) {
		pressing[MOUSE] = false;
	}
	
	public void mouseMoved(MouseEvent e){
		mousePosition.x = e.getX();
		mousePosition.y = e.getY();
	}
	
	public void mouseDragged(MouseEvent e){
		mousePosition.x = e.getX();
		mousePosition.y = e.getY();
	}
	
	public void changeTile(int x,int y){
		if(ribbon == 0)map[y][x] = -1;
		else map[y][x] = ribbon;
	}
	
	public void copyMap(int [][]old,int[][]newMap){
		for(int line = 0; line < newMap.length; line++){
			for(int col = 0; col < newMap[0].length; col++){
				if(line < old.length && col < old[0].length)newMap[line][col] = old[line][col];
				else newMap[line][col] = -1;
			}
		}
	}
	
	public void changeMap(int x, int y){
		int [][]novo = new int[y][x];
		copyMap(map,novo);
		map = novo;		
		camera.setPosition(0, 0);
		
		for(int i = 0; i < pressing.length; i++){
			pressing[i] = false;
		}
	}
	
	public void copyRibbon(int x, int y){
		ribbon = x+2*y;
		if(ribbon > tiles.length-1)ribbon = -1;
		
	}
	
	private void saveMap(){
		try{
			int resp = explorer.showSaveDialog(panel);
			File file;
			
			if(resp == JFileChooser.APPROVE_OPTION){
				
				file = explorer.getSelectedFile();
				if(file.exists()){
					int a = JOptionPane.showConfirmDialog(null, "Do you want to overwrite the existing file?");
					if(a != JOptionPane.OK_OPTION){
						JOptionPane.showMessageDialog(null, "Save cancelled");
						return;
					}				
				}
				
				file.createNewFile();				
				
				PrintWriter out = new PrintWriter(file);
				
				String temp = map.length+"#"+map[0].length;
				out.println(temp);
				
				for(int line = 0; line < map.length; line++){
					temp = "";
					for(int col = 0; col < map[0].length; col++){
						temp += map[line][col];
						if(col != map[0].length-1)temp += " ";
					}
					out.println(temp);					
				}
				out.println(""+numPlatform);
				for(Platform p:platforms){
					temp = p.x+" "+p.y+" "+p.width+" "+p.height+" "+p.friction;
					out.println(temp);
				}
				
				//ENEMIES PRINT
				out.println("0");
				out.close();
				
				
				
			}
			else{
				JOptionPane.showMessageDialog(null, "Could not save, problems choosing the file");
				return;
			}
			
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Could not save");
		}	
		
	}
	
	
	private void loadMap(){
		try{
			int resp = explorer.showOpenDialog(panel);
			File file;
			
			if(resp == JFileChooser.APPROVE_OPTION){
				file = explorer.getSelectedFile();
				String path = file.getAbsolutePath();
				if(path.charAt(path.length()-1)!='p'|| path.charAt(path.length()-4) != '.'||
						path.charAt(path.length()-2)!='a' || path.charAt(path.length()-3)!='m'){
					JOptionPane.showMessageDialog(null, "Not a map file!");
					return;
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "Ok then... Loading clear map");
				return;
			}
			
			Scanner in = new Scanner(file);
			
			String []temp = in.nextLine().split("#");
			
			int width = Integer.parseInt(temp[1]);
			int height = Integer.parseInt(temp[0]);
			
			int[][] map = new int[height][width];
			
			for(int line = 0; line < height; line++){
				temp = in.nextLine().split(" ");
				for(int col = 0; col < width; col++){
					map[line][col] = Integer.parseInt(temp[col]);
				}
			}
			
			int numPlatform = Integer.parseInt(in.nextLine());
			ArrayList<Platform>platforms = new ArrayList<Platform>();
			
			for(int i = 0; i < numPlatform; i++){
				temp = in.nextLine().split(" ");
				platforms.add(new Platform(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]),
						Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4])));
			}
			
			
			in.close();
			
			this.platforms = platforms;
			this.map = map;
			this.numPlatform = numPlatform;
			boxWidth.setText(""+map[0].length);
			boxHeight.setText(""+map.length);
			
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null,"Could not load map");
		}
		
		
	}
	
	
}
