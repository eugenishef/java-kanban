import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

public class SubtaskTest {

  @Test
  public void createSubtask() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number", startTime, durationMinutes);

    assertNotNull(subtask.subtasks);
  }

  @Test
  public void getId() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number", startTime, durationMinutes);

    assertNotNull(subtask.getId());
  }

  @Test
  public void isIdEqualsUUID() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number", startTime, durationMinutes);

    assertTrue(isValidUIID(subtask.getId()));
  }

  private boolean isValidUIID(String uuid) {
    try {
      UUID.fromString(uuid);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
