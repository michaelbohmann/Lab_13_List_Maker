import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileListMaker {
    private static final ArrayList<String> itemList = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean needsToBeSaved = false;
    private static String currentFileName = "";

    public static void main(String[] args) {
        char choice;
        do {
            displayMenu();
            String userInput = SafeInput.getRegExString(scanner, "Enter your choice: ", "[AaDdVvCcSsOoQq]");
            choice = userInput.toUpperCase().charAt(0);
            switch (choice) {
                case 'A':
                    addItemToList();
                    break;
                case 'D':
                    deleteItemFromList();
                    break;
                case 'V':
                    printList();
                    break;
                case 'O':
                    openListFromFile();
                    break;
                case 'S':
                    saveListToFile();
                    break;
                case 'C':
                    clearList();
                    break;
                case 'Q':
                    if (confirmQuit()) {
                        if (needsToBeSaved) {
                            saveListToFile();
                        }
                        System.out.println("Exiting program...");
                        return;
                    }
                    break;
            }
        } while (choice != 'Q');
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("A - Add an item to the list");
        System.out.println("D - Delete an item from the list");
        System.out.println("V - View the list");
        System.out.println("O - Open a list file from disk");
        System.out.println("S - Save the current list file to disk");
        System.out.println("C - Clear the current list");
        System.out.println("Q - Quit");
    }

    private static void addItemToList() {
        String newItem = SafeInput.getNonZeroLenString(scanner, "Enter the item to add: ");
        itemList.add(newItem);
        needsToBeSaved = true;
        System.out.println("Item added successfully.");
    }

    private static void deleteItemFromList() {
        if (itemList.isEmpty()) {
            System.out.println("List is empty. Nothing to delete.");
            return;
        }
        System.out.println("Current items in the list:");
        printList();
        int itemIndex = SafeInput.getRangedInt(scanner, "Enter the number of the item to delete: ", 1, itemList.size());
        String deletedItem = itemList.remove(itemIndex - 1);
        System.out.println("Item '" + deletedItem + "' deleted successfully.");
        needsToBeSaved = true;
    }

    private static void printList() {
        if (itemList.isEmpty()) {
            System.out.println("List is empty.");
            return;
        }
        System.out.println("Current items in the list:");
        for (int i = 0; i < itemList.size(); i++) {
            System.out.println((i + 1) + ". " + itemList.get(i));
        }
    }

    private static void openListFromFile() {
        if (needsToBeSaved && !confirmSaveBeforeOperation("open a list from disk")) {
            return;
        }
        System.out.println("Enter the filename to open (including extension .txt):");
        String fileName = scanner.nextLine();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            itemList.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                itemList.add(line);
            }
            needsToBeSaved = false;
            currentFileName = fileName;
            System.out.println("List loaded successfully from " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void saveListToFile() {
        if (itemList.isEmpty()) {
            System.out.println("List is empty. Nothing to save.");
            return;
        }
        String fileName;
        if (currentFileName.isEmpty()) {
            System.out.println("Enter the filename to save (including extension .txt):");
            fileName = scanner.nextLine();
        } else {
            fileName = currentFileName;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String item : itemList) {
                writer.write(item);
                writer.newLine();
            }
            needsToBeSaved = false;
            currentFileName = fileName;
            System.out.println("List saved successfully to " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void clearList() {
        if (!itemList.isEmpty()) {
            itemList.clear();
            needsToBeSaved = true;
            System.out.println("List cleared.");
        } else {
            System.out.println("List is already empty.");
        }
    }

    private static boolean confirmQuit() {
        if (needsToBeSaved) {
            return confirmSaveBeforeOperation("quit the program");
        }
        return true;
    }

    private static boolean confirmSaveBeforeOperation(String operation) {
        System.out.println("Current list has unsaved changes. Do you want to save before " + operation + "? (Y/N)");
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("Y")) {
            saveListToFile();
            return true;
        } else if (response.equalsIgnoreCase("N")) {
            return true;
        } else {
            System.out.println("Invalid choice. Please enter Y or N.");
            return confirmSaveBeforeOperation(operation);
        }
    }
}