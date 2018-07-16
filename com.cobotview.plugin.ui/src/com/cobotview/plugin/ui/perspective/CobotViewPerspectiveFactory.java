package com.cobotview.plugin.ui.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchIntroManager;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.intro.IIntroPart;

public class CobotViewPerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();

		for( IWorkbenchWindow window : windows )
		{
//			if( window instanceof WorkbenchWindow )
//			{
//				WorkbenchIntroManager introManager = (WorkbenchIntroManager) window.getWorkbench().getIntroManager();
//				
//				IIntroPart introPart = introManager.getIntro();
//				
//				if( introPart == null )
//				{
//					introPart = introManager.showIntro(window, true);
//					introManager.closeIntro(introPart);
//				}
//			}
		}
//		WorkbenchPage page = (WorkbenchPage) workbench.getActiveWorkbenchWindow().getActivePage();
//
//		page.hideActionSet("org.eclipse.ui.externaltools.ExternalToolMenuDelegateToolbar");
//		page.hideActionSet("org.eclipse.debug.ui.launchActionSet");
//		page.getActionBars().getMenuManager().updateAll(true);
	}
}
