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
      Knight knight = data.activeKnights.get(i);
      knight.setActiveFortune(data.getRandomFortune());
    }
    view.printFortunes(data.activeKnights);
  }

  public void runCombat(){
    boolean proceed = true;
    List<Knight> knights = data.activeKnights;
    List<MOB> monsters = data.getRandomMonsters();
  
    view.printBattleText(monsters,knights);
    
    while(proceed){
      int result = doBattle(knights,monsters) + doBattle(monsters,knights);
   
      if(monsters.size() == 0){
        if(view.checkContinue()){
          //if user wants to continue, get more monsters
          monsters = data.getRandomMonsters();
          view.printBattleText(monsters, knights);
          continue;
        }
        else {
          proceed = false;
        }
      }
      else if(knights.size() == 0 ){
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
      if(defenders.size() == 0) break;
      int defendIndex = rnd.nextInt(defenders.size());
      MOB attacker = attackers.get(i);
      MOB defender = defenders.get(defendIndex);
      //choose different defender if already dead
      while(defender.getHP() <= 0 && defenders.size() > 0) {
        defendIndex = rnd.nextInt(defenders.size());
        defender = defenders.get(defendIndex);
      }
      numVictories += attack(attacker,defender);
    }
    //remove defeated defenders 
    defenders.removeIf(d -> d.getHP() <= 0);

    return numVictories;
  }

  //check if attacker makes successfull hit and add damage to defender if needed
  public int attack(MOB attacker,MOB defender){
    int attackRoll = DiceType.D20.roll();
    int count = 0;

    if(attackRoll + attacker.getHitModifier() > defender.getArmor()){
      DiceType damageDie = attacker.getDamageDie();
      int damageRoll = damageDie.roll();

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