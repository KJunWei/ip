package duke.task;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Class that manages tasks.
 *
 * @author Khor Jun Wei
 * @version CS2103T AY22/23 Sem 1
 */
public class TaskManager {

    /**
     * Represents a file writer.
     */
    private final FileWriter fw;

    /**
     * Represents an array list of tasks.
     */
    private final ArrayList<Task> arr;

    /**
     * Represents an indentation for replies.
     */
    private static final String INDENTATION = "     ";

    /**
     * Constructor for Task Manager.
     * @param fw file writer
     */
    public TaskManager(FileWriter fw) {
        this.arr = new ArrayList<>(100);
        this.fw = fw;
    }

    /**
     * Crafts a list of tasks.
     *
     * @return String describing the list
     */
    public String craftList() {
        int length = arr.size();
        String result = "";
        for (int x = 0; x < length; x++) {
            Task task = arr.get(x);
            if (task == null) {
                break;
            } else {
                if (x == 0) {
                    result += (x + 1) + "." + task;
                } else {
                    result += "\n" + INDENTATION + (x + 1) + "." + task;
                }
            }
        }
        return result;
    }

    /**
     * Finds tasks that contain a certain string.
     * @param s String
     * @return message shows the matching tasks
     */
    public String findAndCraft(String s) {
        List<Task> filteredList = arr.stream()
                .filter(x -> x.contains(s))
                .collect(Collectors.toList());
        int counter = 1;
        String result = "";
        for (int i = 0; i < filteredList.size(); i++) {
            Task task = filteredList.get(i);
            if (task.contains(s)) {
                if (counter == 1) {
                    result += counter + "." + task;
                } else {
                    result += "\n" + INDENTATION + counter + "." + task;
                }
                counter++;
            }
        }
        return result;
    }

    /**
     * Crafts a message to be added into the file
     *
     * @return String representing the message
     */
    public String craftTextMessage() {
        int length = arr.size();
        String result = "";
        for (int x = 0; x < length; x++) {
            Task task = arr.get(x);
            if (task == null) {
                break;
            } else {
                if (x == 0) {
                    result += task.textFileMessage();
                } else {
                    result += "\n" + task.textFileMessage();
                }
            }
        }
        return result;
    }

    /**
     * Adds task into array of tasks.
     * @param task given task
     */
    public void addTask(Task task) {
        arr.add(task);
    }

    /**
     * Removes a task.
     * @param location where the task is located
     * @return Task
     */
    public Task removeTask(int location) {
        assert location >= 0 : "the task number should not be negative";
        Task task = arr.get(location);
        arr.remove(location);
        return task;
    }

    /**
     * Represents the number of task is the task list.
     * @return number of tasks
     */
    public int numOfTasks() {
        return arr.size();
    }

    /**
     * Marks a task as completed.
     * @param location where the task is located
     * @return Task
     */
    public Task markTaskComplete(int location) {
        assert location >= 0 : "the task number should not be negative";
        Task task = arr.get(location);
        task.markComplete();
        return task;
    }

    /**
     * Marks a task as incomplete.
     * @param location where the task is located
     * @return Task
     */
    public Task markTaskIncomplete(int location) {
        assert location >= 0 : "the task number should not be negative";
        Task task = arr.get(location);
        task.markIncomplete();
        return task;
    }
}
