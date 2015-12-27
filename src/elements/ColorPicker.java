package elements;

import java.awt.Color;

import javax.swing.JOptionPane;

public class ColorPicker {

	private String title;
	
	public ColorPicker(String name){
		title = name;
	}
	
	public Color getColor(){
		ColorPickerPanel panel = new ColorPickerPanel();
		int result = JOptionPane.showConfirmDialog(null,panel,title,JOptionPane.OK_CANCEL_OPTION);		
		
		if(result == JOptionPane.OK_OPTION){
			
			return panel.getColor();
			
		}
		return null;
	}
	
}
