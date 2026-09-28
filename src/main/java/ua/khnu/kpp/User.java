package ua.khnu.kpp;
import java.util.Objects;
public final class User {
    private final long id;
    private final String name;
    public User(long id, String name) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be positive");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }

        this.id = id;
        this.name = name;
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof User)) {
            return false;
        }

        User otherUser = (User) other;
        return id == otherUser.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
