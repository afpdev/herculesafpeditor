package com.mgz.AFPEditor.dom;

import com.mgz.afp.base.StructuredField;
import com.mgz.afp.exceptions.AFPParserException;
import com.mgz.afp.parser.AFPParser;
import com.mgz.afp.parser.AFPParserConfiguration;

public class DOMParser {

	public static DOMItemComplex createDOM(AFPParserConfiguration parserConfig) throws AFPParserException{


		DOMItemComplex root;
		DOMItemComplex currentDOMItem = root = new DOMItemComplex();
		currentDOMItem.setVirtual(true);

		AFPParser parser = new AFPParser(parserConfig);
		try{
			StructuredField sf = null;
			do{
				sf = parser.parseNextSF();

				if(sf!=null){
					if(sf.isBeginSF()){
						DOMItemComplex newDOMItemComplex = new DOMItemComplex();
						newDOMItemComplex.setDOMBeginSF(sf);
						newDOMItemComplex.setDOMParent(currentDOMItem);
						if(currentDOMItem!=null){
							((DOMItemComplex)currentDOMItem).addDOMChild(newDOMItemComplex);
						}
						currentDOMItem = newDOMItemComplex;
					}else if(sf.isEndSF()){
						((DOMItemComplex)currentDOMItem).setDOMEndSF(sf);
						currentDOMItem = currentDOMItem.getDOMParent();
					}else{
						DOMItem newDOMItem = new DOMItem();
						newDOMItem.setStructuredField(sf);
						((DOMItemComplex)currentDOMItem).addDOMChild(newDOMItem);
					}
				}

			}while(sf!=null);
		}finally{
			parser.quitParsing();
		}


		return root;
	}


}
