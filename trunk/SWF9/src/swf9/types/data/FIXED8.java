package swf9.types.data;

public class FIXED8 {
	
	private byte byteLeft;
	private byte byteRight;
	
	// TODO all!!!!!!
	public FIXED8() {
		
	}
	
	// ATTENTION only int NOW!!!!!
	public FIXED8(int value) {
		byteLeft = (byte)value;
		byteRight = 0;
	}

	public byte[] toByteArray() {
		//swapped!!!!!!!!
		byte[] bytes = {byteRight,byteLeft};
		return bytes;
	}
}
