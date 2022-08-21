package duke.command;

import duke.exception.DukeException;
import duke.storage.Storage;
import duke.task.Task;
import duke.task.TaskManager;
import duke.ui.Ui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents a Command class
 *
 * @author Khor Jun Wei
 * @version CS2103T AY22/23 Sem 1
 */
public abstract class Command {

    /**
     * Represents whether commands can still be made.
     */
    protected boolean ongoing;

    /**
     * Represents the type of commands the class understands.
     */
    public enum Action_keyword {
        DEADLINE,
        DELETE,
        EVENT,
        LIST,
        MARK,
        TODO,
        UNMARK,
    }

    /**
     * Represents the constructor method for Command class.
     */
    private Command() {
        this.ongoing = true;
    }

    /**
     * Creates Command class through a constructor method
     * @param s String of details
     * @return Command
     * @throws DukeException if any is found
     */
    public static Command of(String s) throws DukeException {
        String[] splitResponse = s.split(" ");
        String keyword = splitResponse[0];
        if (splitResponse.length == 1) {
            if (keyword.equals("todo") || keyword.equals("deadline") || keyword.equals("event") ||
                    keyword.equals("delete") || keyword.equals("mark") || keyword.equals("unmark")) {
                throw new DukeException(keyword);
            } else if (keyword.equals("list")) {
                return new ListCommand();
            } else if (keyword.equals("bye")) {
                return new ExitCommand();
            } else {
                throw new DukeException("unknown");
            }
        } else {
            switch (keyword) {
                case "todo":
                    return new AddCommand(Task.Task_type.TODO, s.substring(5), null);
                case "deadline": {
                    try {
                        String[] tempSplit = s.substring(9).split(" /by ");
                        if (tempSplit.length == 1) {
                            throw new DukeException("deadline format");
                        } else {
                            return new AddCommand(Task.Task_type.DEADLINE, tempSplit[0], LocalDate.parse(tempSplit[1]));
                        }
                    } catch (DateTimeParseException e) {
                        throw new DukeException("deadline format");
                    }
                }
                case "event": {
                    try {
                        String[] tempSplit = s.substring(6).split(" /at ");
                        if (tempSplit.length == 1) {
                            throw new DukeException("event format");
                        } else {
                            return new AddCommand(Task.Task_type.EVENT, tempSplit[0], LocalDate.parse(tempSplit[1]));
                        }
                    } catch (DateTimeParseException e) {
                        throw new DukeException("event format");
                    }
                }
                case "delete":
                    try {
                        int location = Integer.parseInt(s.substring(7)) - 1;
                        return new DeleteCommand(location);
                    } catch (NumberFormatException e) {
                        throw new DukeException("non integer input when deleting");
                    }

                case "mark":
                    try {
                        int location = Integer.parseInt(s.substring(5)) - 1;
                        return new MarkCommand(true, location);
                    } catch (NumberFormatException e) {
                        throw new DukeException("non integer input when marking");
                    }
                case "unmark":
                    try {
                        int location = Integer.parseInt(s.substring(7)) - 1;
                        return new MarkCommand(false, location);
                    } catch (NumberFormatException e) {
                        throw new DukeException("non integer input when marking");
                    }
                default:
                    throw new DukeException("unknown");
            }
        }
    }

    /**
     * Represents an Add Command class.
     */
    public static class AddCommand extends Command {

        /**
         * Represents the task type.
         */
        private final Task.Task_type task_type;

        /**
         * Represents what task has to be done.
         */
        private final String todo;

        /**
         * Represents the date of task, if any.
         */
        private final LocalDate by;

        /**
         * Creates Add Command Class through a constructor method.
         * @param type task type
         * @param todo task to be done
         * @param by date when task has to be done, if any
         */
        public AddCommand(Task.Task_type type, String todo, LocalDate by) {
            this.task_type = type;
            this.todo = todo;
            this.by = by;
        }

        /**
         * Executes task.
         * @param tasks list of tasks
         * @param ui user interface being used
         * @param storage place where text is stored
         */
        @Override
        public void execute(TaskManager tasks, Ui ui, Storage storage) {
            if (task_type == Task.Task_type.TODO) {
                Task task = Task.of(Task.Task_type.TODO, todo);
                tasks.addTask(task);
                ui.sendMessage(Action_keyword.TODO, task, tasks.numOfTasks(), null);
            } else if (task_type == Task.Task_type.DEADLINE) {
                Task task = Task.of(Task.Task_type.DEADLINE, todo + " /by " + by);
                tasks.addTask(task);
                ui.sendMessage(Action_keyword.DEADLINE, task, tasks.numOfTasks(), null);
            } else if (task_type == Task.Task_type.EVENT) {
                Task task = Task.of(Task.Task_type.EVENT, todo + " /at " + by);
                tasks.addTask(task);
                ui.sendMessage(Action_keyword.EVENT, task, tasks.numOfTasks(), null);
            }
        }
    }

    /**
     * Represents a Delete Command class.
     */
    public static class DeleteCommand extends Command {

        /**
         * Represents location of task in array of tasks.
         */
        private final int location;

        /**
         * Creates Delete Command through a constructor method.
         * @param location where the task is located
         */
        public DeleteCommand(int location) {
            this.location = location;
        }

        /**
         * Executes task.
         * @param tasks list of tasks
         * @param ui user interface being used
         * @param storage place where text is stored
         * @throws DukeException if it is found
         */
        @Override
        public void execute(TaskManager tasks, Ui ui, Storage storage) throws DukeException {
            try {
                Task task = tasks.removeTask(location);
                ui.sendMessage(Action_keyword.DELETE, task, tasks.numOfTasks(), null);
            } catch (IndexOutOfBoundsException e) {
                throw new DukeException("index out of bounds");
            }
        }

    }

    /**
     * Represents an Exit Command class.
     */
    public static class ExitCommand extends Command {

        /**
         * Creates Exit Command through a constructor method.
         */
        public ExitCommand() {
        }

        /**
         * Executes task.
         * @param tasks list of tasks
         * @param ui user interface being used
         * @param storage place where text is stored
         * @throws IOException if there is such an exception
         */
        @Override
        public void execute(TaskManager tasks, Ui ui, Storage storage) throws IOException {
            this.ongoing = false;
            String message = tasks.craftTextMessage();
            storage.editStorage(message);
            ui.sayBye();
        }
    }

    /**
     * Represents a Mark Command class.
     */
    public static class MarkCommand extends Command {

        /**
         * Represents whether the task is completed.
         */
        private final boolean isCompleted;

        /**
         * Represents the location of the task.
         */
        private final int location;

        /**
         * Creates a Mark Command class through a constructor method.
         * @param bool whether the task is completed
         * @param location where the task is located
         */
        public MarkCommand(boolean bool, int location) {
            this.isCompleted = bool;
            this.location = location;
        }

        /**
         * Executes task.
         * @param tasks list of tasks
         * @param ui user interface being used
         * @param storage place where text is stored
         * @throws DukeException if it is found
         */
        @Override
        public void execute(TaskManager tasks, Ui ui, Storage storage) throws DukeException {
            try {
                if (isCompleted) {
                    Task task = tasks.markTaskComplete(location);
                    ui.sendMessage(Action_keyword.MARK, task, tasks.numOfTasks(), null);
                } else {
                    Task task = tasks.markTaskIncomplete(location);
                    ui.sendMessage(Action_keyword.UNMARK, task, tasks.numOfTasks(), null);
                }
            } catch (IndexOutOfBoundsException e) {
                throw new DukeException("index out of bounds");
            }
        }

    }

    /**
     * Represents a List Command class.
     */
    public static class ListCommand extends Command {

        /**
         * Creates a List Command class through a constructor method.
         */
        public ListCommand() {
        }

        /**
         * Executes task.
         * @param tasks list of tasks
         * @param ui user interface being used
         * @param storage place where text is stored
         */
        @Override
        public void execute(TaskManager tasks, Ui ui, Storage storage) {
            String message = tasks.craftList();
            ui.sendMessage(Action_keyword.LIST, null,tasks.numOfTasks(), message);
        }
    }

    /**
     * Executes task.
     * @param tasks list of tasks
     * @param ui user interface being used
     * @param storage place where text is stored
     * @throws DukeException if it is found
     * @throws IOException if there is such an exception
     */
    public abstract void execute(TaskManager tasks, Ui ui, Storage storage) throws DukeException, IOException;

    /**
     * Checks if one can still give more commands
     * @return boolean
     */
    public boolean isExit() {
        return !ongoing;
    }
}
