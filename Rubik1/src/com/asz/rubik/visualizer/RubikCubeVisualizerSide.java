package com.asz.rubik.visualizer;

import java.util.ArrayList;

import com.asz.rubik.fundamentals.RubikCubeSide;
import com.sun.javafx.scene.CameraHelper;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.SceneUtils;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Box;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class RubikCubeVisualizerSide 
{
	private RubikCubeVisualizer visor;
	private ArrayList<RubikCubeVisualizerPieceFace> pieceFaces = new ArrayList<>();
	public Group sideGroup = new Group();
	private Shape square = null;
	private RubikCubeSide side;
	
	public RubikCubeVisualizerSide(RubikCubeVisualizer visor, RubikCubeSide side) 
	{
		this.visor = visor;
		this.side = side;
		for(int lop = 0; lop < 9; lop++)
		{
			pieceFaces.add(new RubikCubeVisualizerPieceFace(this, lop));
		}
	}

	public RubikCubeSide getCubeSide() 
	{
		return side;
	}
	
	public int getCubeSideTypeCode() 
	{
		return side.getTypeCode();
	}

	public RubikCubeSide getOppositeCubeSide() 
	{
		return side.getOppositeSide();
	}

	public int getOppositeCubeSideTypeCode() 
	{
		return side.getOppositeSide().getTypeCode();
	}
			
	public void display(double x, double y, double width, double height, int printDirection, 
			double elevation, double skewness) throws Exception 
	{
		if(square != null)
		{
			getVisor().getRoot().getChildren().remove(square);
			getVisor().getRoot().getChildren().remove(sideGroup);
			square=null;
		}	
		
		double x1 = x + skewness;
		double x2 = x + width + skewness;
		double x3 = x + width;
		double x4 = x;

		double y1 = y;
		double y2 = y - elevation;
		double y3 = y - elevation + height;
		double y4 = y + height;
		
		displayInPerspective(x, y, width, height, printDirection, x1, y1, x2, y2, x3, y3, x4, y4);
	}
	
	private void displayInPerspective(
			double x, double y, double width, double height, int printDirection,
			double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) 
						throws Exception 
	{
		
		square = new Polygon(x1, y1, x2, y2, x3, y3, x4, y4);
		
		square.setFill(Color.BLACK);		
		square.setStroke(Color.BLACK);
		square.setStrokeWidth(0.5);
				
		Integer[] pieceOrder;
		
		switch(printDirection)
		{
		case 1: //North
			pieceOrder = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
			break;
		case 5: //East
			pieceOrder = new Integer[] {2, 5, 8, 1, 4, 7, 0, 3, 6};
			break;
		case 7: //South
			pieceOrder = new Integer[] {8, 7, 6, 5, 4, 3, 2, 1, 0};
			break;
		case 3: //West
			pieceOrder = new Integer[] {6, 3, 0, 7, 4, 1, 8, 5, 2};
			break;
		default:
			throw new Exception("ERROR, PrintDirection NULL???");
		}
				
		visor.getRoot().getChildren().add(square);
		
		showPieceFace(x, y, width, height, pieceOrder[0], 0, 0); 
		showPieceFace(x, y, width, height, pieceOrder[1], 1, 0); 
		showPieceFace(x, y, width, height, pieceOrder[2], 2, 0);
		showPieceFace(x, y, width, height, pieceOrder[3], 0, 1); 
		showPieceFace(x, y, width, height, pieceOrder[4], 1, 1); 
		showPieceFace(x, y, width, height, pieceOrder[5], 2, 1); 
		showPieceFace(x, y, width, height, pieceOrder[6], 0, 2);
		showPieceFace(x, y, width, height, pieceOrder[7], 1, 2); 
		showPieceFace(x, y, width, height, pieceOrder[8], 2, 2); 
		
		visor.getRoot().getChildren().add(sideGroup);
		PerspectiveTransform e = new PerspectiveTransform();
		e.setUlx(x1);
		e.setUly(y1);
		e.setUrx(x2);
		e.setUry(y2);
		e.setLlx(x4);
		e.setLly(y4);
		e.setLrx(x3);
		e.setLry(y3);
		
		sideGroup.setEffect(e);
	}

	private void showPieceFace (double x, double y, double width, double height, 
			int pieceFaceLocCode, int col, int row) 
	{		
		double space = 1.5;
		pieceFaces.get(pieceFaceLocCode).display(
						x, y, width, height, pieceFaceLocCode, col, row, space);
	}
	
	public RubikCubeVisualizer getVisor() 
	{
		return visor;
	}
	
	public void hide()
	{
		square.setVisible(false);
	
		for(int lop = 0; lop < 9; lop++)
		{
			pieceFaces.get(lop).hide();
		}
	}

	public void show2dReflected(
			double width, double height, 
			Point3D vertex1, Point3D vertex2, Point3D vertex3, Point3D vertex4, 
			RubikCubeVisualizerSide otherSide, int direction, Group group, Paint frameColor) 
	{
		if(square != null)
		{
			getVisor().getRoot().getChildren().remove(square);
			getVisor().getRoot().getChildren().remove(sideGroup);
			square=null;
		}	

		int printDirection = 
				visor.getOrientationCodeWhenSideAt(this.getCubeSide(), otherSide.getCubeSide(), direction);
	
		Point2D vertex2D1 = getPoint2DReflection(vertex1, group); 
		Point2D vertex2D2 = getPoint2DReflection(vertex2, group); 
		Point2D vertex2D3 = getPoint2DReflection(vertex3, group); 
		Point2D vertex2D4 = getPoint2DReflection(vertex4, group); 

		//Do - Main Code
		try {
			displayInPerspective(
					vertex2D1.getX(), vertex2D1.getY(), 
					width, height, printDirection, 
					vertex2D1.getX(), vertex2D1.getY(), 
					vertex2D2.getX(), vertex2D2.getY(), 
					vertex2D4.getX(), vertex2D4.getY(), 
					vertex2D3.getX(), vertex2D3.getY()
					);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Point2D getPoint2DReflection(Point3D point, Group group) 
	{
		Scene scene= group.getScene();
		
		Box voidPoint = new Box(0, 0, 0);
		voidPoint.setTranslateX(point.getX());
		voidPoint.setTranslateY(point.getY());
		voidPoint.setTranslateZ(point.getZ());

		// Use a SubScene
		visor.getRoot().getChildren().add(voidPoint);
		
		// Build the Scene Graph
		@SuppressWarnings("restriction")
		SubScene oldSubScene = NodeHelper.getSubScene(voidPoint);
		
		Point3D pointLocal = voidPoint.localToScene(point);
			
		@SuppressWarnings("restriction")
		Point3D coordinates = SceneUtils.subSceneToScene(oldSubScene, pointLocal);

		@SuppressWarnings("restriction")
		Point2D ret = CameraHelper.project(SceneHelper.getEffectiveCamera(scene), coordinates);
		
		visor.getRoot().getChildren().remove(voidPoint);
		voidPoint = null;
		
		return ret;
	}
}
