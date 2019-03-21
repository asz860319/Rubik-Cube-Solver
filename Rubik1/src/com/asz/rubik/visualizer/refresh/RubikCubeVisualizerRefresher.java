package com.asz.rubik.visualizer.refresh;

import com.asz.rubik.visualizer.RubikCubeVisualizer;

public interface RubikCubeVisualizerRefresher 
{
	void setUp(RubikCubeVisualizer visor);

	void refresh();
}
