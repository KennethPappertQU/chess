//App.java
import game.GameManager;
import UI.UIGameManager;


public class App {
	public static void main(String [] args) {
		System.out.println("--- Starting SER120 Chess App ---");
        UIGameManager gm = new UIGameManager();
        gm.runGame();
        
        
        System.out.println("--- Session Ended ---");
    }
}
