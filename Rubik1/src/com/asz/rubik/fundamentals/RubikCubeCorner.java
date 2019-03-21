package com.asz.rubik.fundamentals;

import java.util.ArrayList;

import com.asz.rubik.RubikCubePrinter2D;

public class RubikCubeCorner extends RubikCubePiece 
{
	RubikCubeSide locationSide1; 
	RubikCubeSide locationSide2;
	RubikCubeSide locationSide3;
	
	RubikCubePieceFace pieceFace2;
	RubikCubePieceFace pieceFace3;

	public RubikCubeCorner(RubikCube cube, 
			RubikCubeSide sideFace1, 
			RubikCubeSide sideFace2, 
			RubikCubeSide sideFace3) 
	{
		this.cube = cube;
		this.locationSide1 = sideFace1;
		this.locationSide2 = sideFace2;
		this.locationSide3 = sideFace3;
		pieceFace = new RubikCubePieceFace(sideFace1);
		pieceFace2 = new RubikCubePieceFace(sideFace2);
		pieceFace3 = new RubikCubePieceFace(sideFace3);
	}

	private RubikCubePieceFace getPieceFace1() {
		return super.getPieceFace();
	}
	
	public RubikCubePieceFace getPieceFace2() {
		return pieceFace2;
	}
	
	public RubikCubePieceFace getPieceFace3() {
		return pieceFace3;
	}
	
	public RubikCubeSide getLocation1()
	{
		return locationSide1;
	}

	public RubikCubeSide getLocation2()
	{
		return locationSide2;
	}
	
	public RubikCubeSide getLocation3()
	{
		return locationSide3;
	}
	
	public void setLocation(RubikCubeSide locationSide1, RubikCubeSide locationSide2, RubikCubeSide locationSide3) 
	{
		this.locationSide1 = locationSide1;
		this.locationSide2 = locationSide2;
		this.locationSide3 = locationSide3;
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
		} else if(side == locationSide3)
		{
			ret = getPieceFace3();
		} else {
			ret = null;
		}
		
		return ret;
	}

	public boolean doesLocationMatch(RubikCubeSide locationA, RubikCubeSide locationB, RubikCubeSide locationC) 
	{
		if(!isLocatedOnSameSide(locationA))
		{
			return false;
		}
		
		if(!isLocatedOnSameSide(locationB))
		{
			return false;
		}
		
		if(!isLocatedOnSameSide(locationC))
		{
			return false;
		}

		return true;
	}

	public boolean hasSameSides(RubikCubeSide sideA, RubikCubeSide sideB, RubikCubeSide sideC) 
	{
		if(!hasSameFace(sideA))
		{
			return false;
		}
		
		if(!hasSameFace(sideB))
		{
			return false;
		}
		
		if(!hasSameFace(sideC))
		{
			return false;
		}

		return true;
	}
	
	private boolean hasSameFace(RubikCubeSide sideToCheck) 
	{
		if(sideToCheck == getPieceFace1().getSide())
		{
			return true;
		}
	
		if(sideToCheck == getPieceFace2().getSide())
		{
			return true;
		}

		if(sideToCheck == getPieceFace3().getSide())
		{
			return true;
		}
		
		return false;
	}

	private boolean isLocatedOnSameSide(RubikCubeSide sideToCheck) 
	{
		if(sideToCheck == locationSide1)
		{
			return true;
		}
	
		if(sideToCheck == locationSide2)
		{
			return true;
		}

		if(sideToCheck == locationSide3)
		{
			return true;
		}
		
		return false;
	}

	public void setLocationWithOneSideRotation(
			RubikCubeSide rotatingSide,
			RubikCubeSide targetPlace1,
			RubikCubeSide targetPlace2,
			RubikCubeSide targetPlace3)
	{
		ArrayList<RubikCubeSide> locationSetOrderArray= new ArrayList<RubikCubeSide>();
		locationSetOrderArray.add(null);
		locationSetOrderArray.add(null);
		locationSetOrderArray.add(null);

		ArrayList<RubikCubeSide> movingSidesArray= new ArrayList<RubikCubeSide>();
		movingSidesArray.add(locationSide1);
		movingSidesArray.add(locationSide2);
		movingSidesArray.add(locationSide3);

		ArrayList<RubikCubeSide> targetSidesArray= new ArrayList<RubikCubeSide>();
		targetSidesArray.add(targetPlace1);
		targetSidesArray.add(targetPlace2);
		targetSidesArray.add(targetPlace3);

		// finding top side
		int rotatingSideMovingIndex = movingSidesArray.indexOf(rotatingSide);
		int rotatingSideTargetIndex = targetSidesArray.indexOf(rotatingSide);

		locationSetOrderArray.set(rotatingSideMovingIndex, targetSidesArray.get(rotatingSideTargetIndex));

		// finding left side
		@SuppressWarnings("unchecked")
		ArrayList<RubikCubeSide> remainingMovingSides=(ArrayList<RubikCubeSide>)movingSidesArray.clone();
		remainingMovingSides.remove(rotatingSideMovingIndex);

		@SuppressWarnings("unchecked")
		ArrayList<RubikCubeSide> remainingTargetSides=(ArrayList<RubikCubeSide>)targetSidesArray.clone();
		remainingTargetSides.remove(rotatingSideTargetIndex);

		RubikCubeSide leftMovingSide=findLeftSide(rotatingSide,   remainingMovingSides.get(0), remainingMovingSides.get(1));
		RubikCubeSide leftTargetSide=findLeftSide(rotatingSide,   remainingTargetSides.get(0), remainingTargetSides.get(1));

		int leftSideMovingIndex = movingSidesArray.indexOf(leftMovingSide);
		int leftSideTargetIndex = targetSidesArray.indexOf(leftTargetSide);

		locationSetOrderArray.set(leftSideMovingIndex,     targetSidesArray.get(leftSideTargetIndex));

		// Seting the right side
		remainingTargetSides.remove(leftTargetSide);
		for(int lop=0; lop<locationSetOrderArray.size(); lop++) {
			if(locationSetOrderArray.get(lop)== null) {
				locationSetOrderArray.set(lop, remainingTargetSides.get(0));				
			}
		}
		
		// setting location
		this.setLocation (
				locationSetOrderArray.get(0),
				locationSetOrderArray.get(1),
				locationSetOrderArray.get(2)
				);
	}

	private RubikCubeSide findLeftSide(
			RubikCubeSide upside, 
			RubikCubeSide candidSide1,
			RubikCubeSide candidSide2) {

		RubikCubeSide ret=null;
		
		if(cube.calculateLeft(upside, candidSide2) == candidSide1) {
			ret=candidSide1;
		}

		if(cube.calculateLeft(upside, candidSide1) == candidSide2) {
			ret=candidSide2;
		}
		
		return ret;
	}

	public RubikCubeSide getSideOn(RubikCubeSide side) 
	{
		RubikCubePieceFace pieceFaceOn = getPieceFaceOn(side);
		if(pieceFaceOn == null)
		{
			return null;
		}
		return pieceFaceOn.getSide();
	}

	public String introduceYourSelf() 
	{
		String ret = "";
		
		RubikCubePrinter2D printer = new RubikCubePrinter2D();
		
		ret += "Corner Piece: { " + printer.convertTypeColor(getPieceFace1().getTypeCode()); 
		ret += ", " + printer.convertTypeColor(getPieceFace2().getTypeCode());
		ret += " and " + printer.convertTypeColor(getPieceFace3().getTypeCode());
		ret += " } ";
		
		ret += "Located between: { " + printer.convertTypeColor(getLocation1().getTypeCode());
		ret += " , " + printer.convertTypeColor(getLocation2().getTypeCode());
		ret += " and " + printer.convertTypeColor(getLocation3().getTypeCode());
		ret += " }";
		
		return ret;		
	}
	
	public boolean isInCorrectLocation() {
		if(getPieceFace1().getSide() != locationSide1)
		{
			return false;
		}
		if(getPieceFace2().getSide() != locationSide2)
		{
			return false;
		}
		
		if(getPieceFace3().getSide() != locationSide3)
		{
			return false;
		}
		return true;
	}

	public void goToDefaultPos() 
	{
		locationSide1 = pieceFace.getSide();
		locationSide2 = pieceFace2.getSide();		
		locationSide3 = pieceFace3.getSide();
	}
}
