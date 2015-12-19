package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Manager {
	private final int LEFT = 0,RIGHT = 1,UP = 2,DOWN = 3,MOUSE = 4;
	
	private boolean[] pressing;
	public boolean pressedEnter;
	
	private boolean showPlatforms;
	
	public int [][]map;
	
	private Platform[] platforms;
	
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
	
	private JButton up;
	private JButton down;
	private int positiony;
	
	private JFileChooser explorer;
	
	private Point mousePosition;
	
	
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
		
		pressing = new boolean[5];
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
		platforms = new Platform[numPlatform];
		
		positiony = 0;
		
		up = new JButton("^");
		down = new JButton("v");
		up.setBounds(1045,268,50,115);
		down.setBounds(1045,392,50,115);
		
		panel.add(up);
		panel.add(down);
		
		mousePosition = new Point(0, 0);
		
		String [] options = {"New Map","Load Map","Cancel"};
		
		int resp = JOptionPane.showOptionDialog(null,"Choose something","Map editor",JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,null,options, options[0]);
		
		
		explorer = new JFileChooser();
		
		if(resp == 2)System.exit(0);
		if(resp == 1){
			resp = explorer.showOpenDialog(panel);
			
			if(resp == JFileChooser.APPROVE_OPTION){
				File file = explorer.getSelectedFile();
				String path = file.getAbsolutePath();
				if(path.charAt(path.length()-1)!='p'|| path.charAt(path.length()-4) != '.'||
						path.charAt(path.length()-2)!='a' || path.charAt(path.length()-3)!='m'){
					JOptionPane.showMessageDialog(null, "Not a map file!");
					System.exit(0);
				}
				loadMap(path);
			}
			else{
				JOptionPane.showMessageDialog(null, "Ok then... Loading clear map");
			}
			
			
		}
		
	}
	
	public void update(){
		if(load.getModel().isPressed()){
			String path = JOptionPane.showInputDialog("Digite o endereco do arquivo");
			loadMap(path);
		}
		
		if(boxMode.getModel().isPressed() && !showPlatforms){
			showPlatforms = true;
		}
		if(tileMode.getModel().isArmed() && showPlatforms){
			showPlatforms = false;
		}
		
		for(Platform p:platforms)p.update(camera);
		
		if(up.getModel().isPressed()){
			if(positiony > 0)positiony -= 10;
		
		}
		if(down.getModel().isPressed()){
			positiony += 10;	
			
		}
		
		
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
				
				x = (int)(camera.getX()+mousePosition.x-10);
				y = (int)(510-mousePosition.y-camera.getY());
				if(x >= 0 && y>=0 && x < camera.getX()+camera.getW()-10 && y< -camera.getY()+camera.getH()-10)
					changeTile(x/tileWidth,y/tileHeight);
	}
		}
		
		
		boxWidth.update();
		boxHeight.update();
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
		
		
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_RIGHT && !pressing[RIGHT]){
			System.out.println("RIGHT");
			if(camera.getX() < tileWidth * (map[0].length-1) - camera.getW())
				camera.translate(tileWidth/2f, 0);	
			pressing[RIGHT] = true;
		}
		if(k == KeyEvent.VK_LEFT && !pressing[LEFT]){
			System.out.println("LEFT");
			if(camera.getX() > 0)
				camera.translate(-tileWidth/2f, 0);	
			pressing[LEFT] = true;
		}
		if(k == KeyEvent.VK_UP && !pressing[UP]){
			if(camera.getY() > -tileHeight*(map.length-1)+camera.getH())
			camera.translate(0,-tileWidth/2f);	
			pressing[UP] = true;
		}
		if(k == KeyEvent.VK_DOWN && !pressing[DOWN]){
			if(camera.getY() < 0)
				camera.translate(0, tileWidth/2f);
			pressing[DOWN] = true;
		}
		if(k == KeyEvent.VK_ENTER && !pressedEnter){
			pressedEnter = true;
		}
		if(k == KeyEvent.VK_DELETE){
			ribbon = 0;
		}
		
	}
	
	
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_RIGHT && pressing[RIGHT]){
			pressing[RIGHT] = false;
		}
		if(k == KeyEvent.VK_LEFT && pressing[LEFT]){
			pressing[LEFT] = false;
		}
		if(k == KeyEvent.VK_UP && pressing[UP]){
			pressing[UP] = false;
		}
		if(k == KeyEvent.VK_DOWN && pressing[DOWN]){
			pressing[DOWN] = false;
		}
		if(k == KeyEvent.VK_ENTER && pressedEnter){
			pressedEnter = false;
		}
		
	}
	
	public void mousePressed(MouseEvent e) {
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
				newMap[line][col] = -1;
			}
		}
		
		for(int line = 0; line < old.length; line++){
			for(int col = 0; col < old[0].length; col++){
				newMap[line][col] = old[line][col];
			}
		}
	}
	
	public void changeMap(int x, int y){
		int [][]novo = new int[y][x];
		copyMap(map,novo);
		map = novo;		
		camera.setPosition(0, 0);
		dimensions.setText("Map size: "+map[0].length+" x "+map.length);
		
		for(int i = 0; i < pressing.length; i++){
			pressing[i] = false;
		}
	}
	
	public void copyRibbon(int x, int y){
		ribbon = x+2*y;
		if(ribbon > tiles.length-1)ribbon = -1;
		
	}
	
	
	private void loadMap(String path){
		try{
			File file = new File(path);
			if(!file.exists()){
				JOptionPane.showMessageDialog(null, "File does not exist");
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
			Platform [] platforms = new Platform[numPlatform];
			
			for(int i = 0; i < numPlatform; i++){
				temp = in.nextLine().split(" ");
				platforms[i] = new Platform(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]),
						Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4]));
			}
			
			
			in.close();
			
			this.platforms = platforms;
			this.map = map;
			this.numPlatform = numPlatform;
			
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null,"Could not load map");
		}
		
		
	}
	
	
}
