package com.mgz.AFPEditor.dom;

import java.util.ArrayList;
import java.util.List;

import com.mgz.afp.base.StructuredField;
import com.mgz.afp.enums.SFType;
import com.mgz.afp.parser.AFPParser;
import com.mgz.afp.parser.AFPParserConfiguration;

public class DOMItemComplex extends DOMItem {
	List<DOMItem> domChildren;
	StructuredField domEndSF;

	/**
	 * Returns the list of children {@link DOMItem}s of this {@link DOMItemComplex} or null/empty list if this {@link DOMItemComplex} has no child.
	 * @return list of children {@link StructuredField}s.
	 */
	public List<DOMItem> getDOMChildren() {
		return domChildren;
	}
	/**
	 * Sets the list of children {@link DOMItems}s of this {@link DOMItemComplex}.
	 */
	public void setDOMChildren(List<DOMItem> childs) {
		this.domChildren = childs;
	}
	
	/**
	 * Adds the given {@link DOMItem} to the list of children.
	 * @param newChild {@link DOMItem} to add.
	 */
	public void addDOMChild(DOMItem newChild){
		if(newChild==null) return;
		if(domChildren==null) domChildren = new ArrayList<DOMItem>();
		domChildren.add(newChild);
	}
	/**
	 * Removes the given child from the list of child
	 * Usually the child relation of {@link StructuredField}s is maintained by the {@link AFPParser} if it is configured to do so.
	 * See {@link AFPParserConfiguration#isBuildDOM()}
	 * @param childToRemove
	 */
	public void removeDOMChild(DOMItem childToRemove){
		if(domChildren==null) return;
		domChildren.remove(childToRemove);
	}
	
	/**
	 * Returns the closing End structured field of this {@link StructuredField}, if the type field of the SF Identifier of this {@link StructuredField} has a value of {@link SFType#Begin}.
	 * The End structured field is maintained by the parser if {@link AFPParserConfiguration#setBuildDOM(boolean)} is set to true.
	 * If maintained by the {@link AFPParser}, the End structured field is available after the End {@link StructuredField} has been parsed.
	 * @return
	 */
	public StructuredField getDOMEndSF() {
		return domEndSF;
	}
	/**
	 * Sets the End structured field of this {@link StructuredField}.
	 * See {@link #getDOMEndSF()}.
	 * @param closingEndSF
	 */
	public void setDOMEndSF(StructuredField closingEndSF) {
		domEndSF = closingEndSF;
	}
	
	
	/**
	 * Returns the opening Begin structured field of this {@link StructuredField}, if the type field of the SF Identifier of this {@link StructuredField} has a value of {@link SFType#End}.
	 * The Begin structured field is maintained by the parser if {@link AFPParserConfiguration#setBuildDOM(boolean)} is set to true.
	 * If maintained by the {@link AFPParser}, the End structured field is available after the End {@link StructuredField} has been parsed.
	 * @return
	 */	
	public StructuredField getDOMBeginSF(){
		return structuredField;
	}
	
	public void setDOMBeginSF(StructuredField beginSD){
		structuredField = beginSD;
	}

}
