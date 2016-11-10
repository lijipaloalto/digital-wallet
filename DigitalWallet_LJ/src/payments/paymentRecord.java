package payments;

public class paymentRecord {
	private String time;
	private String id1;
	private String id2;
	private double amount;
	
	public paymentRecord(String time, String id1, String id2, double amount) {
		this.setTime(time);
		this.setId1(id1);
		this.setId2(id2);
		this.setAmount(amount);
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getId1() {
		return id1;
	}

	public void setId1(String id1) {
		this.id1 = id1;
	}

	public String getId2() {
		return id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
