//A knight is the primary protagonist of the game. It contains stats/attributes, experience points, and a fortune that can adjust the attributes. Additional, knights are assigned an ID, to help separate one from another.
public class Knight extends MOB {
  private Fortune activeFortune = null; //null if no active fortune
  protected final int id;
  protected int xp; //experience points

  public Knight(int id, String name, int hp, int armor, int hitModifier, DiceType damageDie, int xp){
    super(name,hp,armor,hitModifier,damageDie);
    this.id = id;
    this.xp = xp;
  }

  public void setActiveFortune(Fortune activeFortune){
    this.activeFortune = activeFortune;
  }

  public Fortune getActiveFortune(){
    return activeFortune;
  }

  public Integer getId(){
    return (Integer) id;
  }

  public int getXP(){
    return xp;
  }

  public void addXP(int xpNum){
    xp += xpNum;
  }

  @Override
  public int getArmor(){
    if(activeFortune != null){
      //if an activeFortune is in effect, armor is the armor of the knight + the armor bonus from the activeFortune
      armor = super.getArmor() + activeFortune.getArmor();
      return armor;
    }
    return super.getArmor();
  }

  @Override
  public DiceType getDamageDie(){
    if(activeFortune != null){
      /*if the active fortune doesnt have a damage die, return the knight's damage die, 
      otherwise return the active fortune's damage die */
      if(activeFortune.getDamageDie() == null){
        return super.getDamageDie();
      }
      else {
        return activeFortune.getDamageDie();
      }
      
    }
    //if no active fortune, return the knight's damage die
    return super.getDamageDie();
  }

  @Override
  public int getHitModifier(){
    if(activeFortune != null){
      hitModifier = super.getHitModifier() + activeFortune.getHitModifier();
      return hitModifier;
    }
    return super.getHitModifier();
  }

  @Override
  public int getMaxHP(){
    if(activeFortune != null){
      maxHP = super.getMaxHP() + activeFortune.getMaxHP();
      return maxHP;
    }
    return super.getMaxHP();
  }

  public String toCSV(){
    //Returns a Comma Separated value representation of the knight.
    return getName() + "," + getMaxHP() + "," + getArmor() + "," + getHitModifier() + "," + getDamageDie() + "," + getXP();
  }

  @Override
  public String toString(){
    //The toString for the knight, returns a 'knight card' examples of the card as as follows:
    return "+============================+\n" +
    String.format("| %-27s|%n", getName()) +
    String.format("| id: %-23d|%n", getId()) +
    "|                            |\n" +
    String.format("| Health: %-6d  XP: %-7d|%n", getHP(), getXP())  +
    String.format("|  Power: %-6s  Armor: %-4d|%n", getDamageDie().toString(), getArmor()) +
    "|                            |\n" +
    "+============================+";
  }

  public static void main(String[] args){
    Knight knight = new Knight(01,"Guinevere", 35,12,1,DiceType.D6,0);
    Fortune fortune = new Fortune("Valor",10,1,3,DiceType.D8);
    System.out.println("---TESTS IN KNIGHT BEFORE ACTIVE FORTUNE IS ADDED---");
    System.out.println("TESTING get Active Fortune in knight (should be null): " + knight.getActiveFortune());
    System.out.println("TESTING get armor in knight (should be 12): " + knight.getArmor());
    System.out.println("TESTING get damage die in knight (Should be 6): " + knight.getDamageDie());
    System.out.println("TESTING get hit modifier in knight (Should be 1): " + knight.getHitModifier());
    System.out.println("TESTING get max HP in knight (should be 35): " + knight.getMaxHP());
    System.out.println("--SETTING ACTIVE FORTUNE--");
    knight.setActiveFortune(fortune);
    System.out.println("TESTING get Active Fortune in knight (should be 'Valor'): ");
    System.out.println(knight.getActiveFortune());
    System.out.println("TESTING get Armor in knight (Should be 22): " + knight.getArmor());
    System.out.println("TESTING get damage die in knight (Should be 8): " + knight.getDamageDie());
    System.out.println("TESTING get hit modifier in knight (should be 2): " + knight.getHitModifier());
    System.out.println("TESTING get max HP in knight (should be 38): " + knight.getMaxHP());
    System.out.println("TESTING get id in knight: " + knight.getId());
    System.out.println("TESTING get XP in knight: " + knight.getXP());
    System.out.println("TESTING add XP in knight: " );
    knight.addXP(4);
    System.out.println("TESTING get XP in knight (Should be 4 now): " + knight.getXP());
    System.out.println("TESTING to CSV in knight: " + knight.toCSV());
    System.out.println("TESTING to string in knight: ");
    System.out.println(knight.toString());
  }
}