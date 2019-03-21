package com.asz.rubik.fundamentals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.event.EventListenerList;

import com.asz.rubik.RubikCubeHistory;
import com.asz.rubik.solver.RubikCubeSolver;
import com.asz.rubik.visualizer.RubikCubeVisualizerSide;

public class RubikCube implements Cloneable
{
	private static HashMap<Integer, Integer> staticSidesOrderTable;
	private ArrayList<RubikCubeSide> sides;
	private ArrayList<RubikCubeEdge> edges;
	private ArrayList<RubikCubeCorner> corners;
	private final Object MONITOR = new Object();
    private Set<SideMoveObserver> mObservers;
	private int waitTimeMs = 0;
	private RubikCubeHistory history;
    
	public RubikCube()  
	{
		initialize();
	}
	
	private void initialize() 
	{
		sides = new ArrayList<RubikCubeSide>();
		edges = new ArrayList<RubikCubeEdge>();
		corners = new ArrayList<RubikCubeCorner>();

		history = new RubikCubeHistory(this);
		
		for(int lop = 0; lop < 6; lop++)
		{
			sides.add(new RubikCubeSide(this, lop));
		}		
		
		for(int lop = 0; lop < 6; lop++)
		{
			RubikCubeSide side = getSide(lop);
			ArrayList<RubikCubeSide> adjacentSides = side.getAdjacentSidesOrdered();
			
			for(int i = 0; i < adjacentSides.size(); i++)
			{
				RubikCubeSide adj = adjacentSides.get(i);
				
				if(getEdgePieceByFace(side, adj) == null)
				{
					edges.add(new RubikCubeEdge(this, side, adj));
				}
			}
		}	
		
		ArrayList<RubikCubeSide> adjacentSides = getSide(0).getAdjacentSidesOrdered();
		
		for(int lop = 0; lop < adjacentSides.size(); lop++)
		{
			corners.add(new RubikCubeCorner(
					this, 
					getSide(0), 
					adjacentSides.get(lop), 
					calculateLeft(
						getSide(0), 
						adjacentSides.get(lop)))
					);
			
			corners.add(new RubikCubeCorner(
					this, 
					getSide(0).getOppositeSide(), 
					adjacentSides.get(lop), 
					calculateLeft(
						getSide(0).getOppositeSide(), 
						adjacentSides.get(lop)))
					);
		}
	}
	
	public ArrayList<RubikCubeSide> getSides() 
	{
		return sides;
	}

	public RubikCubeSide getSide(int sideCode) 
	{
		return sides.get(sideCode);
	}
	
	public RubikCubeSide calculateLeft(RubikCubeSide sideUp, RubikCubeSide sideFront) 
	{				
		return getSide(getSidesOrder().get(sideUp.getTypeCode() * 6 + sideFront.getTypeCode()));
	}
	
	private HashMap<Integer, Integer> getSidesOrder() 
	{
		if(staticSidesOrderTable == null)
		{
			staticSidesOrderTable = new HashMap<Integer, Integer>();

			addFinalItemsToArray(1, 5, 2);
		}
		return staticSidesOrderTable;
	}

	private void addFinalItemsToArray(int up, int front, int left) 
{		
		addSetsItemsToArray(up, front, left);
		addSetsItemsToArray(5 - left, up, 5 - front);	
		addSetsItemsToArray(front, 5 - left, 5 - up);
	}

	private void addSetsItemsToArray(int up, int front, int left) {
		addItemsToArray(up, front, left);
		addItemsToArray(left, up, front);
	}

	private void addItemsToArray(int up, int front, int left) 
	{
		staticSidesOrderTable.put(up * 6 + front, left);
		staticSidesOrderTable.put(up * 6 + left, getSide(front).getOppositeSide().getTypeCode());
		staticSidesOrderTable.put(up * 6 + getSide(front).getOppositeSide().getTypeCode(), 
				getSide(left).getOppositeSide().getTypeCode());
		staticSidesOrderTable.put(up * 6 + getSide(left).getOppositeSide().getTypeCode(), front);
	}

	public void moveClockwise(RubikCubeSide side) 
	{
		moveClockwise(side, 1);
	}
	
	public void moveClockwise(RubikCubeSide side, int count)
	{
		moveClockwise(side, count, true);
		moveHappened();
	}
	
	public void moveClockwise(RubikCubeSide side, int count, boolean saveHistory) 
	{
		if(saveHistory)
		{
			history.addToHistory(side, count);
		}
		
		for(int i = 0; i < count; i++)
		{
			movePiecesClockwise(side);
		}
		
		try {
			Thread.sleep(waitTimeMs);
		} catch (InterruptedException e) {
		}
	}

	private void moveHappened() 
	{
		notifyObservers();
	}

	private void movePiecesClockwise(RubikCubeSide side)
	{		
		ArrayList<RubikCubeSide> adjacentSides = side.getAdjacentSidesOrderedReverse();
		
		//Ring for edges
		ArrayList<RubikCubeEdge> ringEdge = new ArrayList<RubikCubeEdge>();

		for(int lop = 0; lop < adjacentSides.size(); lop++)
		{
			ringEdge.add(getEdgePieceByLocation(side, adjacentSides.get(lop)));
		}
		crossSideEdgeRotation(side, ringEdge);

		//Ring for corners
		ArrayList<RubikCubeCorner> ringCorner = new ArrayList<RubikCubeCorner>();
		
		for(int lop = 0; lop < adjacentSides.size(); lop++)
		{
			ringCorner.add(
				getCornerPieceByLocation(
					side, 
					adjacentSides.get(lop), 
					calculateLeft(side, adjacentSides.get(lop))
					)
				);
		}
		crossSideCornerRotation(side, ringCorner);		
	}

	private void crossSideCornerRotation(RubikCubeSide side, ArrayList<RubikCubeCorner> ring) 
	{
		RubikCubeSide swapPlace1 = ring.get(0).getLocation1();
		RubikCubeSide swapPlace2 = ring.get(0).getLocation2();
		RubikCubeSide swapPlace3 = ring.get(0).getLocation3();

		for(int lop = 0; lop < ring.size(); lop++)
		{
			RubikCubeSide targetPlace1;
			RubikCubeSide targetPlace2;
			RubikCubeSide targetPlace3;

			RubikCubeCorner movingCorner = ring.get(lop);
		
			if(lop == ring.size() - 1)
			{
				targetPlace1 = swapPlace1;
				targetPlace2 = swapPlace2;
				targetPlace3 = swapPlace3;
			} else 
			{
				targetPlace1 = ring.get(lop + 1).getLocation1();
				targetPlace2 = ring.get(lop + 1).getLocation2();
				targetPlace3 = ring.get(lop + 1).getLocation3();
			}

			movingCorner.setLocationWithOneSideRotation(side, targetPlace1, targetPlace2, targetPlace3);
		}
	}

	private void crossSideEdgeRotation(RubikCubeSide side, ArrayList<RubikCubeEdge> ring) 
	{
		RubikCubeSide swapPlace1 = ring.get(0).getLocation1();
		RubikCubeSide swapPlace2 = ring.get(0).getLocation2();
		
		for(int lop = 0; lop < ring.size(); lop++)
		{
			RubikCubeSide targetPlace1;
			RubikCubeSide targetPlace2;

			RubikCubeEdge movingEdge = ring.get(lop);
		
			if(lop == ring.size() - 1)
			{
				targetPlace1 = swapPlace1;
				targetPlace2 = swapPlace2;
			} else 
			{
				targetPlace1 = ring.get(lop+1).getLocation1();
				targetPlace2 = ring.get(lop+1).getLocation2();
			}
		
			movingEdge.setLocationWithOneSideRotation(side, targetPlace1, targetPlace2);
		}
	}

	public void moveCounterClockwise(RubikCubeSide side)
	{
		moveCounterClockwise(side, 1);
	}
	
	public void moveCounterClockwise(RubikCubeSide side, int count) 
	{
		moveCounterClockwise(side, count, true);
		
		moveHappened();
	}
	
	public void moveCounterClockwise(RubikCubeSide side, int count, boolean saveHistory)  
	{
		if(saveHistory)
		{
			history.addToHistory(side, count);
		}
		
		for(int i = 0; i < count; i++)
		{
			movePiecesCounterClockwise(side);
		}
		
		try {
			Thread.sleep(waitTimeMs);
		} catch (InterruptedException e) {
		}
	}

	private void movePiecesCounterClockwise(RubikCubeSide side) 
	{		
		ArrayList<RubikCubeSide> adjacentSides = side.getAdjacentSidesOrderedReverse();
		
		//Ring for edges
		ArrayList<RubikCubeEdge> ringEdge = new ArrayList<RubikCubeEdge>();

		for(int lop = adjacentSides.size(); lop > 0; lop--)
		{
			ringEdge.add(getEdgePieceByLocation(side, adjacentSides.get(lop - 1)));
		}

		crossSideEdgeRotation(side, ringEdge);

		//Ring for corners
		ArrayList<RubikCubeCorner> ringCorner = new ArrayList<RubikCubeCorner>();
		
		for(int lop = adjacentSides.size(); lop > 0; lop--)
		{
			ringCorner.add(getCornerPieceByLocation(side, adjacentSides.get(lop - 1), 
						   calculateLeft(side, adjacentSides.get(lop - 1))));
		}
		
		crossSideCornerRotation(side, ringCorner);		
	}

	public void undoLastMoves(int i) throws Exception {
		history.undoLastMoves(i);
	}

	public void redoMoves(int i) throws Exception {
		history.redoMoves(i);
	}	 

	public RubikCubeEdge getEdgePieceByLocation(RubikCubeSide locationSide1, RubikCubeSide locationSide2)  
	{
		for(int lop = 0; lop < edges.size(); lop++)
		{
			if(
				(      edges.get(lop).getLocation1() == locationSide1
					&& edges.get(lop).getLocation2() == locationSide2)
					||
				(      edges.get(lop).getLocation1() == locationSide2
					&& edges.get(lop).getLocation2() == locationSide1)
				)
			{
				return edges.get(lop);
			}
		}
		return null;
	}
	
	public RubikCubeCorner getCornerPieceByLocation(
			RubikCubeSide locationSide1, 
			RubikCubeSide locationSide2,
			RubikCubeSide locationSide3) 
	{
		for(int lop = 0; lop < corners.size(); lop++)
		{
			if(corners.get(lop).doesLocationMatch(locationSide1, locationSide2, locationSide3))
			{
				return corners.get(lop);
			}
		}

		return null;
	}
	
	public RubikCubeEdge getEdgePieceByFace(RubikCubeSide side1, RubikCubeSide side2)
	{
		for(int lop = 0; lop < edges.size(); lop++)
		{
			if(edges.get(lop).hasSameSides(side1, side2))
			{
				return edges.get(lop);
			}
		}
		return null;
	}

	public void moveFromNotation(RubikCubeSide sideTop, RubikCubeSide sideFront, 
			String moveCode) throws Exception 
	{
		int actionType = 0; //Not F Prime or F2 (example)
		if(moveCode.length() > 1)
		{
			if(moveCode.substring(1).equals("'"))
			{
				actionType = 1; //F Prime (example)
			} else if(moveCode.substring(1).equals("2"))
			{
				actionType = 2; //F2 (example)
			} else {
				actionType = -1;
			}
		}
		
		String actionSide = moveCode.substring(0, 1);
		RubikCubeSide leftSide = calculateLeft(sideTop, sideFront);
				
		RubikCubeSide sideToTurn = null;
		
		switch(actionSide)
		{
		case "F":
			sideToTurn = sideFront;
			break;
		case "B":
			sideToTurn = sideFront.getOppositeSide();
			break;
		case "R":
			sideToTurn = leftSide.getOppositeSide();
			break;
		case "L":
			sideToTurn = leftSide;
			break;
		case "U":
			sideToTurn = sideTop;
			break;
		case "D":
			sideToTurn = sideTop.getOppositeSide();
			break;		
		}
		
		switch(actionType)
		{
		case 0:
			moveClockwise(sideToTurn);
			break;
		case 1:
			moveCounterClockwise(sideToTurn);
			break;
		case 2:
			moveClockwise(sideToTurn, 2);
			break;
		}		
	}

	public RubikCubeCorner getCornerPieceByFace(RubikCubeSide firstSide, 
			RubikCubeSide secondSide, RubikCubeSide thirdSide) 
	{
		for(int lop = 0; lop < corners.size(); lop++)
		{
			if(corners.get(lop).hasSameSides(firstSide, secondSide, thirdSide))
			{
				return corners.get(lop);
			}
		}
		return null;		
	}

	public boolean isSolved() 
	{
		for(int i = 0; i < sides.size(); i++)
		{
			if(!sides.get(i).isShallowSorted())
			{
				return false;
			}
		}
		return true;
	}

	public RubikCubeEdge getSolvedEdgeInLocation(RubikCubeEdge edgeLocation) 
	{
		return getEdgePieceByFace(edgeLocation.getLocation1(), edgeLocation.getLocation2());
	}

	public interface SideMoveObserver {
		void onSideMoved();
	}
	
    public void registerSideMoveObserver(SideMoveObserver observer) 
    {
        if (observer == null) return;
         
        synchronized(MONITOR) {
            if (mObservers == null) {
                mObservers = new HashSet<>(1);
            }
            if (mObservers.add(observer) && mObservers.size() == 1) {
                performInit(); // some initialization when first observer added
            }
        }
    }
     
    private void performInit() { }
    private void performCleanup() { }

    public void unregisterObserver(SideMoveObserver observer) 
    {
        if (observer == null) return;
         
        synchronized(MONITOR) {
            if (mObservers != null && mObservers.remove(observer) && mObservers.isEmpty()) {
                performCleanup(); // some cleanup when last observer removed
            }
        }
    }

    private void notifyObservers() 
    {
        Set<SideMoveObserver> observersCopy;
         
        synchronized(MONITOR) {
            if (mObservers == null) return;
            observersCopy = new HashSet<>(mObservers);
        }
         
        for (SideMoveObserver observer : observersCopy) { 
            observer.onSideMoved();
        }
    }

	public void setSlowSpeed(int waitTimeMs) 
	{
		this.waitTimeMs = waitTimeMs;
	}

	public int getSlowSpeed() 
	{
		return waitTimeMs;
	}
	
	public void reset() 
	{
		for(int lop = 0; lop < edges.size(); lop++)
		{
			edges.get(lop).goToDefaultPos();
		}
		for(int lop = 0; lop < corners.size(); lop++)
		{
			corners.get(lop).goToDefaultPos();
		}
	}

	public boolean isSolvable() 
	{
		boolean ret;
		RubikCube clone = null;
		try 
		{
			clone = this.clone();
		} catch (CloneNotSupportedException e1) {}
		
		try 
		{
			int oldSpeed = clone.getSlowSpeed();
			clone.setSlowSpeed(0);
			new RubikCubeSolver(clone).solve();
			clone.setSlowSpeed(oldSpeed);
			ret = true;
		} catch (Exception e) 
		{
			ret = false;
		}
		return ret;
	}

	public RubikCube clone() throws CloneNotSupportedException 
	{
		RubikCube clone = (RubikCube) super.clone();

		Map<RubikCubeSide, RubikCubeSide> sideOldToNew = new HashMap<>();
		
		clone.sides = new ArrayList<RubikCubeSide>();
		for(int lop = 0; lop < this.sides.size(); lop++)
		{
			RubikCubeSide orgSide = this.sides.get(lop);
			RubikCubeSide clnSide = orgSide.clone(clone);
			clone.sides.add(clnSide);
			sideOldToNew.put(orgSide, clnSide);
		}
		
		clone.history = new RubikCubeHistory(clone);

		clone.edges = new ArrayList<RubikCubeEdge>();
		for(int lop = 0; lop < this.edges.size(); lop++)
		{
			RubikCubeEdge orgEdge = this.edges.get(lop);
			RubikCubeEdge clnEdge = new RubikCubeEdge(
										clone,
										sideOldToNew.get(orgEdge.pieceFace.getSide()), 
										sideOldToNew.get(orgEdge.pieceFace2.getSide()));
			clnEdge.locationSide1 = sideOldToNew.get(orgEdge.locationSide1);
			clnEdge.locationSide2 = sideOldToNew.get(orgEdge.locationSide2);

			clone.edges.add(clnEdge);
		}
		
		clone.corners = new ArrayList<RubikCubeCorner>();
		for(int lop = 0; lop < this.corners.size(); lop++)
		{
			RubikCubeCorner orgCorner = this.corners.get(lop);
			RubikCubeCorner clnCorner = new RubikCubeCorner(
										clone,
										sideOldToNew.get(orgCorner.pieceFace.getSide()),
										sideOldToNew.get(orgCorner.pieceFace2.getSide()), 
										sideOldToNew.get(orgCorner.pieceFace3.getSide()));
			clnCorner.locationSide1 = sideOldToNew.get(orgCorner.locationSide1);
			clnCorner.locationSide2 = sideOldToNew.get(orgCorner.locationSide2);
			clnCorner.locationSide3 = sideOldToNew.get(orgCorner.locationSide3);

			clone.corners.add(clnCorner);
		}

		return clone;
	}
}
