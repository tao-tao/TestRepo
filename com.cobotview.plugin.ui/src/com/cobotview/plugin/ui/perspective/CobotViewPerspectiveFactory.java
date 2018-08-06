package com.cobotview.plugin.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.cobotview.plugin.ui.navigator.CobotViewNavigator;
import com.cobotview.plugin.ui.views.FunctionAddressView;

public class CobotViewPerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
 		String editorArea = layout.getEditorArea();
		IFolderLayout project= layout.createFolder(CobotViewNavigator.ID, IPageLayout.LEFT, (float)0.25, editorArea);
		project.addView(CobotViewNavigator.ID);

		IFolderLayout functionAddressView = layout.createFolder(FunctionAddressView.ID, IPageLayout.BOTTOM, (float)0.6, CobotViewNavigator.ID);
		functionAddressView.addView(FunctionAddressView.ID);
	}
}
