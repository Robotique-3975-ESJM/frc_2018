package robotique_3975_esjm.frc2018.interface_sonar.proto1;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import robotique_3975_esjm.frc2018.interface_sonar.ISonarDetectionEventListener;
import robotique_3975_esjm.frc2018.interface_sonar.ISonarDetectionEventObservable;
import robotique_3975_esjm.frc2018.interface_sonar.SonarDetectionEvent;
import robotique_3975_esjm.frc2018.sif.IStartable;

/**
 * Simulateur de sonar.
 */
public class NullSonar implements IStartable, ISonarDetectionEventObservable, Runnable
{
	private static final Insets DEFAULT_INSETS = new Insets(5, 5, 5, 5);
	private final LinkedList<ISonarDetectionEventListener> _listeners = new LinkedList<>();
	private JTextField _jtf_deviceId;
	private JTextField _jtf_distanceMM1;
	private JTextField _jtf_distanceMM2;
	private JTextField _jtf_distanceMM3;

	private Thread _thread;
	private int _deviceId;
	private int _distanceMM;

	public NullSonar()
	{
		// ...
	}

	@Override
	public void addSonarDetectionEventListener(ISonarDetectionEventListener listener)
	{
		_listeners.add(listener);
	}

	@Override
	public void removeSonarDetectionEventListener(ISonarDetectionEventListener listener)
	{
		_listeners.remove(listener);
	}

	@Override
	public void onStart()
	{
		// Démarrer la tâche de fond
		_thread = new Thread(this, getClass().getSimpleName());
		_thread.setDaemon(true);
		_thread.start();

		JFrame frame = new JFrame(getClass().getSimpleName());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(320, 240));
		frame.getContentPane().add(createMainPanel());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void run()
	{
		try
		{
			eventGeneratorLoop();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void eventGeneratorLoop() throws Exception
	{
		while (true)
		{
			Thread.sleep(200);
			notifyListeners(new SonarDetectionEvent(System.currentTimeMillis(), _deviceId, _distanceMM));
		}
	}

	private Component createMainPanel()
	{
		JPanel panel = new JPanel(new GridBagLayout());
		JComponent comp;
		GridBagConstraints gbc;

		/////////////////////////////////////////
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = DEFAULT_INSETS;
		comp = new JLabel("Device ID:");
		panel.add(comp, gbc);
		
		gbc = new GridBagConstraints();
		gbc.insets = DEFAULT_INSETS;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		comp = _jtf_deviceId = new JTextField("0");
		panel.add(comp, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = DEFAULT_INSETS;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		comp = new JButton(new UpdateDeviceIdAction(_jtf_deviceId));
		panel.add(comp, gbc);

		/////////////////////////////////////////
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = DEFAULT_INSETS;
		comp = new JLabel("Distance 1 (mm):");
		panel.add(comp, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = DEFAULT_INSETS;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		comp = _jtf_distanceMM1 = new JTextField("0");
		panel.add(comp, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = DEFAULT_INSETS;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		comp = new JButton(new UpdateDistanceAction(_jtf_distanceMM1));
		panel.add(comp, gbc);
		
		/////////////////////////////////////////
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = DEFAULT_INSETS;
		comp = new JLabel("Distance 2 (mm):");
		panel.add(comp, gbc);
		
		gbc = new GridBagConstraints();
		gbc.insets = DEFAULT_INSETS;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		comp = _jtf_distanceMM2 = new JTextField("50");
		panel.add(comp, gbc);
		
		gbc = new GridBagConstraints();
		gbc.insets = DEFAULT_INSETS;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		comp = new JButton(new UpdateDistanceAction(_jtf_distanceMM2));
		panel.add(comp, gbc);
		
		/////////////////////////////////////////
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = DEFAULT_INSETS;
		comp = new JLabel("Distance 3 (mm):");
		panel.add(comp, gbc);
		
		gbc = new GridBagConstraints();
		gbc.insets = DEFAULT_INSETS;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		comp = _jtf_distanceMM3 = new JTextField("100");
		panel.add(comp, gbc);
		
		gbc = new GridBagConstraints();
		gbc.insets = DEFAULT_INSETS;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		comp = new JButton(new UpdateDistanceAction(_jtf_distanceMM3));
		panel.add(comp, gbc);

		/////////////////////////////////////////
		gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weighty = 1;
		comp = new JLabel();
		panel.add(comp, gbc);

		return panel;
	}

	void triggerDetectionEvent()
	{
		_deviceId = getInteger(_jtf_deviceId);
		_distanceMM = getInteger(_jtf_distanceMM1);
	}

	private void notifyListeners(SonarDetectionEvent event)
	{
		for (ISonarDetectionEventListener iSonarDetectionEventListener : _listeners)
		{
			try
			{
				iSonarDetectionEventListener.updateSonarDetectionEvent(event);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private int getInteger(JTextField jtf)
	{
		String str = jtf.getText();
		return Integer.parseInt(str);
	}

	////////////////////////////////////////////////////////
	private class UpdateDeviceIdAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;
		private final JTextField _jTextField;

		public UpdateDeviceIdAction(JTextField jTextField)
		{
			super("MAJ");
			
			_jTextField = jTextField;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			_deviceId = getInteger(_jTextField);
		}

	}
	
	////////////////////////////////////////////////////////
	private class UpdateDistanceAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;
		private final JTextField _jTextField;
		
		public UpdateDistanceAction(JTextField jTextField)
		{
			super("MAJ");
			
			_jTextField = jTextField;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			_distanceMM = getInteger(_jTextField);
		}
		
	}

}
