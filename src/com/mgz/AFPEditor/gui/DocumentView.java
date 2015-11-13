package com.mgz.AFPEditor.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.mgz.AFPEditor.DocumentControler;

public class DocumentView extends JInternalFrame {
	private static final long serialVersionUID = 1L;

	private static final Dimension dimLeft= new Dimension(200, 400);
	private static final Dimension dimRightTop= new Dimension(400, 400);
	private static final Dimension dimRightBottom = new Dimension(400, 200);
	
	
	
	
	public DocumentView(DocumentControler controler){
		super("<New Document>",true,true,true,true);
		
		MgzDocumentTreeView tree = controler.getDocumentTreeView();
		Component rightTop =  controler.getRightTopDetailPane();
		rightTop.setPreferredSize(dimRightTop);
		Component rightBottom = controler.getRightBottomPane();
		rightBottom.setPreferredSize(dimRightBottom);
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(dimLeft);
		scrollPane.setViewportView(tree);
		
		JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		rightSplitPane.setDividerLocation(400);
		rightSplitPane.add(rightTop);
		rightSplitPane.add(rightBottom);

		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainSplitPane.add(scrollPane);
		mainSplitPane.add(rightSplitPane);
		setContentPane(mainSplitPane);
		
		
	}
	
}
