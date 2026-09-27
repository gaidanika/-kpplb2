package ua.khnu.kpp;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class DomainTest {
    final LocalDate day = LocalDate.of(2026, 9, 10);
    User user() { return new User(1, "Olena"); }
    Project project() { return new Project(1, "Course", user()); }
    Task task(CompletionPolicy policy) {
        return new Task(1, "Read", project(), day, day.plusDays(2), policy);
    }
    Task task() { return task((u, d, a) -> a); }
    @Test void userFieldsAndIdentity() {
        User a=user(), b=new User(1,"Other"), c=new User(2,"Olena");
        assertEquals(1,a.id()); assertEquals("Olena",a.name());
        assertEquals(a,b); assertEquals(b,a); assertEquals(a,a);
        assertEquals(a.hashCode(),b.hashCode()); assertNotEquals(a,c);
        assertNotEquals(a,null); assertNotEquals(a,"Olena");
    }
    @Test void invalidUsers() {
        assertThrows(IllegalArgumentException.class,()->new User(0,"A"));
        assertThrows(IllegalArgumentException.class,()->new User(-1,"A"));
        for(String n:new String[]{null,"","  "})
            assertThrows(IllegalArgumentException.class,()->new User(1,n));
    }
    @Test void projectContract() {
        Project p=project(); assertEquals(1,p.id());
        assertEquals("Course",p.title()); assertEquals(user(),p.owner());
        assertThrows(IllegalArgumentException.class,()->new Project(0,"A",user()));
        for(String n:new String[]{null,""," "})
            assertThrows(IllegalArgumentException.class,()->new Project(1,n,user()));
        assertThrows(IllegalArgumentException.class,()->new Project(1,"A",null));
    }
    @Test void taskInitialState() {
        Task t=task(); assertEquals(1,t.id()); assertEquals("Read",t.title());
        assertEquals(1,t.project().id()); assertEquals(day,t.created());
        assertEquals(day.plusDays(2),t.due()); assertEquals(Status.NEW,t.status());
        assertNull(t.assignee());
    }
    @Test void invalidTasks() {
        Project p=project(); CompletionPolicy yes=(u,d,a)->true;
        assertThrows(IllegalArgumentException.class,()->new Task(0,"A",p,day,day,yes));
        for(String n:new String[]{null,""," "})
            assertThrows(IllegalArgumentException.class,()->new Task(1,n,p,day,day,yes));
        assertThrows(IllegalArgumentException.class,()->new Task(1,"A",null,day,day,yes));
        assertThrows(IllegalArgumentException.class,()->new Task(1,"A",p,null,day,yes));
        assertThrows(IllegalArgumentException.class,()->new Task(1,"A",p,day,null,yes));
        assertThrows(IllegalArgumentException.class,()->new Task(1,"A",p,day,day,null));
        assertThrows(IllegalArgumentException.class,()->new Task(1,"A",p,day,day.minusDays(1),yes));
        assertEquals(day,new Task(1,"A",p,day,day,yes).due());
    }
    @Test void assignmentAndStart() {
        Task t=task(); assertThrows(IllegalStateException.class,t::start);
        assertEquals(Status.NEW,t.status());
        assertThrows(IllegalArgumentException.class,()->t.assign(null));
        t.assign(user()); t.assign(new User(2,"Ihor"));
        assertEquals(2,t.assignee().id()); t.start();
        assertEquals(Status.ACTIVE,t.status());
        assertThrows(IllegalStateException.class,t::start);
        assertThrows(IllegalStateException.class,()->t.assign(user()));
        assertEquals(2,t.assignee().id());
    }
    @Test void normalCompletionAndTerminalState() {
        Task t=task(); t.assign(user()); t.start();
        t.complete(day,1,0,true); assertEquals(Status.DONE,t.status());
        assertThrows(IllegalStateException.class,t::start);
        assertThrows(IllegalStateException.class,()->t.assign(user()));
        assertThrows(IllegalStateException.class,()->t.complete(day,1,0,true));
        assertEquals(Status.DONE,t.status());
    }
    @Test void completionBeforeStart() {
        Task t=task(); assertThrows(IllegalStateException.class,()->t.complete(day,1,0,true));
        t.assign(user()); assertThrows(IllegalStateException.class,()->t.complete(day,1,0,true));
        assertEquals(Status.NEW,t.status());
    }
    @Test void datesAndInvalidEvidenceAreAtomic() {
        Task t=task(); t.assign(user()); t.start();
        assertThrows(IllegalArgumentException.class,()->t.complete(null,1,0,true));
        assertThrows(IllegalArgumentException.class,()->t.complete(day.minusDays(1),1,0,true));
        assertThrows(IllegalArgumentException.class,()->t.complete(day,-1,0,true));
        assertThrows(IllegalArgumentException.class,()->t.complete(day,1,-1,true));
        assertEquals(Status.ACTIVE,t.status()); assertEquals(user(),t.assignee());
        t.complete(day.plusDays(3),1,0,true); assertEquals(Status.DONE,t.status());
    }
    @Test void policyRefusalKeepsStateAndCanRetry() {
        Task t=task(); t.assign(user()); t.start();
        assertThrows(IllegalStateException.class,()->t.complete(day,1,0,false));
        assertEquals(Status.ACTIVE,t.status());
        t.complete(day,1,0,true); assertEquals(Status.DONE,t.status());
    }
    @Test void policyIsUsedWithExactArguments() {
        int[] calls={0};
        Task t=task((u,d,a)->{calls[0]++; return u==7 && d==2 && a;});
        t.assign(user()); t.start(); t.complete(day,7,2,true);
        assertEquals(1,calls[0]); assertEquals(Status.DONE,t.status());
    }
    @Test void selectedVariantEvidence() throws Exception {
        int variant=Integer.parseInt(Files.readString(Path.of("variant.txt")).trim());
        assertTrue(variant>=1 && variant<=10,"variant must be 1..10");
        CompletionPolicy policy=new VariantPolicy(); int count=0;
        try(var input=getClass().getResourceAsStream("/cases.csv")) {
            assertNotNull(input);
            for(String row:new String(input.readAllBytes(),java.nio.charset.StandardCharsets.UTF_8).split("\\R")) {
                String[] c=row.split(","); if(Integer.parseInt(c[0])!=variant) continue;
                int u=Integer.parseInt(c[1]),d=Integer.parseInt(c[2]);
                boolean a=Boolean.parseBoolean(c[3]),expected=Boolean.parseBoolean(c[4]);
                assertEquals(expected,policy.allows(u,d,a),row);
                Task t=task(policy); t.assign(user()); t.start();
                if(expected) { t.complete(day,u,d,a); assertEquals(Status.DONE,t.status()); }
                else { assertThrows(IllegalStateException.class,()->t.complete(day,u,d,a));
                    assertEquals(Status.ACTIVE,t.status()); }
                count++;
            }
        }
        assertTrue(count>=6,"missing variant data");
    }
}
