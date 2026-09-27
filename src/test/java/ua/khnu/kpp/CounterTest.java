package ua.khnu.kpp;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CounterTest {
    @Test void increments() {
        Counter c = new Counter(2); c.increment(); assertEquals(3, c.value());
    }
    @Test void rejectsInvalidAndOverflow() {
        assertThrows(IllegalArgumentException.class, () -> new Counter(-1));
        Counter c = new Counter(Integer.MAX_VALUE);
        assertThrows(IllegalStateException.class, c::increment);
        assertEquals(Integer.MAX_VALUE, c.value());
    }
}
