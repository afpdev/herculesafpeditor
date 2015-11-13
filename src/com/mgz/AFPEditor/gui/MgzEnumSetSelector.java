package com.mgz.AFPEditor.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mgz.AFPEditor.gui.ComplexEditor.UniversalChangeListener;
import com.mgz.afp.enums.IMutualExclusiveGroupedFlag;
import com.mgz.util.UtilCharacterEncoding;


@SuppressWarnings({"rawtypes","unchecked"})
public class MgzEnumSetSelector extends JPanel implements IEditorComponent,ChangeListener {
	private static final long serialVersionUID = 1L;
	final Class<Enum> enumClass;
	HashMap<Enum<?>,JToggleButton> components;
	private UniversalChangeListener unversalChangeManager;
	

	public MgzEnumSetSelector(Class<Enum> enumClass, EnumSet values){
		setLayout(new GridBagLayout());
		
		
		setBorder(BorderFactory.createTitledBorder(
				UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(enumClass.getSimpleName()+"s"))
			);
		this.enumClass = enumClass;

		if(IMutualExclusiveGroupedFlag.class.isAssignableFrom(enumClass)){
			buildEnumMutualExclusiveGrouped(values);
		}else{
			buildEnumNormal(values);
		}
		
		if(values!=null) setValue(values);
		
	}
	
	private void buildEnumMutualExclusiveGrouped(EnumSet<?> values){
		components = new HashMap<Enum<?>, JToggleButton>();
		HashMap<Integer, JPanel> groupPanels = new HashMap<Integer, JPanel>();
		HashMap<Integer, ButtonGroup> buttonGroups = new HashMap<Integer, ButtonGroup>();
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;

		Iterator<Enum<?>> iter = EnumSet.allOf(enumClass).iterator();
		while(iter.hasNext()){
			Enum val = iter.next();

			String buttonName = val.name();
			buttonName = buttonName.substring(buttonName.indexOf('_')+1);
			buttonName = UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(buttonName);
			JRadioButton radioButton = new JRadioButton(buttonName);
			radioButton.addChangeListener(this);
			
			Integer groupID = ((IMutualExclusiveGroupedFlag)val).getGroup();
			JPanel group = groupPanels.get(groupID);
			ButtonGroup buttonGroup = buttonGroups.get(groupID);
			if(group==null){
				String groupName = val.name();
				groupName = groupName.substring(0, groupName.indexOf("_"));
				groupName = UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(groupName);
				group = new JPanel();
				group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
				group.setBorder(BorderFactory.createTitledBorder(groupName));
				groupPanels.put(groupID, group);

				buttonGroup=new ButtonGroup();
				buttonGroups.put(groupID, buttonGroup);
				
				add(group,c);
				radioButton.setSelected(true);
			}

			buttonGroup.add(radioButton);
			group.add(radioButton);

			components.put(val,radioButton);
		}
	}
	private void buildEnumNormal( EnumSet values){
		components = new HashMap<Enum<?>,JToggleButton>();
		Iterator<Enum> iter = EnumSet.allOf(enumClass).iterator();
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		
		while(iter.hasNext()){
			Enum val = iter.next();

			JCheckBox checkBox = new JCheckBox(UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(val.name()));
			checkBox.addChangeListener(this);
			add(checkBox, c);
			components.put(val,checkBox);
		}
	}

	@Override
	public Object getValue(){
		EnumSet result = EnumSet.noneOf(enumClass);

		Iterator<Enum> iter = EnumSet.allOf(enumClass).iterator();
		while(iter.hasNext()){
			Enum val = iter.next();
			JToggleButton checkBox = components.get(val);
			if(checkBox.isSelected()) result.add(val);
		}	

		return result;
	}

	@Override
	public void setValue(Object obj){
		EnumSet value = (EnumSet) obj;
		for(JToggleButton checkBox : components.values()){
			checkBox.setSelected(false);
		}
		if(value!=null && !value.isEmpty()){

			Iterator<Enum> iter = value.iterator();
			while(iter.hasNext()){
				components.get(iter.next()).setSelected(true);

			}
		}
	}


	@Override
	public void addUniversalChangeListener(UniversalChangeListener changeListener) {
		this.unversalChangeManager = changeListener;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(this.unversalChangeManager!=null) unversalChangeManager.onChange(this.getValue());
	}
	
}
