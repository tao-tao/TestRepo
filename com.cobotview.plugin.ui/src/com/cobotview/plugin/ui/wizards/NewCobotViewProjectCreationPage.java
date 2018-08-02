package com.cobotview.plugin.ui.wizards;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.BidiUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.dialogs.WorkingSetGroup;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.dialogs.FileSystemSelectionArea;
import org.eclipse.ui.internal.ide.dialogs.IDEResourceInfoUtils;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea.IErrorMessageReporter;
import org.eclipse.ui.internal.ide.filesystem.FileSystemConfiguration;
import org.eclipse.ui.internal.ide.filesystem.FileSystemSupportRegistry;

import com.cobotview.plugin.ui.dialogs.CobotViewResourceInfoUtils;
import com.cobotview.plugin.ui.newresource.ResourceMessages;

@SuppressWarnings("restriction")
public class NewCobotViewProjectCreationPage extends WizardPage {

	private static final int SIZING_TEXT_FIELD_WIDTH = 250;

    private String initialProjectFieldValue;
    Text projectNameField;
	private Text locationPathField;
    private FileSystemSelectionArea fileSystemSelectionArea;
    private String userPath = IDEResourceInfoUtils.EMPTY_STRING;
	private IProject existingProject;
	private static String BROWSE_LABEL = ResourceMessages.ProjectLocationSelectionDialog_browseLabel;
	private IErrorMessageReporter errorReporter;
	private Label locationLabel;
	private Button browseButton;
	private Button button;
	private static final String FILE_SCHEME = "file";
	private static final String SAVED_LOCATION_ATTR = "OUTSIDE_LOCATION";
	private String projectName = IDEResourceInfoUtils.EMPTY_STRING;
	private ProjectContentsLocationArea locationArea;
	private WorkingSetGroup workingSetGroup;

    private Listener nameModifyListener = e -> {
	    boolean valid = validatePage();
	    setPageComplete(valid);
	};

    public NewCobotViewProjectCreationPage(String pageName) {
		super(pageName);
		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);

        initializeDialogUnits(parent);

        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        createProjectNameGroup(composite);

        locationArea = new ProjectContentsLocationArea(getErrorReporter(), composite);
        if(initialProjectFieldValue != null) {
			locationArea.updateProjectName(initialProjectFieldValue);
		}

		// Scale the button based on the rest of the dialog
        Button button = locationArea.getBrowseButton();
        button.setText(BROWSE_LABEL);
		setButtonLayoutData(button);

        setPageComplete(validatePage());
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
        Dialog.applyDialogFont(composite);
	}

	@SuppressWarnings("unused")
	private void createBrowseButtonGroup(Composite composite) {
		int columns = 4;

		// project specification group
		Composite projectGroup = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = columns;
		projectGroup.setLayout(layout);
		projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		button = new Button(projectGroup, SWT.CHECK | SWT.RIGHT);
		button.setText(ResourceMessages.ProjectLocationSelectionDialog_useDefaultLabel);
		button.setSelection(true);
		GridData buttonData = new GridData();
		buttonData.horizontalSpan = columns;
		button.setLayoutData(buttonData);

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean useDefaults = button.getSelection();

				if (useDefaults) {
					userPath = locationPathField.getText();
					locationPathField.setText(TextProcessor
							.process(getDefaultPathDisplayString()));
				} else {
					locationPathField.setText(TextProcessor.process(userPath));
				}
				String error = checkValidLocation();
				errorReporter.reportError(error,
						error != null && error.equals(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectLocationEmpty));
				setUserAreaEnabled(!useDefaults);
			}
		});

		createUserEntryArea(projectGroup);

		setUserAreaEnabled(false);
	}

	private void setUserAreaEnabled(boolean enabled) {

		locationLabel.setEnabled(enabled);
		locationPathField.setEnabled(enabled);
		browseButton.setEnabled(enabled);
		if (fileSystemSelectionArea != null) {
			fileSystemSelectionArea.setEnabled(enabled);
		}
	}

	public String checkValidLocation() {

		String locationFieldContents = locationPathField.getText();
		if (locationFieldContents.length() == 0) {
			return IDEWorkbenchMessages.WizardNewProjectCreationPage_projectLocationEmpty;
		}

		URI newPath = getProjectLocationURI();
		if (newPath == null) {
			return IDEWorkbenchMessages.ProjectLocationSelectionDialog_locationError;
		}

		if (existingProject != null) {
			URI projectPath = existingProject.getLocationURI();
			if (projectPath != null && projectPath.getPath().equals(newPath.getPath())) {
				return IDEWorkbenchMessages.ProjectLocationSelectionDialog_locationIsSelf;
			}
		}

		if (!isDefault()) {
			IStatus locationStatus = ResourcesPlugin.getWorkspace()
					.validateProjectLocationURI(existingProject, newPath);

			if (!locationStatus.isOK()) {
				return locationStatus.getMessage();
			}
		}

		return null;
	}

	public boolean isDefault() {
		return button.getSelection();
	}

	public URI getProjectLocationURI() {

		FileSystemConfiguration configuration = getSelectedConfiguration();
		if (configuration == null) {
			return null;
		}

		return configuration.getContributor().getURI(
				locationPathField.getText());
	}

	private FileSystemConfiguration getSelectedConfiguration() {
		if (fileSystemSelectionArea == null) {
			return FileSystemSupportRegistry.getInstance()
					.getDefaultConfiguration();
		}

		return fileSystemSelectionArea.getSelectedConfiguration();
	}

	@Override
	public void setMessage(String message) {
		super.setMessage(message);
	}

	private String getDefaultPathDisplayString() {

		URI defaultURI = null;
		if (existingProject != null) {
			defaultURI = existingProject.getLocationURI();
		}

		// Handle files specially. Assume a file if there is no project to query
		if (defaultURI == null || defaultURI.getScheme().equals(FILE_SCHEME)) {
			return Platform.getLocation().append(projectName).toOSString();
		}
		return defaultURI.toString();
	}

	private void createUserEntryArea(Composite composite) {
		locationLabel = new Label(composite, SWT.NONE);
		locationLabel
				.setText(ResourceMessages.ProjectLocationSelectionDialog_locationLabel);

		// project location entry field
		locationPathField = new Text(composite, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		data.horizontalSpan = 2;
		locationPathField.setLayoutData(data);

		// browse button
		browseButton = new Button(composite, SWT.PUSH);
		browseButton.setText(BROWSE_LABEL);
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				handleLocationBrowseButtonPressed();
			}
		});

		createFileSystemSelection(composite);

		locationPathField.setText(TextProcessor
					.process(getDefaultPathDisplayString()));
		if (existingProject == null) {
			locationPathField.setText(CobotViewResourceInfoUtils.EMPTY_STRING);
		} else {
			locationPathField.setText(TextProcessor.process(existingProject.getLocation().toOSString()));
		}

		locationPathField.addModifyListener(e -> errorReporter.reportError(checkValidLocation(), false));
	}

	private void handleLocationBrowseButtonPressed() {

		String selectedDirectory = null;
		String dirName = getPathFromLocationField();

		if (!dirName.equals(CobotViewResourceInfoUtils.EMPTY_STRING)) {
			dirName = CobotViewResourceInfoUtils.EMPTY_STRING;
		} else {
			String value = getDialogSettings().get(SAVED_LOCATION_ATTR);
			if (value != null) {
				dirName = value;
			}
		}

		FileSystemConfiguration config = getSelectedConfiguration();
		if (config== null || config.equals(
				FileSystemSupportRegistry.getInstance()
						.getDefaultConfiguration())) {
			DirectoryDialog dialog = new DirectoryDialog(locationPathField
					.getShell(), SWT.SHEET);
			dialog
					.setMessage(ResourceMessages.ProjectLocationSelectionDialog_directoryLabel);

			dialog.setFilterPath(dirName);

			selectedDirectory = dialog.open();

		} else {
			URI uri = getSelectedConfiguration().getContributor()
					.browseFileSystem(dirName, browseButton.getShell());
			if (uri != null)
				selectedDirectory = uri.toString();
		}

		if (selectedDirectory != null) {
			updateLocationField(selectedDirectory);
			getDialogSettings().put(SAVED_LOCATION_ATTR, selectedDirectory);
		}
	}

	private void updateLocationField(String selectedPath) {
		locationPathField.setText(TextProcessor.process(selectedPath));
	}

	public IWorkingSet[] getSelectedWorkingSets() {
		return workingSetGroup == null ? new IWorkingSet[0] : workingSetGroup
				.getSelectedWorkingSets();
	}

	private String getPathFromLocationField() {
		URI fieldURI;
		try {
			fieldURI = new URI(locationPathField.getText());
		} catch (URISyntaxException e) {
			return locationPathField.getText();
		}
		String path= fieldURI.getPath();
		return path != null ? path : locationPathField.getText();
	}

	private void createFileSystemSelection(Composite composite) {
		if (FileSystemSupportRegistry.getInstance().hasOneFileSystem()) {
			return;
		}

		new Label(composite, SWT.NONE);

		fileSystemSelectionArea = new FileSystemSelectionArea();
		fileSystemSelectionArea.createContents(composite);
	}

	private final void createProjectNameGroup(Composite parent) {
        // project specification group
        Composite projectGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // new project label
        Label projectLabel = new Label(projectGroup, SWT.NONE);
        projectLabel.setText(ResourceMessages.WizardNewProjectCreationPage_nameLabel);
        projectLabel.setFont(parent.getFont());

        // new project name entry field
        projectNameField = new Text(projectGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        projectNameField.setLayoutData(data);
        projectNameField.setFont(parent.getFont());

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialProjectFieldValue != null) {
			projectNameField.setText(initialProjectFieldValue);
		}
        projectNameField.addListener(SWT.Modify, nameModifyListener);
        BidiUtils.applyBidiProcessing(projectNameField, BidiUtils.BTD_DEFAULT);
    }

	private IErrorMessageReporter getErrorReporter() {
		return (errorMessage, infoOnly) -> {
			if (infoOnly) {
				setMessage(errorMessage, IStatus.INFO);
				setErrorMessage(null);
			}
			else
				setErrorMessage(errorMessage);
			boolean valid = errorMessage == null;
			if(valid) {
				valid = validatePage();
			}

			setPageComplete(valid);
		};
	}

    public IProject getProjectHandle() {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(
                getProjectName());
    }

    public String getProjectName() {
        if (projectNameField == null) {
			return initialProjectFieldValue;
		}

        return getProjectNameFieldValue();
    }

    private String getProjectNameFieldValue() {
        if (projectNameField == null) {
			return "";
		}

        return projectNameField.getText().trim();
    }

    @Override
	public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
			projectNameField.setFocus();
		}
    }

    protected boolean validatePage() {
        IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();

        String projectFieldContents = getProjectNameFieldValue();
        if (projectFieldContents.equals("")) {
            setErrorMessage(null);
            setMessage(ResourceMessages.WizardNewProjectCreationPage_projectNameEmpty);
            return false;
        }

        IStatus nameStatus = workspace.validateName(projectFieldContents,
                IResource.PROJECT);
        if (!nameStatus.isOK()) {
            setErrorMessage(nameStatus.getMessage());
            return false;
        }

        IProject handle = getProjectHandle();
        if (handle.exists()) {
            setErrorMessage(ResourceMessages.WizardNewProjectCreationPage_projectExistsMessage);
            return false;
        }

        setErrorMessage(null);
        setMessage(null);
        return true;
    }
}
