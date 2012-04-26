package ca.uqam.casinotopia.drag_n_drop;

import java.awt.Point;

import ca.uqam.casinotopia.TypeMise;

public class MisesGhostPictureAdapter extends GhostPictureAdapter {

	TypeMise type;

	public MisesGhostPictureAdapter(GhostGlassPane glassPane, String picture, TypeMise type) {
		super(glassPane, picture);
		this.type = type;
	}

	@Override
	protected void sendGhostDropEvent(Point eventPoint) {
		this.fireGhostDropEvent(new MisesGhostDropEvent(eventPoint, this.type));
	}

}
