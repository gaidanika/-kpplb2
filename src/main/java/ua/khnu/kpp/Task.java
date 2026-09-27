package ua.khnu.kpp;
import java.time.LocalDate;
public final class Task {
    public Task(long id, String title, Project project, LocalDate created, LocalDate due, CompletionPolicy policy) { throw new UnsupportedOperationException("Implement domain contract"); }
    public long id() { throw new UnsupportedOperationException("Implement domain contract"); }
    public String title() { throw new UnsupportedOperationException("Implement domain contract"); }
    public Project project() { throw new UnsupportedOperationException("Implement domain contract"); }
    public LocalDate created() { throw new UnsupportedOperationException("Implement domain contract"); }
    public LocalDate due() { throw new UnsupportedOperationException("Implement domain contract"); }
    public Status status() { throw new UnsupportedOperationException("Implement domain contract"); }
    public User assignee() { throw new UnsupportedOperationException("Implement domain contract"); }
    public void assign(User user) { throw new UnsupportedOperationException("Implement domain contract"); }
    public void start() { throw new UnsupportedOperationException("Implement domain contract"); }
    public void complete(LocalDate date, int units, int defects, boolean approved) { throw new UnsupportedOperationException("Implement domain contract"); }
}
