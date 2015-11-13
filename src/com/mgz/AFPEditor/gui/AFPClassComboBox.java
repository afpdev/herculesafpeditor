package com.mgz.AFPEditor.gui;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.mgz.AFPEditor.gui.ComplexEditor.UniversalChangeListener;
import com.mgz.util.UtilCharacterEncoding;

public class AFPClassComboBox extends JComboBox implements IEditorComponent{
	private static final long serialVersionUID = 1L;

	public AFPClassComboBox(Class<?> clazz, Object selectedValue){
		super(clazz.getDeclaredClasses());
		if(selectedValue!=null) setSelectedItem(selectedValue);
		this.setRenderer(new Renderer());
	}

	@Override
	public void setValue(Object value) {
		setSelectedItem(value);
	}

	@Override
	public Object getValue() {
		return getSelectedItem();
	}

	private static class Renderer extends BasicComboBoxRenderer{
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Class<?> e = (Class<?>) value;
			String name = null;
			if(e!=null) name = UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(e.getSimpleName());
			else name = "???";	
			Component lable = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			this.setText(name);
			return lable;
		}
	}

	@Override
	public void addUniversalChangeListener(UniversalChangeListener changeListener) {
		this.addActionListener(changeListener);
	}
	
}
