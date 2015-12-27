package elements;

import javax.swing.JPanel;

import managers.Manager;

@SuppressWarnings("serial")
public class SizeBox extends TextBox{

	private Manager manager;
	private int tipo;
	
	public SizeBox(JPanel panel,Manager manager, int x, int y, int width, int height, String text,int tipo) {
		super(panel, x, y, width, height, text);
		this.tipo = tipo;
		this.manager = manager;
	}
	
	public void update(){
		String a = getText();
		
		try{
				if(tipo == 0){
					int width = Integer.parseInt(a);
					if(width > 10 && width < 200 && width != manager.map[0].length){
						manager.changeMap(width, manager.map.length);
					}
				}
				if(tipo == 1){
					int height = Integer.parseInt(a);
					if(height > 10 && height < 200 && height != manager.map.length){
						manager.changeMap(manager.map[0].length, height);
					}	
				}
				
				
		}catch(Exception e){
				
		}		
	}

	
}
