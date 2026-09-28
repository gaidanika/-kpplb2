package ua.khnu.kpp;
import java.time.LocalDate;
public final class Task {

    private final long id;
    private final String title;
    private final Project project;
    private final LocalDate created;
    private final LocalDate due;
    private final CompletionPolicy policy;

    private Status status;
    private User assignee;

    public Task(long id, String title, Project project,
                LocalDate created, LocalDate due,
                CompletionPolicy policy) {

        if (id <= 0) {
            throw new IllegalArgumentException("id must be positive");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }

        if (project == null) {
            throw new IllegalArgumentException("project must not be null");
        }

        if (created == null) {
            throw new IllegalArgumentException("created must not be null");
        }

        if (due == null) {
            throw new IllegalArgumentException("due must not be null");
        }

        if (policy == null) {
            throw new IllegalArgumentException("policy must not be null");
        }

        if (due.isBefore(created)) {
            throw new IllegalArgumentException(
                    "due date must not be before created date");
        }

        this.id = id;
        this.title = title;
        this.project = project;
        this.created = created;
        this.due = due;
        this.policy = policy;

        this.status = Status.NEW;
        this.assignee = null;
    }

    public long id() {
        return id;
    }

    public String title() {
        return title;
    }

    public Project project() {
        return project;
    }

    public LocalDate created() {
        return created;
    }

    public LocalDate due() {
        return due;
    }

    public Status status() {
        return status;
    }

    public User assignee() {
        return assignee;
    }

    public void assign(User user) {
        if (status != Status.NEW) {
            throw new IllegalStateException(
                    "task can be assigned only in NEW state");
        }

        if (user == null) {
            throw new IllegalArgumentException(
                    "user must not be null");
        }

        this.assignee = user;
    }

    public void start() {
        if (status != Status.NEW) {
            throw new IllegalStateException(
                    "task can be started only from NEW state");
        }

        if (assignee == null) {
            throw new IllegalStateException(
                    "task cannot be started without an assignee");
        }

        this.status = Status.ACTIVE;
    }

    public void complete(LocalDate date, int units,
                         int defects, boolean approved) {

        if (status != Status.ACTIVE) {
            throw new IllegalStateException(
                    "task can be completed only from ACTIVE state");
        }

        if (date == null) {
            throw new IllegalArgumentException(
                    "completion date must not be null");
        }

        if (date.isBefore(created)) {
            throw new IllegalArgumentException(
                    "completion date must not be before created date");
        }

        if (units < 0) {
            throw new IllegalArgumentException(
                    "units must not be negative");
        }

        if (defects < 0) {
            throw new IllegalArgumentException(
                    "defects must not be negative");
        }

        if (!policy.allows(units, defects, approved)) {
            throw new IllegalStateException(
                    "completion policy does not allow completion");
        }

        this.status = Status.DONE;
    }
}

