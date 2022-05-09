package client;

import java.net.MalformedURLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class ClientApplication extends Shell {
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ClientApplication shell = new ClientApplication(display);
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
	 * @throws MalformedURLException 
	 */
	public ClientApplication(Display display) throws MalformedURLException {
		super(display, SWT.SHELL_TRIM);
		setLayout(new GridLayout(1, false));
		List list = new List(this, SWT.BORDER);
		
		final ClientService clientService = new ClientService(message -> {
			getDisplay().asyncExec(() -> {
				list.add(message);
				list.redraw();
			});
		});
		
		GridData gd_list = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_list.heightHint = 402;
		gd_list.widthHint = 710;
		list.setLayoutData(gd_list);
		
		text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (!text.getText().trim().isEmpty()) {
					clientService.send(text.getText().trim());
					text.setText("");
				}
			}
		});
		btnNewButton.setText("Send");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(732, 534);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
