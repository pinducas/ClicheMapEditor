package elements;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MultipleInputPane{
	
	private JTextField [] fields;
	private JPanel myPanel;
	
	public int [] getNumberInputs(String text,String[] fieldText,String [] fieldValue,int numFields){
		if(numFields == 0)return null;
		if(fieldValue == null){
			fieldValue = new String[numFields];
			for(int i = 0; i < numFields; i++)fieldValue[i]="";
		}
		if(fieldText == null){
			fieldText = new String[numFields];
			for(int i = 0; i < numFields; i++)fieldText[i]="";
		}
		if(fieldValue.length != fieldText.length)return null;
		if(fieldValue.length != numFields)return null;		
		
		myPanel = new JPanel();
		fields  = new JTextField[numFields];
		
		for(int i = 0; i < fieldText.length; i++){
			fields[i] = new JTextField(5);
			fields[i].setText(fieldValue[i]);
			myPanel.add(new JLabel(fieldText[i]));
			myPanel.add(fields[i]);
			myPanel.add(Box.createHorizontalStrut(4));
		}
		
		int result = JOptionPane.showConfirmDialog(null,myPanel,text,JOptionPane.OK_CANCEL_OPTION);
		
		int [] answer = new int[numFields];
		
		if(result == JOptionPane.OK_OPTION){
			for(int i = 0; i < fieldText.length; i++){
				try{
					String temp = fields[i].getText();
					if(temp.length() == 0 || temp.length() > 3)return null;
					answer[i] = Integer.parseInt(temp);
				}catch(Exception e){
					return null;
				}
			}
		}
			
		return answer;
	}
	

}
