package com.asz.rubik.fundamentals;

public abstract class RubikCubePiece 
{
	RubikCube cube;
	protected RubikCubePieceFace pieceFace;
	
	public RubikCube getCube() 
	{
		return cube;
	}

	protected RubikCubePieceFace getPieceFace() 
	{
		return pieceFace;
	}

	public abstract RubikCubePieceFace getPieceFaceOn(RubikCubeSide side);
}
