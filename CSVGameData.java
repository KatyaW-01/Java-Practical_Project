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
        ++ counter,
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
    //loop through every knight and print out the result of toCSV() for each knight
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
   
  }
}