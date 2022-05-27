package trace;

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Trace extends Shell {
	private Text listenPortText;
	private Text destinationHostText;
	private Text destinationPortText;

	private List messageList;
	private ServerSocket serverSocket;
	private ExecutorService executorService = Executors.newFixedThreadPool(10 * Runtime.getRuntime().availableProcessors());
	
	/**
	 * Launch the application.
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
	 * @param display
	 */
	public Trace(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Listen port");
		
		listenPortText = new Text(this, SWT.BORDER);
		listenPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Destination host");
		
		destinationHostText = new Text(this, SWT.BORDER);
		destinationHostText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_2 = new Label(this, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("Destination port");
		
		destinationPortText = new Text(this, SWT.BORDER);
		destinationPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button startBtn = new Button(this, SWT.NONE);
		startBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				listenPortText.setEnabled(false);
				destinationHostText.setEnabled(false);
				destinationPortText.setEnabled(false);
				startBtn.setEnabled(false);
				try {
					serverSocket = new ServerSocket(Integer.parseInt(listenPortText.getText()));
					String destinationHost = destinationHostText.getText();
					int destinationPort = Integer.parseInt(destinationPortText.getText());
					executorService.execute(() -> {
						while (serverSocket != null && !serverSocket.isClosed()) {
							try {
								Socket inboundSocket = serverSocket.accept();
								Socket outboundSocket = new Socket(destinationHost, destinationPort);
								executorService.submit(() -> {
									forward(inboundSocket, outboundSocket, 0);
								});
								executorService.submit(() -> {
									forward(outboundSocket, inboundSocket, 1);
								});
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		startBtn.setText("Start");
		
		Button stopBtn = new Button(this, SWT.NONE);
		stopBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				listenPortText.setEnabled(true);
				destinationHostText.setEnabled(true);
				destinationPortText.setEnabled(true);
				startBtn.setEnabled(true);
				
				if (serverSocket != null && !serverSocket.isClosed()) {
					try {
						serverSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		stopBtn.setText("Stop");
		
		messageList = new List(this, SWT.BORDER);
		GridData gd_messageList = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_messageList.heightHint = 322;
		gd_messageList.widthHint = 660;
		messageList.setLayoutData(gd_messageList);
		createContents();
	}

	
	private void forward(Socket inboundSocket, Socket outboundSocket, int direction) {
		while (inboundSocket != null && !inboundSocket.isClosed()) {
			try {
				int available = inboundSocket.getInputStream().available();
				if (available > 0) {
					byte[] buffer = new byte[available];
					int count = 0;
					while (count < buffer.length) {
						count += inboundSocket.getInputStream().read(buffer, count, buffer.length);
						Display.getDefault().asyncExec(() -> {
							messageList.add(new String(buffer));
							messageList.redraw();
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
	
	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(674, 529);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
