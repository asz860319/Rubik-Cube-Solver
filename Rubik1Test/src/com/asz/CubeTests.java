package com.asz;

import org.junit.Test;
import com.asz.rubik.fundamentals.RubikCube;
import com.asz.rubik.visualizer.RubikCubeVisualizerRunner;

public class CubeTests
{
	@Test
	public void DisplayCube() throws Exception { new RubikCubeVisualizerRunner(new RubikCube()).playWait(); }
}
