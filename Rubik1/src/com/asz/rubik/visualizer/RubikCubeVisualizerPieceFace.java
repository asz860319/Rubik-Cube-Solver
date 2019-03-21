package com.asz.rubik.visualizer;

import com.asz.rubik.fundamentals.RubikCube;
import com.asz.rubik.fundamentals.RubikCubePieceFace;
import com.asz.rubik.solver.RubikCubeSolver;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class RubikCubeVisualizerPieceFace
{
	private RubikCubeVisualizerSide side;
	private int pieceFaceLocNumber;
	private Shape square = null;
	private Color color;
	private boolean mouseClickedSetOnce;
	
	public RubikCubeVisualizerPieceFace(
			RubikCubeVisualizerSide side, int pieceFaceLocNumber) 
	{
		this.side = side;
		this.pieceFaceLocNumber = pieceFaceLocNumber;	
		mouseClickedSetOnce = false;
	}

	private Color getColor() 
	{
		Color ret=null;
		
		for(int lop=0; lop < 10 && ret==null; lop++) {
			ret=getColorDo();
			if(ret!=null) break;

			try { Thread.sleep(lop); } catch (InterruptedException e) {}
		}
		
		if(ret==null) {
			//throw new Exception("failed to get pieceFace color");
		}
		
		return ret;
	}
	
	private Color getColorDo() 
	{
		Color ret=null;
		try 
		{
			RubikCubePieceFace PF = side.getCubeSide().getPieceFace(pieceFaceLocNumber);
			ret = side.getVisor().getColorForType(PF.getTypeCode());
		} 
		catch(Exception e) {}
		
		return ret;
	}

	public void display(double bigX, double bigY, double bigWidth, double bigHeight, 
						int pieceFaceLocCode, int col, int row, double space) 
	{	
		if(square != null)
		{
			side.sideGroup.getChildren().remove(square);
			square = null;
		}	
		
		double spaceWidth  = space / 3;
		double spaceHeight = space / 3;
		double width  = (bigWidth  - spaceWidth * 4) / 3;
		double height = (bigHeight - spaceHeight * 4) / 3;
		
		double x = bigX
				+ spaceWidth * (col + 1) 
				+ width * col;
		
		double y = bigY 
				+ spaceHeight * (row + 1) 
				+ height * row;
		
		double x1 = x + spaceWidth;
		double y1 = y - spaceHeight;

		square = new Rectangle(x1, y1, width, height);
					
		((Rectangle) square).setArcHeight(10);
		((Rectangle) square).setArcWidth(10);
		
		square.setStroke(Color.BLACK);
		square.setStrokeWidth(1);		
				
		side.sideGroup.getChildren().add(square);

		color = getColor();	
		
		square.setFill(color);

		square.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent e) 
			{
				if(side.getVisor().isEditing())
				{
					int colorCode = side.getVisor().getEditColorCode();
					if(colorCode != -1)
					{
						if(pieceFaceLocCode != 4)
						{
							side.getCubeSide().getPieceFace(pieceFaceLocCode).setTypeCode(colorCode);
						}
					}
					
					side.getVisor().refreshCube();
				}
			}
		});
	}

	public void hide() 
	{
		if(square != null)
		{
			square.setVisible(false);
		}
	}
}
