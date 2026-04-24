public class Fortune implements Attributes{
  private final int armor;
  private final DiceType dtype;
  private final int hitModifier;
  private final int hpBonus;
  private final String name;

  public Fortune(String name, int armor, int hitModifier, int hpBonus, DiceType dtype){
    this.name = name;
    this.armor = armor;
    this.hitModifier = hitModifier;
    this.hpBonus = hpBonus;
    this.dtype = dtype;
  }

  public Fortune(String name, int armor, int hitModifier, int hpBonus){
    this.name = name;
    this.armor = armor;
    this.hitModifier = hitModifier;
    this.hpBonus = hpBonus;
    this.dtype = null;
  }

  @Override
  public int getArmor(){
    return armor;
  }

  @Override
  public DiceType getDamageDie(){
    return dtype;
  }

  @Override
  public int getHitModifier(){
    return hitModifier;
  }

  @Override
  public int getMaxHP(){
    return hpBonus;
  }

  public String getName(){
    return name;
  }

  @Override
  public String toString(){
    return "+======================+\n" +
    String.format("|%-22s|%n", getName()) +
    String.format("|HP Bonus: %12s|%n", "+" + getMaxHP()) +
    String.format("|AC Bonus: %12s|%n", "+" + getArmor()) +
    String.format("|Hit Bonus: %11s|%n", "+" + getHitModifier()) +
    String.format("|Damage Adj: %10s|%n", getDamageDie() != null ? getDamageDie() : "-") +
    "+======================+";
  }

  public static void main(String[] args){
    Fortune ftn = new Fortune("Merlin Luck", 10, 5, 2, DiceType.D12);
    System.out.println("TESTING Armor in fortune: " + ftn.getArmor());
    System.out.println("TESTING Damage Die in fortune: " + ftn.getDamageDie());
    System.out.println("TESTING Hit Modifier in fortune: " + ftn.getHitModifier());
    System.out.println("TESTING HP in fortune: " + ftn.getMaxHP());
    System.out.println("TESTING Name in fortune: " + ftn.getName());
    System.out.println("TESTING to String in fortune: ");
    System.out.println(ftn.toString());
  }
}