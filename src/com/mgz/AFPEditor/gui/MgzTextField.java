package com.mgz.AFPEditor.gui;

import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.mgz.AFPEditor.gui.ComplexEditor.UniversalChangeListener;


public class MgzTextField extends JFormattedTextField implements IEditorComponent,DocumentListener{
	private static final long serialVersionUID = 1L;
	private UniversalChangeListener universalChangeListener;
	
	public MgzTextField(int columns) {
		super();
		this.setColumns(columns);
		this.getDocument().addDocumentListener(this);
	}

    /**
     * Creates a <code>JFormattedTextField</code> with no
     * <code>AbstractFormatterFactory</code>. Use <code>setMask</code> or
     * <code>setFormatterFactory</code> to configure the
     * <code>JFormattedTextField</code> to edit a particular type of
     * value.
     */
    public MgzTextField() {
        super();
        this.getDocument().addDocumentListener(this);
    }

    /**
     * Creates a JFormattedTextField with the specified value. This will
     * create an <code>AbstractFormatterFactory</code> based on the
     * type of <code>value</code>.
     *
     * @param value Initial value for the JFormattedTextField
     */
    public MgzTextField(Object value) {
        super(value);
        this.getDocument().addDocumentListener(this);
    }

    /**
     * Creates a <code>JFormattedTextField</code>. <code>format</code> is
     * wrapped in an appropriate <code>AbstractFormatter</code> which is
     * then wrapped in an <code>AbstractFormatterFactory</code>.
     *
     * @param format Format used to look up an AbstractFormatter
     */
    public MgzTextField(java.text.Format format) {
        super(format);
        this.getDocument().addDocumentListener(this);
    }

    /**
     * Creates a <code>JFormattedTextField</code> with the specified
     * <code>AbstractFormatter</code>. The <code>AbstractFormatter</code>
     * is placed in an <code>AbstractFormatterFactory</code>.
     *
     * @param formatter AbstractFormatter to use for formatting.
     */
    public MgzTextField(AbstractFormatter formatter) {
        super(formatter);
        this.getDocument().addDocumentListener(this);
    }

    /**
     * Creates a <code>JFormattedTextField</code> with the specified
     * <code>AbstractFormatterFactory</code>.
     *
     * @param factory AbstractFormatterFactory used for formatting.
     */
    public MgzTextField(AbstractFormatterFactory factory) {
        super(factory);
        this.getDocument().addDocumentListener(this);
    }

    /**
     * Creates a <code>JFormattedTextField</code> with the specified
     * <code>AbstractFormatterFactory</code> and initial value.
     *
     * @param factory <code>AbstractFormatterFactory</code> used for
     *        formatting.
     * @param currentValue Initial value to use
     */
    public MgzTextField(AbstractFormatterFactory factory, Object currentValue) {
        super(factory, currentValue);
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
