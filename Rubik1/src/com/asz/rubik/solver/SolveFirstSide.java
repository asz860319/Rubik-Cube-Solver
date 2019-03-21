package com.asz.rubik.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.asz.rubik.RubikCubePrinter2D;
import com.asz.rubik.fundamentals.RubikCube;
import com.asz.rubik.fundamentals.RubikCubeCorner;
import com.asz.rubik.fundamentals.RubikCubeEdge;
import com.asz.rubik.fundamentals.RubikCubeSide;

public class SolveFirstSide {

	private RubikCube cube;
	private int antiRecurse;
	private int succeded;
	private int failed;
	private RubikCubeSide solvingSide;
	
	public SolveFirstSide(RubikCube cube, RubikCubeSide firstSide) 
	{
		this.cube = cube;
		this.solvingSide = firstSide;
	}

	public void solveCorners() throws Exception 
	{		
		RubikCubeSide bestSide = solvingSide;
		ArrayList<RubikCubeSide> adjacentSides = bestSide.getAdjacentSidesOrderedReverse();
		RubikCubeCorner corner;
		
		for(int lop = 0; lop < adjacentSides.size(); lop++)
		{
			antiRecurse = 0;

			RubikCubeSide c1 = bestSide;
			RubikCubeSide c2 = adjacentSides.get(lop);
		
			RubikCubeSide c3 = lop == adjacentSides.size() - 1 ? adjacentSides.get(0) : adjacentSides.get(lop+1);

			corner = cube.getCornerPieceByFace(c1, c2, c3); 
			//					
//			System.out.println
//					("Solving: " + 	printer.convertTypeColor(c1.getTypeCode()) + 
//					 " and "      + printer.convertTypeColor(c2.getTypeCode()));

			solveFirstSideCornerByActionArray(c1, c2, corner);

			try {
				solveCornersTest(c1, c2, corner);
				succeded++;
			} catch (Exception e) 
			{
				failed++;
			}
			
//			System.out.println("***SuccesRateCorners:" + 100 * succeded / (succeded + failed) + "***");
		}
	}
	
	private void solveCornersTest(RubikCubeSide bestSide, RubikCubeSide solvingSide, 
			RubikCubeCorner corner) throws Exception 
	{
		if(!corner.isInCorrectLocation())
		{	
			throw new Exception();
		}
	}

	public void solveCross() throws Exception 
	{
		RubikCubeSide bestSide = solvingSide;
		
		ArrayList<RubikCubeSide> adjacentSides = bestSide.getAdjacentSidesOrdered();
		ArrayList<RubikCubeEdge> edges = new ArrayList<RubikCubeEdge>();
		
		for(int i = 0; i < adjacentSides.size(); i++)
		{
			edges.add(cube.getEdgePieceByFace(bestSide, adjacentSides.get(i)));
		}
		
		for(int lop = 0; lop < edges.size(); lop++)
		{			
			solveCrossEdgeByActionArray(bestSide, adjacentSides.get(lop), edges.get(lop));
			
			try 
			{
				solveEdgesTest(bestSide, adjacentSides.get(lop), edges.get(lop));
				succeded++;
			} catch (Exception e) 
			{
				failed++;
			}
//			System.out.println("***SuccesRateEdges:" + 100 * succeded / (succeded + failed) + "***");
		}	
	}

	private void solveEdgesTest(
			RubikCubeSide bestSide, RubikCubeSide sideToSolve, RubikCubeEdge edge) throws Exception 
	{
		if(!edge.isInCorrectLocation())
		{
			throw new Exception();
		}
	}

	private void solveCrossEdgeByActionArray(
			RubikCubeSide sideCrossToSolve,
			RubikCubeSide sideCrossToSolveArm, 
			RubikCubeEdge edge
			) throws Exception 
	{		
		RubikCubeSide sideUp     = sideCrossToSolve;
		RubikCubeSide sideFront  = sideCrossToSolveArm;
		RubikCubeSide sideDown   = sideUp.getOppositeSide();
		RubikCubeSide sideLeft   = cube.calculateLeft(sideUp, sideFront);
		RubikCubeSide sideRight  = sideLeft.getOppositeSide();
		RubikCubeSide sideBack   = sideFront.getOppositeSide();
		
		String edgeLocationCode="";
		
		// 2 letters location code
		edgeLocationCode += (edge.getSideOn(sideUp) != null && edge.getSideOn(sideDown) == null) ? "U" : "";
		edgeLocationCode += (edge.getSideOn(sideUp) == null && edge.getSideOn(sideDown) != null) ? "D" : "";
		edgeLocationCode += (edge.getSideOn(sideFront) != null) ? "F" : "";
		edgeLocationCode += (edge.getSideOn(sideBack) != null) ? "B" : "";
		edgeLocationCode += (edge.getSideOn(sideRight) != null) ? "R" : "";
		edgeLocationCode += (edge.getSideOn(sideLeft) != null) ? "L" : "";

		// adding 3rd character orientation code		
		//Top Layer
		edgeLocationCode = solveCrossEdgeByActionArray_addOrientationCode(edgeLocationCode, "UF",
				(edge.getSideOn(sideUp) == sideUp));
		
		edgeLocationCode = solveCrossEdgeByActionArray_addOrientationCode(edgeLocationCode, "UR",
				(edge.getSideOn(sideUp) == sideFront));
		
		edgeLocationCode = solveCrossEdgeByActionArray_addOrientationCode(edgeLocationCode, "UL",
				(edge.getSideOn(sideUp) == sideFront));
		
		edgeLocationCode = solveCrossEdgeByActionArray_addOrientationCode(edgeLocationCode, "UB",
				(edge.getSideOn(sideUp) == sideUp));
		
		//Middle Layer
		edgeLocationCode = solveCrossEdgeByActionArray_addOrientationCode(edgeLocationCode, "FR",
				(edge.getSideOn(sideFront) == sideFront));
		
		edgeLocationCode = solveCrossEdgeByActionArray_addOrientationCode(edgeLocationCode, "FL",
				(edge.getSideOn(sideFront) == sideFront));
		
		edgeLocationCode = solveCrossEdgeByActionArray_addOrientationCode(edgeLocationCode, "BR",
				(edge.getSideOn(sideBack) == sideUp));
		
		edgeLocationCode = solveCrossEdgeByActionArray_addOrientationCode(edgeLocationCode, "BL",
				(edge.getSideOn(sideBack) == sideUp));
		
		//Bottom Layer
		edgeLocationCode = solveCrossEdgeByActionArray_addOrientationCode(edgeLocationCode, "DF",
				(edge.getSideOn(sideDown) == sideUp));
		
		edgeLocationCode = solveCrossEdgeByActionArray_addOrientationCode(edgeLocationCode, "DR",
				(edge.getSideOn(sideDown) == sideUp));
		
		edgeLocationCode = solveCrossEdgeByActionArray_addOrientationCode(edgeLocationCode, "DL",
				(edge.getSideOn(sideDown) == sideUp));
		
		edgeLocationCode = solveCrossEdgeByActionArray_addOrientationCode(edgeLocationCode, "DB",
				(edge.getSideOn(sideDown) == sideUp));
		
		String actionsToDo = getActionForLocation(edgeLocationCode);
		
		String[] actionsList = actionsToDo.split(",");
		
		for(int lop = 0; lop < actionsList.length; lop++) 
		{
			String action = actionsList[lop].trim();
			
			if(action.startsWith("M"))
			{
				String moveType = action.substring(1);
				cube.moveFromNotation(sideUp, sideFront, moveType);
			}

			if(action.startsWith("R")) 
			{
				solveCrossEdgeByActionArray(sideCrossToSolve, sideCrossToSolveArm, edge);
			}

			if(action.startsWith("S")) 
			{
				return;
			}
		}
		
		solveCrossEdgeByActionArray(sideCrossToSolve, sideCrossToSolveArm, edge);

		return;
	}	
	
	private String solveCrossEdgeByActionArray_addOrientationCode(
			String edgeLocationCode, String locCodeCase, boolean easyCondition) 
	{
		String ret = edgeLocationCode;
		
		if(edgeLocationCode.equals(locCodeCase)) 
		{
			ret += easyCondition ? "E" : "H";
		}

		return ret;
	}

	private static Map<String, String> locationCodeToActionMap = null;	
	private String getActionForLocation(String edgeLocationCode) 
	{
		if(SolveFirstSide.locationCodeToActionMap == null)
		{ // initialize once
			SolveFirstSide.locationCodeToActionMap = new HashMap<String, String>();
			
			// Move: MF', Reevaluate: R, Success: S
			
			//Top Layer			
			SolveFirstSide.locationCodeToActionMap.put("UFE", "S");
			SolveFirstSide.locationCodeToActionMap.put("UFH", "MF2");
			SolveFirstSide.locationCodeToActionMap.put("URE", "MR'");
			SolveFirstSide.locationCodeToActionMap.put("URH", "MR2");
			SolveFirstSide.locationCodeToActionMap.put("ULE", "ML");
			SolveFirstSide.locationCodeToActionMap.put("ULH", "ML2");
			SolveFirstSide.locationCodeToActionMap.put("UBE", "MB2");
			SolveFirstSide.locationCodeToActionMap.put("UBH", "MB2");
			
			//Middle Layer
			SolveFirstSide.locationCodeToActionMap.put("FRE", "MF'");
			SolveFirstSide.locationCodeToActionMap.put("FRH", "MR',MD',MR");
			SolveFirstSide.locationCodeToActionMap.put("FLE", "MF");
			SolveFirstSide.locationCodeToActionMap.put("FLH", "ML, MD, ML'");
			SolveFirstSide.locationCodeToActionMap.put("BRE", "MB',MD, MB");
			SolveFirstSide.locationCodeToActionMap.put("BRH", "MR, MD, MR'");
			SolveFirstSide.locationCodeToActionMap.put("BLE", "MB, MD, MB'");
			SolveFirstSide.locationCodeToActionMap.put("BLH", "ML',MD, ML");

			//Bottom Layer
			SolveFirstSide.locationCodeToActionMap.put("DFE", "MF2");
			SolveFirstSide.locationCodeToActionMap.put("DFH", "MD, MR, R, MR'");
			SolveFirstSide.locationCodeToActionMap.put("DRE", "MD'");
			SolveFirstSide.locationCodeToActionMap.put("DRH", "MR, R, MR'");
			SolveFirstSide.locationCodeToActionMap.put("DLE", "MD");
			SolveFirstSide.locationCodeToActionMap.put("DLH", "ML', R, ML");
			SolveFirstSide.locationCodeToActionMap.put("DBE", "MD2");
			SolveFirstSide.locationCodeToActionMap.put("DBH", "MD");	
		}
		
		return SolveFirstSide.locationCodeToActionMap.get(edgeLocationCode);
	}
	
	//CORNERS
	private static Map<String, String> locationCornerCodeToActionMap = null;	
	private String getActionForCornerLocation(String cornerLocationCode) 
	{
		if(SolveFirstSide.locationCornerCodeToActionMap == null)
		{ // initialize once
			SolveFirstSide.locationCornerCodeToActionMap = new HashMap<String, String>();
			
			// Move: MF', Reevaluate: R, Success: S
			
			//Top Layer
			SolveFirstSide.locationCodeToActionMap.put("UFL1", "S"); 
			SolveFirstSide.locationCodeToActionMap.put("UFL2", "ML, MD, ML'"); 
			SolveFirstSide.locationCodeToActionMap.put("UFL3", "ML, MD, ML'");
			SolveFirstSide.locationCodeToActionMap.put("UFR1", "MR', MD', MR");
			SolveFirstSide.locationCodeToActionMap.put("UFR2", "MR', MD', MR");
			SolveFirstSide.locationCodeToActionMap.put("UFR3", "MR', MD', MR, MD");
			
			SolveFirstSide.locationCodeToActionMap.put("UBR1", "MB', MD', MB");
			SolveFirstSide.locationCodeToActionMap.put("UBR2", "MB', MD', MB");
			SolveFirstSide.locationCodeToActionMap.put("UBR3", "MB', MD', MB");
			SolveFirstSide.locationCodeToActionMap.put("UBL1", "MB, MD, MB'");
			SolveFirstSide.locationCodeToActionMap.put("UBL2", "MB, MD, MB'");
			SolveFirstSide.locationCodeToActionMap.put("UBL3", "MB, MD, MB'");
			
			//Bottom Layer
			SolveFirstSide.locationCodeToActionMap.put("DFL3", "ML, MD, ML'");
			SolveFirstSide.locationCodeToActionMap.put("DFL2", "MF', MD', MF");
			SolveFirstSide.locationCodeToActionMap.put("DFL1", "ML, MD, ML'");
			SolveFirstSide.locationCodeToActionMap.put("DFR3", "MD'");
			SolveFirstSide.locationCodeToActionMap.put("DFR2", "MD'");
			SolveFirstSide.locationCodeToActionMap.put("DFR1", "MD'");
			
			SolveFirstSide.locationCodeToActionMap.put("DBR3", "MD2");
			SolveFirstSide.locationCodeToActionMap.put("DBR2", "MD2");
			SolveFirstSide.locationCodeToActionMap.put("DBR1", "MD2");
			SolveFirstSide.locationCodeToActionMap.put("DBL3", "MD");
			SolveFirstSide.locationCodeToActionMap.put("DBL2", "MD");
			SolveFirstSide.locationCodeToActionMap.put("DBL1", "MD");
		}
		return SolveFirstSide.locationCodeToActionMap.get(cornerLocationCode);
	}
	
	private void solveFirstSideCornerByActionArray(RubikCubeSide sideToSolve, 
			RubikCubeSide frontSide, RubikCubeCorner corner) throws Exception 
	{
//		System.out.println("\n" + corner.introduceYourSelf()); 

		if(antiRecurse > 12)
		{
			System.out.println("ANTIRECURSE ABORTED");
			throw new Exception("ANTIRECURSE ABORTED");
		}
		antiRecurse++;
		
		RubikCubeSide sideUp     = sideToSolve;
		RubikCubeSide sideFront  = frontSide;
		RubikCubeSide sideDown = sideUp.getOppositeSide();
		RubikCubeSide sideLeft = cube.calculateLeft(sideUp, sideFront);
		RubikCubeSide sideRight = sideLeft.getOppositeSide();
		RubikCubeSide sideBack = sideFront.getOppositeSide();

		String clc = "";

		clc += corner.getSideOn(sideUp)    != null ? "U" : "D";
		clc += corner.getSideOn(sideFront) != null ? "F" : "B";
		clc += corner.getSideOn(sideLeft)  != null ? "L" : "R";

		//Up Side
		clc = solveFirstSideCornerByActionArrayOrientation(clc, corner, "UFL", sideUp, sideUp, sideFront);
		clc = solveFirstSideCornerByActionArrayOrientation(clc, corner, "UFR", sideUp, sideUp, sideRight);
		clc = solveFirstSideCornerByActionArrayOrientation(clc, corner, "UBR", sideUp, sideUp, sideBack);
		clc = solveFirstSideCornerByActionArrayOrientation(clc, corner, "UBL", sideUp, sideUp, sideLeft);
		
		//Down Side
		clc = solveFirstSideCornerByActionArrayOrientation(clc, corner, "DFL", sideUp, sideDown, sideFront);
		clc = solveFirstSideCornerByActionArrayOrientation(clc, corner, "DFR", sideUp, sideDown, sideRight);
		clc = solveFirstSideCornerByActionArrayOrientation(clc, corner, "DBR", sideUp, sideDown, sideBack);
		clc = solveFirstSideCornerByActionArrayOrientation(clc, corner, "DBL", sideUp, sideDown, sideLeft);

		String cornerLocationCode = clc;
		
		String actionsToDo = getActionForCornerLocation(cornerLocationCode);
	
		String[] actionsList = actionsToDo.split(",");
		
//		System.out.println(cornerLocationCode + " " + actionsToDo);
		
		for(int lop = 0; lop < actionsList.length; lop++) 
		{
			String action = actionsList[lop].trim();
			
			if(action.startsWith("M"))
			{
				String moveType = action.substring(1);
				try 
				{
					cube.moveFromNotation(sideUp, sideFront, moveType);
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}

			if(action.startsWith("R")) 
			{
				solveFirstSideCornerByActionArray(sideToSolve, sideFront, corner);
			}

			if(action.startsWith("S")) 
			{
				return;
			}
		}
		
		solveFirstSideCornerByActionArray(sideToSolve, sideFront, corner);
	}
	
	private String solveFirstSideCornerByActionArrayOrientation
			(
			String cornerLocationCode, 
			RubikCubeCorner corner,
			String locCodeCase,
			RubikCubeSide faceUp,
			RubikCubeSide expectedUp1, 
			RubikCubeSide expectedUp2
			) 
	{
		String ret = cornerLocationCode;
		
		if (cornerLocationCode.equals(locCodeCase)) 
		{
			if(corner.getSideOn(expectedUp1) == faceUp)
			{
				ret += "1";
			} else if(corner.getSideOn(expectedUp2) == faceUp)
			{
				ret += "2";
			} else
			{
				ret += "3";
			}
		}

		return ret;
	}
	
}
