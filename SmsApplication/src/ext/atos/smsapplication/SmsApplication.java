package ext.atos.smsapplication;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;

//VS4E -- DO NOT REMOVE THIS LINE!
public class SmsApplication extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton btnExit;
	private JButton btnStart;
	private ClientDispatchService dispatcher;
	
	private static final String PREFERRED_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	
	public SmsApplication() {
		initComponents();
	}

	private void initComponents() {
		setLayout(new GroupLayout());
		add(getExitButton(), new Constraints(new Trailing(12, 191, 191), new Leading(195, 12, 12)));
		add(getStartButton(), new Constraints(new Trailing(85, 12, 12), new Leading(195, 12, 12)));
		setSize(320, 240);
	}

	private JButton getStartButton() {
		if (btnStart == null) {
			btnStart = new JButton();
			btnStart.setText("Start Listening");
			btnStart.addMouseListener(new MouseAdapter() {
	
				public void mouseClicked(MouseEvent event) {
					startButtonClicked(event);
				}
			});
		}
		return btnStart;
	}

	private JButton getExitButton() {
		if (btnExit == null) {
			btnExit = new JButton();
			btnExit.setText("Exit");
			btnExit.addMouseListener(new MouseAdapter() {
	
				public void mouseClicked(MouseEvent event) {
					System.exit(0);
				}
			});
		}
		return btnExit;
	}

	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null){
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			}
			UIManager.setLookAndFeel(lnfClassname);
		} catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
					+ " on this platform:" + e.getMessage());
		}
	}

	/**
	 * Main entry of the class.
	 * Note: This class is only created so that you can easily preview the result at runtime.
	 * It is not expected to be managed by the designer.
	 * You can modify it as you like.
	 */
	public static void main(String[] args){
		installLnF();
		SwingUtilities.invokeLater(new Runnable(){
		
			public void run(){
				SmsApplication frame = new SmsApplication();
				frame.setDefaultCloseOperation(SmsApplication.EXIT_ON_CLOSE);
				frame.setTitle("SmsApplication");
				frame.getContentPane().setPreferredSize(frame.getSize());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	private void startButtonClicked(MouseEvent event) {
		if(dispatcher == null){
			dispatcher = new ClientDispatchService(this);
			Thread t = new Thread(dispatcher);
			t.start();
			((JButton)event.getSource()).setText("Stop Listener");
		}else{
			closeListener();
			dispatcher = null;
			((JButton)event.getSource()).setText("Start Listener");
		}
	}
	
	protected void finalize(){
		closeListener();
	}
	
	private void closeListener(){
		boolean closed = dispatcher.closeSocket();
		if(!closed){
			System.out.println("Could not close running threads or open sockets.");
		}
	}
	
}
