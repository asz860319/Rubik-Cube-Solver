package com.asz.rubik.fundamentals;

import java.util.ArrayList;

import com.asz.rubik.RubikCubePrinter2D;

public class RubikCubeEdge extends RubikCubePiece implements Cloneable 
{
	RubikCubeSide locationSide1; 
	RubikCubeSide locationSide2;
	RubikCubePieceFace pieceFace2;

	public RubikCubeEdge(RubikCube cube, RubikCubeSide sideFace1, RubikCubeSide sideFace2) 
	{
		this.cube = cube;
		locationSide1 = sideFace1;
		locationSide2 = sideFace2;
		pieceFace  = new RubikCubePieceFace(sideFace1);
		pieceFace2 = new RubikCubePieceFace(sideFace2);
	}
	
	public RubikCubePieceFace getPieceFace1() {
		return super.getPieceFace();
	}
	
	public RubikCubePieceFace getPieceFace2() {
		return pieceFace2;
	}
	
	public RubikCubeSide getLocation1()
	{
		return locationSide1;
	}

	public RubikCubeSide getLocation2()
	{
		return locationSide2;
	}
	
	@Override
	public RubikCubePieceFace getPieceFaceOn(RubikCubeSide side) 
	{
		RubikCubePieceFace ret;
		if(side == locationSide1)
		{
			ret = getPieceFace1();
		} else if(side == locationSide2) 
		{
			ret = getPieceFace2();
		} else
		{
			ret = null;
		}
		
		return ret;
	}

	public void setLocationWithOneSideRotation2(RubikCubeSide side, RubikCubeSide targetPlace1,
			RubikCubeSide targetPlace2) {
		
		this.locationSide1 = targetPlace1;
		this.locationSide2 = targetPlace2;
	}

	public void setLocationWithOneSideRotation(
			RubikCubeSide rotatingSide,
			RubikCubeSide targetPlace1,
			RubikCubeSide targetPlace2)
	{
		ArrayList<RubikCubeSide> locationSetOrderArray= new ArrayList<RubikCubeSide>();
		locationSetOrderArray.add(null);
		locationSetOrderArray.add(null);

		ArrayList<RubikCubeSide> movingSidesArray= new ArrayList<RubikCubeSide>();
		movingSidesArray.add(locationSide1);
		movingSidesArray.add(locationSide2);

		ArrayList<RubikCubeSide> targetSidesArray= new ArrayList<RubikCubeSide>();
		targetSidesArray.add(targetPlace1);
		targetSidesArray.add(targetPlace2);

		// finding top side
		int rotatingSideMovingIndex = movingSidesArray.indexOf(rotatingSide);
		int rotatingSideTargetIndex = targetSidesArray.indexOf(rotatingSide);

		locationSetOrderArray.set(rotatingSideMovingIndex, targetSidesArray.get(rotatingSideTargetIndex));

		// Seting the otherside
		@SuppressWarnings("unchecked")
		ArrayList<RubikCubeSide> remainingTargetSides=(ArrayList<RubikCubeSide>)targetSidesArray.clone();
		remainingTargetSides.remove(rotatingSideTargetIndex);
		for(int lop=0; lop<locationSetOrderArray.size(); lop++) {
			if(locationSetOrderArray.get(lop)== null) {
				locationSetOrderArray.set(lop, remainingTargetSides.get(0));				
			}
		}
		
		// setting location
		this.setLocation (
				locationSetOrderArray.get(0),
				locationSetOrderArray.get(1)
				);
	}

	private void setLocation(RubikCubeSide targetSide1, RubikCubeSide targetSide2) {
		this.locationSide1=targetSide1;
		this.locationSide2=targetSide2;
	}

	public boolean hasSameSides(RubikCubeSide side1, RubikCubeSide side2) 
	{
		RubikCubeSide expectedSide1 = this.getPieceFace1().getSide();
		RubikCubeSide expectedSide2 = this.getPieceFace2().getSide();
		
		if(expectedSide1 == side1 && expectedSide2 == side2) 
		{
			return true;
		}

		if(expectedSide1 == side2 && expectedSide2 == side1) 
		{
			return true;
		}

		return false;
	}

	public boolean isInCorrectLocation() 
	{
		if(getPieceFace1().getSide() != locationSide1)
		{
			return false;
		}
		if(getPieceFace2().getSide() != locationSide2)
		{
			return false;
		}
		return true;
	}

	public RubikCubeSide getSideOn(RubikCubeSide side)
	{
		RubikCubePieceFace pieceFace = getPieceFaceOn(side);
		if(pieceFace == null)
		{
			return null;
		}
		return pieceFace.getSide();
	}

	public String introduce() 
	{
		String ret = "";
		
		RubikCubePrinter2D printer = new RubikCubePrinter2D();
		
		ret += "Edge Piece: { " + printer.convertTypeColor(getPieceFace1().getTypeCode()); 
		ret += " and " + printer.convertTypeColor(getPieceFace2().getTypeCode());
		ret += " } ";
		
		ret += "Located between: { " + printer.convertTypeColor(getLocation1().getTypeCode());
		ret += " and " + printer.convertTypeColor(getLocation2().getTypeCode());
		ret += " }";
		
		return ret;
	}

	public void goToDefaultPos() 
	{
		locationSide1 = pieceFace.getSide();
		locationSide2 = pieceFace2.getSide();
	}
}