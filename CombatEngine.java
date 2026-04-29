import java.util.Random;
// import java.util.List;

public class CombatEngine {
  private final GameData data;
  private final Random rnd;
  private final GameView view;
  
  public CombatEngine(GameData data, GameView view){
    this.data = data;
    this.view = view;
    this.rnd = new Random();
  }

  public void initialize(){
    //assign active knights with a random fortune
    for(int i = 0; i < data.activeKnights.size(); ++i){
      Knight knight = data.activeKnights.get(i);
      knight.setActiveFortune(data.getRandomFortune());
    }
    view.printFortunes(data.activeKnights);
  }

  public void runCombat(){

  }

  //helper method for run combat
  // private int doBattle(List<MOB> attackers, List<MOB> defenders){

  // }

  public void clear(){
    //reset all fortunes to null across all knights
    for(int i = 0; i < data.knights.size(); ++i){
      Knight knight = data.knights.get(i);
      knight.setActiveFortune(null);
    }
  }

  public static void main(String[] args){
    //Testing initialize method
    GameData data = new CSVGameData("gamedata.csv","knights.csv");
    GameView view = new ConsoleView();
    CombatEngine engine = new CombatEngine(data, view);
    System.out.println("TESTING initialize in combat engine: ");
    Knight gwain = data.getKnight("Gwain");
    Knight morrigan = data.getKnight("Morrigan");
    System.out.println(data.setActive(gwain) + " " + data.setActive(morrigan));
    engine.initialize();
    //Testing clear
    System.out.println("TESTING clear in combat engine (both outputs should be null): ");
    engine.clear();
    System.out.println(data.getKnight("Gwain").getActiveFortune());
    System.out.println(data.getKnight("Morrigan").getActiveFortune());
  }
}