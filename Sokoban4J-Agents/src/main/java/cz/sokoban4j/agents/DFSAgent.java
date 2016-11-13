package cz.sokoban4j.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.sokoban4j.Sokoban;
import cz.sokoban4j.simulation.actions.EDirection;
import cz.sokoban4j.simulation.actions.compact.CAction;
import cz.sokoban4j.simulation.actions.compact.CMove;
import cz.sokoban4j.simulation.actions.compact.CPush;
import cz.sokoban4j.simulation.board.compact.BoardCompact;

public class DFSAgent extends ArtificialAgent {

	protected List<EDirection> result;
	
	protected BoardCompact board;
	
	protected boolean solutionFound;
	
	@Override
	protected List<EDirection> think(BoardCompact board) {
		// INIT SEARCH
		this.board = board;
		this.result = new ArrayList<EDirection>();
		this.solutionFound = false;
		
		// DEBUG
		this.board.debugPrint();
		
		// FIRE THE SEARCH
		dfs(10);
		
		if (result.size() == 0) {
			System.out.println("FAILED TO FIND THE SOLUTION...");
		}

		return result;
	}

	private boolean dfs(int level) {
		if (level <= 0) return false;
		
		// COLLECT POSSIBLE ACTIONS
		
		List<CAction> actions = new ArrayList<CAction>(4);
		
		for (CMove move : CMove.getActions()) {
			if (move.isPossible(board)) {
				actions.add(move);
			}
		}
		for (CPush push : CPush.getActions()) {
			if (push.isPossible(board)) {
				actions.add(push);
			}
		}
		
		// TRY ACTIONS
		for (CAction action : actions) {
			// PERFORM THE ACTION
			result.add(action.getDirection());
			action.perform(board);
			
			// DEBUG
			//System.out.println("PERFORMED: " + action);
			//board.debugPrint();
			
			// CHECK VICTORY
			if (board.isVictory()) {
				// SOLUTION FOUND!
				System.out.print("VICTORY[" + result.size() + "]: ");
				for (EDirection winDirection : result) {
					System.out.print(winDirection + " -> ");
				}
				System.out.println("VICTORY");
				return true;
			}
			
			// CONTINUE THE SEARCH
			if (dfs(level-1)) {
				// SOLUTION FOUND!
				return true;
			}
			
			// REVESE ACTION
			result.remove(result.size()-1);
			action.reverse(board);
			
			// DEBUG
			//System.out.println("REVERSED: " + action + " -> " + action.getDirection().opposite());
			//board.debugPrint();
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		Sokoban.playAgent("../Sokoban4J/levels/level0001.s4jl", new DFSAgent());
	}

}