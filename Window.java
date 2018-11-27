import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

import javax.swing.*;


public class Window {

	private int width;
	private int height;
	JButton[] buttons = new JButton[10];
	
	InteractionPanel intPanel;
	NumberPanel numPanel;
	OperationPanel opPanel;
	BasePanel basePanel;
	Handler handler;
	
	public Window(int width, int height ){
		this.width = width;
		this.height = height;
		JFrame frame = new JFrame();
		
		
		handler = new Handler();
		intPanel = new InteractionPanel(handler);
		numPanel = new NumberPanel(handler);
		opPanel = new OperationPanel(handler);
		basePanel = new BasePanel(handler);

		
		frame.add(basePanel, BorderLayout.WEST);
		frame.add(intPanel, BorderLayout.SOUTH);
		frame.add(numPanel, BorderLayout.CENTER);
		frame.add(opPanel, BorderLayout.EAST);
		
		
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(width,height));
		frame.setMaximumSize(new Dimension(width,height));
		frame.setMinimumSize(new Dimension(width,height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	}
	
	// private class Handler
	// event handler 
	private class Handler{
		
		int base;
		boolean buttonPressed;
		BigInteger currentOutput;
		String currentInput;
		
		private Handler(){
			buttonPressed = false;
			currentOutput = new BigInteger("0");
			currentInput = "0";
		}
		
		// real questionable visibility
		void setInput(String s){
			s = intPanel.getInput() + s ;
			intPanel.setInput(s);
		}
		
		void clearInput(){
			intPanel.setInput("");
		}
		
		// given a string currentInput from inputLabel, returns BigInt toString currentOuput
		String evaluate(String input){
			currentInput = input;
			
			
			char[] arr = currentInput.toCharArray();
			char operand = 'e'; int operandIndex = arr.length;  
			for(int i = 0; i < currentInput.length(); i++){
				if(!Character.isDigit(arr[i]) && !Character.isAlphabetic(arr[i])){
					operand = arr[i];
					operandIndex = i;
				}
			}
			if(operand == 'e'){
				return currentInput;
			}
			String s = "";
			for(int i = 0; i < operandIndex; i++){
				s += arr[i];
			}
			
			BigInteger num1 = new BigInteger(s,base);
			s = "";
			
			for(int i = operandIndex + 1; i < currentInput.length(); i++){
				s += arr[i];
			}
			BigInteger num2 = new BigInteger(s, base);
			
			switch(operand){
				case '+':
					currentOutput = num1.add(num2);
					break;
				case '-':
					currentOutput = num1.subtract(num2);
					break;
				case '*':
					currentOutput = num1.multiply(num2);
					break;
				case '/': 
					currentOutput = num1.divide(num2);
					break;	
			}
			
			return currentOutput.toString(base);
			
		}
		
	
		void setBase(String s){
			base = Integer.parseInt(s);
			numPanel.fillButtons();
		}
		
		
		void setBase(int n){
			base = n;
		}
		
	}
	
	// private class InteractionPanel
	// JPanel object that displays input,output and equals button
 	private class InteractionPanel extends JPanel{
		
		JLabel inputLabel;
		JLabel outputLabel;
		JButton equalsButton;
		Handler h;
		
		private InteractionPanel(Handler handler){
			
			h = handler;
			inputLabel = new JLabel("");
			outputLabel = new JLabel("");
			equalsButton = new JButton("=");
			
			equalsButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					outputLabel.setText(h.evaluate(inputLabel.getText()));
				}
				
			});
			
			this.add(inputLabel);
			this.add(equalsButton);
			this.add(outputLabel);
			this.setLayout(new GridLayout(1,3));
		}
		
		String getInput(){
			return inputLabel.getText();
		}
		void setInput(String s){
			inputLabel.setText(s);
		}
		
		String getOutput(){
			return outputLabel.getText();
		}
		void setOutput(String s){
			outputLabel.setText(s);
		}
	}
	
 	// private class BasePanel
 	// JPanel object that displays buttons for changing the base
 	private class BasePanel extends JPanel{
 		
 		JButton[] buttons = new JButton[8];
 		Handler h;
 		
 		private BasePanel(Handler handler){
 			h = handler;
 			
 			this.add(new JLabel("Select Base"));
 			for(int i = 0; i < buttons.length; i++ ){
 				 buttons[i] = new JButton(Integer.toString(2 * i + 2));
 				 this.add(buttons[i]);
 			}
 			for(int i = 0; i < buttons.length; i++){
				String s = buttons[i].getText();
				buttons[i].addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						h.setBase(s);
					}
					
				});
			}
 			this.setLayout(new GridLayout(9,1));
 		}
 	}
 	
	// private class NumberPanel
 	// JPanel object that displays the number buttons
	private class NumberPanel extends JPanel{
		
		JButton[] buttons = new JButton[10];
		Handler h;
		
		private NumberPanel(Handler handler){
			h = handler;
			
			fillButtons();
		}
		
		//fills buttons based on the base given by handler h
		void fillButtons(){
			
			this.removeAll();
			this.updateUI();
			
			int n = h.base;
			buttons = new JButton[n];
			
			for(int i = 0; i < n; i++){
				if( i < 10 )
					buttons[i] = new JButton(Integer.toString(i));
				else{
					char c;
					switch(i){
						case 10: c = 'A';
							break;
						case 11: c = 'B';
							break;
						case 12: c = 'C';
							break;
						case 13: c = 'D';
							break;
						case 14: c = 'E';	
							break;
						case 15: c = 'F';
							break;
						default: c = ' ';
					}
					buttons[i] = new JButton(Character.toString(c));	
				}
				this.add(buttons[i]);
			}
			
			for(int i = 0; i < n; i++){
					String s = buttons[i].getText();
					buttons[i].addActionListener(new ActionListener(){
	
						@Override
						public void actionPerformed(ActionEvent e) {
							h.setInput(s);
						}
						
					});
				}
			this.setLayout(new GridLayout(3,h.base / 2));
		}
	}
	
	// private class OperationPanel
	// JPanel object that displays operations ( +,-,/,*)
	private class OperationPanel extends JPanel{

		JButton aButton;
		JButton sButton;
		JButton mButton;
		JButton dButton;
		JButton clearButton;
		JButton[] buttons = new JButton[4];
		Handler h;
		
		private OperationPanel(Handler handler){
			
			h = handler;
			aButton = new JButton("+");
			sButton = new JButton("-");
			mButton = new JButton("*");
			dButton = new JButton("/");
			clearButton = new JButton("clear");
			fillButtons();
			this.add(aButton);
			this.add(sButton);
			this.add(mButton);
			this.add(dButton);
			this.add(clearButton);
			
			// clear button resets input string and if buttonPressed is true, resets to false
			clearButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					h.clearInput();
					if(h.buttonPressed == true){
						h.buttonPressed = false;
					}
					
				}
				
			});
			
			// adds operand to input string depending on which button is pressed iff buttonPressed is false 
			for(int i = 0; i < buttons.length; i++){
				String s = buttons[i].getText();
				buttons[i].addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e){
						if(h.buttonPressed != true){
							h.setInput(s);
							h.buttonPressed = true;
						}
					}
					
				});
			
			}
			
			this.setLayout(new GridLayout(5,1));
		}
		
		void fillButtons(){
			buttons[0] = aButton;
			buttons[1] = sButton;
			buttons[2] = mButton;
			buttons[3] = dButton;
		}
		
	}
}
