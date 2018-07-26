package com.cobotview.plugin.ui.newresource;

import org.eclipse.osgi.util.NLS;

public class ResourceMessages extends NLS {
	private static final String BUNDLE_NAME = "com.cobotview.plugin.ui.newresource.messages";

	public static String NewProject_windowTitle;
	public static String NewProject_title;
	public static String NewProject_description;
	public static String NewProject_errorMessage;
	public static String NewProject_errorOpeningWindow;
	public static String NewProject_internalError;
	public static String NewProject_caseVariantExistsError;
	public static String WizardNewProjectCreationPage_nameLabel;
	public static String WizardNewProjectDefaultLocation;
	public static String WizardNewProjectCreationPage_projectNameEmpty;
	public static String WizardNewProjectCreationPage_projectExistsMessage;

	static {
		NLS.initializeMessages(BUNDLE_NAME, ResourceMessages.class);
	}
}
