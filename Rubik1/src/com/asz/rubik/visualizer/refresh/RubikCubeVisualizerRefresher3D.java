package com.asz.rubik.visualizer.refresh;

import com.asz.rubik.visualizer.RubikCubeVisualizer;
import com.asz.rubik.visualizer.RubikCubeVisualizerSide;

public class RubikCubeVisualizerRefresher3D implements RubikCubeVisualizerRefresher 
{
	private RubikCubeVisualizer visor;

	@Override
	public void setUp(RubikCubeVisualizer visor) 
	{
		this.visor = visor;	
	}

	@Override
	public void refresh() 
	{
		RubikCubeVisualizerSide currentLeft  = visor.getSideByCode(
				visor.getCube().calculateLeft(visor.getCurrentUp().getCubeSide(), visor.getCurrentFront().getCubeSide()).getTypeCode()
				);

		RubikCubeVisualizerSide currentRight = visor.getSideByCode(currentLeft.getOppositeCubeSideTypeCode());
		RubikCubeVisualizerSide currentBack  = visor.getSideByCode(visor.getCurrentFront().getOppositeCubeSideTypeCode());
		RubikCubeVisualizerSide currentDown  = visor.getSideByCode(visor.getCurrentUp().getOppositeCubeSideTypeCode());
		
		double sideSize = 130;
		double startX = 55; 
		double startY = 170;
		double space = 1.8;
		double viewAngle = (double) visor.viewAngle3d;
		double viewAngleElevation = (double) visor.viewAngle3dElevation;

		double elevation   = Math.sin(Math.toRadians(viewAngleElevation)) * sideSize;
		double skewness    = Math.cos(Math.toRadians(90 - viewAngle)) * sideSize;
		double viewWidthFront  = Math.cos(Math.toRadians(viewAngle)) * sideSize;
		double viewWidthRight = Math.cos(Math.toRadians(90 - viewAngle)) * sideSize;
		double viewHeight = Math.sin(Math.toRadians(viewAngleElevation)) * sideSize;
		double viewHeight2 = Math.sin(Math.toRadians(90 - viewAngleElevation)) * sideSize;

		currentLeft.hide();
		
		visor.refresh.showSide(visor.getCurrentFront(), 
				startX + (sideSize + space) * 1 - viewWidthFront + sideSize, 
				startY - viewHeight + viewHeight, 
				currentRight, 5, viewWidthFront, viewHeight2, -elevation, 0);
		
		visor.refresh.showSide(currentRight, startX + (sideSize + space) * 2, startY + viewHeight, 
				currentBack,  5, viewWidthRight, viewHeight2, elevation, 0);
		
		currentBack.hide();
		
		visor.refresh.showSide(
				visor.getCurrentUp(),    
				startX + (sideSize + space) * 1 + sideSize - viewWidthFront, 
				startY - viewHeight - space - sideSize + viewHeight + sideSize - viewHeight, 
				currentBack, 1, viewWidthFront, viewHeight, -elevation, skewness);
		
		currentDown.hide();
	}
}
