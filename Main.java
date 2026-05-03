public class Main {
  private static String gamedata = "gamedata.csv";
  private static String savedata = "knights.csv";

  public static void main(String[] args){
    processArgs(args);
    GameData data = new CSVGameData(gamedata,savedata);
    GameView view = new ConsoleView();
    CombatEngine engine = new CombatEngine(data,view);
    GameController controller = new GameController(data,view,engine);
    controller.start();
  }

  private static void processArgs(String[] args){
    for(int i = 0; i < args.length; ++i){
      if(args[i].contains("--data")){
        String dataString = args[i].substring(args[i].lastIndexOf("=") + 1);
        gamedata = dataString;
      }
      else {
        savedata = args[i].trim(); 
      }
    }
  }
  
}