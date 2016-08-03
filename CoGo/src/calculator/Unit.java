package calculator;

public enum Unit {
	e_byte ("Byte", "B"),
	e_octet ("Octet", "o"),
	e_bit ("bit", "b");
	
	public String fullname;
	public String letter;
	
	Unit (String fullname, String letter) {
		this.fullname = fullname;
		this.letter = letter;
	}
}
