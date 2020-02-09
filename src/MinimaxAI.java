import java.util.ArrayList;

/**
 * A super OthelloAI-implementation. The method to decide the next move
 * based on AB cut-off
 *
 * @author Weisi
 * @version 4.5.2019
 */
public class MinimaxAI implements IOthelloAI {

    /**
     * Returns first legal move
     */
    public Position decideMove(GameState s) {
        ArrayList<Position> moves = s.legalMoves();
        if (moves.isEmpty()) {
            return new Position(-1, -1);
        }

        // minimax decision
        // Remember the best move
        int best_val = -999;
        Position position = null;
        // Try out every single move
        for (int i = 0; i < moves.size(); i++) {
            // Apply the move to a new board
            GameState tmp_state = new GameState(s.getBoard(), s.getPlayerInTurn());
            position = moves.get(i);
            tmp_state.insertToken(position);
            // Recursive call, initial search ply = 1
            int recall_val = minimaxValue(tmp_state);
            // Remember best move
            if (recall_val > best_val) {
                best_val = recall_val;
                position = moves.get(i);
            }
        }
        return position;
    }


    private int minimaxValue(GameState s) {
        Position position = null;
        // isFinished include changePlayer() and checked legalMoves()
        if (s.isFinished()) {
            return -1;
        }

        // Remember the best move
        // -99999 for getting MAX, 99999 for getting MIN
        int best_val = s.getPlayerInTurn() == 1 ? -999 : 999;
        ArrayList<Position> moves = s.legalMoves();
        // Try out every single move
        for (int i = 0; i < moves.size(); i++) {
            // copy the GameState
            GameState tmp_state = new GameState(s.getBoard(), s.getPlayerInTurn());
            // Apply the move to a new board
            position = moves.get(i);
            tmp_state.insertToken(position);
            // Recursive call
            int recall_val = minimaxValue(tmp_state);
            // Remember best move
            if (tmp_state.getPlayerInTurn() == 1) {
                // Remember max if it's the originator's turn
                if (recall_val > best_val)
                    best_val = recall_val;
            } else {
                // Remember min if it's opponent turn
                if (recall_val < best_val)
                    best_val = recall_val;
            }
        }
        System.out.println(best_val);
        return best_val;
    }
}
