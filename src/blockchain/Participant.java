package blockchain;

import java.math.BigInteger;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Participant {
    String fullName;
    BigInteger accountBalance;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    HashMap<Participant, PublicKey> publicKeys = new HashMap<>();
    List<Ledger> ledgers = new ArrayList<>();
    private static final int KEY_SIZE = 1024;
    private static final BigInteger INIT_MONEY = new BigInteger("100");

    protected Participant(String f) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(KEY_SIZE);
        this.fullName = f;
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
        this.accountBalance = INIT_MONEY;
    }

    protected PrivateKey getPrivateKey() {
        return privateKey;
    }

    protected PublicKey getPublicKey() {
        return publicKey;
    }

    public String toString() {
        return this.fullName + " has " + this.accountBalance.toString() + " Geraltium";
    }

    protected List<String> getTransactionList() {
        List<String> desc = new ArrayList<>();

        this.ledgers.forEach(l -> l.transactions.forEach(t -> {
            if (t.sender.fullName.equals(this.fullName) || t.receiver.fullName.equals(this.fullName))
                desc.add(t.description);
        }));

        return desc;
    }

    protected void getSummary() {
        List<String> desc = getTransactionList();

        System.out.println(this.fullName);
        desc.forEach(System.out::println);
        System.out.println("Account balance: " + this.accountBalance + " Geraltium\n");
    }
}
