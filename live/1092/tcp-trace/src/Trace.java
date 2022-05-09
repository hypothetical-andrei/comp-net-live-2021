import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Trace extends Shell {
	private Text sourcePort;
	private Text destinationPort;
	private List list;
	private Button btnNewButton;
	private ServerSocket serverSocket;
	private ExecutorService executorService = Executors
			.newFixedThreadPool(10 * Runtime.getRuntime().availableProcessors());
	private Button btnNewButton_1;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			Trace shell = new Trace(display);
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
	 * 
	 * @param display
	 */
	public Trace(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(new GridLayout(2, false));

		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Source port");

		sourcePort = new Text(this, SWT.BORDER);
		sourcePort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Destination port");

		destinationPort = new Text(this, SWT.BORDER);
		destinationPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				sourcePort.setEnabled(false);
				destinationPort.setEnabled(false);
				try {
					serverSocket = new ServerSocket(Integer.parseInt(sourcePort.getText()));
					String host = "127.0.0.1";
					int port = Integer.parseInt(destinationPort.getText());
					executorService.execute(() -> {
						try {
							while (serverSocket != null && !serverSocket.isClosed()) {
								Socket inboundSocket = serverSocket.accept();
								Socket outboudSocket = new Socket(host, port);
								executorService.submit(() -> {
									forward(inboundSocket, outboudSocket);
								});
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					});
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btnNewButton.widthHint = 140;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("Capture");

		list = new List(this, SWT.BORDER);
		GridData gd_list = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_list.heightHint = 369;
		gd_list.widthHint = 801;
		list.setLayoutData(gd_list);

		btnNewButton_1 = new Button(this, SWT.NONE);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (serverSocket != null && !serverSocket.isClosed()) {
					try {
						serverSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				sourcePort.setEnabled(true);
				destinationPort.setEnabled(true);
			}
		});
		GridData gd_btnNewButton_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_1.widthHint = 140;
		btnNewButton_1.setLayoutData(gd_btnNewButton_1);
		btnNewButton_1.setText("Stop");
		new Label(this, SWT.NONE);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(965, 515);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private void forward(Socket inboundSocket, Socket outboundSocket) {
		try {
			while (inboundSocket != null && !inboundSocket.isClosed()) {
				int available = inboundSocket.getInputStream().available();
				if (available > 0) {
					byte[] buffer = new byte[available];
					int count = 0;
					while (count < buffer.length) {
						count += inboundSocket.getInputStream().read(buffer, count, buffer.length);
					}
					Display.getDefault().asyncExec(() -> {
						list.add(new String(buffer));
						list.redraw();
					});
					if (outboundSocket != null && !outboundSocket.isClosed()) {
						outboundSocket.getOutputStream().write(buffer, 0, buffer.length);
						outboundSocket.getOutputStream().flush();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
