import java.util.ArrayList;
import java.util.Scanner;

public class ListMaker {
    private static final ArrayList<String> itemList = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        char choice;
        do {
            displayMenu();
            String userInput = SafeInput.getRegExString(scanner, "Enter your choice: ", "[AaDdPpQq]");
            choice = userInput.toUpperCase().charAt(0);
            switch (choice) {
                case 'A':
                    addItemToList();
                    break;
                case 'D':
                    deleteItemFromList();
                    break;
                case 'P':
                    printList();
                    break;
                case 'Q':
                    if (confirmQuit()) {
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
        System.out.println("P - Print the list");
        System.out.println("Q - Quit");
    }

    private static void addItemToList() {
        String newItem = SafeInput.getNonZeroLenString(scanner, "Enter the item to add: ");
        itemList.add(newItem);
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

    private static boolean confirmQuit() {
        return SafeInput.getYNConfirm(scanner, "Are you sure you want to quit? (Y/N)");
    }
}