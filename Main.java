import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

class Block {
    private int index;
    private long timestamp;
    private String data;
    String previousHash; // Atributo de acesso modificado para permitir o acesso de Blockchain
    private String hash;
    private int nonce;

    // Construtor para criar um bloco
    public Block(int index, long timestamp, String data, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash(); // Calcula o hash do bloco
    }

    // Método para calcular o hash do bloco
    public String calculateHash() {
        String dataToHash = index + timestamp + data + previousHash + nonce;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dataToHash.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para minerar o bloco com um certo nível de dificuldade
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash(); // Recalcula o hash com um novo nonce
        }
        System.out.println("Block mined: " + hash);
    }

    // Método para obter o hash do bloco
    public String getHash() {
        return hash;
    }
}

class Blockchain {
    private List<Block> chain;
    private int difficulty;

    // Construtor para criar uma nova blockchain
    public Blockchain() {
        chain = new ArrayList<>();
        difficulty = 2; // Nível de dificuldade da mineração
        createGenesisBlock(); // Cria o bloco gênesis inicial
    }

    // Método privado para criar o bloco gênesis
    private void createGenesisBlock() {
        chain.add(new Block(0, System.currentTimeMillis(), "Genesis Block", "0"));
    }

    // Método para obter o último bloco na blockchain
    public Block getLastBlock() {
        return chain.get(chain.size() - 1);
    }

    // Método para adicionar um novo bloco à blockchain
    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty); // Minera o novo bloco
        newBlock.calculateHash(); // Recalcula o hash do novo bloco após a mineração
        newBlock.previousHash = getLastBlock().getHash(); // Define o hash anterior do novo bloco
        chain.add(newBlock); // Adiciona o novo bloco à blockchain
    }
}

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain(); // Cria uma nova instância da blockchain
        blockchain.addBlock(new Block(1, System.currentTimeMillis(), "Transaction Data 1", "")); // Adiciona o primeiro bloco à blockchain
        blockchain.addBlock(new Block(2, System.currentTimeMillis(), "Transaction Data 2", "")); // Adiciona o segundo bloco à blockchain
    }
}
