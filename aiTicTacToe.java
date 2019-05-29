import java.util.*;
public class aiTicTacToe {

	public int player; //1 for player 1 and 2 for player 2
	private List<List<positionTicTacToe>>  winningLines = new ArrayList<>(); 
	private int getStateOfPositionFromBoard(positionTicTacToe position, List<positionTicTacToe> board)
	{
		//a helper function to get state of a certain position in the Tic-Tac-Toe board by given position TicTacToe
		int index = position.x*16+position.y*4+position.z;
		return board.get(index).state;
	}

	private int isEnded(List<positionTicTacToe> board)
	{
		//test whether the current game is ended
		
		//brute-force
		for(int i=0;i<winningLines.size();i++)
		{
			
			positionTicTacToe p0 = winningLines.get(i).get(0);
			positionTicTacToe p1 = winningLines.get(i).get(1);
			positionTicTacToe p2 = winningLines.get(i).get(2);
			positionTicTacToe p3 = winningLines.get(i).get(3);
			
			int state0 = getStateOfPositionFromBoard(p0,board);
			int state1 = getStateOfPositionFromBoard(p1,board);
			int state2 = getStateOfPositionFromBoard(p2,board);
			int state3 = getStateOfPositionFromBoard(p3,board);
			
			//if they have the same state (marked by same player) and they are not all marked.
			if(state0 == state1 && state1 == state2 && state2 == state3 && state0!=0)
			{
				//someone wins
				p0.state = state0;
				p1.state = state1;
				p2.state = state2;
				p3.state = state3;
				
				//print the satisified winning line (one of them if there are several)
				p0.printPosition();
				p1.printPosition();
				p2.printPosition();
				p3.printPosition();
				return state0;
			}
		}
		for(int i=0;i<board.size();i++)
		{
			if(board.get(i).state==0)
			{
				//game is not ended, continue
				return 0;
			}
		}
		return -1; //call it a draw
	}

	public List<positionTicTacToe> generateAllMoves(List<positionTicTacToe> board)
	{
		List<positionTicTacToe> nextMove = new ArrayList<positionTicTacToe> ();
		for (int i=0;i<board.size();i++) {
			if (board.get(i).state == 0) {
				nextMove.add(board.get(i));
			}
		}
		return nextMove;
	}

	//returns score, x, y, z
	public int[] minimax(List<positionTicTacToe> board, int depth, int turn, int alpha, int beta)
	{
		positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);
		List<positionTicTacToe> nextMove = generateAllMoves(board);

		int score;
      	int bestX = -1;
		int bestY = -1;
		int bestZ = -1;

		if (isEnded(board) != 0 || depth == 0) {
			score = evaluate(board);
			return new int[] {score, bestX, bestY, bestZ};
		} else {
			for (positionTicTacToe move : nextMove) {
			   	// try this move for the current "player"
				board.get(move.x*16+move.y*4+move.z).state = turn;
				
			   	if (turn == player) {  // maximizing player
				  	score = minimax(board, depth - 1, (turn == 2)? 1:2, alpha, beta)[0];
				  	if (score > alpha) {
					 	alpha = score;
					 	bestX = move.x;
						bestY = move.y;
						bestZ = move.z;
				  	}
			   	} else {  // minimizing player
				  	score = minimax(board, depth - 1, (turn == 2)? 1:2, alpha, beta)[0];
				  	if (score < beta) {
						beta = score;
						bestX = move.x;
						bestY = move.y;
						bestZ = move.z;
				  	}
			   	}
			   	// undo move
			   	board.get(move.x*16+move.y*4+move.z).state = 0;
				// cut-off
				if (alpha >= beta) break;
			}
			return new int[] {(turn == player) ? alpha : beta, bestX, bestY, bestZ};
		}
	}

	// + value for maximizing player, - value for minimizing player, 1 for one in a line, 10 for two in a line, 50 for three in a line, 100 for four in a line
	public int evaluate(List<positionTicTacToe> board) {
		winningLines = initializeWinningLines();
		int score = 0;
		for (int i=0;i<winningLines.size();i++) {
			int one = 0;
			int two = 0;
			for (int j=0;j<4;j++){
				int stt = getStateOfPositionFromBoard(winningLines.get(i).get(j), board);
				if (stt==player) {
					one++;
				} else if (stt!=0 && stt!=-1 && stt!=player) {
					two++;
				}

			}
			
			if (one==0) {
				if (two==0) {
					score += 0;
				} else if (two==1) {
					score += -1;
				} else if (two==2) {
					score += -10;
				} else if (two==3) {
					score += -100;
				} else {
					score += -1000;
				}
			} else if (two==0) {
				if (one==0) {
					score += 0;
				} else if (one==1) {
					score += 1;
				} else if (one==2) {
					score += 10;
				} else if (one==3) {
					score += 100;
				} else {
					score += 1000;
				}
			} else {
				score += 0;
			}
		}

		return score;

		
	}

	// helps the player make a calculated move using minimax algorithm
	public positionTicTacToe myAIAlgorithm(List<positionTicTacToe> board, int turn)
	{
		//TODO: this is where you are going to implement your AI algorithm to win the game. The default is an AI randomly choose any available move.
		positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);
		//if (turn == 1) {
			int[] temp = minimax(board, 3, turn, Integer.MIN_VALUE, Integer.MAX_VALUE);
			myNextMove = new positionTicTacToe(temp[1],temp[2],temp[3]);
		/*} else {
			int[] temp = minimax(board, 2, turn, Integer.MIN_VALUE, Integer.MAX_VALUE);
			myNextMove = new positionTicTacToe(temp[1],temp[2],temp[3]);
		}*/
		//myNextMove = new positionTicTacToe(temp[1],temp[2],temp[3]);
		
		return myNextMove;
	}

	// helps the player make a randomized move
	public positionTicTacToe myRandomAlgorithm(List<positionTicTacToe> board, int player)
	{
		positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);
		do
			{
				Random rand = new Random();
				int x = rand.nextInt(4);
				int y = rand.nextInt(4);
				int z = rand.nextInt(4);
				myNextMove = new positionTicTacToe(x,y,z);
			}while(getStateOfPositionFromBoard(myNextMove,board)!=0);
		
		return myNextMove;
	}

	// find all 76 winning lines and put them in a list
	private List<List<positionTicTacToe>> initializeWinningLines()
	{
		//create a list of winning line so that the game will "brute-force" check if a player satisfied any winning condition(s).
		List<List<positionTicTacToe>> winningLines = new ArrayList<List<positionTicTacToe>>();
		
		//48 straight winning lines
		//z axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,j,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//y axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,j,-1));
				winningLines.add(oneWinCondtion);
			}
		//x axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,j,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//12 main diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,0,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,0,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,3,i,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//12 anti diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,3,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,3,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,3,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,0,i,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//4 additional diagonal winning lines
		List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,3,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,3,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,0,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(3,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(0,3,3,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,3,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,0,3,-1));
		winningLines.add(oneWinCondtion);	
		
		return winningLines;
		
	}
	public aiTicTacToe(int setPlayer)
	{
		player = setPlayer;
	}
}
