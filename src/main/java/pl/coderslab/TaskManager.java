package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

    static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    static final String FILE_NAME = "tasks.csv";
    static String[][] tasks;

    public static void main(String[] args) {
        selectOption(OPTIONS);
        tasks = loadDataFromFile(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String option = scanner.nextLine();
            switch (option) {
                case "exit":
                    saveTask(FILE_NAME, tasks);
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    System.exit(0);
                    break;
                case "add":
                    addTask();
                    break;
                case "remove":
                    deleteTask(tasks, getNumber());
                    break;
                case "list":
                    showTaskList(tasks);
                    break;
                default:
                    System.out.println("Select a correct option.");
            }
            selectOption(OPTIONS);
        }
    }

    public static void selectOption(String[] tab) {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option: " + ConsoleColors.RESET);
        for (String option : tab) {
            System.out.println(option);
        }
    }

    public static String[][] loadDataFromFile(String fileName) {
        String[][] tab = null;
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            System.out.println("File not exist!");
            System.exit(0);
        }
        try {
            List<String> loadedData = Files.readAllLines(path);
            tab = new String[loadedData.size()][loadedData.get(0).split(",").length];

            for (int i = 0; i < loadedData.size(); i++) {
                String[] split = loadedData.get(0).split(",");
                for (int j = 0; j < split.length; j++) {
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tab;
    }

    public static void showTaskList(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void addTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description");
        String description = scanner.nextLine();
        System.out.println("Please add task due date");
        String date = scanner.nextLine();
        System.out.println("Is the task important? (true/false?");
        String isImportant = scanner.nextLine();

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = date;
        tasks[tasks.length - 1][2] = isImportant;
    }

    public static boolean isNumber(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    public static int getNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select a number of task to delete it.");

        String number = scanner.nextLine();
        while (!isNumber(number)) {
            System.out.println("Incorrect input passed. Please give a number greater or equal 0.");
            scanner.nextLine();
        }
        return Integer.parseInt(number);
    }

    public static void deleteTask(String[][] tab, int number) {
        try {
            if (number < tab.length) {
                tasks = ArrayUtils.remove(tab, number);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Task with this number not exist.");
        }
    }

    public static void saveTask(String fileName, String[][] tab) {
        Path path = Paths.get(fileName);

        String[] lines = new String[tasks.length];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }
        try {
            Files.write(path, Arrays.asList(lines));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

