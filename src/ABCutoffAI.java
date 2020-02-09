import java.util.ArrayList;
import java.util.HashMap;

/**
 * A super OthelloAI-implementation. The method to decide the next move
 * based on AB cut-off
 *
 * @author Weisi
 * @version 14.5.2019
 */
public class ABCutoffAI implements IOthelloAI {
    int maxDepth = 6;

    /**
     * Alpha_Beta_Search
     */
    public Position decideMove(GameState s) {
        // Record the response time
//        long startTime = System.currentTimeMillis();

        Position position = new Position(-1, -1);
        ArrayList<Position> moves = s.legalMoves();
        if (moves.isEmpty()) {
            return position;
        }
        // Remember the best move
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        position = (Position) maxValue(s, alpha, beta, maxDepth).get("pos");
        // Record the response time

//        long endTime = System.currentTimeMillis();
//        System.out.println((endTime - startTime) + "ms");

        return position;
    }

    private HashMap<String, Object> maxValue(GameState s, int alpha, int beta, int depth) {
        Position bestPosition = new Position(-1, -1);
        HashMap<String, Object> map = new HashMap<String, Object>();

        ArrayList<Position> moves = s.legalMoves();
        // check the depth and gameover
        if (depth <= 0 || moves.size() <= 0) {
            map.put("val", Evaluation.evaluationValue(s));
            return map;
        }
        // init the value for MAX dicision
        int value = Integer.MIN_VALUE;
        // loop every action
        for (int i = 0; i < moves.size(); i++) {
            // copy the GameState
            GameState tmpState = new GameState(s.getBoard(), s.getPlayerInTurn());
            // Apply the move to the new Game State
            Position position = moves.get(i);
            tmpState.insertToken(position);  // insertToken() already include changePlayer()
            // Recursive call minimaxValue, get a map with position and value
            HashMap tmpMap = minValue(tmpState, alpha, beta, depth - 1);
            if (value == (Integer) tmpMap.get("val") && Math.random() <= 0.5) {
                bestPosition = position;
            } else {
                int compvalue = Math.max(value, (Integer) tmpMap.get("val"));
                // at least run one times at the first time
                if (compvalue != value) {
                    bestPosition = position;
                }
                // save the choosed position and value
                value = compvalue;
            }
            map.put("pos", bestPosition);
            map.put("val", value);
            // cut-off useless branches
            if (value >= beta)
                break;
            // update alpha, make sure it is always the highest-value, choice we have found so far at any choice point along the path for MAX.
            alpha = Math.max(alpha, value);
        }
        return map;
    }

    private HashMap<String, Object> minValue(GameState s, int alpha, int beta, int depth) {
        Position bestPosition = new Position(-1, -1);
        HashMap<String, Object> map = new HashMap<String, Object>();
        // check the depth and gameover
        ArrayList<Position> moves = s.legalMoves();
        if (depth <= 0 || moves.size() <= 0) {
            map.put("val", Evaluation.evaluationValue(s));
            return map;
        }
        // init the value for MIN dicision
        int value = Integer.MAX_VALUE;
        // loop every action
        for (int i = 0; i < moves.size(); i++) {
            // copy the GameState
            GameState tmpState = new GameState(s.getBoard(), s.getPlayerInTurn());
            // Apply the move to the new Game State
            Position position = moves.get(i);
            tmpState.insertToken(position);   // insertToken() already include changePlayer()
            // Recursive call minimaxValue, get a map with position and value
            HashMap tmpMap = maxValue(tmpState, alpha, beta, depth - 1);
            if (value == (Integer) tmpMap.get("val") && Math.random() <= 0.5) {
                bestPosition = position;
            } else {
                int compvalue = Math.min(value, (Integer) tmpMap.get("val"));
                // at least run one times at the first time
                if (compvalue != value) {
                    bestPosition = position;
                }
                // save the choosed position and value
                value = compvalue;
            }
            map.put("pos", bestPosition);
            map.put("val", value);
            // cut-off useless branches
            if (value <= alpha)
                break;
            // update beta, make sure it is always the lowest-value, choice we have found so far at any choice point along the path for MIN.
            beta = Math.min(beta, value);
        }
        return map;
    }
}
