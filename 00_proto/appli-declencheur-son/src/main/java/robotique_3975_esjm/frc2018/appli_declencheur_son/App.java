package robotique_3975_esjm.frc2018.appli_declencheur_son;

import java.io.File;

import robotique_3975_esjm.frc2018.interface_sonar.ISonarDetectionEventListener;
import robotique_3975_esjm.frc2018.interface_sonar.SonarDetectionEvent;
import robotique_3975_esjm.frc2018.interface_sonar.proto1.NullSonar;
import robotique_3975_esjm.frc2018.moteur_de_son.ISoundEngine;
import robotique_3975_esjm.frc2018.moteur_de_son.SimpleSoundEngine;
import robotique_3975_esjm.frc2018.sif.IInitializable;
import robotique_3975_esjm.frc2018.sif.InstantiationHelper;

/**
 * Prototype d'une application démontrant l'intégration d'un sonar et d'un moteur de son.
 */
public class App implements IInitializable, ISonarDetectionEventListener
{
	private ISoundEngine _soundEngine;
	private int _seuilMinMM = 20; // 2cm
	private int _seuilMaxMM = 200; // 20cm
	private boolean _dernierEtat = false;

	public App()
	{
		// ...
	}

	public void setSoundEngine(ISoundEngine soundEngine)
	{
		_soundEngine = soundEngine;
	}

	@Override
	public void onInitialize()
	{
	}

	@Override
	public void updateSonarDetectionEvent(SonarDetectionEvent event)
	{
		// System.out.println(event);
		boolean declancement = false;

		// Verifier s'il y a déclenchement...
		// Il y a déclenchement lorsque le sonar rapporte un objet entre _seuilMinMM et _seuilMaxMM
		int distMM = event.getDistanceMM();
		if (distMM > _seuilMinMM && distMM < _seuilMaxMM)
		{
			declancement = true;
		}

		// Lors d'un nouveau déclenchement
		if (declancement && !_dernierEtat)
		{
			try
			{
				System.out.println("déclenchement!!");
				_soundEngine.stopPlayback();
				_soundEngine.playSound(new File("banque_de_son/sm64_mario_its_me.wav"));
			}
			catch (Exception e)
			{
				System.out.println("Failed to command sound engine");
				e.printStackTrace();
			}
		}

		_dernierEtat = declancement;

	}

	public static void main(String[] args)
	{
		try
		{
			doStuff();
		}
		catch (Exception e)
		{
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
	}

	private static void doStuff() throws Exception
	{
		InstantiationHelper helper = new InstantiationHelper();

		SimpleSoundEngine sse = new SimpleSoundEngine();
		helper.addModule(sse);

		App app = new App();
		helper.addModule(app);
		app.setSoundEngine(sse);

		NullSonar sonar = new NullSonar();
		helper.addModule(sonar);
		sonar.addSonarDetectionEventListener(app);

		helper.completeInit();

		Thread.sleep(1000);
	}
}
