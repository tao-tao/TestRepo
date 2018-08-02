package com.cobotview.plugin.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.cobotview.plugin.ui.navigator.CobotViewNavigator;

public class CobotViewPerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
 		String editorArea = layout.getEditorArea();
		IFolderLayout project= layout.createFolder("com.cobotview.plugin.ui.projectView", IPageLayout.LEFT, (float)0.25, editorArea);
		project.addView(CobotViewNavigator.ID);
	}
}
