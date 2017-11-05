package robotique_3975_esjm.frc2018.interface_sonar;

/**
 * Interface d'un générateur d'évènement de type {@link SonarDetectionEvent}
 */
public interface ISonarDetectionEventObservable
{
	void addSonarDetectionEventListener(ISonarDetectionEventListener listener);

	void removeSonarDetectionEventListener(ISonarDetectionEventListener listener);

}
