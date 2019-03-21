package com.asz.rubik.visualizer;	

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.Soundbank;

import com.asz.rubik.RubikCubeShuffler;
import com.asz.rubik.fundamentals.RubikCube;
import com.asz.rubik.fundamentals.RubikCube.SideMoveObserver;
import com.asz.rubik.fundamentals.RubikCubePieceFace;
import com.asz.rubik.fundamentals.RubikCubeSide;
import com.asz.rubik.solver.RubikCubeSolver;
import com.asz.rubik.visualizer.refresh.RubikCubeVisualizerRefresh;
import com.asz.rubik.visualizer.refresh.RubikCubeVisualizerRefresher;
import com.asz.rubik.visualizer.refresh.RubikCubeVisualizerRefresher23D;
import com.asz.rubik.visualizer.refresh.RubikCubeVisualizerRefresher2D;
import com.asz.rubik.visualizer.refresh.RubikCubeVisualizerRefresher3D;
import com.asz.rubik.visualizer.refresh.RubikCubeVisualizerRefresher3DADV;
import com.sun.javafx.scene.CameraHelper;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.SceneUtils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class RubikCubeVisualizer implements SideMoveObserver
{
	private ArrayList<RubikCubeVisualizerSide> sides = new ArrayList<>();
	private RubikCube cube;
	private Group root;
	private Stage stage;
	private ArrayList<RubikCubeVisualizerButton> buttons = new ArrayList<>();
	private RubikCubeVisualizerSide   currentUp;
	private RubikCubeVisualizerSide   currentFront;
	
	private RubikCubeVisualizerButton scrambleButton = new RubikCubeVisualizerButton(this);
	private RubikCubeVisualizerButton solveButton = new RubikCubeVisualizerButton(this);
	private RubikCubeVisualizerButton resetButton = new RubikCubeVisualizerButton(this);
	
	private RubikCubeVisualizerTextBox waitTimeTextBox = new RubikCubeVisualizerTextBox(this);
	private Text waitTimeText;
	
	private Map<String, RubikCubeVisualizerRefresher> refreshers;

	private RubikCubeVisualizerButton xButton = new RubikCubeVisualizerButton(this);
	private RubikCubeVisualizerButton xpButton = new RubikCubeVisualizerButton(this);
	private RubikCubeVisualizerButton yButton = new RubikCubeVisualizerButton(this);
	private RubikCubeVisualizerButton ypButton = new RubikCubeVisualizerButton(this);
	private RubikCubeVisualizerButton zButton = new RubikCubeVisualizerButton(this);
	private RubikCubeVisualizerButton zpButton = new RubikCubeVisualizerButton(this);
	
	private RubikCubeVisualizerButton mButton = new RubikCubeVisualizerButton(this);
	private RubikCubeVisualizerButton mpButton = new RubikCubeVisualizerButton(this);
	private RubikCubeVisualizerButton eButton = new RubikCubeVisualizerButton(this);
	private RubikCubeVisualizerButton epButton = new RubikCubeVisualizerButton(this);
	private RubikCubeVisualizerButton sButton = new RubikCubeVisualizerButton(this);
	private RubikCubeVisualizerButton spButton = new RubikCubeVisualizerButton(this);
	private RubikCubeVisualizerButton revertButton = new RubikCubeVisualizerButton(this);
	
	private ToggleGroup toggleGroup;
	private RadioButton radio2d;
	private RadioButton radio3d;
	private RadioButton radio23d;
	private Toggle oldToggle;
	private ToggleButton radio3dCamera;

	private CheckBox capitalButton;
	
	private Button editButton;
	private Button endEditButton;

	public Number viewAngle2d3d;
	public Number viewAngle3d;
	public Number viewAngle3dElevation;
	private Slider viewSlider2d3d;
	private Slider viewSlider3d;
	private Slider viewSlider3dElevation;
	
	private Scene scene;

	private double mousePosX, mousePosY = 0;
	final double TURN_FACTOR = 0.5;

	public double camera_x  = -150;
	public double camera_y  = -150;
	public double camera_z  =  250;
	public double camera_rx =  0;
	public double camera_ry =  0;
	public double camera_rz =  0;
	
	public RubikCubeVisualizerRefresh refresh;

	private Thread scrambleThread;
	private Thread solveThread;
	private String code;
	
	private RubikCubeSolver solver;
	
	private int editType;
	private boolean isEditing;

	private Rectangle colorPalet;
	private Rectangle white;
	private Rectangle blue;
	private Rectangle red;
	private Rectangle yellow;
	private Rectangle green;
	private Rectangle orange;
	
	private boolean cubeSolvable;

	public RubikCubeVisualizer(RubikCube cube) 
	{
		this.cube = cube;
		root = new Group();
		cubeSolvable = false;
		
		editType = -1;
		isEditing = false;
		
		for(int lop = 0; lop < cube.getSides().size(); lop++)
		{
			sides.add(new RubikCubeVisualizerSide(this, cube.getSides().get(lop)));
			buttons.add(new RubikCubeVisualizerButton(this));
			buttons.add(new RubikCubeVisualizerButton(this));
		}
		
		solver = new RubikCubeSolver(cube);
		
		viewAngle2d3d = 45.0;
		viewAngle3d = 45.0;
		viewAngle3dElevation = 30.0;
				
		resetUpFrontSides();
		
		refresh = new RubikCubeVisualizerRefresh(this);
		
		refreshers = new HashMap<String, RubikCubeVisualizerRefresher>();
		
		refreshers.put("2D",    new RubikCubeVisualizerRefresher2D());
		refreshers.put("23D",   new RubikCubeVisualizerRefresher23D	 ());
		refreshers.put("3D",    new RubikCubeVisualizerRefresher3D	 ());
		refreshers.put("3DADV", new RubikCubeVisualizerRefresher3DADV());
		
		for(Map.Entry<String, RubikCubeVisualizerRefresher> entry : refreshers.entrySet())
		{
			entry.getValue().setUp(this);
		}		
	}
		
	private void resetUpFrontSides() 
	{
		this.currentUp    = getSideByCode(4);
		this.currentFront = getSideByCode(3);
	}

	public void start(Stage stage) 
	{
		this.stage = stage;

		cube.registerSideMoveObserver(this);

		scene = new Scene(root, 650, 600, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.LIGHTGOLDENRODYELLOW);
		stage.setTitle("Rubik's Cube Simulator");
		stage.setScene(scene);
			
		//testingStuff();
		
		showButtons();

		addKeyBinds();
		
		refreshCube();

		stage.setTitle("Rubik Cube");
		
		stage.show();
	}

//	public void testingStuff()
//	{
//		Box box2 = new Box(100, 100, 100);
//		Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
//		Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
//		Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
//		box2.setTranslateX(100);
//		box2.setTranslateY(50);
//		box2.setTranslateZ(0);
//		box2.getTransforms().addAll(rotateZ, rotateY, rotateX);
//
//		PerspectiveCamera camera = new PerspectiveCamera(false);
//        camera.setTranslateX(camera_x);
//        camera.setTranslateY(camera_y);
//        camera.setTranslateZ(camera_z);
//
//        //PerspectiveCamera camera = new PerspectiveCamera(true);
//		//camera.getTransforms().addAll(new Rotate(-20, Rotate.Y_AXIS), new Rotate(-20, Rotate.X_AXIS),
//		//		new Translate(0, 0, -50));
//
//		// Use a SubScene
//		//scene.setCamera(camera);
//
//		// Build the Scene Graph
//		root.getChildren().add(camera);
//		root.getChildren().add(box2);
//
//		ArrayList<Point2D> points = get2dPoints(box2);
//		double[] xyPoints = new double[points.size() * 2];
//		for(int lop = 0; lop < points.size(); lop++)
//		{
//			xyPoints[lop * 2] = points.get(lop).getX();
//			xyPoints[lop * 2 + 1] = points.get(lop).getY();
//		}
//		
//		Polygon poly = new Polygon(xyPoints);
//		/*Line poly = new Line(
//				xyPoints[0],
//				xyPoints[1],
//				xyPoints[2],
//				xyPoints[3]
//				);
//	*/
//		box2.setVisible(false);
//		
//		poly.setFill(Color.RED);
//		poly.setStroke(Color.BLACK);
//		poly.setStrokeWidth(1.5);
//		
//		root.getChildren().add(poly);
//	}
	
	private void addKeyBinds() 
	{
		scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
			code = "";
			
			if(new KeyCodeCombination(KeyCode.R, KeyCombination.ALT_ANY).match(key)) code = "R";
			if(new KeyCodeCombination(KeyCode.R, KeyCombination.SHIFT_DOWN).match(key)) code = "R'";
			if(new KeyCodeCombination(KeyCode.L, KeyCombination.ALT_ANY).match(key)) code = "L";
			if(new KeyCodeCombination(KeyCode.L, KeyCombination.SHIFT_DOWN).match(key)) code = "L'";
			if(new KeyCodeCombination(KeyCode.F, KeyCombination.ALT_ANY).match(key)) code = "F";
			if(new KeyCodeCombination(KeyCode.F, KeyCombination.SHIFT_DOWN).match(key)) code = "F'";
			if(new KeyCodeCombination(KeyCode.B, KeyCombination.ALT_ANY).match(key)) code = "B";
			if(new KeyCodeCombination(KeyCode.B, KeyCombination.SHIFT_DOWN).match(key)) code = "B'";
			if(new KeyCodeCombination(KeyCode.U, KeyCombination.ALT_ANY).match(key)) code = "U";
			if(new KeyCodeCombination(KeyCode.U, KeyCombination.SHIFT_DOWN).match(key)) code = "U'";
			if(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_ANY).match(key)) code = "D";
			if(new KeyCodeCombination(KeyCode.D, KeyCombination.SHIFT_DOWN).match(key)) code = "D'";
			
			if(new KeyCodeCombination(KeyCode.M, KeyCombination.ALT_ANY).match(key)) code = "M";
			if(new KeyCodeCombination(KeyCode.M, KeyCombination.SHIFT_DOWN).match(key)) code = "M'";
			if(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_ANY).match(key)) code = "E";
			if(new KeyCodeCombination(KeyCode.E, KeyCombination.SHIFT_DOWN).match(key)) code = "E'";
			if(new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_ANY).match(key)) code = "S";
			if(new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN).match(key)) code = "S'";

			if(new KeyCodeCombination(KeyCode.X, KeyCombination.ALT_ANY).match(key)) code = "X";
			if(new KeyCodeCombination(KeyCode.X, KeyCombination.SHIFT_DOWN).match(key)) code = "X'";
			if(new KeyCodeCombination(KeyCode.Y, KeyCombination.ALT_ANY).match(key)) code = "Y";
			if(new KeyCodeCombination(KeyCode.Y, KeyCombination.SHIFT_DOWN).match(key)) code = "Y'";
			if(new KeyCodeCombination(KeyCode.Z, KeyCombination.ALT_ANY).match(key)) code = "Z";
			if(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHIFT_DOWN).match(key)) code = "Z'";
		
			if(!code.equals(""))
			{
				if(!solver.isCubeSolving())
				{
					runButtonAction(code);
				}
			}
			
			if(KeyCode.CONTROL == key.getCode())
			{ 
				if(!capitalButton.isSelected())
				{
					for(int lop = 0; lop < buttons.size(); lop++)
					{
						RubikCubeVisualizerButton button = buttons.get(lop);
						button.getButton().setText(button.getButton().getText().toUpperCase());
					}
					capitalButton.setSelected(!capitalButton.isSelected());
				} else if(capitalButton.isSelected())
				{
					for(int lop = 0; lop < buttons.size(); lop++)
					{
						RubikCubeVisualizerButton button = buttons.get(lop);
						button.getButton().setText(button.getButton().getText().toLowerCase());
					}
					capitalButton.setSelected(!capitalButton.isSelected());
				}
			}
		});
	}

	private List<Point3D> generateDots(Node myBox) 
	{
	    List<Point3D> vertices = new ArrayList<>();
	    Bounds bounds = myBox.getBoundsInLocal();
	    vertices.add(myBox.localToScene(new Point3D(bounds.getMinX(), bounds.getMinY(), bounds.getMinZ())));
	    vertices.add(myBox.localToScene(new Point3D(bounds.getMaxX(), bounds.getMinY(), bounds.getMinZ())));
	    vertices.add(myBox.localToScene(new Point3D(bounds.getMaxX(), bounds.getMaxY(), bounds.getMinZ())));
	    vertices.add(myBox.localToScene(new Point3D(bounds.getMinX(), bounds.getMaxY(), bounds.getMinZ())));
	    vertices.add(myBox.localToScene(new Point3D(bounds.getMinX(), bounds.getMinY(), bounds.getMaxZ())));
	    vertices.add(myBox.localToScene(new Point3D(bounds.getMaxX(), bounds.getMinY(), bounds.getMaxZ())));
	    vertices.add(myBox.localToScene(new Point3D(bounds.getMaxX(), bounds.getMaxY(), bounds.getMaxZ())));
	    vertices.add(myBox.localToScene(new Point3D(bounds.getMinX(), bounds.getMaxY(), bounds.getMaxZ())));
	    return vertices;
	}
	
	public ArrayList<Point2D> get2dPoints(Node myBox) 
	{
		@SuppressWarnings("restriction")
		SubScene oldSubScene = NodeHelper.getSubScene(myBox);
		
		ArrayList<Point2D> ret = new ArrayList<>();
		
		generateDots(myBox).stream().forEach(dot -> {
			
			@SuppressWarnings("restriction")
			Point3D coordinates = SceneUtils.subSceneToScene(oldSubScene, dot);
		
			@SuppressWarnings("restriction")
			Point2D p2 = CameraHelper.project(SceneHelper.getEffectiveCamera(myBox.getScene()), coordinates);
			
			ret.add(p2);
		});
		
		return ret;
	}
	
//	private void handleMouseEvents() 
//	{
//		scene.setOnMousePressed((MouseEvent me) -> {
//			mousePosX = me.getSceneX();
//			mousePosY = me.getSceneY();
//		});
//
//		scene.setOnMouseDragged((MouseEvent me) -> {
//			double dx = (mousePosX - me.getSceneX());
//			double dy = (mousePosY - me.getSceneY());
//
//			Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
//			Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
//			Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
//			rotateX.setAngle(rotateX.getAngle() - (dy / box.getHeight() * -360) * (Math.PI / 180));
//			rotateY.setAngle(rotateY.getAngle() - (dx / box.getWidth() * -360) * (Math.PI / 180));
//
//		 	mousePosX = me.getSceneX();
//			mousePosY = me.getSceneY();
////			double dx = (mousePosX - me.getSceneX());
////			double dy = (mousePosY - me.getSceneY());
////			if (me.isPrimaryButtonDown()) {
////				rotateX.setAngle(rotateX.getAngle() - (dy / testBox.getHeight() * 360) * (Math.PI / 180))
////				rotateY.setAngle(rotateY.getAngle() - (dx / testBox.getWidth() * -360) * (Math.PI / 180))
////			}
////			mousePosX = me.getSceneX();
////			mousePosY = me.getSceneY();
//		});
//	}
	
	public Stage getStage()
	{
		return stage;
	}
	
	private void showButtons() 
	{
		int buttonSize = 30;
		int startX = 15;
		int startY = 500;
		int space = 8;

		String[] moveTypes = new String[] {"R", "U", "F", "L", "D", "B"};
		
		for(int lop = 0; lop < buttons.size(); lop += 2)
		{
			buttons.get(lop).display
					(
					buttonSize, buttonSize,
					startX + (buttonSize + space) * (lop / 2 + 1), 
					startY,
					moveTypes[lop / 2]
					);	
			buttons.get(lop + 1).display
					(
					buttonSize, buttonSize,
					startX + (buttonSize + space) * ((lop + 1) / 2 + 1), 
					startY + (buttonSize + space),
					moveTypes[lop / 2] + "'"
					);	
		}
		
		waitTimeText = new Text(300 + 160 + space, startY + (buttonSize + space) + 20, "TPS");
		waitTimeText.setFill(Color.BLACK);
		root.getChildren().add(waitTimeText);		
		
		scrambleButton.display(30, 80, 300, startY, "Scramble");
		solveButton.display(30, 80, 300, startY + (buttonSize + space), "Solve");
		resetButton.display(30, 80, 300 + 80 + space, startY, "Reset");
		waitTimeTextBox.display(30, 80, 300 + 80 + space, startY + (buttonSize + space), "30");

		xButton.display(buttonSize, buttonSize, 330 + 180, startY, "X");
		xpButton.display(buttonSize, buttonSize, 330 + 180, startY  + (buttonSize + space), "X'");
		
		yButton.display(buttonSize, buttonSize, 330 + 225, startY, "Y");
		ypButton.display(buttonSize, buttonSize, 330 + 225, startY  + (buttonSize + space), "Y'");

		zButton.display(buttonSize, buttonSize, 330 + 270, startY, "Z");
		zpButton.display(buttonSize, buttonSize, 330 + 270, startY  + (buttonSize + space), "Z'");
		
		mButton.display(buttonSize, buttonSize, 
				startX + buttonSize * 1 + space * 1, startY - space * 2 - buttonSize * 2, "M");
		mpButton.display(buttonSize, buttonSize, 
				startX + buttonSize * 1 + space * 1, startY - space * 1 - buttonSize * 1, "M'");
		
		eButton.display(buttonSize, buttonSize, 
				startX + buttonSize * 2 + space * 2, startY - space * 2 - buttonSize * 2, "E");
		epButton.display(buttonSize, buttonSize, 
				startX + buttonSize * 2 + space * 2, startY - space * 1 - buttonSize * 1, "E'");

		sButton.display(buttonSize, buttonSize, 
				startX + buttonSize * 3 + space * 3, startY - space * 2 - buttonSize * 2, "S");
		spButton.display(buttonSize, buttonSize, 
				startX + buttonSize * 3 + space * 3, startY - space * 1 - buttonSize * 1, "S'");
		
		revertButton.display(buttonSize, 75, 330 + 180, startY - 38, "Revert");
		
		viewSlider2d3d = new Slider();
		
		viewSlider2d3d.setPrefSize(200, 30);
		viewSlider2d3d.setLayoutX(300);
		viewSlider2d3d.setLayoutY(450);
		viewSlider2d3d.setMin(0);
		viewSlider2d3d.setMax(90);
		viewSlider2d3d.setValue(45);
		
		viewSlider2d3d.setShowTickLabels(true);
		viewSlider2d3d.setShowTickMarks(true);
		viewSlider2d3d.setMinorTickCount(6);
		viewSlider2d3d.setBlockIncrement(10);
		
		viewSlider2d3d.valueProperty().addListener(new ChangeListener<Number>() 
		{
			public void changed(
					ObservableValue<? extends Number> ov, Number old_val, Number new_val) 
			{
				viewAngle2d3d = new_val;
				refreshCube();
			}
		});
		
		viewSlider3d = new Slider();
		
		viewSlider3d.setPrefSize(200, 30);
		viewSlider3d.setLayoutX(300);
		viewSlider3d.setLayoutY(450);
		viewSlider3d.setMin(15);
		viewSlider3d.setMax(75);
		viewSlider3d.setValue(45);
		
		viewSlider3d.setShowTickLabels(true);
		viewSlider3d.setShowTickMarks(true);
		viewSlider3d.setMinorTickCount(6);
		viewSlider3d.setBlockIncrement(10);
		
		viewSlider3d.valueProperty().addListener(new ChangeListener<Number>() 
		{
			public void changed(
					ObservableValue<? extends Number> ov, Number old_val, Number new_val) 
			{
				viewAngle3d = new_val;
				refreshCube();
			}
		});
		
		viewSlider3dElevation = new Slider();
		
		viewSlider3dElevation.setPrefSize(200, 30);
		viewSlider3dElevation.setLayoutX(300);
		viewSlider3dElevation.setLayoutY(420);
		viewSlider3dElevation.setMin(15);
		viewSlider3dElevation.setMax(75);
		viewSlider3dElevation.setValue(30);
		
		viewSlider3dElevation.setShowTickLabels(true);
		viewSlider3dElevation.setShowTickMarks(true);
		viewSlider3dElevation.setMinorTickCount(6);
		viewSlider3dElevation.setBlockIncrement(10);
		
		viewSlider3dElevation.valueProperty().addListener(new ChangeListener<Number>() 
		{
			public void changed(
					ObservableValue<? extends Number> ov, Number old_val, Number new_val) 
			{
				viewAngle3dElevation = new_val;
				refreshCube();
			}
		});
		
		radio2d = new RadioButton("2D");
		
		radio2d.setPrefSize(60, 20);
		radio2d.setLayoutX(50);
		radio2d.setLayoutY(50);
		
		radio23d = new RadioButton("2D/3D");
		
		radio23d.setPrefSize(60, 20);
		radio23d.setLayoutX(50);
		radio23d.setLayoutY(70);
				
		radio3d = new RadioButton("3D");
		
		radio3d.setPrefSize(60, 20);
		radio3d.setLayoutX(50);
		radio3d.setLayoutY(90);
		
		radio3dCamera = new RadioButton("Advanced 3D");
		
		radio3dCamera.setPrefSize(110, 20);
		radio3dCamera.setLayoutX(50);
		radio3dCamera.setLayoutY(110);
		
		toggleGroup = new ToggleGroup();

		radio2d.setToggleGroup(toggleGroup);
		radio3d.setToggleGroup(toggleGroup);
		radio23d.setToggleGroup(toggleGroup);
		radio3dCamera.setToggleGroup(toggleGroup);

		root.getChildren().add(radio2d);
		root.getChildren().add(radio3d);
		root.getChildren().add(radio23d);
		root.getChildren().add(radio3dCamera);
		
		toggleGroup.selectToggle(radio3d);
		
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
		{
		    @Override
		    public void changed(
		    		ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2)
		    {
		    	refreshCube();
		    }
		});				
		
		root.getChildren().add(viewSlider2d3d);
		root.getChildren().add(viewSlider3d);
		root.getChildren().add(viewSlider3dElevation);

		viewSlider2d3d.setVisible(toggleGroup.getSelectedToggle() == radio23d);
		viewSlider3d.setVisible(toggleGroup.getSelectedToggle()   == radio3d);
		viewSlider3dElevation.setVisible(toggleGroup.getSelectedToggle()   == radio3d);
		
		capitalButton = new CheckBox();
		
		capitalButton.setLayoutX(185);
		capitalButton.setLayoutY(430);
		
		capitalButton.setPrefHeight(70);
		capitalButton.setPrefWidth(100);
		capitalButton.setText("Move Mode");
		
		capitalButton.setSelected(true);
		
		capitalButton.setOnAction(new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event) 
			{
				if(!capitalButton.isSelected())
				{
					for(int lop = 0; lop < buttons.size(); lop++)
					{
						RubikCubeVisualizerButton button = buttons.get(lop);
						button.getButton().setText(button.getButton().getText().toLowerCase());
					}
				}
				if(capitalButton.isSelected())
				{
					for(int lop = 0; lop < buttons.size(); lop++)
					{
						RubikCubeVisualizerButton button = buttons.get(lop);
						button.getButton().setText(button.getButton().getText().toUpperCase());
					}
				}
			}
		});
			
		root.getChildren().add(capitalButton);
		
		editButton = new Button();
		
		editButton.setLayoutX(510);
		editButton.setLayoutY(425);
		
		editButton.setPrefWidth(75);
		editButton.setPrefHeight(30);
		editButton.setText("Start Edit");
		
		endEditButton = new Button();
		
		endEditButton.setLayoutX(510);
		endEditButton.setLayoutY(425);
		
		endEditButton.setPrefWidth(75);
		endEditButton.setPrefHeight(30);
		endEditButton.setText("End Edit");
		endEditButton.setVisible(false);
		
		createColorPanel();
		
		hideColorPanel();
		
		editButton.setOnAction(new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event) 
			{
				editType = -1;
				
				oldToggle = toggleGroup.getSelectedToggle();
				toggleGroup.selectToggle(radio2d);
				showColorPanel();
				editButton.setVisible(false);
				endEditButton.setVisible(true);
				isEditing = true;
				
				setButtonEnable(false);
				xButton.getButton().setDisable(true);
				xpButton.getButton().setDisable(true);
				yButton.getButton().setDisable(true);
				ypButton.getButton().setDisable(true);
				zButton.getButton().setDisable(true);
				zpButton.getButton().setDisable(true);

				radio2d.setVisible(false);
				radio3d.setVisible(false);
				radio23d.setVisible(false);
				radio3dCamera.setVisible(false);
			}
		});
			
		endEditButton.setOnAction(new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event) 
			{
				if(!cube.isSolvable())
				{
					Dialog<ButtonType> warning = new Dialog<>();
					warning.setTitle("Warning");
					warning.setHeaderText("Impossible Edit");
					warning.setContentText("Warning: The edit you made is not possible on a regular cube.");
					warning.setResult(ButtonType.CANCEL);

					warning.getDialogPane().getButtonTypes().add(ButtonType.OK);
					warning.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

					warning.showAndWait();
					
					if(warning.getResult() == ButtonType.OK)
					{
						warning.close();
						cubeSolvable = false;
					}
					
					if(warning.getResult() == ButtonType.CANCEL)
					{
						warning.close();
						return;
					}
				} else
				{
					cubeSolvable = true;
				}
				toggleGroup.selectToggle(oldToggle);
				hideColorPanel();
				editButton.setVisible(true);
				endEditButton.setVisible(false);
				isEditing = false;
				
				setButtonEnable(true);
				solveButton.getButton().setDisable(!cubeSolvable);
				xButton.getButton().setDisable(false);
				xpButton.getButton().setDisable(false);
				yButton.getButton().setDisable(false);
				ypButton.getButton().setDisable(false);
				zButton.getButton().setDisable(false);
				zpButton.getButton().setDisable(false);

				radio2d.setVisible(true);
				radio3d.setVisible(true);
				radio23d.setVisible(true);
				radio3dCamera.setVisible(true);
			}
		});
		
		root.getChildren().add(editButton);
		root.getChildren().add(endEditButton);
	}
	
	private void createColorPanel() 
	{
		int x = 355;
		int y = 330;

		colorPalet = new Rectangle(130, 90);
		colorPalet.setX(x);
		colorPalet.setY(y);
		colorPalet.setFill(Color.LIGHTGRAY);
		
		colorPalet.setArcHeight(12);
		colorPalet.setArcWidth(12);
		
		colorPalet.setStroke(Color.BLACK);	
		colorPalet.setStrokeWidth(2);
		
		root.getChildren().add(colorPalet);		
		
		white = new Rectangle(30, 30);
		white. setX(x + 10 * 1 + 30 * 0);
		white. setY(y + 10 * 1 + 30 * 0);
		blue = new Rectangle(30, 30);
		blue.  setX(x + 10 * 2 + 30 * 1);
		blue.  setY(y + 10 * 1 + 30 * 0);
		red = new Rectangle(30, 30);
		red.   setX(x + 10 * 3 + 30 * 2);
		red.   setY(y + 10 * 1 + 30 * 0);
		
		yellow = new Rectangle(30, 30);
		yellow. setX(x + 10 * 1 + 30 * 0);
		yellow. setY(y + 10 * 2 + 30 * 1);
		green = new Rectangle(30, 30);
		green.  setX(x + 10 * 2 + 30 * 1);
		green.  setY(y + 10 * 2 + 30 * 1);
		orange = new Rectangle(30, 30);
		orange. setX(x + 10 * 3 + 30 * 2);
		orange. setY(y + 10 * 2 + 30 * 1);
		
		white.setFill(Color.FLORALWHITE);
		blue.setFill(Color.DODGERBLUE);
		red.setFill(Color.RED);
		yellow.setFill(Color.YELLOW);
		green.setFill(Color.LIMEGREEN);
		orange.setFill(Color.DARKORANGE);
		
		white.setArcHeight(10);
		white.setArcWidth(10);
		blue.setArcHeight(10);
		blue.setArcWidth(10);
		red.setArcHeight(10);
		red.setArcWidth(10);
		yellow.setArcHeight(10);
		yellow.setArcWidth(10);
		green.setArcHeight(10);
		green.setArcWidth(10);
		orange.setArcHeight(10);
		orange.setArcWidth(10);

		root.getChildren().add(white);
		root.getChildren().add(blue);
		root.getChildren().add(red);
		root.getChildren().add(yellow);
		root.getChildren().add(green);
		root.getChildren().add(orange);
		
		white.setStroke(Color.BLACK);
		white.setStrokeWidth(1.5);
		blue.setStroke(Color.BLACK);
		blue.setStrokeWidth(1.5);
		red.setStroke(Color.BLACK);
		red.setStrokeWidth(1.5);
		yellow.setStroke(Color.BLACK);
		yellow.setStrokeWidth(1.5);
		green.setStroke(Color.BLACK);
		green.setStrokeWidth(1.5);
		orange.setStroke(Color.BLACK);
		orange.setStrokeWidth(1.5);

		colorPalet.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) 
			{
				editType = -1;
				colorPalet.setFill(Color.LIGHTGRAY);
			}
		});
		
		white.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent e) 
			{
				editType = 1;
				colorPalet.setFill(Color.FLORALWHITE);
			}
		});

		blue.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent e) 
			{
				editType = 5;
				colorPalet.setFill(Color.DODGERBLUE);
			}
		});

		red.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent e) 
			{
				editType = 2;
				colorPalet.setFill(Color.RED);
			}
		});

		yellow.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent e) 
			{
				editType = 4;
				colorPalet.setFill(Color.YELLOW);
			}
		});

		green.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent e) 
			{
				editType = 0;
				colorPalet.setFill(Color.LIMEGREEN);
			}
		});

		orange.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent e) 
			{
				editType = 3;
				colorPalet.setFill(Color.DARKORANGE);
			}
		});
	}
		
	private void hideColorPanel() 
	{
		colorPalet.setVisible(false);
		white.setVisible(false);
		blue.setVisible(false);
		red.setVisible(false);
		yellow.setVisible(false);
		green.setVisible(false);
		orange.setVisible(false);
	}

	private void showColorPanel() 
	{
		colorPalet.setFill(Color.LIGHTGRAY);
		colorPalet.setVisible(true);
		white.setVisible(true);
		blue.setVisible(true);
		red.setVisible(true);
		yellow.setVisible(true);
		green.setVisible(true);
		orange.setVisible(true);
	}

	public void refreshCube() 
	{
		RubikCubeVisualizerSide currentLeft  = getSideByCode(
				cube.calculateLeft(currentUp.getCubeSide(), currentFront.getCubeSide()).getTypeCode()
				);

		RubikCubeVisualizerSide currentRight = getSideByCode(currentLeft.getOppositeCubeSideTypeCode());
		RubikCubeVisualizerSide currentBack  = getSideByCode(currentFront.getOppositeCubeSideTypeCode());
		RubikCubeVisualizerSide currentDown  = getSideByCode(currentUp.getOppositeCubeSideTypeCode());
		
		refreshers.get("2D").refresh();
		
		currentUp.hide();
		currentFront.hide();
		currentLeft.hide();
		currentDown.hide();
		currentBack.hide();
		currentRight.hide();
		
		if(toggleGroup.getSelectedToggle() == radio2d)
		{
			refreshers.get("2D").refresh();
		} else if(toggleGroup.getSelectedToggle() == radio3d)
		{
			refreshers.get("3D").refresh();
		} else if(toggleGroup.getSelectedToggle() == radio23d)
		{
			refreshers.get("23D").refresh();
		}  else if(toggleGroup.getSelectedToggle() == radio3dCamera)
		{
			refreshers.get("3DADV").refresh();
		}
		
		viewSlider2d3d.setVisible(toggleGroup.getSelectedToggle() == radio23d);
		viewSlider3d.setVisible(toggleGroup.getSelectedToggle()   == radio3d);
		viewSlider3dElevation.setVisible(toggleGroup.getSelectedToggle()  == radio3d);
		
		if(toggleGroup.getSelectedToggle() == radio2d)
		{
			viewSlider2d3d.setVisible(false);
			viewSlider3d.setVisible(false);
			viewSlider3dElevation.setVisible(false);
		}
	}

	public RubikCubeVisualizerSide getSideByCode(int sideCode) 
	{
		RubikCubeVisualizerSide ret=null;

		for(int lop=0; lop < sides.size(); lop++) {
			if(     sides.get(lop).getCubeSide().getTypeCode() == sideCode) {
				ret=sides.get(lop);
				break;
			}
		}

		return ret;
	}

	public Group getRoot() 
	{
		return root;
	}

	public RubikCube getCube() 
	{
		return cube;
	}

	public Color getColorForType(int sideCode) 
	{
		Color color = null;
		switch(sideCode)
		{
		case 0:
			color = Color.LIMEGREEN;
			break;
		case 1:
			color = Color.FLORALWHITE;
			break;
		case 2:
			color = Color.RED;
			break;
		case 3:
			color = Color.DARKORANGE;
			break;
		case 4:
			color = Color.YELLOW;
			break;
		case 5:
			color = Color.DODGERBLUE;
			break;
		}
		return color;
	}

	@Override
	public void onSideMoved() 
	{
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				refreshCube();				
			}
			});
	}

	public void runButtonAction(String buttonAction)
	{		
		switch (buttonAction) 
		{
		case "Scramble":
			scrambleThread = new Thread() {
				@Override
				public void run() 
				{
					cube.setSlowSpeed(1000 / waitTimeTextBox.getIntValue());
					RubikCubeShuffler scrambler = new RubikCubeShuffler(cube);
					try 
					{
				        editButton.   setDisable(true);
				        endEditButton.setDisable(true);
						setButtonEnable(false);
						scrambler.scramble(20);
				        editButton.   setDisable(false);
				        endEditButton.setDisable(false);
						setButtonEnable(true);
					} catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
	        };
	        scrambleThread.start();
			break;
		case "Solve":
			solveThread = new Thread() 
			{			
				@Override
				public void run() 
				{
					cube.setSlowSpeed(1000 / waitTimeTextBox.getIntValue());
					try 
					{
						if(!solveButton.getButton().isDisabled())
						{
							setButtonEnable(false);
					        editButton.   setDisable(true);
					        endEditButton.setDisable(true);
					        solver.solve();
							setButtonEnable(true);
					        editButton.   setDisable(false);
					        endEditButton.setDisable(false);
						}
					} catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
	        };
	        editButton.   setDisable(true);
	        endEditButton.setDisable(true);
	        solveThread.start();
			break;
		case "Reset": 
			resetCube(); 
			break;
		case "X":
			RubikCubeVisualizerSide sideDown = getSideByCode(currentUp.getOppositeCubeSideTypeCode());
			
			this.currentUp = currentFront;
			this.currentFront = sideDown;

			refreshCube();
			break;
		case "X'":
			RubikCubeVisualizerSide sideUp =  getSideByCode(currentUp.getCubeSideTypeCode());
			
			this.currentUp = getSideByCode(currentFront.getOppositeCubeSideTypeCode());
			this.currentFront = sideUp;
			
			refreshCube();
			break;
		case "Y":
			this.currentFront = 
					getSideByCode(cube.calculateLeft(
							currentUp.getCubeSide(), 
							currentFront.getCubeSide()).
							getOppositeSide().
							getTypeCode());

			refreshCube();
			break;
		case "Y'":
			this.currentFront = 
					getSideByCode(cube.calculateLeft(
							currentUp.getCubeSide(), 
							currentFront.getCubeSide()).
							getTypeCode());
			refreshCube();
			break;
		case "Z":
			this.currentUp =
					getSideByCode(cube.calculateLeft(
							currentUp.getCubeSide(), 
							currentFront.getCubeSide()).
							getTypeCode());
			refreshCube();
			break;
		case "Z'":
			this.currentUp =
					getSideByCode(cube.calculateLeft(
						currentUp.getCubeSide(), 
						currentFront.getCubeSide()).
						getOppositeSide().
						getTypeCode());
			refreshCube();
			break;
		case "M":
			refresh.moveMiddleDown(1);
			break;
		case "M'":
			refresh.moveMiddleUp(1);
			break;
		case "E":
			refresh.moveMiddleRight(1);
			break;
		case "E'":
			refresh.moveMiddleLeft(1);
			break;
		case "S":
			refresh.moveMiddleClockwise(1);
			break;
		case "S'":
			refresh.moveMiddleCounterClockwise(1);
			break;
		case "Revert":
			resetUpFrontSides();
			
			if(toggleGroup.getSelectedToggle() == radio3d)
			{
				viewAngle3d = 45.0;
				viewAngle3dElevation = 30.0;
				viewSlider3d.setValue(45.0);
				viewSlider3dElevation.setValue(30.0);
			} else if(toggleGroup.getSelectedToggle() == radio23d)
			{
				viewAngle2d3d = 45.0;
				viewSlider2d3d.setValue(45.0);
			}
				
			refreshCube();
			break;
		default:
			if(buttonAction.equals(""))
			{
				break;
			}
			try 
			{
				if(!capitalButton.isSelected()) //LowerCase
				{ 
					String actionType = buttonAction.length() == 2 ? "'" : "";
					int moveCount = 1;
					boolean prime = actionType.equals("'");
					switch (buttonAction.substring(0, 1)) {
					case "F":
						moveMiddleClockwiseCounter(prime, moveCount);
						break;
					case "B":
						moveMiddleClockwiseCounter(!prime, moveCount);
						break;
					case "R":
						moveMiddleUpDown(prime, moveCount);
						break;
					case "L":
						moveMiddleUpDown(!prime, moveCount);
						break;
					case "U":
						moveMiddleLeftRight(prime, moveCount);
						break;
					case "D":
						moveMiddleLeftRight(!prime, moveCount);
						break;
					}
				}
				cube.moveFromNotation(currentUp.getCubeSide(), currentFront.getCubeSide(), buttonAction);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	private void setButtonEnable(boolean isEnabled) 
	{
		scrambleButton.getButton().setDisable(!isEnabled);
		solveButton.getButton().setDisable(!isEnabled);
		resetButton.getButton().setDisable(!isEnabled);
		waitTimeTextBox.setDisable(!isEnabled);
		
		mButton.getButton().setDisable(!isEnabled);
		mpButton.getButton().setDisable(!isEnabled);
		eButton.getButton().setDisable(!isEnabled);
		epButton.getButton().setDisable(!isEnabled);
		sButton.getButton().setDisable(!isEnabled);
		spButton.getButton().setDisable(!isEnabled);
		revertButton.getButton().setDisable(!isEnabled);
		
		capitalButton.setDisable(!isEnabled);
		
		if(!isEnabled)
		{
			waitTimeText.setFill(Color.LIGHTGRAY);
		} else
		{
			waitTimeText.setFill(Color.BLACK);
		}
		
		for(int lop = 0; lop < buttons.size(); lop++)
		{
			buttons.get(lop).getButton().setDisable(!isEnabled);
		}		
	}	
	
	private void resetCube() 
	{
		for(int sideCode = 0; sideCode < 6; sideCode++)
		{
			for(int lop = 0; lop < 9; lop++)
			{
				cube.getSide(sideCode).getPieceFace(lop).setTypeCode(sideCode);
			}
		}
		refreshCube();
	}

	public int getOrientationCodeWhenSideAt(
			RubikCubeSide thisSide, RubikCubeSide otherSide, int otherSideDirection) 
	{
		int ret = -1;
		
		int otherSideCodeAt = thisSide.getCompassFor(otherSide);
		
		if(otherSideDirection == 5) {
			switch(otherSideCodeAt)
			{
			case 1:
				ret = 3;
				break;
			case 3:
				ret = 7;
				break;
			case 5:
				ret = 1;
				break;
			case 7:
				ret = 5;
				break;
			
			}			
		} else if(otherSideDirection == 7) {
			switch(otherSideCodeAt) //Never used, has Issues
			{
			case 1:
				ret = 3;
				break;
			case 3:
				ret = 5;
				break;
			case 5:
				ret = 7;
				break;
			case 7:
				ret = 1;
				break;
			
			}			
		} else if(otherSideDirection == 1) {
			switch(otherSideCodeAt)
			{
			case 1:
				ret = 1;
				break;
			case 3:
				ret = 3;
				break;
			case 5:
				ret = 5;
				break;
			case 7:
				ret = 7;
				break;
			
			}			
		} else if(otherSideDirection == 3) {
			switch(otherSideCodeAt) //Never used, has issues
			{
			case 1:
				ret = 7;
				break;
			case 3:
				ret = 5;
				break;
			case 5:
				ret = 3;
				break;
			case 7:
				ret = 1;
				break;
			
			}	
		} else
		{
			
		}
		return ret;
	}

	public void moveMiddleUpDown(boolean prime, int count)
	{
		if(prime)
		{
			refresh.moveMiddleDown(count);
		} else
		{
			refresh.moveMiddleUp(count);
		}
	}
	
	public void moveMiddleClockwiseCounter(boolean prime, int count)
	{
		if(prime)
		{
			refresh.moveMiddleCounterClockwise(count);
		} else
		{
			refresh.moveMiddleClockwise(count);
		}
	}
	
	public void moveMiddleLeftRight(boolean prime, int count)
	{
		if(prime)
		{
			refresh.moveMiddleRight(count);
		} else
		{
			refresh.moveMiddleLeft(count);
		}
	}
		
	public RubikCubeVisualizerSide getCurrentUp() 
	{
		return currentUp;
	}
	
	public RubikCubeVisualizerSide getCurrentFront() 
	{
		return currentFront;
	}
	
	public void setCurrentUp(RubikCubeVisualizerSide newUp) 
	{
		currentUp = newUp;
	}
	
	public void setCurrentFront(RubikCubeVisualizerSide newFront) 
	{
		currentFront = newFront;
	}
	
	public int getEditColorCode()
	{
		return editType;
	}
	
	public boolean isEditing() 
	{
		return isEditing;
	}
}
