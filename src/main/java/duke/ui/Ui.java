package duke.ui;

import java.util.Scanner;

import duke.command.Command;
import duke.task.Task;

/**
 * Represents a User interface class
 *
 * @author Khor Jun Wei
 * @version CS2103T AY22/23 Sem 1
 */
public class Ui {

    /**
     * Represents an indentation for replies.
     */
    private static final String INDENTATION = "     ";

    /**
     * Represents an extra indentation for replies.
     */
    private static final String EXTRA_INDENTATION = "  ";

    /**
     * Represents a scanner.
     */
    private final Scanner scanner;

    /**
     * Constructs a UI class through a constructor
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Formats given string message.
     * @param s String message
     * @return formatted string message
     */
    private String formatMessage(String s) {
        return INDENTATION + s;
    }

    /**
     * Greets the user.
     */
    public void showWelcome() {
        showLine();
        System.out.println(formatMessage("Hello! I'm Duke\n" +
                "     What can I do for you?"));
        showLine();
    }

    /**
     * Says bye to the user.
     */
    public void sayBye() {
        System.out.println(formatMessage("Bye. Hope to see you again soon!"));
    }

    /**
     * Reads the command given by the user.
     * @return String representing the command
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows a line as part of text formatting.
     */
    public void showLine() {
        System.out.println("    ____________________________________________________________");
    }

    /**
     * Shows error in a formatted way.
     * @param s
     */
    public void showError(String s) {
        System.out.println(formatMessage(s));
    }

    /**
     * Sends a message to user.
     * @param keyword Type of command
     * @param task a task for the message, if required
     * @param numOfTasks number of tasks in task list, if required
     * @param message additional details for the message, if required
     */
    public void sendMessage(Command.Action_keyword keyword, Task task, int numOfTasks, String message) {
        if (keyword == Command.Action_keyword.DEADLINE || keyword == Command.Action_keyword.TODO || keyword == Command.Action_keyword.EVENT) {
            System.out.println(formatMessage("Got it. I've added this task:\n" +
                    INDENTATION + EXTRA_INDENTATION + task + "\n" +
                    INDENTATION + "Now you have " + numOfTasks + (numOfTasks < 2 ? " task" : " tasks") + " in the list."));
        } else if (keyword == Command.Action_keyword.DELETE) {
            System.out.println(formatMessage("Noted. I've removed the task:\n" +
                    INDENTATION + EXTRA_INDENTATION + task + "\n" +
                    INDENTATION + "Now you have " + numOfTasks + (numOfTasks < 2 ? " task" : " tasks") + " in the list."));
        } else if (keyword == Command.Action_keyword.LIST) {
            if (message.equals("")) {
                System.out.println(formatMessage("There are currently no tasks in your list"));
            } else {
                System.out.println(formatMessage("Here are the task(s) in your list:\n" +
                        INDENTATION + message));
            }
        } else if (keyword == Command.Action_keyword.MARK) {
            System.out.println(formatMessage("Nice! I've marked this task as done:\n" +
                    INDENTATION + EXTRA_INDENTATION + task));
        } else if (keyword == Command.Action_keyword.UNMARK) {
            System.out.println(formatMessage("OK, I've marked this task as not done yet:\n" +
                    INDENTATION + EXTRA_INDENTATION + task));
        } else if (keyword == Command.Action_keyword.FIND) {
            if (message.equals("")) {
                System.out.println(formatMessage("Sorry, there are matching tasks in your list"));
            } else {
                System.out.println(formatMessage("Here are the matching tasks in your list:\n" +
                        INDENTATION + message));
            }
        }
    }
}
