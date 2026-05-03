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
    else if (command.toLowerCase().contains("list active") || command.toLowerCase().contains("listactive")){
      view.listKnights(data.getActiveKnights());
    }
    else if (command.toLowerCase().contains("show")){
      //accounts for if the user does not include spaces or has leading whitespaces
      if(command.length() > 4){
        String substring = command.trim().substring(4);
        processShowKnight(substring.trim());
      }
      else {
        processShowKnight(null);
      }
 
    }
    else if (command.toLowerCase().contains("set active")){
      processSetActive(command);
    }
    else if (command.toLowerCase().contains("remove")){
      processRemoveActive(command);
    }
    else if(command.toLowerCase().contains("save")){
      //add error handling
      String[] words = command.trim().split(" ");
      if(words.length > 1){
        data.save(words[1]);
      }
      else {
        data.save("saveData.csv");
      }
    }
    else if(command.toLowerCase().contains("explore") || command.toLowerCase().contains("adventure") || command.toLowerCase().contains("quest")){
      //add error handling
      //start a combat sequence if there are active knights
      if(data.activeKnights.size() > 0){
        engine.initialize();
        engine.runCombat();
      }
      else {
        System.out.println("Oops, you have no active knights. Add active knights to start your quest!");
      }
    }
    else {
      view.printHelp();
    }
    return true;
  }

  private void processRemoveActive(String remove){
    //accounts for inputs such as removeNAME, remove NAME, remove active NAME
    String[] words = remove.trim().split(" ");
    if(words.length == 2){
      data.removeActive(data.getKnight(words[1]));
    }
    else if(words.length == 3){
      data.removeActive(data.getKnight(words[2]));
    }
    else if(words.length < 2 && remove.length() > 6){
      String substring = remove.trim().substring(6);
      data.removeActive(data.getKnight(substring));
    }
    
  }

  private void processSetActive(String active){
    boolean success = true;
    if(data.activeKnights.size() == 4){
      view.setActiveFailed();
    }
    else {
      String[] words = active.trim().split(" ");
      if(words.length == 2 && words[1].length() > 6){
        success = data.setActive(data.getKnight(words[1].substring(6)));
      }
      else if(words.length == 3){
        success = data.setActive(data.getKnight(words[2]));
      }  
    }
    if(!success){
      System.out.println("Error setting active knight, please try again.");
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