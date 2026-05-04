import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class CSVGameData extends GameData {
  public CSVGameData(String gameData, String saveData){
    loadGameData(gameData);
    loadSaveData(saveData);
  }
  
  public void loadSaveData(String saveData){
    int counter = 0;
    Scanner file = readFile(saveData);
    if(file == null) return;
    while(file.hasNextLine()){
      Scanner line = new Scanner(file.nextLine());
      line.useDelimiter(",");
      Knight kt = new Knight(
        ++counter,
        line.next().trim(),
        line.nextInt(),
        line.nextInt(),
        line.nextInt(),
        DiceType.valueOf(line.next()),
        line.nextInt()
      );
      knights.add(kt);
      line.close();
    }
    file.close();
  }

  public void loadGameData(String gameData){
    Scanner file = readFile(gameData);
    if(file == null) return;
    while(file.hasNextLine()){
      Scanner line = new Scanner(file.nextLine());
      line.useDelimiter(",");
      parseGameDataLine(line);
      line.close();
    }
    file.close();
  }

  private Scanner readFile(String fileName){
    try{
      FileInputStream file = new FileInputStream(fileName);
      Scanner fileRead = new Scanner(file);
      return fileRead;
    }
    catch(FileNotFoundException e){
      System.out.println("File " + fileName + " cannot be found.");
      return null;
    }
  }

  private void parseGameDataLine(Scanner line){
    String type = line.next().trim().toLowerCase();
    if(type.equals("fortune")){
      String name = line.next().trim();
      int a = line.nextInt();
      int b = line.nextInt();
      int c = line.nextInt();
      String dice = line.next().trim();
      Fortune ftn = new Fortune(
        name,
        a,
        b,
        c,
        dice.equals("-") ? null : DiceType.valueOf(dice)
      );
      fortunes.add(ftn);
    }
    else if(type.equals("mob")){
      MOB monster = new MOB(
        line.next().trim(),
        line.nextInt(),
        line.nextInt(),
        line.nextInt(),
        DiceType.valueOf(line.next().trim())
      );
      monsters.add(monster);
    }
  }

  @Override
  public void save(String filename){
    //saves knight data as a CSV to the given filename
    try{
      FileOutputStream fileStream = new FileOutputStream(filename);
      PrintWriter out = new PrintWriter(fileStream);
      for(int i = 0; i < knights.size(); ++i){
        out.println(knights.get(i).toCSV());
      }
      out.close();
      System.out.println("Progress saved!");
    }
    catch(FileNotFoundException e){
      System.out.println("Error: unable to save data");
    }
  }

  public static void main(String[] args){
    CSVGameData data = new CSVGameData("gamedata.csv", "knights.csv");
    //Get all knights
    System.out.println("TESTING get knights: ");
    System.out.println(data.getKnights());
    //get all active knights
    System.out.println("TESTING get active knights: ");
    System.out.println(data.getActiveKnights());
    //setting and removing active knights
    Knight knight = data.getKnight("Gwain");
    Knight morrigan = data.getKnight("Morrigan");
    System.out.println("TESTING set active knight (returns true if successful): " + data.setActive(knight) + " " + data.setActive(morrigan));
    System.out.println("TESTING get active knights (Should now include Gwain + Morrigan): ");
    System.out.println(data.getActiveKnights());
    System.out.println("TESTING remove active knight: ");
    data.removeActive(morrigan);
    System.out.println(data.getActiveKnights());
    System.out.println("TESTING get active: ");
    System.out.println(data.getActive("14"));
    //adding a fifth active knight
    Knight eriu = new Knight(05,"Eriu",21,13,2,DiceType.D4,0);
    Knight danu = new Knight(06,"Danu of Ireland",40,16,1,DiceType.D6,0);
    System.out.println(data.setActive(eriu));
    System.out.println(data.setActive(danu));
    System.out.println(data.setActive(morrigan));
    Knight fodla = new Knight(07,"Fodla",25,10,2,DiceType.D8,0);
    System.out.println("TESTING adding a fifth active knight(should return false): " + data.setActive(fodla));
    System.out.println(data.getActiveKnights());
    //get random fortune
    System.out.println("TESTING get random fortune: ");
    System.out.println("first fortune:");
    System.out.println(data.getRandomFortune());
    System.out.println("second fortune:");
    System.out.println(data.getRandomFortune());
    //get random monsters
    System.out.println("TESTING get random monsters: ");
    System.out.println(data.getRandomMonsters());
    // data.getKnight("danu").addXP(3);
    // data.save("knights.csv");
  }
}