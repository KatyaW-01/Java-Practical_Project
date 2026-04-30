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
      doBattle(knights,monsters); //knights attack first
      doBattle(monsters,knights);
      
      if(knights.size() == 0 ){
        view.printDefeated();
        proceed = false;
      }
      else if(monsters.size() == 0){
        if(view.checkContinue()){
          //if user wants to continue, get more monsters
          monsters = data.getRandomMonsters();
        }
        else {
          proceed = false;
        }
      }
      else {
        if(view.checkContinue() == false){
          proceed = false;
        }
      }
    }

    view.displayMainMenu(); 
  }

  private void doBattle(List<? extends MOB> attackers, List<? extends MOB> defenders){
    int attackSize = attackers.size();
    int defendSize = defenders.size();
    int defendIndex = rnd.nextInt(defendSize);

    //cycle through attackers having them attack a random defender
    for(int i = 0; i < attackSize; ++i){
      MOB attacker = attackers.get(i);
      MOB defender = defenders.get(defendIndex);
      attack(attacker,defender);
    }
  }

  //check if attacker makes successfull hit and add damage to defender if needed
  public void attack(MOB attacker,MOB defender){
    int attackRoll = DiceType.D20.roll();

    if(attackRoll + attacker.getHitModifier() > defender.getArmor()){
      DiceType damageDie = attacker.getDamageDie();
      int damageRoll = damageDie.roll();

      defender.addDamage(damageRoll);

      //if defender has no more health, print battle message
      if(defender.getHP() <= 0){
        manageDefeat(defender);
      } 
    }
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