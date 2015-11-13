package com.mgz.AFPEditor.gui;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.mgz.AFPEditor.gui.ComplexEditor.UniversalChangeListener;

public class MgzTextArea extends JTextArea implements IEditorComponent,DocumentListener{
	private static final long serialVersionUID = 1L;
	private UniversalChangeListener universalChangeListener;
	
	public MgzTextArea(int rows, int columns) {
		super(rows,columns);
		this.getDocument().addDocumentListener(this);
	}

	@Override
	public void setValue(Object value) {
		this.setText(value.toString());
	}

	@Override
	public Object getValue() {
		return getText();
	}

	@Override
	public void addUniversalChangeListener(UniversalChangeListener changeListener) {
		this.universalChangeListener = changeListener;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if(universalChangeListener!=null) universalChangeListener.onChange(getValue());
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if(universalChangeListener!=null) universalChangeListener.onChange(getValue());		
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		if(universalChangeListener!=null) universalChangeListener.onChange(getValue());
	}
}
