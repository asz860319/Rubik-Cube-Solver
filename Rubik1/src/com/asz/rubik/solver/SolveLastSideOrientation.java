package com.asz.rubik.solver;

import java.util.ArrayList;
import java.util.HashMap;

import com.asz.rubik.RubikCubePrinter2D;
import com.asz.rubik.fundamentals.RubikCube;
import com.asz.rubik.fundamentals.RubikCubeCorner;
import com.asz.rubik.fundamentals.RubikCubeEdge;
import com.asz.rubik.fundamentals.RubikCubeSide;

public class SolveLastSideOrientation 
{

	private RubikCube cube;
	private RubikCubeSide downSide;
	private int antiRecurseEdge = 0;
	
	public boolean solveDone;
	private int antiRecurse;
	
	public SolveLastSideOrientation(RubikCube cube, RubikCubeSide downSide) 
	{
		this.cube = cube;
		this.downSide = downSide;
	}

	public void solveCornerOrientation() throws Exception 
	{
		RubikCubeSide sideUp = downSide.getOppositeSide();
		
		ArrayList<RubikCubeSide> adjacentSides = sideUp.getAdjacentSidesOrdered();		
		RubikCubeSide sideFront = adjacentSides.get(0);	
		
		antiRecurse = 0;
		
		solveCrossCornerOrientation(sideUp, sideFront);		
	}

	private void solveCrossCornerOrientation(RubikCubeSide sideUp, RubikCubeSide sideFront) 
			throws Exception 
	{
		if(antiRecurse > 12)
		{
			throw new Exception("ANTIRECURSE ABORTED");
		}
		antiRecurse++;

		RubikCubeSide sideLeft = cube.calculateLeft(sideUp, sideFront);
		RubikCubeSide sideRight = sideLeft.getOppositeSide();
		RubikCubeSide sideBack = sideFront.getOppositeSide();

		RubikCubeCorner cornerLocation1 = cube.getCornerPieceByLocation(sideUp, sideFront, sideLeft);
		RubikCubeCorner cornerLocation2 = cube.getCornerPieceByLocation(sideUp, sideFront, sideRight);
		RubikCubeCorner cornerLocation3 = cube.getCornerPieceByLocation(sideUp, sideBack, sideRight);
		RubikCubeCorner cornerLocation4 = cube.getCornerPieceByLocation(sideUp, sideBack, sideLeft);
		
		String cornerOrientationCode = "";
		
		boolean cornerFacesOnFrontSideMatching = 
				cornerLocation2.getSideOn(sideFront) == cornerLocation1.getSideOn(sideFront);
		boolean cornerFacesOnLeftSideMatching = 
				cornerLocation1.getSideOn(sideLeft) == cornerLocation4.getSideOn(sideLeft);
		boolean cornerFacesOnBackSideMatching = 
				cornerLocation4.getSideOn(sideBack) == cornerLocation3.getSideOn(sideBack);
		boolean cornerFacesOnRightSideMatching = 
				cornerLocation3.getSideOn(sideRight) == cornerLocation2.getSideOn(sideRight);
		
		boolean noCornerFaceMatching = !(
				cornerFacesOnFrontSideMatching || cornerFacesOnBackSideMatching || 
				cornerFacesOnLeftSideMatching || cornerFacesOnRightSideMatching);
		
		cornerOrientationCode += cornerFacesOnBackSideMatching && cornerFacesOnFrontSideMatching && 
								 cornerFacesOnLeftSideMatching && cornerFacesOnRightSideMatching ?
											   "A" : "";
		
		cornerOrientationCode += cornerFacesOnBackSideMatching ?
					"B" : "";
		
		cornerOrientationCode += cornerFacesOnFrontSideMatching ?
				    "C" : "";

		cornerOrientationCode += cornerFacesOnLeftSideMatching ?
				    "D" : "";

		cornerOrientationCode += cornerFacesOnRightSideMatching ?
				    "E" : "";
		
		cornerOrientationCode += noCornerFaceMatching ?
					"F" : "";
		
		cornerOrientationCode = cornerOrientationCode.substring(0, 1);
		
		String actionsToDo = getActionForLocationForCornerOrientation(cornerOrientationCode);
		
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
				solveCrossCornerOrientation(sideUp, sideFront);
			}

			if(action.startsWith("S")) 
			{
				
				return;
			}
		}
		
		solveCrossCornerOrientation(sideUp, sideFront);	
		return;
	}

	private void solveLastCornerOrientationTest(RubikCubeSide sideUp, RubikCubeSide sideFront) throws Exception 
	{
		RubikCubeSide sideLeft = cube.calculateLeft(sideUp, sideFront);
		RubikCubeSide sideRight = sideLeft.getOppositeSide();
		RubikCubeSide sideBack = sideFront.getOppositeSide();
		
		RubikCubeCorner cornerLocation1 = cube.getCornerPieceByLocation(sideUp, sideFront, sideLeft);
		RubikCubeCorner cornerLocation2 = cube.getCornerPieceByLocation(sideUp, sideFront, sideRight);
		RubikCubeCorner cornerLocation3 = cube.getCornerPieceByLocation(sideUp, sideBack, sideLeft);
		RubikCubeCorner cornerLocation4 = cube.getCornerPieceByLocation(sideUp, sideBack, sideRight);

		if(!(
				cornerLocation1.isInCorrectLocation() && cornerLocation2.isInCorrectLocation() &&
				cornerLocation3.isInCorrectLocation() && cornerLocation4.isInCorrectLocation()
				))
		{
			throw new Exception();
		}
	}

	private static HashMap<String, String> cornerOrientationCodeToActionMap = null;
	private String getActionForLocationForCornerOrientation(String cornersOrientationCode) 
	{
		if(SolveLastSideOrientation.cornerOrientationCodeToActionMap == null)
		{ // initialize once
			SolveLastSideOrientation.cornerOrientationCodeToActionMap = new HashMap<String, String>();
			
			// Move: MF', Reevaluate: R, Success: S
			
			//Top Layer			
			SolveLastSideOrientation.cornerOrientationCodeToActionMap.put("A", "S");
			
			SolveLastSideOrientation.cornerOrientationCodeToActionMap.put("B", 
					"MR', MF, MR', MB2, MR, MF', MR', MB2, MR2");
			SolveLastSideOrientation.cornerOrientationCodeToActionMap.put("C", "MU2");
			SolveLastSideOrientation.cornerOrientationCodeToActionMap.put("D", "MU");
			SolveLastSideOrientation.cornerOrientationCodeToActionMap.put("E", "MU'");

			SolveLastSideOrientation.cornerOrientationCodeToActionMap.put("F", 
					"MR', MF, MR', MB2, MR, MF', MR', MB2, MR2");
		}
		return SolveLastSideOrientation.cornerOrientationCodeToActionMap.get(cornersOrientationCode);
	}	
	
	public void solveEdgeOrientation() throws Exception 
	{
		RubikCubeSide sideUp = downSide.getOppositeSide();
		
		ArrayList<RubikCubeSide> adjacentSides = sideUp.getAdjacentSidesOrdered();		
		RubikCubeSide sideFront = adjacentSides.get(0);	
		
		solveEdgeLocation(sideUp, sideFront);		
		
		try 
		{
			solveEdgeLocationTest(sideUp, sideFront);
			solveDone = true;
		} catch (Exception e) 
		{
			solveDone = false;
		}
	}

	private void solveEdgeLocationTest(RubikCubeSide sideUp, RubikCubeSide sideFront) throws Exception 
	{
		if(!cube.isSolved())
		{
			throw new Exception();
		}
	}
	
	private void solveEdgeLocation(RubikCubeSide sideUp, RubikCubeSide sideFront) throws Exception 
	{
		antiRecurseEdge = 0;
		solveEdgeLocationDo(sideUp, sideFront);
	}
	
	private void solveEdgeLocationDo(RubikCubeSide sideUp, RubikCubeSide sideFront) throws Exception 
	{		
		if(antiRecurseEdge > 6)
		{
			throw new Exception("ANTI RECURSE");
		}
		antiRecurseEdge++;

		ArrayList<RubikCubeSide> adjacentSides = sideUp.getAdjacentSidesOrdered();
						
		RubikCubeSide adjacentSideSolved = null;
		
		for(int i = 0; i < adjacentSides.size(); i++)
		{
			if(adjacentSides.get(i).isShallowSorted())
			{
				adjacentSideSolved = adjacentSides.get(i);
				break;
			}
		}
		
		String edgeCode = null;
		RubikCubeSide viewSide = null;
		
		if(adjacentSideSolved != null)
		{
			viewSide = adjacentSideSolved.getOppositeSide();
			
			RubikCubeEdge edgeLocationUL = cube.getEdgePieceByLocation(sideUp, 
					cube.calculateLeft(sideUp, viewSide));
			RubikCubeEdge edgeLocationUR = cube.getEdgePieceByLocation(sideUp, 
					cube.calculateLeft(sideUp, viewSide).getOppositeSide());
			
			boolean clockwiseCase = 
					edgeLocationUL == cube.getSolvedEdgeInLocation(edgeLocationUR);
			
			edgeCode = "A" + (clockwiseCase ? "+" : "-");
		} else 
		{
			viewSide = sideFront;

			RubikCubeEdge edgeLocationUL = cube.getEdgePieceByLocation(sideUp, 
					cube.calculateLeft(sideUp, viewSide));
			RubikCubeEdge edgeLocationUR = cube.getEdgePieceByLocation(sideUp, 
					cube.calculateLeft(sideUp, viewSide).getOppositeSide());
			RubikCubeEdge edgeLocationUF = cube.getEdgePieceByLocation(sideUp, viewSide);
			RubikCubeEdge edgeLocationUB = cube.getEdgePieceByLocation(sideUp, viewSide.getOppositeSide());

			boolean oppositeSwitchCase = 
					edgeLocationUF == cube.getSolvedEdgeInLocation(edgeLocationUB) &&
					edgeLocationUR == cube.getSolvedEdgeInLocation(edgeLocationUL);
			
			edgeCode = "B" + (oppositeSwitchCase ? "+" : "-");
		}
		
		if(cube.isSolved())
		{
			edgeCode = "S";
		}
				
		String actionsToDo = getActionForLocationForEdge(edgeCode);
		
		String[] actionsList = actionsToDo.split(",");		
		
		for(int lop = 0; lop < actionsList.length; lop++) 
		{
			String action = actionsList[lop].trim();
			
			if(action.startsWith("M"))
			{
				String moveType = action.substring(1);
				cube.moveFromNotation(sideUp, viewSide, moveType);
			}

			if(action.startsWith("R")) 
			{
				solveEdgeLocationDo(sideUp, sideFront);
			}

			if(action.startsWith("S")) 
			{
				return;
			}
		}
		
		solveEdgeLocationDo(sideUp, sideFront);	
		return;
	}

	private static HashMap<String, String> edgeCodeToActionMap = null;
	private String getActionForLocationForEdge(String edgeCode) 
	{
		if(SolveLastSideOrientation.edgeCodeToActionMap == null)
		{ // initialize once
			SolveLastSideOrientation.edgeCodeToActionMap = new HashMap<String, String>();
			
			// Move: MF', Reevaluate: R, Success: S
			
			//Top Layer			
			SolveLastSideOrientation.edgeCodeToActionMap.put("S", "S");
			
			SolveLastSideOrientation.edgeCodeToActionMap.put("A+", 
					"MR2, MU, MR, MU, MR', MU', MR', MU', MR', MU, MR'");
			SolveLastSideOrientation.edgeCodeToActionMap.put("A-", 
					"MR, MU', MR, MU, MR, MU, MR, MU', MR', MU', MR2");
			
			SolveLastSideOrientation.edgeCodeToActionMap.put("B+", 
					"ML2, MR2, MD, ML2, MR2, MD2, ML2, MR2, MD, ML2, MR2, MD2, MU2");
			SolveLastSideOrientation.edgeCodeToActionMap.put("B-", 
					"MR2, MU, MR, MU, MR', MU', MR', MU', MR', MU, MR'");
		}
		return SolveLastSideOrientation.edgeCodeToActionMap.get(edgeCode);
	}

	public void moveCornersToCorrectSpot() throws Exception 
	{
		RubikCubeSide sideUp = downSide.getOppositeSide();
		RubikCubeSide sideFront = sideUp.getAdjacentSidesOrdered().get(0);
		RubikCubeSide sideLeft = cube.calculateLeft(sideUp, sideFront);
		
		RubikCubeCorner corner = cube.getCornerPieceByLocation(sideUp, sideFront, sideLeft);
		
		for(int lop = 0; lop < 3 && !corner.isInCorrectLocation(); lop++)
		{
			sideUp.moveClockwise();
		}
		
		try 
		{
			solveLastCornerOrientationTest(sideUp, sideFront);
		} catch (Exception e) 
		{
		}
	}

}
