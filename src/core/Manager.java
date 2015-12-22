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
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Manager {
	private final int LEFT = 0,RIGHT = 1,UP = 2,DOWN = 3,MOUSE = 4, A = 5, S = 6,D = 7, W = 8;
	
	private boolean[] pressing;
	public boolean pressedEnter;
	private int creation;
	private boolean changedMode;
	
	private boolean control;
	private boolean z;
	private boolean redo;
	
	private int newWidth;
	private int newHeight;
	private int newX;
	private int newY;
	
	private boolean showPlatforms;
	
	public int [][]map;
	
	private ArrayList<Platform> platforms;
	
	private ArrayList<String> control_z;
	private Point lastTileChanged;
	private Point lastPlatform;
	private int lastId;
	
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
	
	private JButton changeMode;
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
	
	private JLabel currentTool;
	private JLabel mode;
	private JLabel widthResult;
	private JLabel heightResult;
	
	private JTextField widthMult;
	private JTextField heightMult;
	
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
		
		boxWidth = new SizeBox(panel, this,1000, 545, 40,30, ""+map[0].length,0);
		boxHeight = new SizeBox(panel, this,1060, 545, 40,30, ""+map.length,1);
		
		x = new JLabel("X");
		x.setBounds(1044,545,40,30);
		panel.add(x);
		
		pressing = new boolean[9];
		for(int i = 0; i < pressing.length; i++)
			pressing[i] = false;
		
		camera = new Camera(0, 0, 780, 500);
		
		ribbon = -1;
		
		dimensions = new JLabel("Map size ");
		dimensions.setBounds(920,540,250,40);
		panel.add(dimensions);
		
		changeMode = new JButton("To Box Mode");
		changeMode.setBounds(10,540,140,40);
		panel.add(changeMode);
		
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
		
		currentTool = new JLabel("Current Tool: Delete Tile");
		currentTool.setBounds(800, 205, 520, 50);
		panel.add(currentTool);
		mode = new JLabel("Mode: Tile");
		mode.setBounds(800, 225, 520, 50);
		panel.add(mode);
		
		widthResult = new JLabel("X Tile Width = 0");
		heightResult = new JLabel("X Tile Height = 0");
		
		widthResult.setBounds(664, 510, 520, 50);
		heightResult.setBounds(664, 550, 520, 50);

		widthMult = new JTextField("0");
		heightMult = new JTextField("0");
		
		widthMult.setBounds(620, 520, 40, 30);
		heightMult.setBounds(620, 560, 40, 30);

		
		panel.add(widthMult);
		panel.add(heightMult);
		panel.add(widthResult);
		panel.add(heightResult);
		
		mousePosition = new Point(0, 0);
		
		String [] options = {"New Map","Load Map","Cancel"};
		
		int resp = JOptionPane.showOptionDialog(null,"Choose something","Map editor",JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,null,options, options[0]);
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".map FILES", "map");
		explorer = new JFileChooser();
		explorer.setFileFilter(filter);
		
		selectedPlatform = null;
		
		creation = 0;
		
		newWidth = 0;
		newHeight = 0;
		newX = 0;
		newY = 0;
		
		if(resp == 2)System.exit(0);
		if(resp == 1){
			loadMap();		
		}
		changedMode = false;
		
		control_z = new ArrayList<String>();
		
		control = z = redo = false;
		
		lastTileChanged = new Point(-1, -1);
		lastPlatform = new Point(-1, -1);
		lastId = -1;
	}
	
	public void update(){	
		if(load.getModel().isPressed())loadMap();
		if(save.getModel().isPressed())saveMap();
		if(changeMode.getModel().isPressed() && !changedMode){
			showPlatforms = !showPlatforms;
			if(showPlatforms){
				mode.setText("Mode: Platforms");
				changeMode.setText("To Tile Mode");
				currentTool.setText("Current Tool: Select Platform");
				boxtool = 3;
			}
			else {
				mode.setText("Mode: Tiles");
				changeMode.setText("To Box Mode");
				currentTool.setText("Current Tool: Delete Tile");
				lastTileChanged = new Point(-1,-1);
				ribbon = 0;
			}
			changedMode = true;
		}
		if(!changeMode.getModel().isPressed())changedMode = false;
		if(up.getModel().isPressed() && positiony > 0)positiony -= 10;
		if(down.getModel().isPressed())positiony += 10;
		if(createbox.getModel().isPressed())boxtool = 0;
		if(deletebox.getModel().isPressed())boxtool = 1;
		if(movebox.getModel().isPressed())boxtool = -1;
		if(changebox.getModel().isPressed())boxtool = 2;
		if(selectbox.getModel().isPressed())boxtool = 3;
		
		for(Platform p:platforms)p.update(camera);
		
		
		if(control && z && !redo){
			redo = true;
			if(!control_z.isEmpty() ){
				String[] temp = control_z.get(control_z.size()-1).split(" ");
				control_z.remove(control_z.size()-1);
				try{
					if(temp[0].equals("Tile")){
						map[Integer.parseInt(temp[2])][Integer.parseInt(temp[1])] = Integer.parseInt(temp[3]);
					}
					else if(temp[0].equals("Move")){
						Platform a = null;
						int id = Integer.parseInt(temp[3]);
						for(Platform p:platforms){
							if(p.id == id){
								a = p;
								break;
							}
						}
						a.x = Integer.parseInt(temp[1]);
						a.y = Integer.parseInt(temp[2]);
					}
					else if(temp[0].equals("Change")){
						Platform a = null;
						int id = Integer.parseInt(temp[5]);
						for(Platform p:platforms){
							if(p.id == id){
								a = p;
								break;
							}
						}
						a.x = Integer.parseInt(temp[1]);
						a.y = Integer.parseInt(temp[2]);
						a.width = Integer.parseInt(temp[3]);
						a.height = Integer.parseInt(temp[4]);
					}
					else if(temp[0].equals("Create")){
						Platform a = null;
						int id = Integer.parseInt(temp[1]);
						for(Platform p:platforms){
							if(p.id == id){
								a = p;
								break;
							}
						}						
						platforms.remove(a);
						
					}
					else if(temp[0].equals("Delete")){
						int x = Integer.parseInt(temp[1]);
						int y = Integer.parseInt(temp[2]);
						int w = Integer.parseInt(temp[3]);
						int h = Integer.parseInt(temp[4]);
						Platform p = new Platform(x, y, w, h, 1, numPlatform);
						numPlatform++;
						platforms.add(p);
					}
					
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(null, "Error trying to undo");
				}
			}
			
		}
		
		if(!showPlatforms){
			if(ribbon == 0){
				currentTool.setText("Current Tool: Delete Tile");
			}
			else{
				currentTool.setText("Current Tool: Drawing Tile "+ribbon);
			}
		}
		else{
			switch(boxtool){
				case -1:
					currentTool.setText("Current Tool: Move Platform");
					break;
				case 0:
					currentTool.setText("Current Tool: Create Platform");
					break;
				case 1:
					currentTool.setText("Current Tool: Delete Platform");
					break;
				case 2:
					currentTool.setText("Current Tool: Platform Properties");
					break;
				case 3:
					currentTool.setText("Current Tool: Select Platform");
					break;
			}
		}
		
		
		if(pressing[MOUSE]){
			int x,y;
			if(mousePosition.x > 800 && mousePosition.y> 268){
				boxtool = 3;
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
				
			}//STOPED HERE
			else if(showPlatforms){
				panel.requestFocus();

				if(selectedPlatform == null){
					Rectangle m = new Rectangle(mouseMapPos().x-2-(int)camera.getX(),
							500-mouseMapPos().y-2-(int)camera.getY(),4,4);
					for(Platform p:platforms){
						if(p.getRect(camera).intersects(m)){
							selectedPlatform = p;
							break;
						}
					}
				}
				
				if(boxtool == -1){
					creation = 0;
					if(selectedPlatform != null){
						if(lastId == -1){
							lastPlatform = new Point(selectedPlatform.x,selectedPlatform.y);
							lastId = selectedPlatform.id;
						}
						selectedPlatform.x = mouseMapPos().x;
						selectedPlatform.y = mouseMapPos().y;
					}
				}
				if(boxtool == 2){
					creation = 0;
					if(selectedPlatform != null){
						String resp = "";
						int tempBox[] = new int[4];
						tempBox[0] = selectedPlatform.x;
						tempBox[1] = selectedPlatform.y;
						tempBox[2] = selectedPlatform.width;
						tempBox[3] = selectedPlatform.height;
						
						try{
							resp = JOptionPane.showInputDialog("Current platform values",(int)(selectedPlatform.x-selectedPlatform.width/2)+"x"+
						(int)(selectedPlatform.y - selectedPlatform.height/2)+"x"+selectedPlatform.width+"x"+selectedPlatform.height);
						}catch(Exception e){
							selectedPlatform = null;
							boxtool = 3;
							return;
						}
						if(resp == null){
							selectedPlatform = null;
							boxtool = 3;
							return;
						}
						if(resp.length() < 7){
							selectedPlatform = null;
							boxtool = 3;
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
									 w > (map[0].length-1)*tileWidth ||
									 h > (map.length-1)*tileHeight){
								boxtool = 3;
								return;
							}
							
							selectedPlatform.x = tx;
							selectedPlatform.y = ty;
							selectedPlatform.width = w;
							selectedPlatform.height = h;
							
							control_z.add("Change "+tempBox[0]+" "+tempBox[1]+" "+tempBox[2]+" "+tempBox[3]+" "+selectedPlatform.id);
							
							selectedPlatform = null;
							
							boxtool = 3;
						}catch(Exception e){
							selectedPlatform = null;
							boxtool = 3;
							return;
						}
						
					}	
				}
				if(boxtool == 0){
					if(creation == 0){
						creation = 1;
						newX = mouseMapPos().x;
						newY = mouseMapPos().y;
					}					
				}
				if(boxtool == 1){
					creation = 0;
					if(selectedPlatform != null){
						control_z.add("Delete "+selectedPlatform.x+" "+selectedPlatform.y+" "+selectedPlatform.width+" "+selectedPlatform.height);
						platforms.remove(selectedPlatform);
						selectedPlatform = null;
						boxtool = 3;
					}
				}				
			}
		}
		
		
		if(boxtool == 0){
			if(creation == 2){
				String resp = "";
				try{
					resp = JOptionPane.showInputDialog("New tile dimensions",tileWidth+"x"+tileHeight);
				}catch(Exception e){
					boxtool = 3;
					creation = 0;
					return;
				}
				
				try{
					String []temp = resp.split("x");
					if(temp.length < 2)return;
					int w =  Integer.parseInt(temp[0]);
					int h =  Integer.parseInt(temp[1]);
					
					if(w > (map[0].length-1)*tileWidth ||
							 h > (map.length-1)*tileHeight){
						boxtool = 3;
						return;
					}
					
					Platform p = new Platform(mouseMapPos().x, mouseMapPos().y, w, h, 1,numPlatform);
					platforms.add(p);
					numPlatform++;
					selectedPlatform = p;
					selectedPlatform.width = w;
					selectedPlatform.height = h;
					
					control_z.add("Create "+p.id);
					
					boxtool = 3;
				}catch(Exception e){
					selectedPlatform = null;
					boxtool = 3;
					creation = 0;
					return;
				}
			}
			if(creation == 3){
				
				int tempX = newX;
				int tempY = newY;
				int tempW = newWidth;
				int tempH = newHeight;
				
				if(newWidth < 0){
					tempX += newWidth;
					tempW = -tempW;
				}
				if(newHeight < 0){
					tempY -= newHeight;
					tempH = -tempH;
				}
				
				tempX += (int)(tempW/2);
				tempY -= (int)(tempH/2);
				
				Platform p = new Platform(tempX,tempY,tempW,tempH,1,numPlatform);
				platforms.add(p);
				numPlatform++;
				newX = 0;
				newY = 0;
				newWidth = 0;
				newHeight= 0;
				creation = 0;
				
				control_z.add("Create "+p.id);
				
				boxtool = 3;
			}
			
		}
		
		if(widthMult.getText() != null){
			if(widthMult.getText() != "0"){
				int value = 0;
				try{
					value = Integer.parseInt(widthMult.getText());
					if(value >=0 && value <= 100){
						widthResult.setText("X Tile Width = "+(value*tileWidth));
					}
				}catch(Exception e){
					
				}
			}			
		}
		if(heightMult.getText() != null){
			if(heightMult.getText() != "0"){
				int value = 0;
				try{
					value = Integer.parseInt(heightMult.getText());
					if(value >=0 && value <= 100){
						heightResult.setText("X Tile Height = "+(value*tileHeight));
					}
				}catch(Exception e){
					
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
		
		
		if(creation == 1){
			gm.setColor(new Color(0,254,0,100));
						
			int tempX = newX - (int)camera.getX();
			int tempY = 500 - newY - (int)camera.getY();
			int tempW = newWidth;
			int tempH = newHeight;
			
			if(newWidth < 0){
				tempX += newWidth;
				tempW = -tempW;
			}
			if(newHeight < 0){
				tempY += newHeight;
				tempH = -tempH;
			}
			
			gm.fillRect(tempX,tempY,tempW,tempH);
			
			
			gm.setColor(Color.WHITE);
		}

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
			lastTileChanged = new Point(-1,-1);
			ribbon = 0;
			boxtool = 1;
		}
		
		if(k == KeyEvent.VK_CONTROL && !control && !redo){
			control = true;
		}
		if(k == KeyEvent.VK_Z && !z && !redo){
			z = true;
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
		if(k == KeyEvent.VK_CONTROL && control){
			control = false;
			redo = false;
		}
		if(k == KeyEvent.VK_Z && z){
			z = false;
			redo = false;
		}
		
	}
	
	public void mousePressed(MouseEvent e) {
		if(!pressing[MOUSE])selectedPlatform = null;
		pressing[MOUSE] = true;
	}
	
	public void mouseReleased(MouseEvent e) {
		pressing[MOUSE] = false;
		if(creation == 1){
			if((newWidth > -tileWidth/5 && newWidth < tileWidth/5) || (newHeight > -tileHeight/5 && newHeight < tileHeight/5))
				creation = 2;
			else
				creation = 3;		
		}
		if(boxtool == -1 && lastId != -1){
			control_z.add("Move "+lastPlatform.x+" "+lastPlatform.y+" "+lastId);
			lastPlatform = new Point(-1, -1);
			lastId = -1;
		}
		
	}
	
	
	public void mouseMoved(MouseEvent e){
		mousePosition.x = e.getX();
		mousePosition.y = e.getY();
	}
	
	public void mouseDragged(MouseEvent e){
		mousePosition.x = e.getX();
		mousePosition.y = e.getY();
		
		if(creation == 1){
			newWidth = mouseMapPos().x - newX;
			newHeight = newY-mouseMapPos().y ;
		}
		
	}
	
	public void changeTile(int x,int y){
		if(ribbon == 0 && (lastTileChanged.x != x || lastTileChanged.y != y)){
			control_z.add("Tile "+x+" "+y+" "+map[y][x]);
			map[y][x] = -1;
			lastTileChanged.x = x;
			lastTileChanged.y = y;
		}
		else if(lastTileChanged.x != x || lastTileChanged.y != y){
			control_z.add("Tile "+x+" "+y+" "+map[y][x]);
			map[y][x] = ribbon;
			lastTileChanged.x = x;
			lastTileChanged.y = y;
		}
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
		lastTileChanged = new Point(-1,-1);
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
				out.println(""+platforms.size());
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
						Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4]),i));
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
