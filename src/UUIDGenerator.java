import java.util.UUID;

public class UUIDGenerator {
    public String generateUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
