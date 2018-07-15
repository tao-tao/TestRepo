package com.cobotview.plugin.ui.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchPage;

public class CobotViewPerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
//		WorkbenchPage page = (WorkbenchPage) workbench.getActiveWorkbenchWindow().getActivePage();
//
//		page.hideActionSet("org.eclipse.ui.externaltools.ExternalToolMenuDelegateToolbar");
//		page.hideActionSet("org.eclipse.debug.ui.launchActionSet");
//		page.getActionBars().getMenuManager().updateAll(true);
	}
}
