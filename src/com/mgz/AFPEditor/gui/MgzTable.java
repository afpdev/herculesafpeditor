package com.mgz.AFPEditor.gui;

import static com.mgz.util.UtilReflection.getFieldValue;
import static com.mgz.util.UtilReflection.isAFPType;

import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import com.mgz.afp.ptoca.controlSequence.PTOCAControlSequence;
import com.mgz.afp.ptoca.controlSequence.PTOCAControlSequence.TRN_TransparentData;
import com.mgz.afp.triplets.Triplet;
import com.mgz.util.UtilCharacterEncoding;
import com.mgz.util.UtilReflection;

public class MgzTable extends JTable  implements TableCellRenderer{
	private static final long serialVersionUID = 1L;
	private static JLabel lableCounter;
	private static JLabel lableComponent;
	private static JLabel lableDetail;
	
	private int maxWidthDetailField;
	
	Logger log;
	
	MgzTable(List<Object> model){
		super();

		log = Logger.getLogger(MgzTable.class.getSimpleName());
		
		lableCounter = new JLabel();
		lableCounter.setOpaque(true);
		lableComponent = new JLabel();
		lableComponent.setOpaque(true);
		lableDetail  = new JLabel();
		lableDetail.setOpaque(true);
		lableComponent.setHorizontalAlignment(SwingConstants.CENTER);
		lableCounter.setHorizontalAlignment(SwingConstants.CENTER);
		lableDetail.setHorizontalAlignment(SwingConstants.LEFT);
		
		
		setModel(new MgzTableModel(model));

		setCellSelectionEnabled(false);
		setRowSelectionAllowed(true);
		getColumnModel().getColumn(0).setPreferredWidth(40);
		getColumnModel().getColumn(0).setMaxWidth(40);
		getColumnModel().getColumn(1).setPreferredWidth(150);
		getColumnModel().getColumn(1).setMaxWidth(1000);
		getColumnModel().getColumn(2).setMaxWidth(10000);
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column){
		return this;
	}

	public static class MgzTableModel extends AbstractTableModel{
		private static final long serialVersionUID = 1L;
		List<Object> model;
		
		public MgzTableModel(List<Object> model){
			this.model = model;

		}
		
		@Override
		public String getColumnName(int colIdx){
			if(colIdx==0) return "#";
			else if (colIdx==1) return "Type";
			else return "Details";
		}

		@Override
		public int getRowCount() {
			if(model==null) return 0;
			else return model.size();
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(model==null || rowIndex>=model.size()) return null;

			switch(columnIndex){
			case 0: return Integer.valueOf(rowIndex+1);
			case 1:{
				Object obj = model.get(rowIndex);
				if(obj instanceof Triplet) return UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(((Triplet)obj).getTripletID().name());
				else if(obj instanceof PTOCAControlSequence){
					String s = ((PTOCAControlSequence)obj).getCsi().getControlSequenceFunctionType().name();
					s = s.split("_",2)[0];
					return s;
				}
				else return UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(obj.getClass().getSimpleName());
			}
			case 2: return model.get(rowIndex);
			default: return "???";
			}
		}
		
		public void addItem(Object item, int position){
			model.add(position,item);
		}
		
		public void removeItem(Object item){
			model.remove(item);
		}
	}

	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if(column==0){
				lableCounter.setText(value.toString());
				if(isSelected) 
					lableCounter.setBackground(Color.lightGray);
				else lableCounter.setBackground(Color.white);
				return lableCounter;
			}else if(column==1){
				String txt = value.toString();
				if(txt.equalsIgnoreCase("TRN")) lableComponent.setForeground(Color.BLUE);
				else  lableComponent.setForeground(Color.BLACK);
				lableComponent.setText(txt);
				if(isSelected) 
					lableComponent.setBackground(Color.lightGray);
				else lableComponent.setBackground(Color.white);
				return lableComponent;
			}else if(column==2){
				lableDetail.setText(createComplexRenderingComponent(value));
				if(value instanceof TRN_TransparentData) lableDetail.setForeground(Color.BLUE);
				else  lableDetail.setForeground(Color.BLACK);
				int width = (int)lableDetail.getPreferredSize().getWidth();
				
				if(width>maxWidthDetailField){
					maxWidthDetailField = width;
					getColumnModel().getColumn(2).setPreferredWidth(width);
				}
				if(isSelected) 
					lableDetail.setBackground(Color.lightGray);
				else lableDetail.setBackground(Color.white);
				return lableDetail;
			}else{
				return new JLabel("???");
			}
	}
	
	private String createComplexRenderingComponent(Object triplet){
		
		StringBuilder sb = new StringBuilder();
		boolean isFirst=true;
		for(Field field : UtilReflection.getAFPFields(triplet.getClass())){
			
			String compValue = buildValueLabel(field, triplet);
			if(compValue==null) continue;
			

			if(isFirst) isFirst=false;
			else sb.append(", ");
			
			
			// sb.append(UtilCharacterEncoding.reduceLabel(field.getName())).append(" = ");
			sb.append(field.getName()).append("=");
			if(field.getType()==String.class) sb.append('"');
			sb.append(compValue);
			if(field.getType()==String.class) sb.append('"');
		}
		return sb.toString();
	}
	
	
	@SuppressWarnings({"rawtypes"})
	private static String buildValueLabel(Field field, Object instance){
		if(field==null || instance==null) return null;
		Object fieldValue = getFieldValue(field, instance);
		if(fieldValue==null) return null;
		

		Class<?> fieldType = field.getType();
		Type genericType = field.getGenericType();
		

		String component = null;		
		if(fieldType.isArray()){
			component = null;
		}else if (genericType instanceof ParameterizedType) {
			if(EnumSet.class.isAssignableFrom(fieldType)){
				StringBuilder txt = new StringBuilder("{");
				EnumSet es = (EnumSet)fieldValue;
				boolean isFirst = true;
				for(Object e : es.toArray()) {
					if(isFirst) isFirst = false;
					else txt.append(", ");
					txt.append(UtilCharacterEncoding.addBlankBeforeUpperCaseGroupAndDigitGroup(e.toString()));
				}
				txt.append("}");
				
				component = txt.toString();
			}else{
				component = null;
			}
		}else if(isAFPType(fieldType)){
			component = null;
		}else{
			component = "" + fieldValue;
		}	

		return component;
	}
}
