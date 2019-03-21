package com.asz.rubik;

import java.util.ArrayList;

import com.asz.rubik.fundamentals.RubikCube;
import com.asz.rubik.fundamentals.RubikCubeSide;

public class RubikCubeHistory
{
	private RubikCube cube;

	private int historyIndex = -1;
	ArrayList<RubikCubeHistoryItem> historyItems = new ArrayList<RubikCubeHistoryItem>();
	
	public RubikCubeHistory(RubikCube cube)
	{
		this.cube = cube;
	}
	
	public void redoMoves(int moveCount) throws Exception
	{
		if(moveCount + historyIndex > historyItems.size())
		{
			throw new Exception("'CAN'T REDO SOMETHING I HAVEN'T DONE' - RUBIK CUBE HISTORY");
		}
		
		for(int lop = historyIndex + 1; 
				lop < historyIndex + moveCount + 1; lop++)
		{
			RubikCubeHistoryItem historyItem = historyItems.get(lop);
			
			if(historyItem.getMoveTypeCount() > 0)
			{
				cube.moveClockwise(historyItem.getSide(), Math.abs(historyItem.getMoveTypeCount()), false);
			} else {
				cube.moveCounterClockwise(historyItem.getSide(), Math.abs(historyItem.getMoveTypeCount()), false);
			}
		}
		
		historyIndex += moveCount;
	}
	
	public void undoLastMoves(int pastMoveCount) throws Exception {
		for(int lop = historyIndex; 
				lop > historyIndex - pastMoveCount; lop--)
		{
			RubikCubeHistoryItem historyItem = historyItems.get(lop);
			
			if(historyItem.getMoveTypeCount() > 0)
			{
				cube.moveCounterClockwise(historyItem.getSide(),   historyItem.getMoveTypeCount(), false);
			} else {
				cube.moveClockwise(historyItem.getSide(), Math.abs(historyItem.getMoveTypeCount()), false);
			}
		}
		
		historyIndex -= pastMoveCount;
	}

	public void addToHistory(RubikCubeSide side, int moveTypeCount) 
	{
		if(historyIndex < historyItems.size())
		{
			for(int lop = historyItems.size() - 1; lop > historyIndex; lop--)
			{
				historyItems.remove(lop);
			}
		}
		historyItems.add(new RubikCubeHistoryItem(side, moveTypeCount));
		historyIndex++;
	}
}
