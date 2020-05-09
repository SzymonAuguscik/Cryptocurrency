package blockchain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.security.NoSuchAlgorithmException;

class TransactionTest {
    Participant p1 = new Participant("Alexios");
    Participant p2 = new Participant("Kassandra");

    Transaction t = new Transaction(p1, p2, "10", 1);

    TransactionTest() throws NoSuchAlgorithmException {
    }

    @Test
    void signAndVerify() throws Exception {
        assertTrue(t.verify(p1.getPublicKey()));
        assertFalse(t.verify(p2.getPublicKey()));
    }
}