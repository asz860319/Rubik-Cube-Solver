package com.asz.rubik.visualizer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;

public class RubikCubeVisualizerButton implements EventHandler<ActionEvent> {

	private RubikCubeVisualizer visor;
	private String buttonAction = "";
	private Button button;

	public RubikCubeVisualizerButton(
			RubikCubeVisualizer visor) 
	{
		this.visor = visor;
	}

	public void display(int height, int length, int x, int y, String buttonAction)
	{
		display(height, length, x, y, buttonAction, buttonAction);
	}

	@Override
	public void handle(ActionEvent event) 
	{
		try 
		{
			visor.runButtonAction(buttonAction);
		} catch (Exception e) 
		{
			
		}
	}
	
	public void display(int height, int length, int x, int y, String buttonAction, String title) 
	{
		this.buttonAction = buttonAction;
		
		Button button = new Button(title);  
		
		button.setTextFill(Color.BLACK);
		button.setPrefSize(length, height);
		button.setLayoutX(x);
		button.setLayoutY(y);
		
		visor.getRoot().getChildren().add(button);
		
		button.setOnAction(this);
		
		this.button = button;
	}

	public Button getButton() 
	{
		return button;
	}
}
