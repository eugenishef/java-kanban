import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class SubtaskTest {

  @Test
  public void testCreateSubtask() {
    Subtask subtask = new Subtask("Позвонить в тех. поддержку", "Узнать ФИО и номер курьера");

    assertNotNull(subtask.subtasks);
  }

  @Test
  public void testGetId() {
    Subtask subtask = new Subtask("Позвонить в тех. поддержку", "Узнать ФИО и номер курьера");

    assertNotNull(subtask.getId());
  }

  @Test
  public void testIsIdEqualsUUID() {
    Subtask subtask = new Subtask("Позвонить в тех. поддержку", "Узнать ФИО и номер курьера");

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
