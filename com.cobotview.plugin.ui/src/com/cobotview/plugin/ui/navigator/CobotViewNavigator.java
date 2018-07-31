package com.cobotview.plugin.ui.navigator;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.common.CommandException;
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
		if (openProjectCommand != null && openProjectCommand.isHandled() && openProjectCommand.isEnabled()) {
			IStructuredSelection selection = (IStructuredSelection) anEvent
					.getSelection();
			Object element = selection.getFirstElement();
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
		super.handleDoubleClick(anEvent);
	}
}
