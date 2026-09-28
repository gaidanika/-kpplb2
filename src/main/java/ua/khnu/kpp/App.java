package ua.khnu.kpp;

import java.time.LocalDate;

public class App {
    public static void main(String[] args) {
        LocalDate created = LocalDate.of(2026, 9, 10);
        LocalDate due = LocalDate.of(2026, 9, 12);
        LocalDate completeDate = LocalDate.of(2026, 9, 11);

        User user = new User(1, "Nika");
        Project project = new Project(1, "Video Lessons Course", user);
        CompletionPolicy policy = new VariantPolicy();
        Task task = new Task(1, "Record lessons", project, created, due, policy);

        System.out.println("--- Initial state ---");
        System.out.println("Task status: " + task.status());

        task.assign(user);
        task.start();
        
        System.out.println("\n--- After assignment and start ---");
        System.out.println("Task status: " + task.status());

        System.out.println("\n--- Completion attempt (2 lessons, approved) ---");
        try {
            task.complete(completeDate, 2, 0, true);
        } catch (IllegalStateException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
        
        System.out.println("Status after failure: " + task.status());

        System.out.println("\n--- Completion attempt (3 lessons, approved) ---");
        task.complete(completeDate, 3, 0, true);
        
        System.out.println("Status after success: " + task.status());
    }
}