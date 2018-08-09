package com.cobotview.plugin.ui.navigator;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.common.CommandException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.navigator.resources.ResourceToItemsMapper;
import org.eclipse.ui.internal.navigator.resources.plugin.WorkbenchNavigatorPlugin;
import org.eclipse.ui.navigator.CommonNavigator;

import com.cobotview.plugin.ui.commands.ICobotViewCommandConstants;

/**
 * @author TaoTao
 *
 */
@SuppressWarnings("restriction")
public class CobotViewNavigator extends CommonNavigator {

	public static final String ID = "com.cobotview.plugin.ui.cobotviewNavigator";

	@Override
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);

		if (!false)
			getCommonViewer().setMapper(new ResourceToItemsMapper(getCommonViewer()));
	}

	@Override
	protected void handleDoubleClick(DoubleClickEvent anEvent) {
		ICommandService commandService = getViewSite().getService(ICommandService.class);
		Command openProjectCommand = commandService.getCommand(IWorkbenchCommandConstants.PROJECT_OPEN_PROJECT);
		IStructuredSelection selection = (IStructuredSelection) anEvent.getSelection();
		Object element = selection.getFirstElement();

		if (openProjectCommand != null && openProjectCommand.isHandled() && openProjectCommand.isEnabled())
		{
			if (element instanceof IProject && !((IProject) element).isOpen()) {
				try {
					openProjectCommand.executeWithChecks(new ExecutionEvent());
				} catch (CommandException ex) {
					IStatus status = WorkbenchNavigatorPlugin.createErrorStatus("'Open Project' failed", ex); //$NON-NLS-1$
					WorkbenchNavigatorPlugin.getDefault().getLog().log(status);
				}
				return;
			}
		}

		if (element instanceof IFile)
		{
			try {
				IFile file = (IFile) element;
				String fileExtension = file.getFileExtension();

				if (fileExtension == null || fileExtension.endsWith("exe") || fileExtension.endsWith("dll")
						|| fileExtension.isEmpty())
				{
					Command openFileCommand = commandService.getCommand(ICobotViewCommandConstants.OPEN_EXE);
					openFileCommand.executeWithChecks(new ExecutionEvent());
				}else if (fileExtension.endsWith("c"))
				{
					Command openCCommand = commandService.getCommand(ICobotViewCommandConstants.OPEN_C);
					openCCommand.executeWithChecks(new ExecutionEvent());
				}else if (fileExtension.endsWith("asm"))
				{
					Command openASMCommand = commandService.getCommand(ICobotViewCommandConstants.OPEN_ASM);
					openASMCommand.executeWithChecks(new ExecutionEvent());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.handleDoubleClick(anEvent);
	}
}
