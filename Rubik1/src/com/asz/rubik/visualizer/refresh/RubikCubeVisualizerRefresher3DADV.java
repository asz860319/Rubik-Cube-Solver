package com.asz.rubik.visualizer.refresh;

import com.asz.rubik.visualizer.RubikCubeVisualizer;
import com.asz.rubik.visualizer.RubikCubeVisualizerSide;

import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class RubikCubeVisualizerRefresher3DADV implements RubikCubeVisualizerRefresher 
{
	private RubikCubeVisualizer visor;
	private boolean keyPressedFlagSet = false;

	@Override
	public void setUp(RubikCubeVisualizer visor) 
	{
		this.visor = visor;	
	}

	@Override
	public void refresh() 
	{
//		if(!keyPressedFlagSet) 
//		{
//			visor.getStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
//				  if(key.getCode()==KeyCode.S) { visor.camera_x+=10; }
//				  if(key.getCode()==KeyCode.D) { visor.camera_x-=10; }
//				  if(key.getCode()==KeyCode.E) { visor.camera_y+=10; }
//				  if(key.getCode()==KeyCode.X) { visor.camera_y-=10; }
//				  if(key.getCode()==KeyCode.R) { visor.camera_z+=10; }
//				  if(key.getCode()==KeyCode.Z) { visor.camera_z-=10; }
//				  if(key.getCode()==KeyCode.U) { visor.camera_rx+=2; }
//				  if(key.getCode()==KeyCode.N) { visor.camera_rx-=2; }
//				  if(key.getCode()==KeyCode.H) { visor.camera_ry+=2; }
//				  if(key.getCode()==KeyCode.J) { visor.camera_ry-=2; }
//				  if(key.getCode()==KeyCode.I) { visor.camera_rz+=2; }
//				  if(key.getCode()==KeyCode.B) { visor.camera_rz-=2; }
//				  refresh();
//				});
//			keyPressedFlagSet = true;
//		}
		
		double centerX = 100;
		double centerY = 100;
		double centerZ = 200;
		double sideLength = 50;
		double halfLen = sideLength / 2;

		Group cubeGroup = visor.getRoot();
		//Scene cubeScene = new Scene(cubeGroup, 800, 800);
		Scene cubeScene = visor.getRoot().getScene();
		//stage.setScene(cubeScene);

		//this.getRoot().getChildren().add(cubeGroup);
		
		//Setting 3d Specifications
		PerspectiveCamera cubeCameraFront= new PerspectiveCamera(false);
		cubeCameraFront.setTranslateX(visor.camera_x);
		cubeCameraFront.setTranslateY(visor.camera_y);
		cubeCameraFront.setTranslateZ(visor.camera_z);
		Rotate rx = new Rotate(visor.camera_rx, Rotate.X_AXIS);
		Rotate ry = new Rotate(visor.camera_ry, Rotate.Y_AXIS);
		Rotate rz = new Rotate(visor.camera_rz, Rotate.Z_AXIS);
		cubeCameraFront.getTransforms().add(rx);
		cubeCameraFront.getTransforms().add(ry);
		cubeCameraFront.getTransforms().add(rz);
		Camera oldCamera = cubeScene.getCamera();
		cubeScene.setCamera(cubeCameraFront);
		
		cubeGroup.getChildren().add(cubeCameraFront);

		Point3D center = new Point3D(centerX, centerY, centerZ);
		double x = center.getX();
		double y = center.getY();
		double z = center.getZ();

		//UP
		Point3D vULF = new Point3D(x - halfLen, y - halfLen, z + halfLen);
		Point3D vURF = new Point3D(x + halfLen, y - halfLen, z + halfLen);
		Point3D vULB = new Point3D(x - halfLen, y + halfLen, z + halfLen);
		Point3D vURB = new Point3D(x + halfLen, y + halfLen, z + halfLen);
		
		//DOWN
		Point3D vDLF = new Point3D(x - halfLen, y - halfLen, z - halfLen);
		Point3D vDRF = new Point3D(x + halfLen, y - halfLen, z - halfLen);
		Point3D vDLB = new Point3D(x - halfLen, y + halfLen, z - halfLen);
		Point3D vDRB = new Point3D(x + halfLen, y + halfLen, z - halfLen);

		RubikCubeVisualizerSide currentLeft  = visor.getSideByCode(
				visor.getCube().calculateLeft(visor.getCurrentUp().getCubeSide(), visor.getCurrentFront().getCubeSide()).getTypeCode()
				);
		RubikCubeVisualizerSide currentRight = visor.getSideByCode(currentLeft.getOppositeCubeSideTypeCode());
		RubikCubeVisualizerSide currentBack  = visor.getSideByCode(visor.getCurrentFront().getOppositeCubeSideTypeCode());
		RubikCubeVisualizerSide currentDown  = visor.getSideByCode(visor.getCurrentUp().getOppositeCubeSideTypeCode());

		//Displaying Cube		
		visor.getCurrentUp().   show2dReflected(
				sideLength, sideLength, vULF, vURF, vULB, vURB, currentBack,  1, cubeGroup, Color.WHITE);
		visor.getCurrentFront().show2dReflected(
				sideLength, sideLength, vULF, vURF, vDLF, vDRF, currentRight, 5, cubeGroup, Color.RED);
		currentRight.show2dReflected(
				sideLength, sideLength, vURB, vURF, vURB, vURB, currentBack,  5, cubeGroup, Color.BLUE);
		currentLeft. show2dReflected(
				sideLength, sideLength, vULB, vULF, vDLB, vDRB, visor.getCurrentFront(), 5, cubeGroup, Color.YELLOW);
		currentDown. show2dReflected(
				sideLength, sideLength, vDLF, vDRF, vDLB, vDRB, visor.getCurrentFront(), 1, cubeGroup, Color.ORANGE);
		currentBack. show2dReflected(
				sideLength, sideLength, vULB, vURB, vDLB, vDRB, currentLeft,  5, cubeGroup, Color.GREEN);
		
		cubeScene.setCamera(oldCamera);
	}
}
