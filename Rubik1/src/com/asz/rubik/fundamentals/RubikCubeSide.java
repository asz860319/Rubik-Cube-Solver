package com.asz.rubik.fundamentals;

import java.util.ArrayList;
import java.util.Collections;

public class RubikCubeSide extends RubikCubePiece implements Cloneable
{
	private int typeCode;
	
	public RubikCubeSide(RubikCube cube, int typeCode) 
	{
		this.cube = cube;
		this.typeCode = typeCode;
		pieceFace = new RubikCubePieceFace(this);
	}
	
	public RubikCubeSide getOppositeSide()
	{
		return cube.getSide(5 - typeCode);
	}
	
	private ArrayList<RubikCubeSide> getAdjacentSides() {
		
		ArrayList<RubikCubeSide> ret = new ArrayList<RubikCubeSide>();
		
		for(int lop = 0; lop < cube.getSides().size(); lop++)
		{
			if(cube.getSides().get(lop).getTypeCode() != typeCode && 
					cube.getSides().get(lop) != 
						cube.getSide(typeCode).getOppositeSide())
			{
				ret.add(cube.getSides().get(lop));
			}
		}	
		
		return ret;
	}

	public RubikCubePieceFace getPieceFace(int location) 
	{
		return getPieceAtLocation(location).getPieceFaceOn(this);
	}
	
	private RubikCubePieceFace getPieceFace(int locationCode, RubikCubeSide sideNorth) 
	{
		RubikCubePiece ret;

		switch (locationCode) {
		case 0:
			ret = getPieceAtLocation(0, sideNorth);
			break;
		case 1:
			ret = getPieceAtLocation(1, sideNorth);
			break;
		case 2:
			ret = getPieceAtLocation(2, sideNorth);
			break;
		default:
			ret = null;
			break;
		}
		
		return ret.getPieceFaceOn(this);
	}
	
	private RubikCubePiece getPieceAtLocation(int locationCode, RubikCubeSide sideNorth) 
	{
		RubikCubePiece ret;
		
		RubikCubeSide sideWest = cube.calculateLeft(sideNorth, this);
		RubikCubeSide sideEast = sideWest.getOppositeSide();

		switch(locationCode)
		{
		case 0:
			ret=cube.getCornerPieceByLocation(this, sideNorth, sideWest);
			break;
		case 1:
			ret=cube.getEdgePieceByLocation(this, sideNorth);
			break;
		case 2:
			ret=cube.getCornerPieceByLocation(this, sideNorth, sideEast);
			break;
		default:
			ret = null;
			break;
		}
		return ret;
	}

	private RubikCubePiece getPieceAtLocation(int locationCode) 
	{
		RubikCubePiece ret;
		
		switch(locationCode)
		{
		case 0:
			ret=cube.getCornerPieceByLocation(this, getNorthSide(), getWestSide());
			break;
		case 1:
			ret=cube.getEdgePieceByLocation(this, getNorthSide());
			break;
		case 2:
			ret=cube.getCornerPieceByLocation(this, getNorthSide(), getEastSide());
			break;
		case 3:
			ret=cube.getEdgePieceByLocation(this, getWestSide());
			break;
		case 4:
			ret=this;
			break;
		case 5:
			ret=cube.getEdgePieceByLocation(this, getEastSide());
			break;
		case 6:
			ret=cube.getCornerPieceByLocation(this, getSouthSide(), getWestSide());
			break;
		case 7:
			ret=cube.getEdgePieceByLocation(this, getSouthSide());
			break;
		case 8:
			ret=cube.getCornerPieceByLocation(this, getSouthSide(), getEastSide());
			break;
		default:
			ret=null;
		}

		return ret;
	}

	public int getTypeCode() {
		return typeCode;
	}
	
	public ArrayList<RubikCubeSide> getAdjacentSidesOrdered() 
	{
		ArrayList<RubikCubeSide> ret = new ArrayList<RubikCubeSide>();
		
		RubikCubeSide s0 = this.getAdjacentSides().get(0);
		RubikCubeSide s3 = cube.calculateLeft(this, s0);
		
		RubikCubeSide s1 = s3.getOppositeSide();
		RubikCubeSide s2 = s0.getOppositeSide();
		
		ret.add(s0);
		ret.add(s1);
		ret.add(s2);
		ret.add(s3);

		return ret;
	}
	
	public ArrayList<RubikCubeSide> getAdjacentSidesOrderedReverse() 
	{
		ArrayList<RubikCubeSide> ret = getAdjacentSidesOrdered();
		Collections.reverse(ret);
		return ret;
	}
	
	public int getCompassFor(RubikCubeSide sideDirectionUnkown)
	{
		int ret = -1;
		
		//Found north of sideCodeMain from list,
		RubikCubeSide north = this.getNorthSide();
		//Found south of sideCodeMain,
		RubikCubeSide south = north.getOppositeSide();
		//Found east of sideCodeMain,
		RubikCubeSide east = cube.calculateLeft(this, north);
		//Found west of SideCodeMain.
		RubikCubeSide west = east.getOppositeSide();
		//Find which of these 4 are sideCodeDirectionUnkown
				
		if		 (sideDirectionUnkown == north)
		{
			ret = 1;
		} else if(sideDirectionUnkown == west)
		{
			ret = 3;
		} else if(sideDirectionUnkown == south)
		{
			ret = 7;
		} else if(sideDirectionUnkown == east)
		{
			ret = 5;
		} else {
			System.out.println("ERROR");
		}
		
		return ret;
	}

	public RubikCubeEdge getEdgePieceByLocation(Integer locationCode) 
	{
		RubikCubeSide otherSide = getSideAtEdgeLocation(locationCode);
		
		return cube.getEdgePieceByLocation(this, otherSide);
	}

	public RubikCubeCorner getCornerPieceByLocation(Integer locationCode) 
	{
		ArrayList<RubikCubeSide> otherSides = getSidesAtCornerLocation(locationCode);

		return cube.getCornerPieceByLocation(this, otherSides.get(0), otherSides.get(1));
	}
		
	private ArrayList<RubikCubeSide> getSidesAtCornerLocation(Integer locationCode) {
		ArrayList<RubikCubeSide> ret=new ArrayList<RubikCubeSide>();
		
		switch(locationCode)
		{
		case 0:
			ret.add(getNorthSide());
			ret.add(getWestSide());
			break;
		case 2:
			ret.add(getNorthSide());
			ret.add(getEastSide());
			break;
		case 6:
			ret.add(getSouthSide());
			ret.add(getWestSide());
			break;
		case 8:
			ret.add(getSouthSide());
			ret.add(getEastSide());
			break;
		default:
			ret = null;
		}

		return ret;
	}

	private RubikCubeSide getSideAtEdgeLocation(Integer locationCode) {
		RubikCubeSide ret;
		
		switch(locationCode)
		{
		case 1:
			ret = getNorthSide();
			break;
		case 3:
			ret = getWestSide();
			break;
		case 5:
			ret = getEastSide();
			break;
		case 7:
			ret = getSouthSide();
			break;
		default:
			ret = null;
		}
		
		return ret;
	}
	
	private RubikCubeSide getSouthSide() {
		return getNorthSide().getOppositeSide();
	}

	private RubikCubeSide getEastSide() {
		return getWestSide().getOppositeSide();
	}

	private RubikCubeSide getWestSide() {
		return cube.calculateLeft(getNorthSide(), this);
	}

	private RubikCubeSide getNorthSide() {
		int northSideCode;		
		
		switch(typeCode) {
		case 0:
			northSideCode = 2;
			break;
		case 1:
			northSideCode = 3;
			break;
		case 2:
			northSideCode = 4;
			break;
		case 3:
			northSideCode = 5;
			break;
		case 4:
			northSideCode = 0;
			break;
		case 5:
			northSideCode = 1;
			break;
		default:
			northSideCode = -1;
		}		
		
		return cube.getSide(northSideCode);
	}

	@Override
	public RubikCubePieceFace getPieceFaceOn(RubikCubeSide side) {
		return this.getPieceFace();
	}

	public void moveClockwise() 
	{
		try {
			cube.moveClockwise(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isShallowSorted() 
	{		
		for(int lop = 0; lop < 9; lop++)
		{
			if(getPieceFace(lop).getSide() != this)
			{
				return false;
			}
		}
		
		
		return true;
	}

	public boolean isTopRowSolved(RubikCubeSide sideNorth) 
	{
		if(getPieceFace(0, sideNorth).getSide() != getPieceFace(1, sideNorth).getSide())
		{
			return false;
		}
		
		if(getPieceFace(0, sideNorth).getSide() != getPieceFace(2, sideNorth).getSide())
		{
			return false;
		}
		
		return true;
	}

	public void setTypeCode(int typeCode) 
	{
		this.typeCode = typeCode;
	}
	
	public RubikCubeSide clone(RubikCube cube) throws CloneNotSupportedException 
	{
		RubikCubeSide ret = (RubikCubeSide) super.clone();
		ret.cube = cube;
		ret.pieceFace = new RubikCubePieceFace(ret);
		return ret;
	}
}
