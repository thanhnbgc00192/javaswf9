package swf9.types;
/**
 * Copyright (c) 2008  Stefano Cottafavi and .:: A Quarter to Seven ::.
 * 
 * Some rights reserved.
 * 
 * Licensed under the CREATIVE COMMONS Attribution-Noncommercial-Share Alike 3.0
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at: http://creativecommons.org/licenses/by-nc-sa/3.0/us/
 * 
 * Please visit http://www.stefanocottafavi.com for more information
**/

public class CXFORM {

	public int HasAddTerms;
	public int HasMultiTerms;
	public int Nbits;
	public int RedMultiTerm;
	public int GreenMultiTerm;
	public int BlueMultiTerm;
	public int RedAddTerm;
	public int GreenAddTerm;
	public int BlueAddTerm;
	
	public int length;
	
	public CXFORM() {	
		// TODO: pag 45
	}
	
	public CXFORM(byte[] swf, int offset) {
		
	}
	
	public byte[] toByteArray() {
		// TODO
		return null;
	}
}
