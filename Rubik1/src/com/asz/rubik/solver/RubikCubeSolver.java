package com.asz.rubik.solver;

import java.util.ArrayList;
import java.util.Random;

import com.asz.rubik.fundamentals.RubikCube;
import com.asz.rubik.fundamentals.RubikCubeEdge;
import com.asz.rubik.fundamentals.RubikCubeSide;

public class RubikCubeSolver {

	private RubikCube cube;
	private RubikCubeSide firstSide;
	private boolean solving;
	private int antiRecurse;
	
	public RubikCubeSolver(RubikCube cube) 
	{
		this.cube = cube;
		solving = false;
		antiRecurse = 0;
	}

	public boolean isCubeSolving()
	{
		return solving;
	}
	
	public void solve() throws Exception 
	{
		solving = true;
		firstSide = selectBestFirstSide();
		solveFirstSide();
		antiRecurse = 0;
		solveF2L();
		antiRecurse = 0;
		solveShallowLastSide();
		antiRecurse = 0;
		solveLastSideOrientation();
		antiRecurse = 0;
		solving = false;
	}

	public RubikCubeSide selectBestFirstSide()
	{
		ArrayList<RubikCubeSide> sides = cube.getSides();
		
		int points = 0;
		int oldPoints = 0;
		
		RubikCubeSide ret = cube.getSide(new Random().nextInt(6));
		
		for(int lop = 0; lop < sides.size(); lop++)
		{
			points = 0;
			RubikCubeSide side = sides.get(lop);
			
			ArrayList<RubikCubeEdge> edges = new ArrayList<>();
			
			for(int i = 0; i < side.getAdjacentSidesOrdered().size(); i++)
			{
				RubikCubeSide side2=side.getAdjacentSidesOrdered().get(i);
				edges.add(cube.getEdgePieceByFace(side, side2));
			}
			
			for(int i = 0; i < edges.size(); i++) 
			{
				RubikCubeEdge edge = edges.get(i);
				if(edge.getSideOn(edge.getPieceFace1().getSide()) == side)
				{
					points++;
				}
				if(edge.getSideOn(edge.getPieceFace2().getSide()) == side)
				{
					points++;
				}
			}
			
			if(points > oldPoints)
			{
				ret = side;
			}
			oldPoints = points;
		}
		
		return ret;
	}
	
	private void solveLastSideOrientation() throws Exception 
	{
		new SolveLastSideOrientation(cube, firstSide).solveCornerOrientation();
		new SolveLastSideOrientation(cube, firstSide).moveCornersToCorrectSpot();
		
		new SolveLastSideOrientation(cube, firstSide).solveEdgeOrientation();
	}
	
	private void solveShallowLastSide() throws Exception 
	{
		new SolveLastSide(cube, firstSide).solveLastCross();
		new SolveLastSide(cube, firstSide).solveLastCorners();
	}
	
	private void solveF2L() throws Exception 
	{
		new SolveF2L(cube, firstSide).solveF2L();
	}

	private void solveFirstSide() throws Exception 
	{
		new SolveFirstSide(cube, firstSide).solveCross();
		new SolveFirstSide(cube, firstSide).solveCorners();
	}
}