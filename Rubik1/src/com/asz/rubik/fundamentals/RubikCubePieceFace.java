package com.asz.rubik.fundamentals;

public class RubikCubePieceFace 
{
	private RubikCubeSide side;

	public RubikCubePieceFace(RubikCubeSide side) 
	{
		this.side = side;
	}
	
	public int getTypeCode() {
		return side.getTypeCode();
	}

	public RubikCubeSide getSide() {
		return this.side;
	}

	public void setTypeCode(int typeCode) 
	{
		side = side.getCube().getSide(typeCode);
	}
}
