package client;

import java.net.MalformedURLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;

public class Second extends Shell {
	private Text text;
	private ClientService service;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			Second shell = new Second(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public Second(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(null);
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(10, 341, 751, 30);
		
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR && !text.getText().trim().isEmpty()) {
					service.send(text.getText().trim());
					text.setText("");
				}
			}
		});
		
		List list = new List(this, SWT.BORDER);
		list.setBounds(10, 0, 751, 335);
		createContents();
		
		try {
			service = new ClientService(message -> {
				getDisplay().asyncExec(() -> {
					list.add(message);
					list.redraw();
				});
			});
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(777, 425);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
