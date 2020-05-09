package blockchain;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Market {
    List<Participant> participants;
    List<Ledger> blockChain = new ArrayList<>();
    private static final int MIN_TRANS = 4;
    private static final int MAX_TRANS = 7;
    private static final int LEDGERS = 10;
    private static final int MONEY = 20;

    protected Market(String part_file, String[] tran_files) throws Exception {
        this.participants = setParticipants(part_file);
        int proof = 0;

        spreadPublicKeys();

        for (int i = 0; i < tran_files.length; i++) {
            Ledger ledger = new Ledger(i, tran_files[i], proof, this.participants);
            proof = ledger.proofOfWork;
            this.filterLedger(ledger);
            this.blockChain.add(ledger);
            spreadLedger(ledger);
        }
    }

    protected static List<Participant> setParticipants(String file) throws Exception {
        Scanner scan = new Scanner(new File(file));
        List<Participant> parts = new ArrayList<>();

        while(scan.hasNextLine()) {
            Participant p = new Participant(scan.nextLine());
            parts.add(p);
        }

        return parts;
    }

    protected void spreadLedger(Ledger ledger) {
        this.participants.forEach(p -> p.ledgers.add(ledger));
    }

    protected void spreadPublicKeys() {
        this.participants.forEach(p1 -> this.participants.stream()
                .filter(p2 -> !p2.fullName.equals(p1.fullName))
                .forEach(p3 -> p3.publicKeys.put(p1, p1.getPublicKey())));
    }

    protected void filterLedger(Ledger ledger) throws Exception {
        int i;

        for (i = 0; i < ledger.transactions.size(); i++) {
            Participant p = findSender(ledger.transactions.get(i), this.participants);

            if (!ledger.transactions.get(i).verify(p.getPublicKey())) {
                ledger.transactions.remove(i);
                i--;
            }
        }
    }

    protected void getTheBestParticipants() {
        List<Participant> parts = this.participants.stream().sorted(
                (Participant p1, Participant p2) -> p2.accountBalance.compareTo(p1.accountBalance)
                ).collect(Collectors.toList());

        System.out.println("The best participants");
        parts.forEach(System.out::println);
        System.out.println();
    }

    protected static int drawTransactionNumber() {
        return new Random().nextInt(MAX_TRANS - MIN_TRANS + 1) + MIN_TRANS;
    }

    protected static int drawMoneyTransfer() {
        return new Random().nextInt(MONEY) + 1;
    }

    protected static String[] drawParticipants() throws Exception {
        Random random = new Random();
        List<Participant> parts = setParticipants("participants.csv");
        int partSize = parts.size();
        int n1 = random.nextInt(partSize), n2 = random.nextInt(partSize);

        String name1 = parts.get(n1).fullName, name2;

        while(n1 == n2)
            n2 = random.nextInt(partSize);

        name2 = parts.get(n2).fullName;

        return new String[] {name1,name2};
    }

    protected static String fillFile(int n) throws Exception {
        String file = "file"+n+".txt";
        FileWriter writer = new FileWriter(file);
        int transNumber = drawTransactionNumber();

        for (int i = 0; i < transNumber; i++) {
            String[] participants = drawParticipants();
            int money = drawMoneyTransfer();

            writer.write(participants[0]+";");
            writer.write(participants[1]+";");
            writer.write(money+";");

            if (i != transNumber-1)
                writer.write("\n");
        }

        writer.close();

        return file;
    }

    protected static String[] initFiles(int size) throws Exception {
        String[] files = new String[size];

        for (int i = 0; i < size; i++)
            files[i] = fillFile(i);

        return files;
    }

    protected static Participant findSender(Transaction t, List<Participant> parts) {
        int i = 0;
        String name = parts.get(i).fullName;

        while (!name.equals(t.sender.fullName)) {
            i++;
            name = parts.get(i).fullName;
        }

        return parts.get(i);
    }

    public static void main(String[] args) {
        try {
            String[] s = initFiles(LEDGERS); //generate data
            Market market = new Market("participants.csv", s);

            market.participants.forEach(Participant::getSummary);
            market.getTheBestParticipants();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
