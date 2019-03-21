package com.asz.rubik.visualizer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class RubikCubeVisualizerTextBox {

	private RubikCubeVisualizer visor;
	private TextField txtSpeed;

	public RubikCubeVisualizerTextBox(RubikCubeVisualizer visor) 
	{
		this.visor = visor;
	}

	public void display(int height, int length, int x, int y, String buttonAction)
	{
		display(height, length, x, y, buttonAction, buttonAction);
	}

	public void display(int height, int length, int x, int y, String buttonAction, String title) 
	{
		txtSpeed = new TextField(title);
		
		txtSpeed.setPrefSize(length, height);
		txtSpeed.setLayoutX(x);
		txtSpeed.setLayoutY(y);
		
		visor.getRoot().getChildren().add(txtSpeed);
		
		txtSpeed.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	txtSpeed.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});		
	}

	public Integer getIntValue() 
	{
		int ret = 0;
		try {
			ret = Integer.parseInt(txtSpeed.getText());
		} catch (Exception e) {	}
		if(ret == 0)
		{
			ret = 7;
			txtSpeed.setText("7");
		}
		return ret;
	}
	
	public void setDisable(boolean b) {
		txtSpeed.setDisable(b);
	}
}
