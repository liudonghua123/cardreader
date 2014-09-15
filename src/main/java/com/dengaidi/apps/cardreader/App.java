package com.dengaidi.apps.cardreader;

import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dengaidi.apps.cardreader.entity.Employee;
import com.dengaidi.apps.cardreader.entity.EntranceRecord;
import com.dengaidi.apps.cardreader.service.EmployeeService;
import com.dengaidi.apps.cardreader.service.EntranceRecordService;
import com.dengaidi.apps.cardreader.utils.Utils;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class App {

	protected Shell shell;
	private Text textID;
	private Button button;
	private Table table;
	
	//@Autowired
	private EmployeeService employeeService;
	private EntranceRecordService entranceRecordService;
	private Text textAddress;

	public App() {
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		//ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ApplicationContextConfig.class);
        // org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'employeeService' is defined
        context.refresh();

		try {
			App window = new App();
			window.employeeService = (EmployeeService) context.getBean("employeeService");
			window.entranceRecordService = (EntranceRecordService) context.getBean("entranceRecordService");
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		// prevent-window-from-resizing
		shell = new Shell(SWT.SHELL_TRIM & (~SWT.RESIZE));
		shell.setSize(488, 300);
		shell.setText("SWT Application");
		
		Label labelID = new Label(shell, SWT.NONE);
		labelID.setBounds(10, 13, 55, 15);
		labelID.setText("员工编号：");
		
		textID = new Text(shell, SWT.BORDER);
		textID.setText("");
		textID.setBounds(71, 10, 98, 21);
		
		button = new Button(shell, SWT.NONE);
		button.setBounds(175, 8, 47, 25);
		button.setText("查询");
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 37, 452, 214);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(228, 13, 39, 15);
		label.setText("地址：");
		
		textAddress = new Text(shell, SWT.BORDER);
		textAddress.setBounds(273, 10, 189, 21);
		final String[] titles = { "ID", "姓名", "年龄","刷卡时间","刷卡地址" };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}
		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}
		
		textID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				// detect whethe a enter key pressed
				// http://www.mkyong.com/swt/swt-how-to-capture-keyboard-event/
				// http://tesisuade.googlecode.com/svn/trunk/TestDJ/src/chrriis/dj/nativeswing/swtimpl/SWTUtils.java
				if((event.keyCode == SWT.CR || event.keyCode == SWT.LF) && !textID.getText().equals("")) {
					int id = Integer.parseInt(textID.getText());
					Employee employee = employeeService.findEmployeeById(id);
					if(employee != null) {
						TableItem item = new TableItem(table, SWT.NONE);
						// 获取员工基本信息并且显示在Table里
						Date currentDate = new Date(System.currentTimeMillis());
						item.setText(0, String.valueOf(employee.getId()));
						item.setText(1, employee.getName());
						item.setText(2, String.valueOf(employee.getAge()));
						item.setText(3, Utils.formatDate(currentDate));
						item.setText(4, textAddress.getText());
						// 插入刷卡记录
						EntranceRecord entranceRecord = new EntranceRecord(employee.getId(), textAddress.getText(), currentDate);
						entranceRecordService.addEntranceRecord(entranceRecord);
						// 调整每列宽度
						for (int i = 0; i < titles.length; i++) {
							table.getColumn(i).pack();
						}
						// 高亮显示最后插入的记录
						table.select(table.getItems().length - 1);
						table.showSelection();
					}
					else {
						// 提示不存在该雇员
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
						messageBox.setMessage("此编号的雇员不存在，无法添加刷卡记录");
						messageBox.open();
					}
					
					// 清空输入框内的内容
					textID.setText("");
		        }
			}
		});
		
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if(!textID.getText().equals("")) {
					table.removeAll();
					int id = Integer.parseInt(textID.getText());
					Employee employee = employeeService.findEmployeeById(id);
					List<EntranceRecord> entranceRecords = entranceRecordService.findEntranceRecordByEmployeeId(id);
					if(entranceRecords != null && entranceRecords.size() > 0) {
						for(int i = 0; i < entranceRecords.size(); i++) {
							TableItem item = new TableItem(table, SWT.NONE);
							String address = entranceRecords.get(i).getAddress() == null ? "" : entranceRecords.get(i).getAddress();
							item.setText(0, String.valueOf(employee.getId()));
							item.setText(1, employee.getName());
							item.setText(2, String.valueOf(employee.getAge()));
							item.setText(3, Utils.formatDate(entranceRecords.get(i).getDatetime()));
							item.setText(4, address);
						}
						// 调整每列宽度
						for (int i = 0; i < titles.length; i++) {
							table.getColumn(i).pack();
						}
					}
					else {
						// 提示查询不到雇员刷卡记录信息
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
						messageBox.setMessage("查询不到此雇员刷卡记录信息");
						messageBox.open();
					}
				}
			}
		});

	}
}
