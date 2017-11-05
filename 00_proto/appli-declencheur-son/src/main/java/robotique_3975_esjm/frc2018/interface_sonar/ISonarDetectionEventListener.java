package robotique_3975_esjm.frc2018.interface_sonar;

/**
 * Interface d'un consommateur d'évènement de type {@link SonarDetectionEvent}
 */
public interface ISonarDetectionEventListener
{
	/**
	 * Cette méthode sera appelée lorsqu'un évènement de type {@link SonarDetectionEvent} est généré par un
	 * {@link ISonarDetectionEventObservable}
	 * 
	 * @param event
	 *            référence non-null à un objet {@link SonarDetectionEvent}
	 */
	void updateSonarDetectionEvent(SonarDetectionEvent event);

}
