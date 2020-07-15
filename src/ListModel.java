import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 * ListModel contains the core calculations, sorting, and
 * organization of Commands in the ArrayList of Commands
 *
 * @author Evan Johns
 * @version 0.3
 * @since 2020-7-14
 */
public class ListModel extends AbstractTableModel {

  // TODO: FIX ADDING COMMAND WHILE DISPLAY = SEARCHING

  private ArrayList<Command> list;
  private ArrayList<Command> filteredList;

  private ScreenDisplay display;

  public String[] columnNames = {"Name", "Description"};

  public ListModel() {
    list = new ArrayList<>();
    filteredList = new ArrayList<>();

    display = ScreenDisplay.DefaultDisplay;

    loadList();
    sort();
  }

  /**
   * Sorts either the filteredList or list depending on the current ScreenDisplay
   */
  public void sort() {
    switch (display) {
      case DefaultDisplay:
        list.sort(Comparator.comparing(Command::getName, String.CASE_INSENSITIVE_ORDER));
        fireTableStructureChanged();
      case SearchingDisplay:
        filteredList.sort(Comparator.comparing(Command::getName, String.CASE_INSENSITIVE_ORDER));
        fireTableStructureChanged();
    }

  }

  /**
   * Searches the list for a given string, then puts all commands that are similar to the string
   * into the filteredList
   * @param str Used to be compared to list of commands
   */
  public void search(String str) {
    filteredList.clear();

    str = str.toLowerCase();

    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getName().toLowerCase().contains(str) || list.get(i).getDescription()
          .toLowerCase().contains(str)) {
        filteredList.add(list.get(i));
      }

    }
    display = ScreenDisplay.SearchingDisplay;
    sort();
    fireTableStructureChanged();
  }

  /**
   * Adds a command to either ArrayList depending on ScreenDisplay
   * @param command Command to be added
   */
  public void add(Command command) {
    switch (display) {
      case DefaultDisplay:
        list.add(command);
        sort();
        break;
      case SearchingDisplay:
        filteredList.add(command);
        sort();
        break;
      default:
        throw new RuntimeException("[ERROR] : Not valid display type : " + display);
    }
  }

  /**
   * Removes a command from either ArrayList depending on ScreenDisplay
   * @param i Index to Command to be removes
   * @return Command Removed Command
   */
  public Command remove(int i) {
    Command temp;
    switch (display) {
      case DefaultDisplay:
        temp = list.get(i);
        list.remove(i);
        return temp;
      case SearchingDisplay:
        temp = filteredList.get(i);
        filteredList.remove(i);
        return temp;
      default:
        throw new RuntimeException("[ERROR] : Not valid display type : " + display);
    }
  }

  /**
   * Returns a command from either ArrayList depending on the ScreenDisplay
   * @param i Index to Command to be returned
   * @return Command Command being returned
   */
  public Command get(int i) {
    switch (display) {
      case DefaultDisplay:
        return list.get(i);
      case SearchingDisplay:
        return filteredList.get(i);
      default:
        throw new RuntimeException("[ERROR] : Not valid display type : " + display);
    }
  }

  public void setDisplay(ScreenDisplay display) {
    this.display = display;
    fireTableStructureChanged();
  }

  public ScreenDisplay getDisplay() {
    return display;
  }

  /**
   * Loads the default list
   */
  public void loadList() {
    int c = 0;
    try {
      File file = new File("C:/Users/evanl/IdeaProjects/TerminalCommands/src/cmds.txt");
      Scanner scnr = new Scanner(file);
      while (scnr.hasNextLine()) {
        String in = scnr.nextLine();
        String[] data = in.split(", ");
        add(new Command(data[0], data[1]));
        c++;
      }
      scnr.close();
    } catch (FileNotFoundException e) {
      System.out.println("File read error occurred");
      e.printStackTrace();
    } catch (IndexOutOfBoundsException e) {
      System.out.println("[ERROR] : No delimiter found at line : " + c);
    }
  }

  /**
   * Loads a text file containing Commands
   * @param filePath Path and name of given file
   */
  public void loadFile(String filePath) {
    list.clear();
    filteredList.clear();
    display = ScreenDisplay.DefaultDisplay;

    String line = null;
    String[] lineArray;

    try {
      FileReader file = new FileReader(filePath);
      BufferedReader reader = new BufferedReader(file);

      while ((line = reader.readLine()) != null) {
        lineArray = line.split(", ");
        add(new Command(lineArray[0], lineArray[1]));
      }
      sort();
      reader.close();
    } catch (Exception e) {
      throw new RuntimeException("[ERROR] : Loading file problem : " + e);
    }
  }

  /**
   * Saves the current text file to a given path
   * @param filePath Path and name of file to save
   */
  public void saveFile(String filePath) {
    try {
      FileOutputStream file = new FileOutputStream(filePath);
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(file));

      for (int i = 0; i < list.size(); i++) {
        writer.write(list.get(i).getName() + ", ");
        writer.write(list.get(i).getDescription());
        writer.newLine();
      }
      writer.close();
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getColumnName(int col) {
    return columnNames[col];
  }

  @Override
  public int getRowCount() {
    switch (display) {
      case DefaultDisplay:
        return list.size();
      case SearchingDisplay:
        return filteredList.size();
      default:
        throw new RuntimeException("[ERROR] : Not valid display type : " + display);
    }
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (display) {
      case DefaultDisplay:
        switch (columnIndex) {
          case 0:
            return list.get(rowIndex).getName();
          case 1:
            return list.get(rowIndex).getDescription();
          default:
            throw new RuntimeException(
                "[ERROR] : Row or Column are out of range : " + rowIndex + " " + columnIndex);
        }
      case SearchingDisplay:
        switch (columnIndex) {
          case 0:
            return filteredList.get(rowIndex).getName();
          case 1:
            return filteredList.get(rowIndex).getDescription();
          default:
            throw new RuntimeException(
                "[ERROR] : Row or Column are out of range : " + rowIndex + " " + columnIndex);
        }
      default:
        throw new RuntimeException("[ERROR] : Not valid display type : " + display);
    }
  }
}
