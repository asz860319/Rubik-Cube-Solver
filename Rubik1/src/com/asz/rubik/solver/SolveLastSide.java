package com.asz.rubik.solver;

import java.util.ArrayList;
import java.util.HashMap;

import com.asz.rubik.RubikCubePrinter2D;
import com.asz.rubik.fundamentals.RubikCube;
import com.asz.rubik.fundamentals.RubikCubeCorner;
import com.asz.rubik.fundamentals.RubikCubeEdge;
import com.asz.rubik.fundamentals.RubikCubeSide;

public class SolveLastSide 
{
	private RubikCube cube;
	private RubikCubeSide bottomSide;
	private static int antiRecurse;
	private static int succeded;
	private static int failed;

	public SolveLastSide(RubikCube cube, RubikCubeSide bottomSide) 
	{
		this.cube = cube;
		this.bottomSide = bottomSide;
		succeded = 0;
		failed = 0;
	}

	public void solveLastCross() throws Exception 
	{
		RubikCubeSide topSide = bottomSide.getOppositeSide();
		
		ArrayList<RubikCubeSide> adjacentSides = topSide.getAdjacentSidesOrdered();		
		RubikCubeEdge edge1;
		RubikCubeEdge edge2;
		RubikCubeEdge edge3;
		RubikCubeEdge edge4;

		for(int i = 0; i < adjacentSides.size(); i++)
		{
			antiRecurse = 0;
			
			RubikCubeSide c1 = topSide;
			RubikCubeSide c2 = adjacentSides.get(i);
			RubikCubeSide c3;
			RubikCubeSide c4;
			RubikCubeSide c5;

			if(i == adjacentSides.size() - 1)
			{
				c3 = adjacentSides.get(0);
				c4 = adjacentSides.get(0);
				c5 = adjacentSides.get(0);
			} else if(i == adjacentSides.size() - 2)
			{
				c3 = adjacentSides.get(i + 1);
				c4 = adjacentSides.get(1);
				c5 = adjacentSides.get(1);
			} else if(i == adjacentSides.size() - 3)
			{
				c3 = adjacentSides.get(i + 1);
				c4 = adjacentSides.get(i + 2);
				c5 = adjacentSides.get(2);
			} else
			{
				c3 = adjacentSides.get(i + 1);
				c4 = adjacentSides.get(i + 2);
				c5 = adjacentSides.get(i + 3);
			}

			edge1 = cube.getEdgePieceByFace(c1, c2);
			edge2 = cube.getEdgePieceByFace(c1, c3);
			edge3 = cube.getEdgePieceByFace(c1, c4);
			edge4 = cube.getEdgePieceByFace(c1, c5);

			solveCrossEdgeByActionArray(c1, c2, edge1, edge2, edge3);
			
			try 
			{
				solveLastSideCrossTest(c1, c2, edge1);
				solveLastSideCrossTest(c1, c2, edge2);
				solveLastSideCrossTest(c1, c2, edge3);
				solveLastSideCrossTest(c1, c2, edge4);
				succeded++;
			} catch (Exception e) 
			{
				failed++;
			}
		}	
	}

	private void solveLastSideCrossTest(RubikCubeSide topSide, RubikCubeSide facingYouSide, 
			RubikCubeEdge edge) 
			throws Exception 
	{
		if(edge.getPieceFaceOn(topSide).getSide() != topSide)
		{	
			throw new Exception();
		}
	}

	private void solveCrossEdgeByActionArray(RubikCubeSide topSide, RubikCubeSide facingYouSide, 
			RubikCubeEdge edge1, RubikCubeEdge edge2, RubikCubeEdge edge3) throws Exception 
	{
		if(antiRecurse > 12)
		{
			throw new Exception("ANTIRECURSE ABORTED");
		}
		antiRecurse++;

		RubikCubeSide sideUp = topSide;
		RubikCubeSide sideFront = facingYouSide;
		RubikCubeSide sideLeft = cube.calculateLeft(sideUp, sideFront);
		RubikCubeSide sideBack = sideFront.getOppositeSide();
		RubikCubeSide sideRight = sideLeft.getOppositeSide();
				
		String edgesTypeCode = "";
		
		// 1 letter type code
		RubikCubeEdge edgeLocation1 = cube.getEdgePieceByLocation(sideUp, sideFront);
		RubikCubeEdge edgeLocation2 = cube.getEdgePieceByLocation(sideUp, sideBack);
		RubikCubeEdge edgeLocation3 = cube.getEdgePieceByLocation(sideUp, sideLeft);
		RubikCubeEdge edgeLocation4 = cube.getEdgePieceByLocation(sideUp, sideRight);
		
		edgesTypeCode += (
				edge1.getSideOn(sideUp) == sideUp && 
				edge2.getSideOn(sideUp) == sideUp && 
				edge3.getSideOn(sideUp) == sideUp) ? 
						"A" : "";
		
		edgesTypeCode += (
				edge1.getSideOn(sideUp) != sideUp && 
				edge2.getSideOn(sideUp) != sideUp && 
				edge3.getSideOn(sideUp) != sideUp) ? 
						"D" : "";
		
		edgesTypeCode += 
				edgeLocation1.getSideOn(sideUp) == sideUp && 
				edgeLocation2.getSideOn(sideUp) == sideUp || 
				edgeLocation3.getSideOn(sideUp) == sideUp && 
				edgeLocation4.getSideOn(sideUp) == sideUp ? 
						"C" : "B";
		
		edgesTypeCode = edgesTypeCode.substring(0, 1);
		
		// second letter orientation code?
		switch(edgesTypeCode)
		{
		case "A":
			edgesTypeCode += "1";
			break;
		case "B":
			if(
					edgeLocation2.getSideOn(sideUp) == sideUp && 
					edgeLocation3.getSideOn(sideUp) == sideUp)
			{
				edgesTypeCode += "1";
			} else if(
					edgeLocation1.getSideOn(sideUp) == sideUp && 
					edgeLocation4.getSideOn(sideUp) == sideUp) 
			{
				edgesTypeCode += "2";
			} else
			{
				edgesTypeCode += "3";
			}
			break;
		case "C":
			if(
				edge1.getSideOn(sideFront) == sideUp || 
				edge2.getSideOn(sideFront) == sideUp || 
				edge3.getSideOn(sideFront) == sideUp)
			{
				edgesTypeCode += "1";
			} else
			{
				edgesTypeCode += "2";
			}
			break;
		case "D":
			edgesTypeCode += "1";
			break;
		}
		
		//What moves to do
		String actionsToDo = getActionForLocation(edgesTypeCode);
		
		String[] actionsList = actionsToDo.split(",");
		
//		System.out.println(edge1.introduce());
//		System.out.println(edge2.introduce());
//		System.out.println(edge3.introduce());

//		System.out.println(edgesTypeCode + ": " + actionsToDo);
		
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
				solveCrossEdgeByActionArray(sideUp, sideFront, edge1, edge2, edge3);
			}

			if(action.startsWith("S")) 
			{
				return;
			}
		}
		
		solveCrossEdgeByActionArray(sideUp, sideFront, edge1, edge2, edge3);

		return;
	}

	private static HashMap<String, String> locationCodeToActionMap = null;
	private String getActionForLocation(String edgeTypeCode) 
	{
		if(SolveLastSide.locationCodeToActionMap == null)
		{ // initialize once
			SolveLastSide.locationCodeToActionMap = new HashMap<String, String>();
			
			// Move: MF', Reevaluate: R, Success: S
			
			//Top Layer			
			SolveLastSide.locationCodeToActionMap.put("A1", "S");
			
			SolveLastSide.locationCodeToActionMap.put("B1", "MF, MU, MR, MU', MR', MF'");
			SolveLastSide.locationCodeToActionMap.put("B2", "MU2");
			SolveLastSide.locationCodeToActionMap.put("B3", "MU'");
			
			SolveLastSide.locationCodeToActionMap.put("C1", "MF, MR, MU, MR', MU', MF'");
			SolveLastSide.locationCodeToActionMap.put("C2", "MU");
			
			SolveLastSide.locationCodeToActionMap.put("D1", "MF, MR, MU, MR', MU', MF'");
		}
		return SolveLastSide.locationCodeToActionMap.get(edgeTypeCode);
	}	
	
	public void solveLastCorners() throws Exception 
	{
		RubikCubeSide topSide = bottomSide.getOppositeSide();
		
		ArrayList<RubikCubeSide> adjacentSides = topSide.getAdjacentSidesOrdered();		
		RubikCubeCorner corner1 = null;
		RubikCubeCorner corner2 = null;
		RubikCubeCorner corner3 = null;
		RubikCubeCorner corner4 = null;
		
		RubikCubeSide c1 = topSide;
		RubikCubeSide c2 = null;
		RubikCubeSide c3 = null;
		RubikCubeSide c4 = null;
		RubikCubeSide c5 = null;
		
		for(int i = 0; i < adjacentSides.size(); i++)
		{
			antiRecurse = 0;

			c2 = adjacentSides.get(i);
			
			if(i == adjacentSides.size() - 1)
			{
				c3 = adjacentSides.get(0);
				c4 = adjacentSides.get(0);
				c5 = adjacentSides.get(0);
			} else if(i == adjacentSides.size() - 2)
			{
				c3 = adjacentSides.get(i + 1);
				c4 = adjacentSides.get(1);
				c5 = adjacentSides.get(1);
			} else if(i == adjacentSides.size() - 3)
			{
				c3 = adjacentSides.get(i + 1);
				c4 = adjacentSides.get(i + 2);
				c5 = adjacentSides.get(2);
			} else
			{
				c3 = adjacentSides.get(i + 1);
				c4 = adjacentSides.get(i + 2);
				c5 = adjacentSides.get(i + 3);
			}

			corner1 = cube.getCornerPieceByFace(c1, c2, c3);
			corner2 = cube.getCornerPieceByFace(c1, c2, c5);
			corner3 = cube.getCornerPieceByFace(c1, c4, c3);
			corner4 = cube.getCornerPieceByFace(c1, c4, c5);
		}
		solveTopSideCornersByActionArray(c1, c2, corner1, corner2, corner3, corner4);	
		
		try 
		{
			solveLastSideCornerTest(c1, c2, corner1, corner2, corner3, corner4);
			succeded++;
		} catch (Exception e) 
		{
			failed++;
		}

//		System.out.println("***SuccesRateLastSideCorners:" + 100 * 
//				succeded / (succeded + failed) + "***" + succeded + "***");
	}

	private void solveLastSideCornerTest(RubikCubeSide topSide, RubikCubeSide facingYouSide, 
			RubikCubeCorner corner1, RubikCubeCorner corner2, 
			RubikCubeCorner corner3, RubikCubeCorner corner4) 
			throws Exception 
			
	{
		if(corner1.getSideOn(topSide) != topSide &&
			corner2.getSideOn(topSide) != topSide &&
			corner3.getSideOn(topSide) != topSide &&
			corner4.getSideOn(topSide) != topSide)
		{	
			throw new Exception();
		}
	}
	
	public int isCornerFaceOn(RubikCubeSide chosenSide, 
			RubikCubeCorner corner, RubikCubeSide excpectedSide) 
	{
		if(corner.getSideOn(chosenSide) == excpectedSide)
		{
			return 0;
		}
		
		if(corner.getSideOn(chosenSide.getOppositeSide()) == excpectedSide)
		{
			return 1;
		}

		return -1;
	}
	
	private void solveTopSideCornersByActionArray(
			RubikCubeSide topSide, RubikCubeSide facingYouSide, RubikCubeCorner corner1,
			RubikCubeCorner corner2, RubikCubeCorner corner3, RubikCubeCorner corner4) throws Exception 
	{
		if(antiRecurse > 12)
		{
			System.out.println("ANTIRECURSE ABORTED");
			throw new Exception("ANTIRECURSE ABORTED");
		}
		antiRecurse++;

		RubikCubeSide sideUp = topSide;
		RubikCubeSide sideFront = facingYouSide;
		RubikCubeSide sideLeft = cube.calculateLeft(sideUp, sideFront);
		RubikCubeSide sideBack = sideFront.getOppositeSide();
		RubikCubeSide sideRight = sideLeft.getOppositeSide();
				
		String cornersTypeCode = "";
		
		// 2 letter type code
		
		cornersTypeCode +=  addDigitAndGetAllSymmetricCasesFor2(
				sideUp, sideFront, sideBack, sideLeft, sideRight, 
				"A", sideUp, sideUp, sideUp, sideUp);

		cornersTypeCode +=  addDigitAndGetAllSymmetricCasesFor2(
				sideUp, sideFront, sideBack, sideLeft, sideRight, 
				"B", sideFront, sideFront, sideBack, sideBack);
		
		cornersTypeCode +=  addDigitAndGetAllSymmetricCasesFor2(
				sideUp, sideFront, sideBack, sideLeft, sideRight, 
				"C", sideLeft, sideFront, sideBack, sideLeft);

		cornersTypeCode +=  addDigitAndGetAllSymmetricCasesFor2(
				sideUp, sideFront, sideBack, sideLeft, sideRight, 
				"D", sideUp, sideFront, sideRight, sideBack);

		cornersTypeCode +=  addDigitAndGetAllSymmetricCasesFor2(
				sideUp, sideFront, sideBack, sideLeft, sideRight, 
				"E", sideFront, sideUp, sideBack, sideLeft);

		cornersTypeCode +=  addDigitAndGetAllSymmetricCasesFor2(
				sideUp, sideFront, sideBack, sideLeft, sideRight, 
				"F", sideFront, sideUp, sideRight, sideUp);

		cornersTypeCode +=  addDigitAndGetAllSymmetricCasesFor2(
				sideUp, sideFront, sideBack, sideLeft, sideRight, 
				"G", sideFront, sideUp, sideUp, sideBack);

		cornersTypeCode +=  addDigitAndGetAllSymmetricCasesFor2(
				sideUp, sideFront, sideBack, sideLeft, sideRight, 
				"H", sideFront, sideFront, sideUp, sideUp);

		//Solving the cube
		String actionsToDo = getActionForLocationForCorners(cornersTypeCode);		
		
		String[] actionsList = actionsToDo.split(",");

//		System.out.println(cornersTypeCode);
//		System.out.println(new RubikCubePrinter2D().getPiecesAsCross(cube));
		
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
				solveTopSideCornersByActionArray(sideUp, sideFront, corner1, corner2, corner3, corner4);
			}

			if(action.startsWith("S")) 
			{
				return;
			}
		}
		
		solveTopSideCornersByActionArray(sideUp, sideFront, corner1, corner2, corner3, corner4);

		return;
	}

	private String addDigitAndGetAllSymmetricCasesFor2(
			RubikCubeSide sideUp, RubikCubeSide sideFront,
			RubikCubeSide sideBack, RubikCubeSide sideLeft, 
			RubikCubeSide sideRight, 
			String cornerTypeCode, 
			RubikCubeSide expectedSide1, RubikCubeSide expectedSide2, 
			RubikCubeSide expectedSide3, RubikCubeSide expectedSide4) 
	{
		String ret = "";
		
		ret += addDigitAndGetAllSymmetricCasesFor3(
				sideUp, 
				getLeftSide(sideUp, expectedSide1, 0), 
				getLeftSide(sideUp, expectedSide2, 0), 
				getLeftSide(sideUp, expectedSide3, 0), 
				getLeftSide(sideUp, expectedSide4, 0), 
				cornerTypeCode + "1", sideFront, sideRight, sideBack, sideLeft);
		
		ret += addDigitAndGetAllSymmetricCasesFor3(
				sideUp, 				
				getLeftSide(sideUp, expectedSide1, 1), 
				getLeftSide(sideUp, expectedSide2, 1), 
				getLeftSide(sideUp, expectedSide3, 1), 
				getLeftSide(sideUp, expectedSide4, 1), 
				cornerTypeCode + "2", sideLeft, sideFront, sideRight, sideBack);
		
		ret += addDigitAndGetAllSymmetricCasesFor3(
				sideUp, 				
				getLeftSide(sideUp, expectedSide1, 2), 
				getLeftSide(sideUp, expectedSide2, 2), 
				getLeftSide(sideUp, expectedSide3, 2), 
				getLeftSide(sideUp, expectedSide4, 2), 
				cornerTypeCode + "3", sideBack, sideLeft, sideFront, sideRight);
		
		ret += addDigitAndGetAllSymmetricCasesFor3(
				sideUp, 				
				getLeftSide(sideUp, expectedSide1, 3), 
				getLeftSide(sideUp, expectedSide2, 3), 
				getLeftSide(sideUp, expectedSide3, 3), 
				getLeftSide(sideUp, expectedSide4, 3), 
				cornerTypeCode + "2", sideRight, sideBack, sideLeft, sideFront);
		
		if(ret.length()>2) {
			ret=ret.substring(0, 2);
		}
		return ret;
	}
	
	private RubikCubeSide getLeftSide(
			RubikCubeSide sideUp, RubikCubeSide sideFront, int rotationCount) 
	{
		RubikCubeSide ret = sideFront;
		
		if(sideUp != sideFront)
		{
			for(int lop = 0; lop < rotationCount; lop++)
			{
				ret = cube.calculateLeft(sideUp, ret);
			}
		} else
		{
			ret = sideUp;
		}

		return ret;
	}

	private String addDigitAndGetAllSymmetricCasesFor3(
			RubikCubeSide sideUp, 
			RubikCubeSide expectedSide1, RubikCubeSide expectedSide2, 
			RubikCubeSide expectedSide3, RubikCubeSide expectedSide4, String cornerTypeCode, 
			RubikCubeSide sideFront, RubikCubeSide sideRight, 
			RubikCubeSide sideBack, RubikCubeSide sideLeft
			) 
	{
		RubikCubeCorner cornerLocationFL = cube.getCornerPieceByLocation(sideUp, sideFront, sideLeft);
		RubikCubeCorner cornerLocationFR = cube.getCornerPieceByLocation(sideUp, sideFront, sideRight);
		RubikCubeCorner cornerLocationBR = cube.getCornerPieceByLocation(sideUp, sideBack, sideRight);
		RubikCubeCorner cornerLocationBL = cube.getCornerPieceByLocation(sideUp, sideBack, sideLeft);
		
		RubikCubeSide face1 = cornerLocationFL.getSideOn(expectedSide1);
		RubikCubeSide face2 = cornerLocationFR.getSideOn(expectedSide2);
		RubikCubeSide face3 = cornerLocationBR.getSideOn(expectedSide3);
		RubikCubeSide face4 = cornerLocationBL.getSideOn(expectedSide4);
//		
//		if(face1!=null) {
//			System.out.println("face1:" + face1.getTypeCode());
//		}
//		if(face2!=null) {
//			System.out.println("face2:" + face2.getTypeCode());
//		}
//		if(face3!=null) {
//			System.out.println("face3:" + face3.getTypeCode());
//		}
//		if(face4!=null) {
//			System.out.println("face4:" + face4.getTypeCode());
//		}
		
		boolean finalOLLCheck = 
				(face1 == sideUp) && (face2 == sideUp) && (face3 == sideUp) && (face4 == sideUp);

		return finalOLLCheck ? cornerTypeCode : "";
	}
	
	private static HashMap<String, String> locationCodeToActionMapCorners = null;
	private String getActionForLocationForCorners(String cornerTypeCode) 
	{
		if(SolveLastSide.locationCodeToActionMapCorners == null)
		{ // initialize once
			SolveLastSide.locationCodeToActionMapCorners = new HashMap<String, String>();
			
			// Move: MF', Reevaluate: R, Success: S
			
			SolveLastSide.locationCodeToActionMapCorners.put("", "S");
			SolveLastSide.locationCodeToActionMapCorners.put("A1", "S");

			SolveLastSide.locationCodeToActionMapCorners.put("B1", ""
					+ "MF, MR, MU, MR', MU', MR, MU, MR', MU', MR, MU, MR', MU', MF'");
			SolveLastSide.locationCodeToActionMapCorners.put("B2", "MU'");

			SolveLastSide.locationCodeToActionMapCorners.put("C1", ""
					+ "MR, MU2, MR2, MU', MR2, MU', MR2, MU2, MR");
			SolveLastSide.locationCodeToActionMapCorners.put("C3", "MU2");
			SolveLastSide.locationCodeToActionMapCorners.put("C2", "MU'");

			SolveLastSide.locationCodeToActionMapCorners.put("D1", ""
					+ "MR, MU, MR', MU, MR, MU2, MR'");
			SolveLastSide.locationCodeToActionMapCorners.put("D3", "MU2");
			SolveLastSide.locationCodeToActionMapCorners.put("D2", "MU'");

			SolveLastSide.locationCodeToActionMapCorners.put("E1", ""
					+ "ML', MU', ML, MU', ML', MU2, ML");
			SolveLastSide.locationCodeToActionMapCorners.put("E3", "MU2");
			SolveLastSide.locationCodeToActionMapCorners.put("E2", "MU'");

			SolveLastSide.locationCodeToActionMapCorners.put("F1", ""
					+ "MR', MF, MR, MB', MR', MF', MR, MB");
			SolveLastSide.locationCodeToActionMapCorners.put("F3", "MU2");
			SolveLastSide.locationCodeToActionMapCorners.put("F2", "MU'");

			SolveLastSide.locationCodeToActionMapCorners.put("G1", ""
					+ "ML, MF, MR', MF', ML', MF, MR, MF'");
			SolveLastSide.locationCodeToActionMapCorners.put("G3", "MU2");
			SolveLastSide.locationCodeToActionMapCorners.put("G2", "MU'");

			SolveLastSide.locationCodeToActionMapCorners.put("H1", ""
					+ "MR2, MD, MR', MU2, MR, MD', MR', MU2, MR'");
			SolveLastSide.locationCodeToActionMapCorners.put("H3", "MU2");
			SolveLastSide.locationCodeToActionMapCorners.put("H2", "MU'");
		}
		
		return SolveLastSide.locationCodeToActionMapCorners.get(cornerTypeCode);
	}	
}
