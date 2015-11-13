package com.mgz.AFPEditor;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.mgz.AFPEditor.dom.DOMItem;
import com.mgz.AFPEditor.gui.ComplexEditor;
import com.mgz.AFPEditor.gui.DocumentView;
import com.mgz.AFPEditor.gui.MgzDocumentTreeView;
import com.mgz.AFPEditor.gui.MgzDocumentTreeView.EditorConfiguration;
import com.mgz.afp.base.StructuredField;
import com.mgz.afp.exceptions.AFPParserException;
import com.mgz.afp.parser.AFPParserConfiguration;
import com.mgz.util.UtilGUI;

public class DocumentControler implements TreeSelectionListener{
	DocumentModel model;
	DocumentView documentView;
	MgzDocumentTreeView documentTree;
	EditorConfiguration config;
	
	
	JScrollPane rightTopDetailPane;
	private JScrollPane rightBottomDetailPane;
	public DocumentControler(EditorConfiguration config, AFPParserConfiguration parserConfig){
		this.config = config;
		model = new DocumentModel(config.isShallow);
		
		documentTree = new MgzDocumentTreeView(model,parserConfig);
		documentTree.setVisible(true);
		documentTree.addTreeSelectionListener(this);
		
	}
	
	/**
	 * Loads the given document file.
	 * @param documentFile document file to load.
	 * @throws AFPParserException if parsing fails.
	 */
	public void loadAFPData(AFPParserConfiguration config) throws AFPParserException{
		String filename = config.getAFPFile()!=null ? config.getAFPFile().getAbsolutePath() : "<new document>";
		documentView.setTitle(filename);
		model.loadDocument(config);
	}
	
	@Override
    public void valueChanged(TreeSelectionEvent e) {

		JTree documentTree = (JTree) e.getSource();
		
		TreePath[] selectedPaths = documentTree.getSelectionPaths();
		if(selectedPaths!=null && selectedPaths.length>0){
			JPanel listPane = new JPanel();
			BoxLayout bl = new BoxLayout(listPane, BoxLayout.Y_AXIS);
			
			listPane.setLayout(bl);
			
			
			for(TreePath tp : selectedPaths){
				DOMItem domItem = model.getDOMItem(tp);
				
				listPane.add(Box.createRigidArea(new Dimension(0,5)));
				StructuredField sf = domItem.getStructuredField();
				
				String name = UtilGUI.getSignature(sf);
				
				JPanel editor = new ComplexEditor((Class<?>)sf.getClass(),sf,name);
				
				editor.setAlignmentX(JComponent.LEFT_ALIGNMENT);
				listPane.add(editor);
				listPane.add(Box.createRigidArea(new Dimension(0,5)));
			}
			listPane.add(Box.createVerticalGlue());
			rightTopDetailPane.setViewportView(listPane);
			
		}else{
			rightTopDetailPane.setViewport(null);
		}
    }

	public JScrollPane getRightTopDetailPane() {
		if(rightTopDetailPane==null) rightTopDetailPane = new JScrollPane();
		return rightTopDetailPane;
	}

	public JScrollPane getRightBottomPane(){
		if(rightBottomDetailPane==null) rightBottomDetailPane = new JScrollPane();
		return rightBottomDetailPane;
	}

	public DocumentView getDocumentView() {
		if(documentView==null) documentView = new DocumentView(this);
		return documentView;
	}

	public MgzDocumentTreeView getDocumentTreeView() {
		return this.documentTree;
	}
	
}