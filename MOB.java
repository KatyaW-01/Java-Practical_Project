//MOBS are Mobile Objects
//Most notably monsters, but knights inherit from MOB
public class MOB implements Attributes{
  protected int armor;
  protected int damage = 0;
  protected DiceType damageDie;
  protected int hitModifier;
  protected int maxHP;
  private final String name;

  public MOB(String name, int hp, int armor, int hitModifier, DiceType damageDie){
    this.name = name;
    this.maxHP = hp;
    this.armor = armor;
    this.hitModifier = hitModifier;
    this.damageDie = damageDie;
  }

  @Override
  public int getArmor(){
    return armor;
  }

  public int getDamage(){
    return damage;
  }

  @Override
  public DiceType getDamageDie(){
    return damageDie;
    //returns a DiceType often ranging from d4-d12
  }

  @Override
  public int getHitModifier(){
    return hitModifier;
  }

  @Override
  public int getMaxHP(){
    return maxHP;
  }

  public int getHP(){
    //returns the current HPs of the MOB
    //damage done subtracted from the maxHP
    return maxHP - damage;
  }

  public String getName(){
    return name;
  }

  public MOB copy(){
    //Copies the mob to a new mob
    //does not care about any damage taken
    MOB mobCopy = new MOB(name,maxHP,armor,hitModifier,damageDie);
    return mobCopy;
  }

  public void addDamage(int damage){
    this.damage += damage;
    //Adds damage to the mobs overall damage
  }

  public void resetDamage(){
    damage = 0;
    //resets the damage taken to 0
  }

  @Override
  public String toString(){
    //builds a MOB card for easy printing of the stats
    return "+============================+\n" +
    String.format("| %-27s|%n", getName()) +
    "|                            |\n" +
    String.format("|         Health: %-10d |%n", getHP())  +
    String.format("|  Power: %-6s  Armor: %-4d|%n", getDamageDie().toString(), getArmor()) +
    "|                            |\n" +
    "+============================+";
  }

  public static void main(String[] args){
    MOB mob = new MOB("Red Dragon",120,17,4,DiceType.D12);
    System.out.println("TESTING Armor in mob: " + mob.getArmor());
    System.out.println("TESTING Damage in mob: " + mob.getDamage());
    System.out.println("TESTING Damage Die in mob: " + mob.getDamageDie());
    System.out.println("TESTING Hit Modifier in mob: " + mob.getHitModifier());
    System.out.println("TESTING Max HP in mob: " + mob.getMaxHP());
    System.out.println("TESTING HP in mob: " + mob.getHP());
    System.out.println("TESTING Name in mob: " + mob.getName());
    System.out.println("TESTING copy in mob: ");
    System.out.println(mob.copy());
    System.out.println("TESTING add Damage in mob: " );
    mob.addDamage(3);
    System.out.println("Damage in mob after add damage (should be 3): " + mob.getDamage());
    System.out.println("TESTING reset Damage in mob: " );
    mob.resetDamage();
    System.out.println("Damage in mob after reset (should be 0): " + mob.getDamage());
    System.out.println("TESTING to String in mob: ");
    System.out.println(mob.toString());
  }
}