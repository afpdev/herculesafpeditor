package com.mgz.AFPEditor.gui;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.mgz.AFPEditor.gui.ComplexEditor.UniversalChangeListener;
import com.mgz.util.UtilCharacterEncoding;

/**
 * Implements selection of an enum value as JComboBox.
 */
public class MgzEnumComboBox extends JComboBox implements IEditorComponent{
	private static final long serialVersionUID = 1L;

	public MgzEnumComboBox(Class<Enum<?>> enumClass, Enum<?> selectedValue){
		super(enumClass.getEnumConstants());
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

	public static class Renderer extends BasicComboBoxRenderer{
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Enum<?> e = (Enum<?>) value;
			String name = UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(e.name());
			return super.getListCellRendererComponent(list, name, index, isSelected, cellHasFocus);
			
		}
	}

	@Override
	public void addUniversalChangeListener(UniversalChangeListener changeListener) {
		this.addActionListener(changeListener);
	}
	
}
