package elements;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import core.Panel;
import managers.Manager;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar{
	
	private Manager manager;
		
	private JMenu tempMenu;
	private JMenuItem newMapItem;
	
	private boolean canDraw;
	
	public MenuBar(Manager manager,Panel panel){
		this.manager = manager;
		
		tempMenu = new JMenu("Test");
		tempMenu.setMnemonic(KeyEvent.VK_F1);
		tempMenu.getAccessibleContext().setAccessibleDescription("Testing this stuff");
		this.add(tempMenu);
		
		newMapItem = new JMenuItem("New Map");
		newMapItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		newMapItem.getAccessibleContext().setAccessibleDescription("Clear the current map and returns a new blank one");		
		tempMenu.add(newMapItem);
		
		panel.getFrame().setJMenuBar(this);
		
		canDraw = true;
	}
	
	public void update(){
		if(manager == null)manager.saveMap();

		if(tempMenu.getModel().isSelected()){
			canDraw = false;
		}
		else{
			canDraw = true;
		}
		
		if(newMapItem.getModel().isPressed()){
			JOptionPane.showMessageDialog(null, "SUCK IT BITCHES");
		}
	
	}
	
	
	public boolean canDraw(){return canDraw;}
}
