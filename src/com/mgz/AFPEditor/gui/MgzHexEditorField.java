package com.mgz.AFPEditor.gui;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.bind.DatatypeConverter;

import com.mgz.AFPEditor.gui.ComplexEditor.UniversalChangeListener;
import com.mgz.util.UtilCharacterEncoding;

public class MgzHexEditorField extends JFormattedTextField implements IEditorComponent,DocumentListener{
	private static final long serialVersionUID = 1L;
    private boolean filtering= false;
	private UniversalChangeListener universalChangeListener;

	public MgzHexEditorField(int minSizeInBytes, int maxSizeInBytes){
		super();
		this.setColumns(2 + 3 * (maxSizeInBytes-1));
        getDocument().addDocumentListener(this);
		
	}

	@Override
	public void setValue(Object value) {
		String hexString = UtilCharacterEncoding.bytesToHexString((byte[])value);
		setText(hexString);
	}

	@Override
	public Object getValue() {
		return getBytes();
	}

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        filterText();
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        filterText();
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        filterText();
    }

    private void filterText()
    {
        if(filtering)
            return;
        filtering= true;

        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                String input= getText().toUpperCase();
                String filtered= "";
                int index= 0;

                // filter
                for(int i= 0; i < input.length(); i++)
                {
                    char c= input.charAt(i);
                    if("0123456789ABCDEF".indexOf(c) >= 0)  // hex only
                    {
                        filtered+= c;
                        if(index++ % 2 == 1 && i != input.length() - 1)
                            filtered+= " "; // whitespace after each byte
                    }
                }

                // limit size
                int maxBytes= 256;
                if(filtered.length() > 3 * maxBytes)
                {
                    filtered= filtered.substring(0, 3 * maxBytes);
                    Toolkit.getDefaultToolkit().beep();
                }

                setText(filtered);
                
                if(universalChangeListener!=null) universalChangeListener.onChange(getBytes());
                
                filtering= false;
            }
        });
    }

    private byte[] getBytes()
    {
        String text= getText().replace(" ", "");
        if(text.length() == 0)
            return null;

        if(text.length() % 2 == 1)
            text= text.substring(0, text.length()-1) + "0" + text.charAt(text.length()-1);

        byte[] bytes= DatatypeConverter.parseHexBinary(text);
        if(bytes.length == 0)
            return null;

        return bytes;
    }

	@Override
	public void addUniversalChangeListener(UniversalChangeListener changeListener) {
		this.universalChangeListener = changeListener;
	}
	
}
