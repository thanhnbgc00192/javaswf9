package swf9.tags;
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

import java.io.File;
import java.util.Vector;

import swf9.SWF9Compression;
import swf9.tags.bitmaps.DefineBits;
import swf9.tags.bitmaps.JPEGTables;

public class Tag extends SWF9Compression {
		
	public static String RecordHeader;
	public int type;
	public int length;
	
	public static String RECORDHEADER_SHORT	= "short";
	public static String RECORDHEADER_LONG	= "long";
	
	// SWF tags
	public final static int End						= 0; 	
	public final static int ShowFrame				= 1;	
	public final static int DefineShape				= 2;	// 3 not present
	public final static int PlaceObject				= 4;
	public final static int RemoveObject			= 5;
	public final static int DefineBits 				= 6;
	public final static int DefineButton			= 7;
	public final static int JPEGTables 				= 8;	
	public final static int SetBackgroundColor		= 9;
	public final static int DefineFont				= 10;
	public final static int DefineText				= 11;
	public final static int DoAction				= 12;
	public final static int DefineFontInfo			= 13;
	public final static int DefineSound				= 14;
	public final static int StartSound				= 15;	// 16 not present
	public final static int DefineButtonSound 		= 17;
	public final static int SoundStreamHead			= 18;
	public final static int SoundStreamBlock		= 19;
	public final static int DefineBitsLossless 		= 20;	
	public final static int DefineBitsJPEG2 		= 21;	
	public final static int DefineShape2			= 22;
	public final static int DefineButtonCxform		= 23;
	public final static int Protect					= 24;	// 25 not present
	public final static int PlaceObject2			= 26;	// 27 not present
	public final static int RemoveObject2			= 28;
	public final static int DefineShape3			= 32;
	public final static int DefineText2				= 33;
	public final static int DefineButton2			= 34;
	public final static int DefineBitsJPEG3 		= 35;
	public final static int DefineBitsLossless2		= 36;
	public final static int DefineEditText 			= 37;
	public final static int DefineSprite			= 39;
	public final static int FrameLabel				= 43;
	public final static int SoundStreamHead2		= 45;
	public final static int DefineMorphShape		= 46;
	public final static int DefineFont2				= 48;
	public final static int ExportAssets			= 56;
	public final static int ImportAssets			= 57;
	public final static int EnableDebugger			= 58;
	public final static int DoInitAction			= 59;
	public final static int DefineVideoStream 		= 60;	
	public final static int VideoFrame				= 61;
	public final static int DefineFontInfo2			= 62;
	public final static int EnableDebugger2			= 64;
	public final static int ScriptLimits			= 65;
	public final static int SetTabIndex				= 66;
	public final static int FileAttributes			= 69;
	public final static int PlaceObject3			= 70;
	public final static int ImportAssets2			= 71;
	public final static int DefineFontAlignZones	= 73;
	public final static int CSMTextSettings			= 74;
	public final static int DefineFont3				= 75;
	public final static int SymbolClass				= 76;
	public final static int MetaData				= 77;
	public final static int DefineScalingGrid		= 78;
	public final static int DoABC					= 82;
	public final static int DefineShape4			= 83;
	public final static int DefineMorphShape2 		= 84;
	public final static int DefineSceneAndFrameLabelData = 86;
	public final static int DefineBinaryData		= 87;
	public final static int DefineFontName			= 88;
	public final static int DefineStartSound2		= 89;
	
	public static Vector<Class<Tag>> TagBible = new Vector<Class<Tag>>();
	
	public Tag() {
		initBible();
	}
	
	public Tag(byte[] swf, int offset) {
		initBible();
		type 	= getTagType(swf,offset);
		length 	= getTagLength(swf,offset);
	}
	
	private static void initBible() {
		//TagBible.add(End, new DefineBits().getClass());
	}
	
	public int getType() {
		return type;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getTagType(byte[] swf, int offset) {
		
		byte[] bytes = new byte[2];
		System.arraycopy(swf, offset, bytes, 0, 2);
		type = readTagType(bytes);
		return type;
		
	}
	
	public int getTagLength(byte[] swf, int offset) {
		
		int len;
		
		byte[] bytes = new byte[2];
		System.arraycopy(swf, offset, bytes, 0, 2);
		len = readTagLength(bytes);
		
		if(len>=63) {
			bytes = new byte[4];
			System.arraycopy(swf, offset+2, bytes, 0, 4);
			len = readTagLengthExt(bytes);
			
			RecordHeader = RECORDHEADER_LONG;
			length = len+6;
			return length;
			
		}
		
		RecordHeader = RECORDHEADER_SHORT;
		length = len+2;
		return length;
		
	}
		
	// 0
	public boolean isEnd(byte[] swf, int offset) {
		if(swf[offset]==End && swf[offset+1]==End) {
			return true;
		}
		return false;
	}
	
	// 1
	public boolean isShowFrame(byte[] swf, int offset) {
		if(getTagType(swf,offset)==ShowFrame)
			return true;
		return false;
	}
	
	// 18
	public boolean isSoundStreamHead(byte[] swf, int offset) {
		if(getTagType(swf,offset)==SoundStreamHead)
			return true;
		return false;
	}
	
	public boolean isFileAttributes(byte[] swf, int offset) {
		if(getTagType(swf,offset)==FileAttributes)
			return true;
		return false;
	}
	
	// 9
	public boolean isSetBackgroundColor(byte[] swf, int offset) {
		if(getTagType(swf,offset)==SetBackgroundColor)
			return true;
		return false;
	}
	
	// 10
	public boolean isDefineFont(byte[] swf, int offset) {
		if(getTagType(swf,offset)==DefineFont) {
			return true;
		}
		return false;
	}
	
	// 11
	public boolean isDefineText(byte[] swf, int offset) {
		if(getTagType(swf,offset)==DefineText) {
			return true;
		}
		return false;
	}
	
	// 28
	public boolean isRemoveObject2(byte[] swf, int offset) {
		if(getTagType(swf,offset)==RemoveObject2) {
			return true;
		}
		return false;
	}
	
	// 37
	public boolean isDefineEditText(byte[] swf, int offset) {
		if(getTagType(swf,offset)==DefineEditText) {
			return true;
		}
		return false;
	}
	
	public boolean isDefineShape(byte[] swf, int offset) {
		if(getTagType(swf,offset)==DefineShape) {
			return true;
		}
		return false;
	}
	
	public boolean isDefineShape2(byte[] swf, int offset) {
		if(getTagType(swf,offset)==DefineShape2) {
			return true;
		}
		return false;
		
	}
	
	public boolean isDefineShape3(byte[] swf, int offset) {
		
		if(getTagType(swf,offset)==DefineShape3) {
			return true;
		}
		return false;
		
	}
	
	public boolean isPlaceObject(byte[] swf, int offset) {
		if(getTagType(swf,offset)==PlaceObject) {
			return true;
		}
		return false;	
	}
	
	public boolean isPlaceObject2(byte[] swf, int offset) {
		if(getTagType(swf,offset)==PlaceObject2) {
			return true;
		}
		return false;
	}
	
	public boolean isPlaceObject3(byte[] swf, int offset) {
		if(getTagType(swf,offset)==PlaceObject3) {
			return true;
		}
		return false;
	}
	
	public boolean isDefineSprite(byte[] swf, int offset) {
		if(getTagType(swf,offset)==DefineSprite) {
			return true;
		}
		return false;
	}
	
	public boolean isDefineBits(byte[] swf, int offset) {
		if(getTagType(swf,offset)==DefineBits) {
			return true;
		}
		return false;
	}
	
	public boolean isDefineBitsJPEG2(byte[] swf, int offset) {
		if(getTagType(swf,offset)==DefineBitsJPEG2) {
			return true;
		}
		return false;
	}
	
	public boolean isDefineBitsJPEG3(byte[] swf, int offset) {
		if(getTagType(swf,offset)==DefineBitsJPEG3) {
			return true;
		}
		return false;
	}
	
	public boolean isJPEGTables(byte[] swf, int offset) {
		if(getTagType(swf,offset)==JPEGTables) {
			return true;
		}
		return false;
	}
	
	public boolean isDefineBitsLossless(byte[] swf, int offset) {
		if(getTagType(swf,offset)==DefineBitsLossless) {
			return true;
		}
		return false;
	}

	public boolean isDefineVideoStream(byte[] swf, int offset) {
		if(getTagType(swf,offset)==DefineVideoStream) {
			return true;
		}
		return false;
	}

	public boolean isVideoFrame(byte[] swf, int offset) {
		if(getTagType(swf,offset)==VideoFrame) {
			return true;
		}
		return false;
	}
	
	public boolean isDefineFontInfo2(byte[] swf, int offset) {
		if(getTagType(swf,offset)==DefineFontInfo2) {
			return true;
		}
		return false;
	}
	
	// 46
	public boolean isDefineMorphShape(byte[] swf, int offset) {
		if(getTagType(swf,offset)==DefineMorphShape) {
			return true;
		}
		return false;
	}
	
	public void exportImage(String file) {
		// override
	}
	public void exportImage(File file) {
		// override
	}
	
	public void exportImage(File file, JPEGTables table) {
		// override
	}
	
	public byte[] toByteArray() {
		// override
		return null;
	}
	
}
