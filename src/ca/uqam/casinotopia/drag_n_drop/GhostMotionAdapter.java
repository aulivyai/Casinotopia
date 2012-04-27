package ca.uqam.casinotopia.drag_n_drop;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.SwingUtilities;

public class GhostMotionAdapter extends MouseMotionAdapter {
	private GhostGlassPane glassPane;

	public GhostMotionAdapter(GhostGlassPane glassPane) {
		this.glassPane = glassPane;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Component c = e.getComponent();

		Point p = (Point) e.getPoint().clone();
		SwingUtilities.convertPointToScreen(p, c);
		SwingUtilities.convertPointFromScreen(p, this.glassPane);
		this.glassPane.setPoint(p);

		this.glassPane.repaint();
	}
}