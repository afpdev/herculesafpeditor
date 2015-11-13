package com.mgz.AFPEditor.dom;

import java.io.File;

import com.mgz.afp.base.StructuredField;
import com.mgz.afp.modca.BDT_BeginDocument;
import com.mgz.afp.modca.BRG_BeginResourceGroup;
import com.mgz.afp.parser.AFPParser;
import com.mgz.afp.parser.AFPParserConfiguration;

public class DOMItem {
		boolean isVirtual;
		boolean isEdited;
		File originFile;
		DOMItemComplex domParent;

		StructuredField structuredField;
		
		/**
		 * Returns true if this SF has been edited, its state has changed.
		 * @return
		 */
		public boolean isEdited() {
			return isEdited;
		}
		public void setEdited(boolean isEdited) {
			this.isEdited = isEdited;
		}

		
		/**
		 * Returns true if this {@link StructuredField} is virtual.
		 * Virtual {@link StructuredField}s are not written to AFP output files.
		 * @param isVirtual
		 */
		public void setVirtual(boolean isVirtual) {
			this.isVirtual = isVirtual;
		}
		
		/**
		 * Returns the file from where this structured field has been
		 * @return
		 */
		public File getOriginFile() {
			return originFile;
		}
		public void setOriginFile(File file) {
			this.originFile = file;
		}

		/**
		 * Returns the structural depth of this structured field.
		 * All root objects (e.g. {@link BDT_BeginDocument}, {@link BRG_BeginResourceGroup}, etc.) have a structural depth of 0.
		 * Structured fields directly contained in root have a structural depth of 1 and so on.
		 *
		 * @return hierarchy level of this structured field
		 */
		public int getDOMStructuralDepth() {
			if(domParent==null) return 1;
			else return 1 + domParent.getDOMStructuralDepth();
		}
		
		/**
		 * Returns the parent {@link StructuredField} of this {@link StructuredField} or null if this is a {@link StructuredField} on root level.
		 * The parser has to be configured to build DOM structure (see {@link AFPParserConfiguration#setBuildDOM(boolean)} otherwise the parent/child relation is not considered and this method allways returns null.
		 * @return parent {@link StructuredField}.
		 */
		public DOMItemComplex getDOMParent() {
			return domParent;
		}
		
		/**
		 * Used to set the parent relation ship of this {@link StructuredField} manually.
		 * Usually the parent relation of {@link StructuredField}s is maintained by the {@link AFPParser} if it is configured to do so
		 * by setting {@link AFPParserConfiguration#setBuildDOM(boolean)} to true. 
		 * @param parent
		 */
		public void setDOMParent(DOMItemComplex parent) {
			domParent = parent;
		}
		
		
		/**
		 * Returns true if this {@link StructuredField} is virtual.
		 * Virtual {@link StructuredField}s are not written to AFP output files.
		 * @return
		 */
		public boolean isVirtual() {
			return isVirtual;
		}
		public StructuredField getStructuredField() {
			return structuredField;
		}
		public void setStructuredField(StructuredField structuredField) {
			this.structuredField = structuredField;
		}		
		
		
		/* Code that built DOM. 
		
		if(parserConf.isBuildDOM){
			// Maintain DOM relationships.
			if(!stack.isEmpty() && !sf.isEndSF()){
				StructuredField parent = stack.peek();
				sf.setDOMParent(parent);
				parent.addDOMChild(sf);
			}
			if(sf.isBeginSF()){
				stack.push(sf);
			}else if(sf.isEndSF() && !stack.isEmpty()){
				StructuredField beginnSF = stack.pop();
				sf.setDOMBeginSF(beginnSF);
				beginnSF.setDOMEndSF(sf);;
			}
		}
		
		*/
		
}