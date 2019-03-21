	package com.asz.rubik.visualizer.refresh;

import com.asz.rubik.fundamentals.RubikCube;
import com.asz.rubik.visualizer.RubikCubeVisualizer;
import com.asz.rubik.visualizer.RubikCubeVisualizerSide;

import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class RubikCubeVisualizerRefresh 
{
	private RubikCubeVisualizer visor;
	private RubikCube cube;
	
	private boolean L = false;
	private Integer moveAmount = 0;
	
	public RubikCubeVisualizerRefresh(RubikCubeVisualizer visor)
	{
		this.visor = visor;
		this.cube = visor.getCube();
	}
	
	public void showSide (
			RubikCubeVisualizerSide thisSide, double x, double y, 
			RubikCubeVisualizerSide otherSide, int direction, 
			double width, double height, double elevation, double skewness) 
	{
//		showSide(thisSide, x, y, otherSide, direction, width, height, elevation, skewness, -1);
//	}
	
//	public void showSide (
//			RubikCubeVisualizerSide thisSide, double x, double y, 
//			RubikCubeVisualizerSide otherSide, int direction, 
//			double width, double height, double elevation, double skewness
//			) 
//	{	
		int printDirection = 
				visor.getOrientationCodeWhenSideAt(
						thisSide.getCubeSide(), otherSide.getCubeSide(), direction);
		
		try 
		{
//			thisSide.pickColor(colorCode);
			thisSide.display(
					x, y, width, height,  printDirection, elevation, skewness);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void moveMiddleDown(int count)
	{
		RubikCubeVisualizerSide currentLeft = visor.getSideByCode(
				cube.calculateLeft(visor.getCurrentUp().getCubeSide(), 
						visor.getCurrentFront().getCubeSide()).getTypeCode());
		RubikCubeVisualizerSide currentRight = visor.getSideByCode(
				cube.calculateLeft(
						visor.getCurrentUp().getCubeSide(), 
						visor.getCurrentFront().getCubeSide()).getOppositeSide().getTypeCode());
		
		cube.moveClockwise(currentRight.getCubeSide(), count);
		cube.moveCounterClockwise(currentLeft.getCubeSide(), count);
			
		RubikCubeVisualizerSide sideUp =  visor.getSideByCode(visor.getCurrentUp().getCubeSideTypeCode());
		
		this.visor.setCurrentUp(visor.getSideByCode(visor.getCurrentFront().getOppositeCubeSideTypeCode()));
		this.visor.setCurrentFront(sideUp);
	}

	public void moveMiddleUp(int count)
	{
		RubikCubeVisualizerSide currentLeft = visor.getSideByCode(
				cube.calculateLeft(
						visor.getCurrentUp().getCubeSide(), 
						visor.getCurrentFront().getCubeSide()).getTypeCode());
		RubikCubeVisualizerSide currentRight = visor.getSideByCode(
				cube.calculateLeft(
						visor.getCurrentUp().getCubeSide(), visor.getCurrentFront().getCubeSide()).getOppositeSide().getTypeCode());
		
		cube.moveClockwise(currentLeft.getCubeSide(), count);
		cube.moveCounterClockwise(currentRight.getCubeSide(), count);
		
		for(int lop = 0; lop < count; lop++)
		{
			RubikCubeVisualizerSide sideDown = visor.getSideByCode(visor.getCurrentUp().getOppositeCubeSideTypeCode());
			
			this.visor.setCurrentUp(visor.getCurrentFront());
			this.visor.setCurrentFront(sideDown);	
		}
	}
	
	public void moveMiddleLeft(int count)
	{
		cube.moveClockwise(visor.getCurrentUp().getCubeSide().getOppositeSide(), count);
		cube.moveCounterClockwise(visor.getCurrentUp().getCubeSide(), count);
		
		for(int lop = 0; lop < count; lop++)
		{
			RubikCubeVisualizerSide sideRight = 
					visor.getSideByCode(
							cube.calculateLeft(visor.getCurrentUp().getCubeSide(), visor.getCurrentFront().getCubeSide()).
					getOppositeSide().getTypeCode());
			
			this.visor.setCurrentFront(sideRight);	
		}
	}
	
	public void moveMiddleRight(int count)
	{
		cube.moveClockwise(visor.getCurrentUp().getCubeSide(), count);
		cube.moveCounterClockwise(visor.getCurrentUp().getCubeSide().getOppositeSide(), count);
		
		for(int lop = 0; lop < count; lop++)
		{
			RubikCubeVisualizerSide sideLeft = 
					visor.getSideByCode(
							cube.calculateLeft(visor.getCurrentUp().getCubeSide(), visor.getCurrentFront().getCubeSide()).
					getTypeCode());
			
			this.visor.setCurrentFront(sideLeft);	
		}
	}
	
	public void moveMiddleCounterClockwise(int count)
	{
		cube.moveCounterClockwise(visor.getCurrentFront().getCubeSide().getOppositeSide(), count);
		cube.moveClockwise(visor.getCurrentFront().getCubeSide(), count);
		
		for(int lop = 0; lop < count; lop++)
		{
			this.visor.setCurrentUp(
					visor.getSideByCode(cube.calculateLeft(
							visor.getCurrentUp().getCubeSide(), 
							visor.getCurrentFront().getCubeSide()).
							getOppositeSide().
							getTypeCode()));
		}
	}
	
	public void moveMiddleClockwise(int count)
	{
		cube.moveCounterClockwise(visor.getCurrentFront().getCubeSide(), count);
		cube.moveClockwise(visor.getCurrentFront().getCubeSide().getOppositeSide(), count);
		
		for(int lop = 0; lop < count; lop++)
		{
			this.visor.setCurrentUp(
					visor.getSideByCode(cube.calculateLeft(
							visor.getCurrentUp().getCubeSide(), 
							visor.getCurrentFront().getCubeSide()).
							getTypeCode()));
		}
	}
}
