package com.asz.rubik;

import java.util.Random;

import com.asz.rubik.fundamentals.RubikCube;
import com.asz.rubik.fundamentals.RubikCubeSide;

public class RubikCubeShuffler {

	private RubikCube cube;
	
	public RubikCubeShuffler(RubikCube cube) {
		this.cube = cube;
	}

	public void scramble(int movesCount) throws Exception 
	{
		Random rand = new Random();
		for(int lop = 0; lop < movesCount; lop++)
		{
			int randMoveAmount = 1 + rand.nextInt(7);
			boolean clockwise = rand.nextBoolean();
			RubikCubeSide randSide = cube.getSide(rand.nextInt(6));
			
			if(clockwise)
			{
				cube.moveClockwise(randSide, randMoveAmount);
			} else 
			{
				cube.moveCounterClockwise(randSide, randMoveAmount);
			}
		}
	}
}
