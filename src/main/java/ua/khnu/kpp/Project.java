package ua.khnu.kpp;
public final class Project {
    private final long id;
    private final String title;
    private final User owner;
    public Project(long id, String title, User owner) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be positive");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }

        if (owner == null) {
            throw new IllegalArgumentException("owner must not be null");
        }

        this.id = id;
        this.title = title;
        this.owner = owner;
    }

    public long id() {
        return id;
    }

    public String title() {
        return title;
    }

    public User owner() {
        return owner;
    }
}

