package blockchain;

import org.junit.jupiter.api.Test;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    @Test
    void testToString() throws NoSuchAlgorithmException {
        Participant p = new Participant("Alexios");
        assertEquals("Alexios has 100 Geraltium", p.toString());
    }
}