package com.cobotview.plugin.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;


public class BibotNewProjectContentPage extends WizardPage {
	
    public BibotNewProjectContentPage(String pageName) {
        super(pageName);
    }

	// Widgets
	private Composite composite;
	private Table table;
	private Button buttonAdd;
	private Button btnRadioButton_1;
	private Text folderText;
	private Button buttonScan;
	private TableItem items[];
	//private static String []itemsData= new String[50];
	private static Map<String,String>itemsData= new HashMap<String,String>();
	private static String folderPath;
	public static Map<String,String> getItemsData() {
		return itemsData;
	}

	public static void setItemsData(Map<String,String> itemsData) {
		BibotNewProjectContentPage.itemsData = itemsData;
	}
	public static String getFolderPath() {
		return folderPath;
	}

	public static void setFolderPath(String folderPath) {
		BibotNewProjectContentPage.folderPath = folderPath;
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		itemsData.clear();
		folderPath = null;
		composite = new Composite(parent, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;
		
		GridData gdRb = new GridData();
		gdRb.horizontalSpan = 2;

		Button btnRadioButton = new Button(composite, SWT.RADIO);
		btnRadioButton.setBounds(34, 69, 97, 17);
		btnRadioButton.setText("\u6587\u4ef6\u5bfc\u5165"); //$NON-NLS-1$//"文件导入"
		btnRadioButton.setLayoutData(gdRb);
		btnRadioButton.setSelection(true);
		
		GridData gdTable = new GridData();
		gdTable.verticalSpan = 7;
		//final TableViewer tableViewer = new TableViewer(c, SWT.BORDER_SOLID | SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		table = new Table(composite, SWT.BORDER_SOLID | SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setItemCount(5);
		table.setLayoutData(gdTable);
		
		String[] columnNames = new String[] {"\u6587\u4ef6\u540d\u79f0", "\u8def\u5f84"}; //$NON-NLS-1$ //$NON-NLS-2$ //"文件名称", "路径"
		int[] columnWidths = new int[] {150, 200};
		int[] columnAlignments = new int[] {SWT.LEFT, SWT.LEFT};
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn tableColumn = new TableColumn(table, columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}
		buttonAdd = new Button (composite, SWT.PUSH);
		buttonAdd.setText ("\u6dfb\u52a0\u2026"); //$NON-NLS-1$ //"添加…"
		GridData gdAdd = new GridData();
		gdAdd.widthHint = 80;
		//buttonAdd.setBounds(34, 100, 150, 60);
		buttonAdd.setLayoutData(gdAdd);
		/*		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				button.setText("I Was Clicked");
			}
		});*/

		final Button buttonRemove = new Button (composite, SWT.PUSH);
		GridData gdRemove = new GridData();
		gdRemove.verticalAlignment = SWT.TOP;
		gdRemove.verticalSpan = 5;
		gdRemove.widthHint = 80;
		buttonRemove.setText ("\u5220\u9664"); //$NON-NLS-1$ //"删除"
		//buttonRemove.setBounds(94, 100, 150, 60); //exclude被设置为true时，GridLayout将按照AbsoluteLayout的方式来安排。GridLayout不能通过setSize或setBounds来自定义控件尺寸，如果定义需要使用widthHint和heightHint两个参数。
		buttonRemove.setLayoutData(gdRemove);
/*		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				button.setText("I Was Clicked");
			}
		});*/  
		
		final Button buttonEmpty = new Button (composite, SWT.PUSH);
		GridData gdEmpty = new GridData();
		gdEmpty.verticalAlignment = SWT.TOP;
		gdEmpty.widthHint = 80;
		buttonEmpty.setText ("\u6e05\u7a7a"); //$NON-NLS-1$ //"清空"
		buttonEmpty.setLayoutData(gdEmpty);
		
		btnRadioButton_1 = new Button(composite, SWT.RADIO);
		btnRadioButton_1.setBounds(34, 129, 97, 17);
		btnRadioButton_1.setText("\u6587\u4ef6\u5939\u5bfc\u5165"); //$NON-NLS-1$  //"文件夹导入"
		btnRadioButton_1.setLayoutData(gdRb);
		
		//final Text folderText = new Text(c,SWT.SINGLE);
		folderText = new Text(composite,SWT.BORDER);
		folderText.addModifyListener(new ModifyListener() {
	         @Override
			public void modifyText(ModifyEvent e) {
	            //updatePageComplete();
	         }
	      });
		GridData gdFt = new GridData();
		gdFt.widthHint = 360;
		gdFt.heightHint = 20;
		folderText.setLayoutData(gdFt);
		folderText.setEnabled(false);
		
		
		buttonScan = new Button (composite, SWT.PUSH);
		GridData gdScan = new GridData();
		gdScan.widthHint = 80;
		//buttonRemove.setBounds(94, 100, 150, 60);
		buttonScan.setLayoutData(gdScan);
		buttonScan.setText("\u6d4f\u89c8\u2026"); //$NON-NLS-1$  //"浏览…"
		buttonScan.setEnabled(false);
		
		//添加文件
		btnRadioButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				table.clearAll();
				itemsData.clear();
				table.setEnabled(true);
				buttonAdd.setEnabled(true);
				buttonRemove.setEnabled(true);
				//folderText.clearSelection();
				//folderPath = null;
				folderText.setEnabled(false);
				buttonScan.setEnabled(false);
			}
		}
		);
		//添加文件夹
		btnRadioButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				folderText.setText("");
				folderPath = null;
				folderText.setEnabled(true);
				buttonScan.setEnabled(true);
				table.setEnabled(false);
				buttonAdd.setEnabled(false);
				buttonRemove.setEnabled(false);
				buttonEmpty.setEnabled(false);
			}
		});
		
		buttonAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				int itemsNum = 0;
				FileDialog dialog = new FileDialog(composite.getShell(), SWT.OPEN|SWT.MULTI);
			    dialog.setText("\u6dfb\u52a0"); //$NON-NLS-1$  //"添加"
			    dialog.setFilterExtensions (new String [] {"*.exe", "*.*"}); 
				String filePath = dialog.open();//返回最后一个选择文件的全路径
				System.out.print("filePath="+filePath);
				String[] fileNames = dialog.getFileNames();//返回所有选择的文件名，不包括路径
				System.out.print("fileNames="+fileNames+"\r\n");
				String path = dialog.getFilterPath();//返回选择的路径，这个和fileNames配合可以得到所有的文件的全路径
				//System.out.println("path="+path);
				//dialog.setFilterNames (new String [] {"Batch Files", "All Files (*.*)"});
				//dialog.setFilterExtensions (new String [] {"*.exe", "*.*"}); //Windows wild cards
				//String[] filter = {"*.exe;*.*"};//指定文件格式 //$NON-NLS-1$
				//dialog.setFilterExtensions(filter);
				//dialog.setFilterExtensions (new String [] {"*.exe"});
				//dialog.setFilterPath ("c:\\"); //Windows path
				//dialog.setFileName ("fred.bat");
				//System.out.println ("Save to: " + dialog.open ()); //打开对话框
				int n = fileNames.length;
				//items = table.getItems();
				for (int i = 0;i < n;i++) {
					itemsData.put(fileNames[i],path);
				}
				itemsNum = itemsData.size();
				System.out.println ("itemsNum=" + itemsNum);
				if(itemsNum>5)
					table.setItemCount(itemsNum);
				items = table.getItems();
				Iterator<String> iter = itemsData.keySet().iterator();
				int i = 0;
				String fileName;
				String filepath;
				while (iter.hasNext()) {
					fileName = iter.next();
					filepath = itemsData.get(fileName);
					items[i].setText(new String[] {fileName,filepath+"\\"+fileName});	
					i++;
				}
/*				for (int i = 0;i < n;i++) {
					TableItem item = new TableItem(table,SWT.NULL);
					items[i].setText(new String[] {fileNames[i],path+fileNames[i]});
				}*/
/*				int m = table.getItemCount();
				itemsNum += n;
				//System.out.print("m="+m+"\r\n");
				System.out.println("itemsNum="+itemsNum);
				if(itemsNum <= m) {
					for (int i = 0;i < n;i++) {
						//TableItem item = new TableItem(table,SWT.NULL);
						//items[i].setText(new String[] {fileNames[i],path+fileNames});
						items[i].setText(new String[] {fileNames[i],path+"\\"});
						//itemsData[dataCount+i]=path+fileNames[i];
						//itemsData.put("path", path);
						//itemsData.put("name",fileNames[i]);
						itemsData.put(fileNames[i],path);
						System.out.print("itemsData1="+itemsData+"\r\n");
					}
				}
				else {
					for (int i = 0;i < m;i++) {
						//items[i].setText(new String[] {fileNames[i],path+fileNames[i]});
						items[i].setText(new String[] {fileNames[i],path+"\\"});
						//itemsData[dataCount+i]=path+fileNames[i];
						//itemsData.put("path", path);
						//itemsData.put("name",fileNames[i]);
						itemsData.put(fileNames[i],path);
						//System.out.print("itemsData21="+itemsData.values(i)+itemsData.k+"\r\n");
					}
					for (int i = m;i < itemsNum;i++) {
						TableItem item = new TableItem(table,SWT.NULL);
						//item.setText(new String[] {fileNames[m+i],path+fileNames[m+i]});
						items[i].setText(new String[] {fileNames[i],path+"\\"});
						//itemsData[dataCount+i]=path+fileNames[i];
						//itemsData.put("path", path);
						//itemsData.put("name",fileNames[i]);
						itemsData.put(fileNames[i],path);
						System.out.print("itemsData22="+itemsData+"\r\n");
					}
				}*/
				//updateData(btnRadioButton, table, buttonAdd, buttonRemove,btnRadioButton_1, folderText, buttonScan, getWizard());
				//System.out.print("items2="+items);
			} 
		});
		buttonRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				int[] indices = table.getSelectionIndices();
				table.remove(indices);
				//updateData(btnRadioButton, table, buttonAdd, buttonRemove,btnRadioButton_1, folderText, buttonScan, getWizard());
			}
		});
		
		buttonEmpty.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				table.removeAll();
			}
		});
		buttonScan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
			    DirectoryDialog dialog = new DirectoryDialog(composite.getShell());
			    //dialog.setFilterPath("c:\\"); // Windows specific
			    folderPath = dialog.open();
			    System.out.print("folderPath="+folderPath);
                if(dialog!=null){  
                	folderText.setText(folderPath);  
                } 
                
           } 
		}
		);
		//setPageComplete(validatePage());
		setControl(composite);
	}
}
