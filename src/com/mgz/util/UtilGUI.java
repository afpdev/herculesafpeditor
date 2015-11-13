package com.mgz.util;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.mgz.AFPEditor.gui.MgzDocumentTreeView.EditorConfiguration;
import com.mgz.afp.base.IHasName;
import com.mgz.afp.base.StructuredField;
import com.mgz.afp.modca.NOP_NoOperation;
import com.mgz.afp.modca.TLE_TagLogicalElement;
import com.mgz.afp.parser.AFPParserConfiguration;
import com.mgz.afp.triplets.Triplet;
import com.mgz.afp.triplets.Triplet.AttributeValue;
import com.mgz.afp.triplets.Triplet.FullyQualifiedName;

public class UtilGUI {
	private static final String class2ColorFilename = "class2color.properties";
	private static Properties class2ColorProps;
	private static Map<Class<?>,Color> class2ColorMap;
	
	
	public static Color getRepresentativeColor(Class<?> clazz){
		if(class2ColorProps==null) openClass2ColorProperties();
		Color color = fetchColor(clazz);
		return color;
	}
	
	private static Color fetchColor(Class<?> clazz){
		if(class2ColorMap!=null && class2ColorMap.containsKey(clazz)){
			synchronized (class2ColorMap) {
				return class2ColorMap.get(clazz);
			}
		}
		
		Color color = null;
		String key = clazz.getSimpleName();
		if(class2ColorProps!=null){
			if(class2ColorProps.containsKey(key)){
				String strVal = class2ColorProps.getProperty(key);
				try{
					color = new Color(Integer.parseInt(strVal));
				}catch(NumberFormatException nfex){
					nfex.printStackTrace();
				}
				if(color==null){
					strVal = class2ColorProps.getProperty("default");
					try{
						color = new Color(Integer.parseInt(strVal));
					}catch(NumberFormatException nfex){
						nfex.printStackTrace();
					}
				}
				if(color==null) color = Color.white;
			}
			
			
		}
		
		if(color==null) color = Color.white; // Fall back color is white.
		
		synchronized (class2ColorMap) {
			class2ColorMap.put(clazz,color);
		}
		
		return color;
	}
	
	public static void openClass2ColorProperties(){
		
		
		File externalPropFile = new File(class2ColorFilename);
		if(externalPropFile.exists()) {
			Properties props = new Properties();
			
			InputStream input = null;
			try{
				input = new FileInputStream(externalPropFile);
				props.load(input);
				class2ColorProps = props;
			}catch(IOException ioex){
				ioex.printStackTrace();
			}finally{
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if(class2ColorProps==null){
			Properties props = new Properties();
			InputStream input = null;
			try{
				input = UtilGUI.class.getClassLoader().getResourceAsStream(class2ColorFilename);
				props.load(input);
				class2ColorProps = props;
			}catch(IOException ioex){
				ioex.printStackTrace();
			}finally{
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}		
		
		class2ColorMap = new HashMap<Class<?>, Color>();
	}


    public static String colorToHex(Color c) {
        return Integer.toHexString(c.getRGB()).substring(2);
    }

	public static String getSignature(Object instance) {
		StringBuilder sb = new StringBuilder();
		if(instance instanceof StructuredField){
			StructuredField sf = (StructuredField) instance;
			
			sb.append("0x").append(UtilCharacterEncoding.bytesToHexString(UtilBinaryDecoding.longToByteArray(sf.getStructuredFieldIntroducer().getFileOffset(), 8))).append(" ")
			.append(UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(instance.getClass().getSimpleName()));
		}else if(instance instanceof Triplet){
			Triplet trip = (Triplet) instance;
			sb.append(UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(trip.getTripletID().name()));
		}else {
			sb.append(UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(instance.getClass().getSimpleName()));
		}
		
		return sb.toString();
	}

	public static String value2String(Object newVal) {
		return newVal.toString();
	}
    
	public static String getSignatureHTML(StructuredField sf, EditorConfiguration config, AFPParserConfiguration parserConfig){
			
			StringBuilder sb = new StringBuilder("<html>");
			if(config.isShowFileOffset){
				long fileOffset = sf.getStructuredFieldIntroducer().getFileOffset();
				int offsetLen = fileOffset > 0x7FFFFFFF ? 8 : 4; 
				sb.append("<small>0x").append(UtilCharacterEncoding.bytesToHexString(
						UtilBinaryDecoding.longToByteArray(fileOffset, offsetLen))
					).append("</small>&nbsp;");
			}

			sb.append("<b>");

			String className = sf.getClass().getSimpleName();
			
			int idx_ = className.indexOf('_');
			if(idx_>=0){
				sb.append(className.substring(0, idx_));
			}else{
				sb.append(className);
			}

			if(sf instanceof IHasName) {
				IHasName withName = (IHasName) sf;
				String name = withName.getName();
				if(name!=null && name.length()>0){
					sb.append(" \"").append(name).append("\"");
				}
			}

			if(sf instanceof TLE_TagLogicalElement){
				TLE_TagLogicalElement tle = (TLE_TagLogicalElement) sf;
				String tleKey=null, tleValue=null;
				if(tle.getTriplets()!=null){
					for(Triplet t : tle.getTriplets()){
						if(t instanceof FullyQualifiedName) tleKey = ((FullyQualifiedName)t).getNameAsString();
						else if(t instanceof Triplet.AttributeValue) tleValue = ((AttributeValue)t).getAttributeValue();
					}
					sb.append(" ").append(tleKey).append(" = \"").append(tleValue).append("\"");
				}
			}else if(sf instanceof NOP_NoOperation){
				NOP_NoOperation nop = (NOP_NoOperation) sf;
				
				byte[] data = nop.getData();
				String dataAsText = null;
				if(data!=null){
					if(UtilCharacterEncoding.isEBCDIC(data)){
						dataAsText = new String(data,parserConfig.getAfpCharSet());
					}else{
						dataAsText = new String(data);
					}				
				}
				if(dataAsText!=null){
					sb.append(" \"").append(dataAsText.length()>15 ? dataAsText.substring(0, 15):dataAsText).append("\"");	
				}

			}
			sb.append("</b></html>");
			
			return sb.toString();		
	}

	




}
