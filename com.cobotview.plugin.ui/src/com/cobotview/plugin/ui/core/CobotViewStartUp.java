package com.cobotview.plugin.ui.core;

import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;

public class CobotViewStartUp implements IStartup {

	@Override
	public void earlyStartup() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().syncExec(new Runnable() {
			public void run() {
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				if (window != null) {
					try {
						IPerspectiveRegistry reg = workbench.getPerspectiveRegistry();
						window.getActivePage().setPerspective(reg.findPerspectiveWithId("com.cobotview.ui.perspective"));
						IIntroManager introManager = PlatformUI.getWorkbench().getIntroManager();
						IIntroPart intro = introManager.getIntro();
						introManager.closeIntro(intro);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

}
