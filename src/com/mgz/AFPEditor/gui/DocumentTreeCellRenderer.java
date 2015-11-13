package com.mgz.AFPEditor.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

import sun.swing.DefaultLookup;

public class DocumentTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;

	/**
	 * Configures the renderer based on the passed in components.
	 * The value is set from messaging the tree with
	 * <code>convertValueToText</code>, which ultimately invokes
	 * <code>toString</code> on <code>value</code>.
	 * The foreground color is set based on the selection and the icon
	 * is set based on the <code>leaf</code> and <code>expanded</code>
	 * parameters.
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel,
			boolean expanded,
			boolean leaf, int row,
			boolean hasFocus) {
		String stringValue = tree.convertValueToText(
				value, sel, expanded, leaf, row, hasFocus);

		this.hasFocus = hasFocus;
		setText(stringValue);

		Color fg = null;

		JTree.DropLocation dropLocation = tree.getDropLocation();
		if (dropLocation != null
				&& dropLocation.getChildIndex() == -1
				&& tree.getRowForPath(dropLocation.getPath()) == row) {

			Color col = DefaultLookup.getColor(this, ui, "Tree.dropCellForeground");
			if (col != null) {
				fg = col;
			} else {
				fg = getTextSelectionColor();
			}

		} else if (sel) {
			fg = getTextSelectionColor();
		} else {
			fg = getTextNonSelectionColor();
		}

		setForeground(fg);

		Icon icon = null;
		if (leaf) {
			icon = getLeafIcon();
		} else if (expanded) {
			icon = getOpenIcon();
		} else {
			icon = getClosedIcon();
		}

		if (!tree.isEnabled()) {
			setEnabled(false);
			LookAndFeel laf = UIManager.getLookAndFeel();
			Icon disabledIcon = laf.getDisabledIcon(tree, icon);
			if (disabledIcon != null) icon = disabledIcon;
			setDisabledIcon(icon);
		} else {
			setEnabled(true);
			setIcon(icon);
		}
		setComponentOrientation(tree.getComponentOrientation());

		selected = sel;

		
		return this;
	}


}
