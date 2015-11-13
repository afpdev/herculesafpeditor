package com.mgz.AFPEditor.gui;

import static com.mgz.util.UtilReflection.getFieldValue;
import static com.mgz.util.UtilReflection.isAFPType;
import static com.mgz.util.UtilReflection.isNumeric;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import com.mgz.afp.base.StructuredField;
import com.mgz.afp.base.annotations.AFPField;
import com.mgz.afp.enums.IMutualExclusiveGroupedFlag;
import com.mgz.util.UtilCharacterEncoding;
import com.mgz.util.UtilGUI;
import com.mgz.util.UtilReflection;

public class ComplexEditor extends JPanel{
	private static final long serialVersionUID = 1L;
	public final static int defaultNumberFieldLength = 10;
	public final static int defaultTextLineLength = 20;
	public final static int maxTextLineLength = 20;

	public final static int maxByteArrayLineLength = 16;
	public final static int maxByteArrayRows = 16;
	public final static int defaultByteArrayLineLength = maxByteArrayLineLength;

	public final static String CLIENTPROPERTY_activatableComponent = "activatableComponent";
	public final static ComponentActivator componentActivator = new ComponentActivator();
	public final static String NAME_itemEditor = "itemEditorHolder";
	public static final String NAME_itemList = "itemList";

	private Logger log;

	public static class UniversalChangeListener implements ActionListener{
		private static final Logger log = Logger.getLogger(ComplexEditor.class.getSimpleName());
		Field field;
		Object instance;
		public UniversalChangeListener(Field field, Object instance){
			this.field = field;
			this.instance = instance;
		}
		public void onChange(Object newValue){
			UtilReflection.setFieldValue(field, instance, newValue);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			IEditorComponent comp = (IEditorComponent) e.getSource();
			Object newVal = comp.getValue();
			onChange(newVal);
			log.log(Level.FINEST,UtilGUI.getSignature(instance) + " {\""+field.getName()+"\":\""+UtilGUI.value2String(newVal)+"\"}");
		}
	}


	public ComplexEditor(Class<?> clazz, Object instance, String name){
		log = Logger.getLogger(ComplexEditor.class.getSimpleName());

		if(name==null) name = UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(clazz.getSimpleName());

		this.setName(name);
		TitledBorder titledBorder = BorderFactory.createTitledBorder(name);
		this.setBorder(titledBorder);
		this.setLayout(new GridBagLayout());

		buildEditor(clazz,instance);
	}

	private void buildEditor(Class<?> clazz, Object instance){
		JComponent editorCompponent;
		GridBagConstraints gbc = createGridBagConstraints();

		List<Field> listOfFields = UtilReflection.getAFPFields(clazz);

		for(Field field : listOfFields){
			AFPField annotation = field.getAnnotation(AFPField.class);
			if(annotation==null) annotation = UtilReflection.getAFPFieldDefaultAnnotation();

			String lableText = UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(field.getName());

			editorCompponent = buildFieldEditorComponent(field, instance,annotation);

			gbc.weightx = 1.0d;
			if(!isComplexEditorComponent(field,annotation)){
				JLabel lable = new JLabel(lableText+":");
				gbc.weightx=0d;
				add(lable, gbc);
				gbc.gridx++;

				gbc.weightx=1d;
				add(editorCompponent, gbc);

			}else{
				gbc.gridwidth=2;
				JToggleButton buttonEdit = createOpenDetailEditorButton(lableText, editorCompponent);
				add(buttonEdit, gbc);

				gbc.gridy++;
				editorCompponent.setVisible(false);
				add(editorCompponent, gbc);
			}

			// Move to next row, first cell. Set grid width to = 1;
			gbc.weightx = 0d;
			gbc.gridx=0;
			gbc.gridy++;
			gbc.gridwidth=1;
		}


		gbc.gridx=0;
		gbc.gridy++;
		gbc.weighty=1.0;
		gbc.gridwidth=3;
		add(new JLabel(""), gbc);

	}



	private boolean isComplexEditorComponent(Field field, AFPField annotation) {
		Class<?> fieldType = field.getType();
		Type genericType = field.getGenericType();
		Class<?> componentType = fieldType.getComponentType();

		if (genericType instanceof ParameterizedType) {
			return true;
		}else if(fieldType.isArray()){
			if(Byte.class.isAssignableFrom(componentType)){
				int arrLen = Math.max(annotation.size(),annotation.maxSize());
				if(arrLen>maxByteArrayLineLength){
					return true;
				}
			}
		}else if(fieldType.isEnum() && IMutualExclusiveGroupedFlag.class.isAssignableFrom(fieldType)){
			return true;
		}else if(isAFPType(fieldType)){
			return true;
		}
		return false;
	}

	@SuppressWarnings({"unchecked","rawtypes"})
	private JComponent buildFieldEditorComponent(Field field, Object instance, AFPField annotation){
		JComponent component = null;

		Class<?> fieldType = field.getType();
		Object fieldValue = getFieldValue(field, instance);




		if(CharSequence.class.isAssignableFrom(fieldType)){
			component = createTextComponent(annotation);

		}else if(isNumeric(fieldType)){
			component = createNumericComponent(fieldType,annotation);

		}else if(fieldType.isArray()){
			Class<?> componentType = fieldType.getComponentType();
			if(byte.class.isAssignableFrom(componentType)){
				component = createByteArrayComponent(field,annotation);
			}else{
				System.out.println("Field " + field.getName() + " is an Array type with unknown component type " + componentType.getSimpleName() + ".");
			}

		}else if(Enum.class.isAssignableFrom(fieldType)){
			component = new MgzEnumComboBox((Class<Enum<?>>) fieldType, null);

		}else if(isAFPType(fieldType)){
			component = new ComplexEditor(fieldType, fieldValue,field.getName());

		}else{


			Type genericType = field.getGenericType();

			if (genericType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) genericType;			
				Type types[] =pt.getActualTypeArguments();

				if(List.class.isAssignableFrom(fieldType)){
					component = new MgzListEditor(field,fieldType,types,annotation,(List)fieldValue);

				}else if(EnumSet.class.isAssignableFrom(fieldType)){
					component = new MgzEnumSetSelector((Class<Enum>)types[0], (EnumSet)fieldValue);
				}else{
					if(fieldType!=null && types.length>0 && types[0] instanceof Class){
						Class<?> clazz = (Class<?>)types[0];
						if(clazz!=StructuredField.class && isAFPType(clazz)){

							component = new ComplexEditor(clazz, fieldValue, field.getName());
						}
					}else{
						component = createTextComponent(annotation);
					}
				}

			}else{
				// Unknown
				MgzTextField tf = null;
				component = tf = new MgzTextField(defaultTextLineLength);
				tf.setText(fieldType.getSimpleName());
				tf.setBackground(Color.red);
				log.log(Level.WARNING,"Editor component for field type " + fieldType.getName() +" is unknown (no @AFPType/@AFPField annotation found).");
			}


		}	

		if(component instanceof IEditorComponent){
			IEditorComponent editorComponent = (IEditorComponent) component;
			if(fieldValue!=null ){
				editorComponent.setValue(fieldValue);
			}
			editorComponent.addUniversalChangeListener(new UniversalChangeListener(field, instance));
		}

		if(!annotation.isEditable()){
			component.setEnabled(false);
		}
		

		return component;


	}




	public static JComponent createByteArrayComponent(Field field, AFPField annotation) {
		JComponent component = null;

		int minSize = Math.max(annotation.size(), annotation.minSize());
		if(minSize==-1) 
			minSize = 0;
		int arrLen = Math.max(annotation.size(), annotation.maxSize());
		if(arrLen == -1)
			arrLen = maxByteArrayLineLength;

		if(arrLen<=maxByteArrayLineLength){
			component = new MgzHexEditorField(minSize,arrLen);

		}else{
			component = new MgzHexEditorArea(minSize,arrLen,maxByteArrayLineLength,maxByteArrayRows);
		}
		return component;
	}

	public static JComponent createNumericComponent(Class<?> fieldType, AFPField annotation) {
		JComponent component = null;
		component = new MgzTextField();
		return component;
	}

	public static JComponent createTextComponent(AFPField annotation) {
		JComponent component = null;
		int size = Math.max(annotation.size(), annotation.maxSize());
		if(size<=0){
			component = new MgzTextField(defaultTextLineLength);
		}else if(size<=maxTextLineLength){
			component = new MgzTextField(size);
		}else{
			component = new MgzTextArea(size / maxTextLineLength + 1, maxTextLineLength);
		}
		return component;
	}

	public static class ComponentActivator implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JToggleButton button = (JToggleButton) e.getSource();
			JComponent activableComponent = (JComponent) button.getClientProperty(CLIENTPROPERTY_activatableComponent);

			activableComponent.setVisible(!activableComponent.isVisible());
			activableComponent.invalidate();
		}
	}	

	public static JToggleButton createOpenDetailEditorButton(String txt, JComponent detailEditorComponent){
		JToggleButton buttonEdit = new JToggleButton(txt);
		buttonEdit.putClientProperty(CLIENTPROPERTY_activatableComponent, detailEditorComponent);
		buttonEdit.addActionListener(componentActivator);
		return buttonEdit;
	}



	public static GridBagConstraints createGridBagConstraints(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.weightx = 0d;
		gbc.weighty = 0d;
		gbc.fill = GridBagConstraints.HORIZONTAL; 
		gbc.gridx=0;
		gbc.gridy=0;

		return gbc;
	}




}
