import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class SubtaskTest {

  @Test
  public void createSubtask() {
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number");

    assertNotNull(subtask.subtasks);
  }

  @Test
  public void getId() {
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number");

    assertNotNull(subtask.getId());
  }

  @Test
  public void isIdEqualsUUID() {
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number");

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
