package com.cobotview.plugin.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class CobotViewNewWizard extends Wizard implements INewWizard {

	public static final String WIZARD_ID = "com.cobotview.plugin.ui.CobotViewNewWizard";

	private IProject newProject;

	public CobotViewNewWizard() {
		
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public boolean performFinish() {
		return false;
	}

}
