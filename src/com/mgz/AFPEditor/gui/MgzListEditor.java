package com.mgz.AFPEditor.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import com.mgz.afp.base.annotations.AFPField;
import com.mgz.util.UtilCharacterEncoding;

public class MgzListEditor extends JPanel {
	private static final long serialVersionUID = 1L;

	public MgzListEditor(Field field, Class<?> fieldType, Type[] types, AFPField annotation, List<Object> collection) {
			super(new GridBagLayout());
			String title = UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(field.getName());
			setBorder(new TitledBorder(title));

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(3, 3, 3, 3);
			gbc.fill = GridBagConstraints.HORIZONTAL; 
			gbc.gridx=0;
			gbc.gridy=0;
			gbc.weightx=1d;
			gbc.gridwidth=1;
			gbc.gridheight=1;

			JComponent list = new MgzTable(collection);
			list.setName(field.getName());
			JScrollPane listScroller = new JScrollPane(list);
			// listScroller.setPreferredSize(new Dimension(500, 80));		
			add(listScroller,gbc);

		}
}