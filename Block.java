package riichain;
import java.util.Date;
import java.util.ArrayList;
import com.google.gson.annotations.Expose;


public class Block {
    @Expose
	public String hash;
    @Expose
	public String previousHash;
    @Expose
    public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //our data will be a simple message.
	
	private long timeStamp;
	private int nonce;
	
	public Block( String previousHash) {
		this.previousHash=previousHash;
		this.timeStamp=new Date().getTime();
		this.hash= calculateHash();
	}
	
	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256(previousHash+ Long.toString(timeStamp)+ Integer.toString(nonce)+ merkleRoot);
		return calculatedhash;
	}
	
	//Increases nonce value until hash target is reached.
		public void mineBlock(int difficulty) {
			merkleRoot = StringUtil.getMerkleRoot(transactions);
			String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0" 
			while(!hash.substring( 0, difficulty).equals(target)) {
				nonce ++;
				hash = calculateHash();
			}
			System.out.println("Block Mined!!! : " + hash);
		}
		
		//Add transactions to this block
		public boolean addTransaction(Transaction transaction) {
			//process transaction and check if valid, unless block is genesis block then ignore.
			if(transaction == null) return false;		
			if((previousHash != "0")) {
				if((transaction.processTransaction() != true)) {
					System.out.println("Transaction failed to process. Discarded.");
					return false;
				}
			}
			transactions.add(transaction);
			System.out.println("Transaction Successfully added to Block");
			return true;
		}
	
}
