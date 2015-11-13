package com.mgz.AFPEditor.gui;

import com.mgz.AFPEditor.gui.ComplexEditor.UniversalChangeListener;

public interface IEditorComponent {
	public void setValue(Object value);
	public Object getValue();
	
	public void addUniversalChangeListener(UniversalChangeListener changeListener);
}
