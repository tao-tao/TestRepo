package com.cobotview.plugin.ui;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

public class CobotViewPerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		WorkbenchWindow workbenchWindow =  (WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow();	
		IMenuManager menuManager =  workbenchWindow.getMenuManager();
		IContributionItem[] menuItems = menuManager.getItems();

		for( IContributionItem item : menuItems )
		{
			if(item.getId().equals("CobotFile")
				|| item.getId().equals("CobotEdit")
				|| item.getId().equals("CobotGraph")
				|| item.getId().equals("CobotSearch")
				|| item.getId().equals("CobotWindow")
				|| item.getId().equals("CobotHelp"))
			{
				item.setVisible(true);
			}else {
				item.setVisible(false);
			}
		}

		String[] itemIds = { "org.eclipse.jdt.ui.SearchActionSet","org.eclipse.search.searchActionSet" };

        for (String itemId : itemIds) {
            IContributionItem item = menuManager.find(itemId);
            if (item != null) {
                item.setVisible(false);
                menuManager.update();
            }
        }

		menuManager.updateAll(true);
	}

}
