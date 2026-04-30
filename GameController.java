public class GameController {
  private final GameData data;
  private final CombatEngine engine;
  private final GameView view;

  public GameController(GameData data, GameView view, CombatEngine engine){
    this.data = data;
    this.view = view;
    this.engine = engine;
  }

  public void start(){
    view.splashScreen();
    while(processCommand(view.displayMainMenu())){
    }
    view.endGame();
  }

  protected boolean processCommand(String command){
    if (command.toLowerCase().contains("exit") || command.toLowerCase().contains("bye")){
      return false;
    }
    else if (command.toLowerCase().contains("ls") || command.toLowerCase().contains("list all")){
      view.listKnights(data.getKnights());
    }
    else if (command.toLowerCase().contains("list active")){
      view.listKnights(data.getActiveKnights());
    }
    else if (command.toLowerCase().contains("show")){
      String[] words = command.split(" ");
      processShowKnight(words[1]);
    }
    else if (command.toLowerCase().contains("set active")){
      processSetActive(command);
    }
    else if (command.toLowerCase().contains("remove")){
      processRemoveActive(command);
    }
    else if(command.toLowerCase().contains("save")){
      String[] words = command.trim().split(" ");
      if(words.length > 1){
        data.save(words[1]);
      }
      else {
        data.save("saveData.csv");
      }
    }
    else if(command.toLowerCase().contains("explore") || command.toLowerCase().contains("adventure") || command.toLowerCase().contains("quest")){
      //start a combat sequence
      System.out.println("Combat coming soon");
      engine.runCombat();
    }
    else {
      view.printHelp();
    }
    return true;
  }

  private void processRemoveActive(String remove){
    String[] words = remove.split(" ");
    data.removeActive(data.getKnight(words[1]));
  }

  private void processSetActive(String active){
    String[] words = active.split(" ");
    boolean success = data.setActive(data.getKnight(words[2]));
    if(!success){
      view.setActiveFailed();
    }
  }

  private void processShowKnight(String nameOrId){
    Knight knight = data.getKnight(nameOrId);
    if(knight != null){
      view.showKnight(knight);
    }
    else {
      view.knightNotFound();
    }
  }

  public static void main(String[] args){
    GameData data = new CSVGameData("gamedata.csv","knights.csv");
    GameView view = new ConsoleView();
    CombatEngine engine = new CombatEngine(data, view);
    GameController controller = new GameController(data,view,engine);
    controller.start();
  }
  
}