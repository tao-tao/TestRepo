package com.cobotview.plugin.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.ide.undo.CreateProjectOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.StatusUtil;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import com.cobotview.plugin.ui.newresource.ResourceMessages;

@SuppressWarnings("restriction")
public class CobotViewNewWizard extends BasicNewResourceWizard implements INewWizard {

	public static final String WIZARD_ID = "com.cobotview.plugin.ui.CobotViewNewWizard";
	private NewBinaryProjectCreationPage creationPage;

	private IProject newProject;

	public CobotViewNewWizard() {
		this.setHelpAvailable(false);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		setNeedsProgressMonitor(true);
		setWindowTitle(ResourceMessages.NewProject_windowTitle);
	}

	public void addPages() {
		super.addPages();
		creationPage = new NewBinaryProjectCreationPage("Create a new project");
		creationPage.setTitle(ResourceMessages.NewProject_title);
		creationPage.setDescription(ResourceMessages.NewProject_description);
		this.addPage(creationPage);
	}

	private IProject createNewProject() {
		if (newProject != null) {
			return newProject;
		}

		// get a project handle
		final IProject newProjectHandle = creationPage.getProjectHandle();

		// get a project descriptor
		URI location = null;

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProjectDescription description = workspace
				.newProjectDescription(newProjectHandle.getName());
		description.setLocationURI(location);

		// create the new project operation
		IRunnableWithProgress op = monitor -> {
CreateProjectOperation op1 = new CreateProjectOperation(
			description, ResourceMessages.NewProject_windowTitle);
try {
		// see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=219901
		// directly execute the operation so that the undo state is
		// not preserved.  Making this undoable resulted in too many
		// accidental file deletions.
		op1.execute(monitor, WorkspaceUndoUtil
			.getUIInfoAdapter(getShell()));
} catch (ExecutionException e) {
		throw new InvocationTargetException(e);
}
};

		// run the new project creation operation
		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			return null;
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			if (t instanceof ExecutionException
					&& t.getCause() instanceof CoreException) {
				CoreException cause = (CoreException) t.getCause();
				StatusAdapter status;
				if (cause.getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
					status = new StatusAdapter(
							StatusUtil
									.newStatus(
											IStatus.WARNING,
											NLS.bind(
															ResourceMessages.NewProject_caseVariantExistsError,
															newProjectHandle.getName()), cause));
				} else {
					status = new StatusAdapter(StatusUtil.newStatus(cause
							.getStatus().getSeverity(),
							ResourceMessages.NewProject_errorMessage, cause));
				}
				status.setProperty(StatusAdapter.TITLE_PROPERTY,
						ResourceMessages.NewProject_errorMessage);
				StatusManager.getManager().handle(status, StatusManager.BLOCK);
			} else {
				StatusAdapter status = new StatusAdapter(new Status(
						IStatus.WARNING, IDEWorkbenchPlugin.IDE_WORKBENCH, 0,
						NLS.bind(ResourceMessages.NewProject_internalError, t
								.getMessage()), t));
				status.setProperty(StatusAdapter.TITLE_PROPERTY,
						ResourceMessages.NewProject_errorMessage);
				StatusManager.getManager().handle(status,
						StatusManager.LOG | StatusManager.BLOCK);
			}
			return null;
		}

		newProject = newProjectHandle;

		return newProject;
	}

	@Override
	public void setHelpAvailable(boolean b) {
		super.setHelpAvailable(b);
	}

	@Override
	public boolean isHelpAvailable() {
		return false;
	}

	@Override
	public boolean performFinish() {
		return false;
	}

}
