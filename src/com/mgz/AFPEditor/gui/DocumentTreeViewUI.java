package com.mgz.AFPEditor.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.mgz.AFPEditor.dom.DOMItem;
import com.mgz.AFPEditor.gui.MgzDocumentTreeView.EditorConfiguration;
import com.mgz.afp.base.StructuredField;
import com.mgz.util.UtilGUI;

public class DocumentTreeViewUI extends BasicTreeUI {
	boolean isLeftToRight=true;
	boolean isPaintLines=true;
	private AffineTransform at;
	
	EditorConfiguration config;

	public DocumentTreeViewUI(){
		at = new AffineTransform();
		at.setToQuadrantRotation(1);
	}

	protected void paintHorizontalPartOfLeg(Graphics g, Rectangle clipBounds,
			Insets insets, Rectangle bounds,
			TreePath path, int row,
			boolean isExpanded,
			boolean hasBeenExpanded, boolean
			isLeaf) 
	{
	}
	
	/**
	 * Paints the vertical part of the leg. The receiver should
	 * NOT modify <code>clipBounds</code>, <code>insets</code>.<p>
	 */
	protected void paintVerticalPartOfLeg(Graphics g, Rectangle clipBounds,
			Insets insets, TreePath path) {
		Graphics2D g2d = (Graphics2D)g;

		int depth = path.getPathCount() - 1;
		if (depth == 0 && !getShowsRootHandles() && !isRootVisible()) {
			return;
		}
		int lineX = getRowX(-1, depth + 1);
		if (isLeftToRight) {
			lineX = lineX - getRightChildIndent() + insets.left;
		}
		else {
			lineX = tree.getWidth() - lineX - insets.right +
					getRightChildIndent() - 1;
		}
		Rectangle lastChildBounds = getPathBounds(tree, getLastChildPath(path));
		if(lastChildBounds == null)return;
		Rectangle parentBounds = getPathBounds(tree, path);

		int clipTop = clipBounds.y;
		int clipBottom = clipBounds.y + clipBounds.height;
		int top;
		if(parentBounds == null) {
			top = Math.max(insets.top + getVerticalLegBuffer(), clipTop);
		}else{
			top = Math.max(parentBounds.y + parentBounds.height + getVerticalLegBuffer(), clipTop);
		}

		if(depth == 0 && !isRootVisible()) {
			TreeModel model = getModel();

			if(model != null) {
				Object root = model.getRoot();

				if(model.getChildCount(root) > 0) {
					parentBounds = getPathBounds(tree, path.pathByAddingChild(model.getChild(root, 0)));
					if(parentBounds != null)
						top = Math.max(insets.top + getVerticalLegBuffer(),
								parentBounds.y +
								parentBounds.height / 2);
				}
			}
		}

		int bottom = Math.min(lastChildBounds.y +
				(lastChildBounds.height / 2), clipBottom);

		if (top <= bottom) {
			int sigMargin = 50;

			Shape oldClip = g.getClip();
			Font oldFont = g.getFont();
			Color oldColor = g.getColor();
			
			DOMItem domItem = (DOMItem) path.getLastPathComponent();
			StructuredField sf = domItem.getStructuredField();
			Color color = config.isColorFull ? UtilGUI.getRepresentativeColor(sf.getClass()) : Color.lightGray;
			
			g.setColor(color);
			g.fillRect(lineX-6, top, 16, bottom-top);
			g2d.fillOval(lineX-7, bottom-7, 17, 16);
			g.setColor(color.darker());



			try{
				g.setFont(oldFont.deriveFont(Font.PLAIN,12f).deriveFont(at));
				g.setClip(lineX-5, top, 11, bottom-top +5);
				String sig = sf.getClass().getSimpleName().substring(0, 3);
				FontMetrics oldFm = g2d.getFontMetrics();
				int sigWidth = oldFm.stringWidth(sig) + sigMargin;

				int offsetY = top - (top % sigWidth) - sigWidth;
				while(offsetY<=bottom){
					g2d.drawString(sig,lineX-3, offsetY + sigMargin);
					offsetY+=sigWidth;
				}

			}catch(Throwable th){
				th.printStackTrace();
			}

			g.setColor(oldColor);
			g.setFont(oldFont);
			g.setClip(oldClip);
		}
	}

	public EditorConfiguration getConfig() {
		return config;
	}

	public void setConfig(EditorConfiguration config) {
		this.config = config;
	}



}
