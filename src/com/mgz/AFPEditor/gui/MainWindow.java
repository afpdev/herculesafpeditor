package com.mgz.AFPEditor.gui;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mgz.AFPEditor.DocumentControler;
import com.mgz.AFPEditor.gui.MgzDocumentTreeView.EditorConfiguration;
import com.mgz.afp.exceptions.AFPParserException;
import com.mgz.afp.parser.AFPParserConfiguration;


public class MainWindow extends JFrame implements ActionListener, InternalFrameListener{
	private static final Rectangle BOUNDS_documentView = new Rectangle(12, 12, 800, 600);
	private static final Rectangle BOUNDS_mainWindow = new Rectangle(50, 50, 1024, 768);
	private static final String appTitle = "Hercules AFP Editor";
	private static final long serialVersionUID = 1L;
	private static Logger log;
	private static JFileChooser fc;
	
	private JDesktopPane desktopPane;
	private MainMenu mainMenu;

	private AFPParserConfiguration parserConfiguration;
	private EditorConfiguration editorConfiguration;

	public static void main(String[] args) {
		
		log = Logger.getLogger(MainMenu.class.getSimpleName());
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(true);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("AFP Files","afp","AFP"));
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.setTitle(appTitle);
					URL url = MainWindow.class.getResource("/page.png");
					window.setIconImage(new ImageIcon(url).getImage());
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainWindow() {
		parserConfiguration = new AFPParserConfiguration();
		parserConfiguration.setEscalateParsingErrors(false);
		parserConfiguration.setBuildShallow(true);

		editorConfiguration = new EditorConfiguration();
		
		setBounds(BOUNDS_mainWindow);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(mainMenu = new MainMenu(this));

		desktopPane = new JDesktopPane();
		desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		setContentPane(desktopPane);
	}

	private void newDocument(EditorConfiguration config, AFPParserConfiguration parserConfiguration){
		DocumentControler documentController = new DocumentControler(config,parserConfiguration);
		JInternalFrame iFrame = documentController.getDocumentView();
		iFrame.addInternalFrameListener(this);
		iFrame.setBounds(BOUNDS_documentView);
		iFrame.setVisible( true ); // show internal frame
		desktopPane.add( iFrame ); // attach internal frame
		try {
			iFrame.setSelected(true);
		} catch (PropertyVetoException e) {
			// NOP.
		}
	}

	private void openDocument(final File afpFile, EditorConfiguration config, AFPParserConfiguration parserConfig){
		final DocumentControler documentController = new DocumentControler(config,parserConfig);
		
		final DocumentView documentView = documentController.getDocumentView(); 
		documentView.setBounds(BOUNDS_documentView);
		documentView.setVisible( true ); // show internal frame
		
		documentView.addInternalFrameListener(this);
		desktopPane.add( documentView ); // attach internal frame
		try {
			documentView.setSelected(true);
		} catch (PropertyVetoException e) {
			// NOP. Should never happen.
		}
		
		parserConfiguration.resetCurrentAFPObjects();
		parserConfiguration.setInputStream(null);
		parserConfiguration.setAFPFile(afpFile);
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					documentController.loadAFPData(parserConfiguration);
				} catch (AFPParserException e1) {
					log.log(Level.SEVERE, "An exception occurred while parsing the AFP file '" + afpFile.getName() + "'.", e1);
					StringBuilder sb = new StringBuilder(e1.getMessage());
					if(e1.getCause()!=null) sb.append("\n").append(e1.getCause().getMessage());
					JOptionPane.showMessageDialog(documentView,
							e1.getMessage(),
							"AFP Parser Exception",
							JOptionPane.ERROR_MESSAGE
							);
				}
			}
		});
		


	}	

	public void internalFrameOpened(InternalFrameEvent e) {
		System.out.println("internalFrameOpened: " + e.paramString());

		ButtonGroup btnGroup = (ButtonGroup) mainMenu.getWindowMenu().getClientProperty(ButtonGroup.class);

		JRadioButtonMenuItem cbmi = new JRadioButtonMenuItem(e.getInternalFrame().getTitle());
		cbmi.setActionCommand(MainMenu.ACTIONCOMMAND_WINDOW_ACTIVATE);
		btnGroup.add(cbmi);

		cbmi.putClientProperty(DocumentView.class, e.getInternalFrame());
		cbmi.addActionListener(this);
		mainMenu.getWindowMenu().add(cbmi);

	}
	public void internalFrameActivated(InternalFrameEvent e) {
		System.out.println("internalFrameActivated: " + e.paramString());

		DocumentView internalFrame = (DocumentView)e.getInternalFrame();
		JMenu windowMenu = mainMenu.getWindowMenu();
		int itemCount = windowMenu.getItemCount();
		for(int i=0; i<itemCount; i++){
			JMenuItem item = windowMenu.getItem(i);
			if(item.getClientProperty(DocumentView.class)==internalFrame){
				item.setSelected(true);
				break;
			}
		}
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		JInternalFrame internalFrame = e.getInternalFrame();
		JMenu windowMenu = mainMenu.getWindowMenu();
		ButtonGroup btnGroup = (ButtonGroup) mainMenu.getWindowMenu().getClientProperty(ButtonGroup.class);

		int itemCount = windowMenu.getItemCount();
		for(int i=0; i<itemCount; i++){
			JMenuItem item = windowMenu.getItem(i);
			if(item.getClientProperty(DocumentView.class)==internalFrame){
				item.putClientProperty(DocumentView.class, null);
				btnGroup.remove(item);
				windowMenu.remove(item);
				break;
			}
		}

	}

	public void internalFrameDeactivated(InternalFrameEvent e) {}
	public void internalFrameClosing(InternalFrameEvent e) {}
	public void internalFrameIconified(InternalFrameEvent e) {}
	public void internalFrameDeiconified(InternalFrameEvent e) {}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if(MainMenu.ACTIONCOMMAND_FILE_OPEN.equals(command)){
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File afpFile = fc.getSelectedFile();
				if(afpFile!=null && afpFile.exists()){
					log.log(Level.FINEST,"Opening: " + afpFile.getName() + ".\n");
					openDocument(afpFile,editorConfiguration, parserConfiguration);
				}
			} else {
				log.log(Level.FINEST,"Open command canceled by user.\n");
			}			
		}else if(MainMenu.ACTIONCOMMAND_FILE_SAVE_AS.equals(command)){
			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				//This is where a real application would open the file.
				log.log(Level.FINEST,"Save to: " + file.getName() + ".\n");
			} else {
				log.log(Level.FINEST,"Save command cancelled by user.\n");
			}			
		}else if(MainMenu.ACTIONCOMMAND_FILE_NEW.equals(command)){
			newDocument(editorConfiguration, parserConfiguration);
		}else if(MainMenu.ACTIONCOMMAND_WINDOW_ACTIVATE.equals(command)){
			Object source = e.getSource();
			if(source!=null){
				JMenuItem mi = (JMenuItem) source;
				DocumentView dv = (DocumentView) mi.getClientProperty(DocumentView.class);
				try {
					dv.setSelected(true);
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}