package blockchain;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ledger {
    int id;
    int prevProof = 0;
    int proofOfWork;
    List<Transaction> transactions;
    private static final int ONES = 20;    //number of ones in the beginning of hash

    protected Ledger(int i, String t, int pP, List<Participant> parts) throws Exception {
        this.id = i;
        if (i != 0)
            this.prevProof = pP;
        this.transactions = setTransactions(t, parts);
        this.proofOfWork = setProof();
    }

    protected List<Transaction> setTransactions(String file, List<Participant> parts) throws Exception {
        List<Transaction> trans = new ArrayList<>();
        Scanner scan = new Scanner(new File(file));
        int i = 1;

        while (scan.hasNextLine()) {
            String[] fields = scan.nextLine().split(";");
            Participant p1 = parts.stream().filter(p -> p.fullName.equals(fields[0])).findAny().orElse(null);
            Participant p2 = parts.stream().filter(p -> p.fullName.equals(fields[1])).findAny().orElse(null);

            assert p1 != null && p2 != null;
            Transaction t = new Transaction(p1, p2, fields[2], i);
            i++;

            if (!t.signature.equals("")) {           //unauthorized transaction
                if (p1.accountBalance.subtract(t.value).compareTo(new BigInteger("0")) > 0) {
                    p1.accountBalance = p1.accountBalance.subtract(t.value);
                    p2.accountBalance = p2.accountBalance.add(t.value);
                    trans.add(t);
                }
            }
        }

        return trans;
    }

    protected int setProof() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        int possibleProof = 1;
        StringBuilder message = new StringBuilder();
        StringBuilder controlChain = new StringBuilder();

        controlChain.append("1".repeat(ONES));

        message.append(possibleProof);
        for (var t : this.transactions)
            message.append(t.description);

        String hash = new BigInteger(md.digest(message.toString().getBytes(StandardCharsets.UTF_8))).toString(2);

        while(!hash.substring(0,ONES).equals(controlChain.toString())) {
            possibleProof++;

            message = new StringBuilder();
            message.append(possibleProof);
            for (var t : this.transactions)
                message.append(t.description);

            hash = new BigInteger(md.digest(message.toString().getBytes(StandardCharsets.UTF_8))).toString(2);
        }

        return possibleProof;
    }
}