package com.asz.rubik.visualizer;

import com.asz.rubik.fundamentals.RubikCube;

import javafx.application.Application;
import javafx.stage.Stage;

public class RubikCubeVisualizerRunner extends Application
{
	private static RubikCubeVisualizer visor;
	private Thread jfxThread;

	public RubikCubeVisualizerRunner() {}
	
	public RubikCubeVisualizerRunner(RubikCube cube) 
	{
    	visor = new RubikCubeVisualizer(cube);
	}

	public void playNoWait() 
	{
		jfxThread = new Thread() {
			@Override 
			public void run() {
				Application.launch(RubikCubeVisualizerRunner.class, new String[] {});
			}
        };

        jfxThread.setDaemon(true);
		jfxThread.start();
	}
	
	public void playWait() 
	{
		playNoWait();
		try {
			jfxThread.join();
		} catch (InterruptedException e) {}
	}
	
    @Override
    public void start(Stage stage) throws Exception 
    {
    	visor.start(stage);
    }
}
