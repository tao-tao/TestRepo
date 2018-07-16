package com.cobotview.plugin.ui.core;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchWindowAdvisor;

public class CobotViewWorkbenchWindowAdvisor extends IDEWorkbenchWindowAdvisor {

	private IWorkbenchWindowConfigurer configurer;

	public CobotViewWorkbenchWindowAdvisor(IDEWorkbenchAdvisor wbAdvisor, IWorkbenchWindowConfigurer configurer) {
		super(wbAdvisor, configurer);
		this.configurer = configurer;
	}

	@Override
	protected IWorkbenchWindowConfigurer getWindowConfigurer() {
		return this.configurer;
	}

	@Override
	public void openIntro() {
		return;
	}

	@Override
	public void preWindowOpen() {
		configurer.setShowPerspectiveBar(false);
	}
}
