package swf9.types.video;
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

public class MACROBLOCK {

	public int CodedMacroblockFlag;
	public int MacroblockType;
	public int BlockPattern;
	public int QuantizerInformation;
	public int MotionVectorData;
	public int ExtraMotionVectorData;
	public BLOCKDATA BlockData;
	
	public MACROBLOCK() {	
	}
	
	public MACROBLOCK(byte[] swf, int offset) {
		
	}
}
