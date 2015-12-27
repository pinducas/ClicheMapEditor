package elements;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import core.Panel;
import managers.Manager;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar implements ActionListener{
	
	private final int FILE = 0, OPTION = 1;
	private final int NEWMAP = 0, SAVEMAP = 1, LOADMAP = 2, CHANGECOLOR = 3, TILESPEED = 4;
	
	private Manager manager;
	
	private JMenu [] menu;
	private JMenuItem[] menuItem;
	
	
	private JMenu currentSelectedMenu;
	private boolean selected;
	
	private boolean canDraw;
	
	public MenuBar(Manager manager,Panel panel){
		this.manager = manager;
		
		menu = new JMenu[2];
		menuItem = new JMenuItem[5];
		
		menu[FILE] = new JMenu("File");
		menu[FILE].setMnemonic(KeyEvent.VK_F1);
		menu[FILE].getAccessibleContext().setAccessibleDescription("File operations");
		this.add(menu[FILE]);
		
		menu[OPTION] = new JMenu("Options");
		menu[OPTION].setMnemonic(KeyEvent.VK_F2);
		menu[OPTION].getAccessibleContext().setAccessibleDescription("Misc options");
		this.add(menu[OPTION]);
		
		menuItem[NEWMAP] = new JMenuItem("New Map");
		menuItem[NEWMAP].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		menuItem[NEWMAP].getAccessibleContext().setAccessibleDescription("Clear the current map and returns a new blank one");		
		menu[FILE].add(menuItem[NEWMAP]);
		
		menuItem[SAVEMAP] = new JMenuItem("Save Map");
		menuItem[SAVEMAP].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		menuItem[SAVEMAP].getAccessibleContext().setAccessibleDescription("Saves the current map to a map file");		
		menu[FILE].add(menuItem[SAVEMAP]);
		
		menuItem[LOADMAP] = new JMenuItem("Open map");
		menuItem[LOADMAP].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		menuItem[LOADMAP].getAccessibleContext().setAccessibleDescription("Loads a map file");		
		menu[FILE].add(menuItem[LOADMAP]);
		
		menuItem[CHANGECOLOR] = new JMenuItem("Cursor Color");
		menuItem[CHANGECOLOR].getAccessibleContext().setAccessibleDescription("Loads a map file");		
		menu[OPTION].add(menuItem[CHANGECOLOR]);
		
		menuItem[TILESPEED] = new JMenuItem("Tile Search Speed");
		menuItem[TILESPEED].getAccessibleContext().setAccessibleDescription("Changes the speed of the tile browse buttons");		
		menu[OPTION].add(menuItem[TILESPEED]);
		
		
		for(JMenuItem jm:menuItem)jm.addActionListener(this);
		
		
		panel.getFrame().setJMenuBar(this);
		
		canDraw = true;
		currentSelectedMenu = null;
		selected = false;
	}
	
	public void update(){
		if(manager == null)manager.saveMap();		
			
		if(menu[FILE].isSelected() && !selected){
			selected = true;
			canDraw = false;
			currentSelectedMenu = menu[FILE];
		}
		if(menu[OPTION].isSelected() && !selected){
			selected = true;
			canDraw = false;
			currentSelectedMenu = menu[OPTION];
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
		
	} 
}
