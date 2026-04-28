import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class GameData{
  protected final List<Knight> activeKnights = new ArrayList<>();
  protected final List<Fortune> fortunes = new ArrayList<>();
  protected final List<Knight> knights = new ArrayList<>();
  private static final int MAX_ACTIVE = 4;
  protected final List<MOB> monsters = new ArrayList<>();
  private static final Random random = new Random();

  public GameData(){

  }

  public List<Knight> getKnights(){
    return knights;
  }
  
  public List<Knight> getActiveKnights(){
    return activeKnights;
  }

  public Knight getActive(String nameOrId){
    return findKnight(nameOrId,activeKnights);
  }

  public Knight getKnight(String nameOrId){
    return findKnight(nameOrId, knights);
  }

  protected Knight findKnight(String nameOrId, List<Knight> list){
    //word in knights name or id
    Knight found = null;
    for(Knight knight: list){
        if(knight.getName().toLowerCase().contains(nameOrId.toLowerCase()) || knight.getId().toString().equals(nameOrId)){
          found = knight;
        }
    }
    return found;
  }

  public boolean setActive(Knight kt){
    if(activeKnights.size() < MAX_ACTIVE){
      activeKnights.add(kt);
      return true;
    }
    return false;
  }

  public void removeActive(Knight kt){
    //reset damage and remove knight
    kt.resetDamage();
    if(activeKnights.remove(kt)){
      System.out.println("knight successfully removed");
    }
  }

  public Fortune getRandomFortune(){
    int listSize = fortunes.size();
    int randomIndex = random.nextInt(listSize);
    return fortunes.get(randomIndex);
  }

  //not sure if doing this method correctly
  public List<MOB> getRandomMonsters(){
    List<MOB> monsterList = new ArrayList<>();
    int listSize = monsters.size();
    for(int i = 0; i < activeKnights.size(); ++i){
      int randomIndex = random.nextInt(listSize);
      monsterList.add(monsters.get(randomIndex));
    }
    return monsterList;
  }

  //not sure if doing this method correctly
  public List<MOB> getRandomMonsters(int number){
    //monsters should be copied into the return List, so they can be modified individually
    int listSize = monsters.size(); //number of monsters cannot be greater than active knights
    List<MOB> monsterList = new ArrayList<>();

    for(int i = 0; i < number; ++i){
      int randomIndex = random.nextInt(listSize);
      monsterList.add(monsters.get(randomIndex).copy());
    }
    return monsterList;
  }

  public abstract void save(String filename);
}