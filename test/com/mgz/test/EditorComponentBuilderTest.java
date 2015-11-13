package com.mgz.test;

import static org.junit.Assert.*;

import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Test;

import com.mgz.AFPEditor.gui.ComplexEditor;
import com.mgz.afp.base.StructuredField;
import com.mgz.afp.base.StructuredFieldIntroducer;
import com.mgz.afp.enums.SFTypeID;
import com.mgz.afp.parser.AFPParser;
import com.mgz.util.UtilFile;

public class EditorComponentBuilderTest {
	private static String imagePath = "editorComponentScreenshots";
	private static String imagePathTest = imagePath + "/" + "test/";
	private static String imagePathAccepted = imagePath + "/" + "accepted/";
	
	@Test
	public void testBuildEditor() throws IOException {
		JFrame hostingFrame = new JFrame();
		hostingFrame.setVisible(true);
		
		for(SFTypeID sfTypeID : SFTypeID.values()){
			File testFile = new File(imagePathTest + sfTypeID.name() + ".png");
			testFile.mkdirs();
			File acceptedFile = new File(imagePathAccepted+ sfTypeID.name() + ".png");
			
			
			StructuredFieldIntroducer sfi = new StructuredFieldIntroducer();
			sfi.setSFTypeID(sfTypeID);
			StructuredField sf = AFPParser.createSFInstance(sfi);

			JPanel editor = new ComplexEditor(sf.getClass(),sf,sf.getClass().getSimpleName());
			assertNotNull(sf.getClass().getName(),editor);
			
			hostingFrame.setContentPane(editor);
			hostingFrame.pack();
			
			Container cont = hostingFrame.getContentPane();
			
			BufferedImage bim = new BufferedImage(cont.getWidth(), cont.getHeight(), BufferedImage.TYPE_INT_BGR);
			cont.paint(bim.getGraphics());
			ImageIO.write(bim, "png", testFile);
			
			assertTrue(testFile.getAbsolutePath()+" doesn't exist.", testFile.exists());
			
			if(acceptedFile.exists()){
				byte[] digTest = UtilFile.digestFile(testFile);
				byte[] digAccepted = UtilFile.digestFile(acceptedFile);
				assertArrayEquals("Editor screenshot has changed. " + testFile.getName()+ " != " + acceptedFile.getName(), digAccepted, digTest);
			}
		}
	}
}
