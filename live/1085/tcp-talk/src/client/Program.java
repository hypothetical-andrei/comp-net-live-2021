package client;

import java.io.IOException;
import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

import common.Settings;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class Program extends Shell {
	private Text text;
	private List list;
	private Client client;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			Program shell = new Program(display);
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
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public Program(Display display) throws UnknownHostException, IOException {
		super(display, SWT.SHELL_TRIM);
		setLayout(new GridLayout(1, false));
		
		list = new List(this, SWT.BORDER);
		GridData gd_list = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 6);
		gd_list.heightHint = 198;
		gd_list.widthHint = 436;
		list.setLayoutData(gd_list);
		
		text = new Text(this, SWT.BORDER);
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
					try {
						if (e.keyCode == SWT.CR && text.getText().trim().length() > 0)  {
							client.send(text.getText().trim());
							text.setText("");
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		createContents();
		
		client = new Client(Settings.HOST, Settings.PORT, message -> {
			Display.getDefault().asyncExec(() -> {
				list.add(message);
				list.redraw();
			});
		});
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
