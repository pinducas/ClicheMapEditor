package elements;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.Panel;
import managers.Manager;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar implements ActionListener{
	
	private final int FILE = 0, OPTION = 1,HELP = 2;
	private final int NEWMAP = 0, SAVEMAP = 1, LOADMAP = 2, CHANGECOLOR = 3, TILESPEED = 4,ABOUT = 5,
			RESET= 6,CAMSPEED = 7,CHANGETILE = 8;
	
	private Manager manager;
	
	private JMenu [] menu;
	private JMenuItem[] menuItem;
	
	
	private JMenu currentSelectedMenu;
	private boolean selected;
	
	private boolean canDraw;
	
	public MenuBar(Manager manager,Panel panel){
		this.manager = manager;
		
		menu = new JMenu[3];
		menuItem = new JMenuItem[9];
		
		menu[FILE] = new JMenu("File");
		menu[FILE].setMnemonic(KeyEvent.VK_F1);
		menu[FILE].getAccessibleContext().setAccessibleDescription("File operations");
		this.add(menu[FILE]);
		
		menu[OPTION] = new JMenu("Options");
		menu[OPTION].setMnemonic(KeyEvent.VK_F2);
		menu[OPTION].getAccessibleContext().setAccessibleDescription("Misc options");
		this.add(menu[OPTION]);
		
		menu[HELP] = new JMenu("Help");
		menu[HELP].setMnemonic(KeyEvent.VK_F3);
		menu[HELP].getAccessibleContext().setAccessibleDescription("Help and misc stuff");
		this.add(menu[HELP]);
		
		menuItem[NEWMAP] = new JMenuItem("New Map");
		menuItem[NEWMAP].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		menuItem[NEWMAP].getAccessibleContext().setAccessibleDescription("Clear the current map and returns a new blank one");		
		menu[FILE].add(menuItem[NEWMAP]);
				
		menuItem[LOADMAP] = new JMenuItem("Open map");
		menuItem[LOADMAP].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		menuItem[LOADMAP].getAccessibleContext().setAccessibleDescription("Loads a map file");		
		menu[FILE].add(menuItem[LOADMAP]);
		
		menuItem[SAVEMAP] = new JMenuItem("Save Map");
		menuItem[SAVEMAP].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		menuItem[SAVEMAP].getAccessibleContext().setAccessibleDescription("Saves the current map to a map file");		
		menu[FILE].add(menuItem[SAVEMAP]);
		
		menuItem[CAMSPEED] = new JMenuItem("Camera Speed");
		menuItem[CAMSPEED].getAccessibleContext().setAccessibleDescription("Changes the speed of the camera");		
		menu[OPTION].add(menuItem[CAMSPEED]);
		
		menuItem[TILESPEED] = new JMenuItem("Tile Search Speed");
		menuItem[TILESPEED].getAccessibleContext().setAccessibleDescription("Changes the speed of the tile browse buttons");		
		menu[OPTION].add(menuItem[TILESPEED]);
				
		menuItem[CHANGECOLOR] = new JMenuItem("Cursor Color");
		menuItem[CHANGECOLOR].getAccessibleContext().setAccessibleDescription("Loads a map file");		
		menu[OPTION].add(menuItem[CHANGECOLOR]);
		
		menuItem[CHANGETILE] = new JMenuItem("Change tileset");
		menuItem[CHANGETILE].getAccessibleContext().setAccessibleDescription("Change the tileset");		
		menu[OPTION].add(menuItem[CHANGETILE]);
		
		menuItem[ABOUT] = new JMenuItem("Help me");
		menuItem[ABOUT].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,ActionEvent.CTRL_MASK));
		menuItem[ABOUT].getAccessibleContext().setAccessibleDescription("Shows the commands");		
		menu[HELP].add(menuItem[ABOUT]);
		
		menuItem[RESET] = new JMenuItem("Reset Configs");
		menuItem[RESET].getAccessibleContext().setAccessibleDescription("Reset all configurations");		
		menu[HELP].add(menuItem[RESET]);
		
		for(JMenuItem jm:menuItem)jm.addActionListener(this);
		
		
		panel.getFrame().setJMenuBar(this);
		
		canDraw = true;
		currentSelectedMenu = null;
		selected = false;
	}
	
	public void update(){
		if(manager == null)manager.saveMap();		
		
		for(int i = 0; i < menu.length;i++){
			if(menu[i].isSelected() && !selected){
				selected = true;
				canDraw = false;
				currentSelectedMenu = menu[i];
			}
		}
	
		if(selected){
			if(!currentSelectedMenu.isSelected()){
				canDraw = true;
				selected = false;
				currentSelectedMenu = null;
			}
		}	
	}
	
	
	public boolean canDraw(){return canDraw;}

	@Override
	public void paint(java.awt.Graphics g) {
		super.paint(g);
		for(JMenuItem m:menuItem)m.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if(o == menuItem[NEWMAP]){
			int temp = JOptionPane.showConfirmDialog(null, "Are you sure you want to create a new map? Cancel and save your current work!");
			if(temp == JOptionPane.OK_OPTION){
				manager.newMap();
			}
		}
		else if(o == menuItem[SAVEMAP]){
			manager.saveMap();
		}
		else if(o == menuItem[LOADMAP]){
			manager.loadMap();
		}
		else if(o == menuItem[CHANGECOLOR]){
			MultipleInputPane pane = new MultipleInputPane();
			String [] fieldText = {"R: ","G: ","B: ","A: "};
			Color c = manager.getCursorColor();
			String [] fieldValue = {""+c.getRed(),""+c.getGreen(),""+c.getBlue(),""+c.getAlpha()};
			int [] resp = pane.getNumberInputs("Define the R G B A values", fieldText, fieldValue, 4);
			if(resp != null){
				manager.setCursorColor(new Color(resp[0],resp[1],resp[2],resp[3]));
			}			
		}
		else if(o == menuItem[TILESPEED]){
			String resp = JOptionPane.showInputDialog("What speed would you like it?",""+manager.getTileSpeed());
			if(resp == null){
				JOptionPane.showMessageDialog(null, "Oops, could not change tile browse speed");
				return;
			}
			try{
				int speed = Integer.parseInt(resp);
				manager.setTileSpeed(speed);
			}
			catch(Exception ee){
				JOptionPane.showMessageDialog(null, "Oops, could not change tile browse speed");
				return;
			}
		}
		
		else if(o == menuItem[ABOUT]){
			JOptionPane.showMessageDialog(null, "Welcome to the 2015 map editor\n\n"
					+ "The big left panel is where the map preview will be shown and where you will click to create and change objects in the map.\n"
					+ "The right panel is where you select your current tile. To that by clicking on it.\n"
					+ "To save your map press CONTROL+S or go to FILE->SAVE\n"
					+ "To open an existing map press CONTROL+O or go to FILE->OPEN\n"
					+ "To create a new empty map pres CONTROL+N or go to FILE->NEW MAP\n"
					+ "\n\nCreated by Paulo Gaspar and Allan Alves for their gaming engine");			
		}
		else if(o == menuItem[RESET]){
			int temp = JOptionPane.showConfirmDialog(null, "Are you sure you want to create a new map? Cancel and save your current work!");
			if(temp == JOptionPane.OK_OPTION){
				manager.defaultConfig();
				manager.saveConfig();
			}
		}
		else if(o == menuItem[CAMSPEED]){
			MultipleInputPane pane = new MultipleInputPane();
			String [] fieldText = {"X SPEED: ","Y SPEED: "};
			String [] fieldValue = {""+manager.getCameraSpeed().x,""+manager.getCameraSpeed().y};
			int [] resp = pane.getNumberInputs("Define the camera speed in pixels", fieldText, fieldValue, 2);
			if(resp != null){
				manager.setCameraSpeed(new Point(resp[0],resp[1]));
			}
		}
		else if(o == menuItem[CHANGETILE]){
			FileNameExtensionFilter filter = new FileNameExtensionFilter(".png FILES", "png");
			JFileChooser explorer = new JFileChooser();
			explorer.setFileFilter(filter);
			
			int r = explorer.showOpenDialog(null);
			
			if(r != JFileChooser.APPROVE_OPTION)return;
			if(!explorer.getSelectedFile().exists())return;
			
			String a = explorer.getSelectedFile().getAbsolutePath();
			
				
			MultipleInputPane pane = new MultipleInputPane();
			String [] fieldText = {"Tiles Accross x: ","Tiles Accross y: "};
			String [] fieldValue = {""+manager.getMapDim()[0],""+manager.getMapDim()[1]};
			
			int [] resp = pane.getNumberInputs("Define the number of tiles in the seet", fieldText, fieldValue, 2);
			if(resp != null)manager.setCameraSpeed(new Point(resp[0],resp[1]));
			
			JOptionPane.showMessageDialog(null, "PATH: "+a+" SIZES "+resp[0]+" "+resp[1]);
			
			manager.setTilesAccross(resp[0], resp[1]);
			manager.setImagePath(a);
			
			
			manager.saveConfig();
			//manager.loadConfig();
		}
		
	} 
}
