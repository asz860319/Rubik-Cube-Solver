package com.asz.rubik.visualizer.refresh;

import com.asz.rubik.visualizer.RubikCubeVisualizer;
import com.asz.rubik.visualizer.RubikCubeVisualizerSide;

public class RubikCubeVisualizerRefresher23D implements RubikCubeVisualizerRefresher 
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
				visor.getCube().calculateLeft(
				visor.getCurrentUp().getCubeSide(), visor.getCurrentFront().getCubeSide()).getTypeCode()
				);

		RubikCubeVisualizerSide currentRight = visor.getSideByCode(currentLeft.getOppositeCubeSideTypeCode());
		RubikCubeVisualizerSide currentBack  = visor.getSideByCode(visor.getCurrentFront().getOppositeCubeSideTypeCode());
		RubikCubeVisualizerSide currentDown  = visor.getSideByCode(visor.getCurrentUp().getOppositeCubeSideTypeCode());
		
		double sideSize = 130;
		double startX = 55; 
		double startY = 170;
		double space = 1.8;
		double viewAngle = (double) visor.viewAngle2d3d;

		double elevation   = Math.sin(Math.toRadians(viewAngle)) * sideSize;
		double skewness    = Math.cos(Math.toRadians(viewAngle)) * sideSize;
		double shortWidth  = Math.cos(Math.toRadians(viewAngle)) * sideSize;
		double shortHeight = Math.sin(Math.toRadians(viewAngle)) * sideSize;
		
		visor.refresh.showSide(currentLeft,  startX + (sideSize + space) * 0, startY,
				visor.getCurrentFront(), 5, sideSize, sideSize, 0, 0);
		visor.refresh.showSide(visor.getCurrentFront(), startX + (sideSize + space) * 1, startY, 
				currentRight, 5, sideSize, sideSize, 0, 0);
		visor.refresh.showSide(currentRight, startX + (sideSize + space) * 2, startY, 
				currentBack,  5, shortWidth, sideSize, elevation, 0);
		visor.refresh.showSide(currentBack,  startX + 2 * sideSize + 3 * space + shortWidth, startY - elevation, 
				currentLeft,  5, sideSize, sideSize, 0, 0);
		visor.refresh.showSide(visor.getCurrentUp(),    startX + (sideSize + space) * 1, startY - shortHeight - space, 
				currentBack, 1, sideSize, shortHeight, 0, skewness);
		visor.refresh.showSide(currentDown,  startX + (sideSize + space) * 1, startY + sideSize + space, 
				visor.getCurrentFront(), 1, sideSize, sideSize, 0, 0);
	}
}
