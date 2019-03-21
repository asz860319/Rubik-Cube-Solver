package com.asz.rubik;

import java.util.ArrayList;

import com.asz.rubik.fundamentals.RubikCube;
import com.asz.rubik.fundamentals.RubikCubeSide;

public class RubikCubePrinter2D {

	private ArrayList<String> colorCodes;
	
	public RubikCubePrinter2D() {
		this("G", "W", "R", "O", "Y", "B");
	}
	
	public RubikCubePrinter2D(
			String code0, 
			String code1, 
			String code2, 
			String code3,
			String code4,
			String code5
			) 
	{
		colorCodes = new ArrayList<String>();
		
		colorCodes.add(code0);
		colorCodes.add(code1);
		colorCodes.add(code2);
		colorCodes.add(code3);
		colorCodes.add(code4);
		colorCodes.add(code5);

	}
	
	public String getPieceFacesAsCross(RubikCube myCube, RubikCubeSide side_up,  RubikCubeSide side_front) 
			throws Exception
	{
		if(side_up.getOppositeSide() == side_front)
		{
			throw new Exception("IMPOSSIBLE SIDES GIVEN");
		}
		
		RubikCubeSide side_back = side_front.getOppositeSide();
		RubikCubeSide side_bottom = side_up.getOppositeSide();
		RubikCubeSide side_left = myCube.calculateLeft(side_up, side_front);
		
		RubikCubeSide side_right = side_left.getOppositeSide();
		
		return getPieceFacesAsCross(myCube, side_up, side_left, side_front, side_right, 
				side_back, side_bottom); 
	}
	
	public String getPieceFacesAsCross(RubikCube myCube, RubikCubeSide side_up, RubikCubeSide side_left, RubikCubeSide side_front, 
			RubikCubeSide side_right, RubikCubeSide side_back, RubikCubeSide side_bottom) 
	{
		String ret = "";
		String box0 = getEmptyBox();
		box0 = addToTheRight(box0, getSideVericalSeparator());
		box0 = addToTheRight(box0, getSidePrinted(myCube, side_up, 1));
		
		box0+="\n    ---";
		
		String box1 = getSidePrinted(myCube, side_left, 3);
		box1 = addToTheRight(box1, getSideVericalSeparator());
		box1 = addToTheRight(box1, getSidePrinted(myCube, side_front, 7));
		
		box1 = addToTheRight(box1, getSideVericalSeparator());
		box1 = addToTheRight(box1, getSidePrinted(myCube, side_right, 1));
		
		box1 = addToTheRight(box1, getSideVericalSeparator());
		box1 = addToTheRight(box1, getSidePrinted(myCube, side_back, 5));
		
		box1+="\n    ---";
		
		String box2 = getEmptyBox();	
		
		box2 = addToTheRight(box2, getSideVericalSeparator());
		box2 = addToTheRight(box2, getSidePrinted(myCube, side_bottom, 5));
	
		ret = addUnderneath(ret, box0);
		ret = addUnderneath(ret, box1);
		ret = addUnderneath(ret, box2);
		
		return ret;
	}
	
	private String getSideVericalSeparator() {
		return getSideVericalSeparator(" ");
	}
	private String getSideVericalSeparator(String sideSeparatoer) {
		String ret = sideSeparatoer;
		
		ret += "\n";
		ret += sideSeparatoer;
		ret += "\n";
		ret += sideSeparatoer;
		
		return ret;
	}

	private String addUnderneath(String boxA, String boxB) {
		String ret = boxA;
		ret += boxB;
		ret += "\n";
		return ret;
	}

	private String addToTheRight(String boxA, String boxB) 
	{
		String[] boxASplited = boxA.split("\n");
		String[] boxBSplited = boxB.split("\n");
			
		String ret = "";
		
		for(int lop = 0; lop < 3; lop++)
		{
			ret += boxASplited[lop] + boxBSplited[lop];
			if(lop < 2)
			{
				ret += "\n";
			}
		}
		return ret;
	}

	private String getSidePrintedUsingPieces(RubikCube cube, RubikCubeSide side, int printDirection) {
		String ret = "";
		
		Integer[] printOrder;
		
		switch(printDirection)
		{
		case 1: //North
			printOrder = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
			break;
		case 5: //East
			printOrder = new Integer[] {2, 5, 8, 1, 4, 7, 0, 3, 6};
			break;
		case 7: //South
			printOrder = new Integer[] {8, 7, 6, 5, 4, 3, 2, 1, 0};
			break;
		case 3: //West
			printOrder = new Integer[] {6, 3, 0, 7, 4, 1, 8, 5, 2};
			break;
		default:
			return "Error in RubikCubePrinter2D, getSidePrintedUsingPieces()";
		}
		
		ret +=  convertTypeColor(side.getCornerPieceByLocation(printOrder[0]).getPieceFaceOn(side).getTypeCode()) + 
				convertTypeColor(side.getEdgePieceByLocation(printOrder[  1]).getPieceFaceOn(side).getTypeCode()) + 
				convertTypeColor(side.getCornerPieceByLocation(printOrder[2]).getPieceFaceOn(side).getTypeCode());
		ret += "\n";
		ret +=  convertTypeColor(side.getEdgePieceByLocation(printOrder[  3]).getPieceFaceOn(side).getTypeCode()) + 
				convertTypeColor(side.getPieceFace(printOrder[			  4]).getTypeCode()) +
				convertTypeColor(side.getEdgePieceByLocation(printOrder[  5]).getPieceFaceOn(side).getTypeCode());
		ret += "\n";
		ret +=  convertTypeColor(side.getCornerPieceByLocation(printOrder[6]).getPieceFaceOn(side).getTypeCode()) + 
				convertTypeColor(side.getEdgePieceByLocation(printOrder[  7]).getPieceFaceOn(side).getTypeCode()) + 
				convertTypeColor(side.getCornerPieceByLocation(printOrder[8]).getPieceFaceOn(side).getTypeCode());
		return ret;
	}

	private String getSidePrinted(RubikCube cube, RubikCubeSide side, int printDirection) {
		String ret = "";
		
		Integer[] printOrder;
		
		switch(printDirection)
		{
		case 1: //North
			printOrder = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
			break;
		case 5: //East
			printOrder = new Integer[] {2, 5, 8, 1, 4, 7, 0, 3, 6};
			break;
		case 7: //South
			printOrder = new Integer[] {8, 7, 6, 5, 4, 3, 2, 1, 0};
			break;
		case 3: //West
			printOrder = new Integer[] {6, 3, 0, 7, 4, 1, 8, 5, 2};
			break;
		default:
			return "Error in RubikCubePrinter2D, getSidePrinted()";
		}
		
		ret +=  convertTypeColor(side.getPieceFace(printOrder[0]).getTypeCode()) + 
				convertTypeColor(side.getPieceFace(printOrder[1]).getTypeCode()) + 
				convertTypeColor(side.getPieceFace(printOrder[2]).getTypeCode());
		ret += "\n";
		ret +=  convertTypeColor(side.getPieceFace(printOrder[3]).getTypeCode()) + 
				convertTypeColor(side.getPieceFace(printOrder[4]).getTypeCode()) +
//				sideCode + 
				convertTypeColor(side.getPieceFace(printOrder[5]).getTypeCode());
		ret += "\n";
		ret +=  convertTypeColor(side.getPieceFace(printOrder[6]).getTypeCode()) + 
				convertTypeColor(side.getPieceFace(printOrder[7]).getTypeCode()) + 
				convertTypeColor(side.getPieceFace(printOrder[8]).getTypeCode());
		return ret;
	}

	
	public String convertTypeColor(int sideCode) 
	{		
		return colorCodes.get(sideCode);
	}

	private String getEmptyBox() {
		String ret = "";
		
		ret += "   ";
		ret += "\n";
		ret += "   ";
		ret += "\n";
		ret += "   ";
		
		return ret;
	}

	public String getPieceFacesAsCross(RubikCube cube) throws Exception {
		return getPieceFacesAsCross(cube, cube.getSide(2), cube.getSide(1));
	}

	public String getPiecesAsCross(RubikCube myCube, 
			RubikCubeSide side_up, RubikCubeSide side_left, RubikCubeSide side_front, 
			RubikCubeSide side_right, RubikCubeSide side_back, RubikCubeSide side_bottom) 
	{
		String ret = "";
		String box0 = getEmptyBox();
		box0 = addToTheRight(box0, getSideVericalSeparator());
		box0 = addToTheRight(box0, getSidePrintedUsingPieces(myCube, side_up, 1));
		
		box0+="\n    ---";
		
		String box1 = getSidePrintedUsingPieces(myCube, side_left, 3);
		box1 = addToTheRight(box1, getSideVericalSeparator());
		box1 = addToTheRight(box1, getSidePrintedUsingPieces(myCube, side_front, 7));
		
		box1 = addToTheRight(box1, getSideVericalSeparator());
		box1 = addToTheRight(box1, getSidePrintedUsingPieces(myCube, side_right, 1));
		
		box1 = addToTheRight(box1, getSideVericalSeparator());
		box1 = addToTheRight(box1, getSidePrintedUsingPieces(myCube, side_back, 5));
		
		box1+="\n    ---";
		
		String box2 = getEmptyBox();	
		
		box2 = addToTheRight(box2, getSideVericalSeparator());
		box2 = addToTheRight(box2, getSidePrintedUsingPieces(myCube, side_bottom, 5));
	
		ret = addUnderneath(ret, box0);
		ret = addUnderneath(ret, box1);
		ret = addUnderneath(ret, box2);
		
		return ret;
	}

	public String getPiecesAsCross(RubikCube myCube, RubikCubeSide side_up,  RubikCubeSide side_front) 
			throws Exception
	{
		if(side_up.getOppositeSide() == side_front)
		{
			throw new Exception("IMPOSSIBLE SIDES GIVEN");
		}
		
		RubikCubeSide side_back = side_front.getOppositeSide();
		RubikCubeSide side_bottom = side_up.getOppositeSide();
		RubikCubeSide side_left = myCube.calculateLeft(side_up, side_front);
		
		RubikCubeSide side_right = side_left.getOppositeSide();
		
		return getPiecesAsCross(myCube, side_up, side_left, side_front, side_right, 
				side_back, side_bottom); 
	}

	public String getPiecesAsCross(RubikCube cube) throws Exception {
		return getPiecesAsCross(cube, cube.getSide(2), cube.getSide(1));
	}
	
}
