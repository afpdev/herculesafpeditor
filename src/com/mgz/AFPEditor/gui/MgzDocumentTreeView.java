package com.mgz.AFPEditor.gui;

import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.mgz.AFPEditor.dom.DOMItem;
import com.mgz.AFPEditor.dom.DOMItemComplex;
import com.mgz.afp.base.StructuredField;
import com.mgz.afp.parser.AFPParserConfiguration;
import com.mgz.util.UtilGUI;

public class MgzDocumentTreeView extends JTree{
	private static final long serialVersionUID = 1L;

	private EditorConfiguration editorConfig;
	private AFPParserConfiguration parserConfig;
	private DocumentTreeViewUI ui;

	public static class EditorConfiguration{
		public boolean isShowFileOffset=true;
		public boolean isColorFull=true;
		public boolean isShallow=true;
	}


	public MgzDocumentTreeView(TreeModel documentModel,AFPParserConfiguration parserConfig){
		super(documentModel);
		this.parserConfig = parserConfig;
		editorConfig = new EditorConfiguration();
		ui = new DocumentTreeViewUI();
		ui.setConfig(editorConfig);
		setUI(ui);
		this.setDoubleBuffered(false);
		setRootVisible(false);
		setToolTipText("This is a tree");
		ToolTipManager.sharedInstance().registerComponent(this);

	}

	@Override
	public String convertValueToText(Object value, boolean isSelected,
			boolean isExpanded, boolean isLeaf, int rowidx,
			boolean isHasFocus) {
		if(value != null && value!=getModel().getRoot() && value instanceof DOMItem) {
			
			StructuredField sf = ((DOMItem) value).getStructuredField();
			return UtilGUI.getSignatureHTML(sf, editorConfig, parserConfig);

		}

		return "";
	}

	/**
	 * Returns tool tip text from the given value, or null if the tool tip text for the given value is undefined.
	 * @param value
	 * @return
	 */
	public String convertValueToToolTipText(Object value) {
		if(value != null && value!=getModel().getRoot() && value instanceof DOMItem) {
			StructuredField sf = null;
			if(value instanceof DOMItemComplex) sf = ((DOMItemComplex)value).getDOMBeginSF();
			else sf = ((DOMItem) value).getStructuredField();

			StringBuilder sb = new StringBuilder();

			String className = sf.getClass().getSimpleName();
			className = className.substring(className.indexOf("_")+1);
			boolean isFirst=true;
			for(int i=0; i<className.length(); i++){
				if(Character.isUpperCase(className.charAt(i))){
					if(isFirst) isFirst = false;
					else sb.append(' ');
				}else if(className.charAt(i)=='_'){
					continue;
				}

				sb.append(className.charAt(i));
			}

			return sb.toString();
		}else{
			return null;
		}
	}


	@Override
	public String getToolTipText(MouseEvent e){
		if(e==null) return null;

		TreePath tp =getPathForLocation(e.getX(), e.getY());
		if(tp==null) return null;

		return convertValueToToolTipText(tp.getLastPathComponent());
	}

	public EditorConfiguration getConfig() {
		return editorConfig;
	}

	public void setConfig(EditorConfiguration config) {
		this.editorConfig = config;
		ui.setConfig(config);
	}

}
