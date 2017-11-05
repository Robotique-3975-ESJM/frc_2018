package robotique_3975_esjm.frc2018.appli_declencheur_son;

import java.io.File;

import ca.smchan.sif.core.IInitializable;
import ca.smchan.sif.core.SifPropertyDef;
import robotique_3975_esjm.frc2018.interface_sonar.ISonarDetectionEventListener;
import robotique_3975_esjm.frc2018.interface_sonar.SonarDetectionEvent;
import robotique_3975_esjm.frc2018.moteur_de_son.ISoundEngine;

/**
 * Prototype d'une application démontrant l'intégration d'un sonar et d'un moteur de son.
 */
public class App implements IInitializable, ISonarDetectionEventListener
{
    private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager
            .getLogger(App.class);

    private ISoundEngine _soundEngine;
    private int _seuilMinMM = 20; // 2cm
    private int _seuilMaxMM = 200; // 20cm
    private boolean _dernierEtat = false;

    public App()
    {
        // ...
    }

    @SifPropertyDef(name = "sound_engine")
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
                LOGGER.info("déclenchement!!");
                _soundEngine.stopPlayback();
                _soundEngine.playSound(new File("banque_de_son/sm64_mario_its_me.wav"));
            }
            catch (Exception e)
            {
                LOGGER.warn("Failed to command sound engine", e);
            }
        }

        _dernierEtat = declancement;

    }

}
