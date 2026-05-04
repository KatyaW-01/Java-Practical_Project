import java.util.Random;
import java.util.List;

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
      data.activeKnights.get(i).setActiveFortune(data.getRandomFortune());
    }
    view.printFortunes(data.activeKnights);
  }

  public void runCombat(){
    boolean proceed = true;
    List<MOB> monsters = data.getRandomMonsters();
    List<Knight> activeKnights = data.activeKnights;

    view.printBattleText(monsters,activeKnights);
    
    while(proceed){
      int result = 0;
      if(activeKnights.size() > 0 && monsters.size() > 0){
        result += doBattle(activeKnights,monsters);
      }
      if(monsters.size() > 0 && activeKnights.size() > 0){
        result += doBattle(monsters,activeKnights);
      }

      if(monsters.size() == 0){
        if(view.checkContinue()){
          //if user wants to continue, get more monsters
          monsters = data.getRandomMonsters();
          view.printBattleText(monsters, activeKnights);
          continue;
        }
        else {
          proceed = false;
        }
      }
      else if(activeKnights.size() == 0 ){
        view.printDefeated();
        proceed = false;
      }
      //if no monsters or knights are defeated during battle, continue to do battle
      else if(result == 0){
        continue;
      }
      /*if a mob or knight is defeated but there are still other active mobs and knights, 
      ask user if they want to continue */
      else {
        if(view.checkContinue() == false){
          proceed = false;
        }
      }
    }
 
    //once finished with quest, remove active fortunes from knights
    clear();
  }

  private int doBattle(List<? extends MOB> attackers, List<? extends MOB> defenders){
    int attackSize = attackers.size();
    int numVictories = 0;

    //cycle through attackers having them attack a random defender
    for(int i = 0; i < attackSize; ++i){
      //remove defeated defenders, exit loop if there are no more defenders
      defenders.removeIf(d -> d.getHP() <= 0);
      if(defenders.size() == 0) break;

      int defendIndex = rnd.nextInt(defenders.size());
      MOB attacker = attackers.get(i);
      MOB defender = defenders.get(defendIndex);
      numVictories += attack(attacker,defender);
    }
    
    return numVictories;
  }

  //check if attacker makes successfull hit and add damage to defender if needed
  public int attack(MOB attacker,MOB defender){
    int attackRoll = DiceType.D20.Roll();
    int count = 0;

    if(attackRoll + attacker.getHitModifier() > defender.getArmor()){
      DiceType damageDie = attacker.getDamageDie();
      int damageRoll = damageDie.Roll();

      defender.addDamage(damageRoll);

      //if defender has no more health, print battle message
      if(defender.getHP() <= 0){
        manageDefeat(defender);
        count += 1;
      } 
    }
    return count;
  }

  public void manageDefeat(MOB dead){
    view.printBattleText(dead);
    if(dead instanceof Knight){
      //if a Knight, remove from active knights
      data.removeActive((Knight) dead);
    }
    else {
      //if a MOB, give every active knight 1 xp point
      for(int i = 0; i < data.activeKnights.size(); ++i){
        data.activeKnights.get(i).addXP(1);
      }
    }
  }

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