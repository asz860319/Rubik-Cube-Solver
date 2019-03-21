package com.asz.rubik.solver;

import java.util.ArrayList;
import java.util.HashMap;

import com.asz.rubik.fundamentals.RubikCube;
import com.asz.rubik.fundamentals.RubikCubeEdge;
import com.asz.rubik.fundamentals.RubikCubeSide;

public class SolveF2L {

	private RubikCube cube;
	private RubikCubeSide bottomSide;
	private static int succeded = 0;
	private static int failed = 0;

	public SolveF2L(RubikCube cube, RubikCubeSide firstSide)
	{
		this.cube = cube;
		this.bottomSide = firstSide;
	}

	public void solveF2L() throws Exception 
	{
		RubikCubeSide topSide = bottomSide.getOppositeSide();
		
		ArrayList<RubikCubeSide> adjacentSides = topSide.getAdjacentSidesOrdered();		
		RubikCubeEdge edge;
		
		for(int i = 0; i < adjacentSides.size(); i++)
		{
			RubikCubeSide c1 = topSide;
			RubikCubeSide c2 = adjacentSides.get(i);
			RubikCubeSide c3;
			
			if(i == adjacentSides.size() - 1)
			{
				c3 = adjacentSides.get(0);
			} else 
			{
				c3 = adjacentSides.get(i + 1);
			}

			edge = cube.getEdgePieceByFace(c2, c3);
			
//			System.out.println(edge.introduce());
			solveF2LEdgeByActionArray(c1, c3, edge);
			
			try 
			{
				solveF2LEdgesTest(c1, c3, edge);
				succeded++;
			} catch (Exception e) 
			{
				failed++;
			}
						
//			System.out.println("***SuccesRateF2L:" + 100 * succeded / (succeded + failed) + "***");
//			System.out.println("NEXT!!!");
		}	
		
//		System.out.println(new RubikCubePrinter2D().getPiecesAsCross(cube));
	}

	private void solveF2LEdgesTest(RubikCubeSide topSide, RubikCubeSide facingYouSide, 
			RubikCubeEdge edge) throws Exception 
	{
		if(!edge.isInCorrectLocation())
		{	
			throw new Exception();
		}
	}

	private void solveF2LEdgeByActionArray(RubikCubeSide topSide, RubikCubeSide facingYouSide,
			RubikCubeEdge edge) throws Exception
	{
 		RubikCubeSide sideUp = topSide;
		RubikCubeSide sideFront = facingYouSide;
		RubikCubeSide sideLeft = cube.calculateLeft(sideUp, sideFront);
		RubikCubeSide sideDown = sideUp.getOppositeSide();
		RubikCubeSide sideBack = sideFront.getOppositeSide();
		RubikCubeSide sideRight = sideLeft.getOppositeSide();
				
		String edgeLocationCode="";
		
		// 2 letters location code
		edgeLocationCode += (edge.getSideOn(sideUp) != null && edge.getSideOn(sideDown) == null) ? "U" : "";
		edgeLocationCode += (edge.getSideOn(sideFront) != null && edge.getSideOn(sideBack) == null) ? "F" : "";
		edgeLocationCode += (edge.getSideOn(sideBack) != null && edge.getSideOn(sideFront) == null) ? "B" : "";
		edgeLocationCode += (edge.getSideOn(sideRight) != null && edge.getSideOn(sideLeft) == null) ? "R" : "";
		edgeLocationCode += (edge.getSideOn(sideLeft) != null && edge.getSideOn(sideRight) == null) ? "L" : "";

		// adding 3rd character orientation code		
		//Top Layer
		edgeLocationCode = solveF2lEdgeByActionArray_addOrientationCode(edgeLocationCode, "UF",
				(edge.getSideOn(sideUp) == sideLeft));
		
		edgeLocationCode = solveF2lEdgeByActionArray_addOrientationCode(edgeLocationCode, "UR",
				(edge.getSideOn(sideUp) == sideLeft));
		
		edgeLocationCode = solveF2lEdgeByActionArray_addOrientationCode(edgeLocationCode, "UL",
				(edge.getSideOn(sideUp) == sideLeft));
		
		edgeLocationCode = solveF2lEdgeByActionArray_addOrientationCode(edgeLocationCode, "UB",
				(edge.getSideOn(sideUp) == sideLeft));
		
		//Middle Layer
		edgeLocationCode = solveF2lEdgeByActionArray_addOrientationCode(edgeLocationCode, "FL",
				(edge.getSideOn(sideFront) == sideFront));

		edgeLocationCode = solveF2lEdgeByActionArray_addOrientationCode(edgeLocationCode, "FR",
				(edge.getSideOn(sideFront) != null));
		
		edgeLocationCode = solveF2lEdgeByActionArray_addOrientationCode(edgeLocationCode, "BR",
				(edge.getSideOn(sideBack) == sideBack));
		
		edgeLocationCode = solveF2lEdgeByActionArray_addOrientationCode(edgeLocationCode, "BL",
				(edge.getSideOn(sideBack) != null));
		
		//What moves to do
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
				solveF2LEdgeByActionArray(sideUp, sideFront, edge);
			}

			if(action.startsWith("S")) 
			{
				return;
			}
		}
		
		solveF2LEdgeByActionArray(sideUp, sideFront, edge);

		return;
	}

	private static HashMap<String, String> locationCodeToActionMap = null;
	private String getActionForLocation(String edgeLocationCode) 
	{
		if(SolveF2L.locationCodeToActionMap == null)
		{ // initialize once
			SolveF2L.locationCodeToActionMap = new HashMap<String, String>();
			
			// Move: MF', Reevaluate: R, Success: S
			
			//Top Layer			
			SolveF2L.locationCodeToActionMap.put("UFE", "MU'");
			SolveF2L.locationCodeToActionMap.put("UFH", "MU2");
			SolveF2L.locationCodeToActionMap.put("URE", "ML', MU', ML, MU, MF, MU, MF'");
			SolveF2L.locationCodeToActionMap.put("URH", "MU'");
		
			SolveF2L.locationCodeToActionMap.put("ULE", "MU2");
			SolveF2L.locationCodeToActionMap.put("ULH", "MU");
			SolveF2L.locationCodeToActionMap.put("UBE", "MU");
			SolveF2L.locationCodeToActionMap.put("UBH", "MF, MU, MF', MU', ML', MU', ML");
			
			//Middle Layer
			SolveF2L.locationCodeToActionMap.put("FRE", "MR, MU, MR', MU', MF', MU', MF");
			SolveF2L.locationCodeToActionMap.put("FRH", "MR, MU, MR', MU', MF', MU', MF");
			SolveF2L.locationCodeToActionMap.put("FLE", "S");
			SolveF2L.locationCodeToActionMap.put("FLH", "ML', MU', ML, MU, MF, MU, MF'");			

			SolveF2L.locationCodeToActionMap.put("BRE", "MB, MU, MB', MU', MR', MU', MR");
			SolveF2L.locationCodeToActionMap.put("BRH", "MB, MU, MB', MU', MR', MU', MR");
			SolveF2L.locationCodeToActionMap.put("BLE", "MB', MU', MB, MU, ML, MU, ML'");
			SolveF2L.locationCodeToActionMap.put("BLH", "MB', MU', MB, MU, ML, MU, ML'");
		}
		return SolveF2L.locationCodeToActionMap.get(edgeLocationCode);
	}

	private String solveF2lEdgeByActionArray_addOrientationCode
			(
			String edgeLocationCode, 
			String locCodeCase, 
			boolean easyCondition
			) 
	{
		String ret = edgeLocationCode;
		
		if(edgeLocationCode.equals(locCodeCase)) 
		{
			ret += easyCondition ? "E" : "H";
		}

		return ret;
	}
	
}
