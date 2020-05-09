package blockchain;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class Transaction {
    Participant sender;
    Participant receiver;
    BigInteger value;
    String description;
    int id;
    String signature = "";

    protected Transaction(Participant s, Participant r, String v, int i) {
        BigInteger bi = new BigInteger(v);
        if (s.accountBalance.compareTo(bi) > 0) {
            this.sender = s;
            this.receiver = r;
            this.value = new BigInteger(v);
            this.id = i;
            this.description = this.sender.fullName + " sends " + this.receiver.fullName + " " + this.value.toString() + " Geraltium.";
            try {
                this.signature = this.sign();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected String sign() throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(this.sender.getPrivateKey());
        String message = this.description + this.id;
        privateSignature.update(message.getBytes(StandardCharsets.UTF_8));

        byte[] byteSign = privateSignature.sign();

        return Base64.getEncoder().encodeToString(byteSign);
    }

    protected boolean verify(PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        String message = this.description + this.id;
        publicSignature.update(message.getBytes(StandardCharsets.UTF_8));

        byte[] byteSign = Base64.getDecoder().decode(this.signature);

        return publicSignature.verify(byteSign);
    }
}
