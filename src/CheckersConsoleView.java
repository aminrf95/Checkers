import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CheckersConsoleView {

    public static void main(String[] args) {
        //Runs a local game of checkers on the console.
        CheckersModel game = new CheckersModel();
        Scanner in = new Scanner(System.in);
        while(game.getWinner() == 0) {
            System.out.println(game);
            String player;
            if(game.getCurrentPlayer() == CheckersModel.RED_PIECE) {
                player = "Red";
            }
            else {
                player = "Black";
            }
            System.out.println(player+"'s turn...");
            Map<String, Set<String>> possibleMoves = game.getPossibleMoves();
            for(String from : possibleMoves.keySet()) {
                for(String to : possibleMoves.get(from)) {
                    System.out.println("(" + from.charAt(0) +"," + from.charAt(1) + ") -> (" + to.charAt(0) +"," + to.charAt(1) + ")");
                }
            }
            int[] from = new int[2];
            int[] to = new int[2];
            from[0] = in.nextInt();
            from[1] = in.nextInt();
            to[0] = in.nextInt();
            to[1] = in.nextInt();
            while(!game.makeMove(from[0],from[1],to[0],to[1])) {
                System.out.println("Invalid move, try again...");
                from[0] = in.nextInt();
                from[1] = in.nextInt();
                to[0] = in.nextInt();
                to[1] = in.nextInt();
            }
        }
        System.out.println(game);
        String player;
        if(game.getCurrentPlayer() == CheckersModel.RED_PIECE) {
            player = "Black";
        }
        else {
            player = "Red";
        }
        System.out.println(player + " wins!");
    }
}
