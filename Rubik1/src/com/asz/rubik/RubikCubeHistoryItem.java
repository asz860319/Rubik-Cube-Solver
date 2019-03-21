package com.asz.rubik;

import com.asz.rubik.fundamentals.RubikCubeSide;

public class RubikCubeHistoryItem {

	private RubikCubeSide side;
	private int moveTypeCount;

	public RubikCubeHistoryItem(RubikCubeSide side, int moveTypeCount) {
		this.side = side;
		this.moveTypeCount = moveTypeCount;
	}

	public RubikCubeSide getSide() {
		return side;
	}

	public int getMoveTypeCount() {
		return moveTypeCount;
	}	
}
