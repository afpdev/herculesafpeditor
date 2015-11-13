package com.mgz.AFPEditor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.mgz.AFPEditor.dom.DOMItem;
import com.mgz.AFPEditor.dom.DOMItemComplex;
import com.mgz.AFPEditor.dom.DOMParser;
import com.mgz.afp.exceptions.AFPParserException;
import com.mgz.afp.parser.AFPParser;
import com.mgz.afp.parser.AFPParserConfiguration;

public class DocumentModel implements TreeModel{
	DOMItemComplex root;
	List<TreeModelListener> treeModelListeners;
	AFPParserConfiguration parserConfig;
	boolean isShallow;

	public DocumentModel(boolean isShallow){
		this.isShallow=isShallow;
	}

	public boolean isLeaf(Object node) {
		if(node==root) return false;
		return !(node instanceof DOMItemComplex);
	}

	public Object getRoot() {
		return root;
	}

	public int getIndexOfChild(Object parent, Object child) {
		DOMItemComplex sf = (DOMItemComplex) parent;
		return sf.getDOMChildren().indexOf(child);
	}

	public int getChildCount(Object parent) {
		DOMItemComplex sf = (DOMItemComplex) parent;
		if(sf.getDOMChildren()==null) return 0;
		return sf.getDOMChildren().size();
	}

	public Object getChild(Object parent, int index) {
		DOMItemComplex sf = (DOMItemComplex) parent;
		return sf.getDOMChildren().get(index);
	}

	/**
	 * Parses the given AFP file and build the {@link DocumentModel}.
	 * 
	 * @param config applied to the AFP parser.
	 * @throws AFPParserException if parsing fails.
	 */
	public void loadDocument(AFPParserConfiguration config) throws AFPParserException{
		parserConfig = config;

		root = DOMParser.createDOM(parserConfig);

		if(treeModelListeners!=null && !treeModelListeners.isEmpty()){
			TreeModelEvent treeModelEvent = new TreeModelEvent(root, new Object[]{root});
			for(TreeModelListener listener : treeModelListeners){
				listener.treeStructureChanged(treeModelEvent);
			}
		}
	}

	public DOMItem getDOMItem(TreePath tp) {
		DOMItem domItem = (DOMItem) tp.getLastPathComponent();
		if(isShallow && !domItem.isEdited()){
			try {
				AFPParser.reload(domItem.getStructuredField());
			} catch (AFPParserException e1) {
				e1.printStackTrace();
			}
		}
		return domItem;
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		return;
	}

	public void addTreeModelListener(TreeModelListener l) {
		if(l==null) return;
		if(treeModelListeners==null) treeModelListeners = new ArrayList<TreeModelListener>(1);
		treeModelListeners.add(l);
	}

	public void removeTreeModelListener(TreeModelListener l) {
		if(treeModelListeners==null) return;
		else treeModelListeners.remove(l);
	}


}