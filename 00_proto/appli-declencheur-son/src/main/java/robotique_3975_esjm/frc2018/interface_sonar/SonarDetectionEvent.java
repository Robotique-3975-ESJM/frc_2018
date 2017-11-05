package robotique_3975_esjm.frc2018.interface_sonar;

/**
 * Objet de données qui représente un évènement de détection du sonar
 */
public class SonarDetectionEvent
{
	private final long _timestamp;
	private final int _distanceMM;
	private final int _deviceId;

	/**
	 * Constructeur d'un objet.
	 * 
	 * @param timestamp
	 *            représentation du temps en millisecondes {@link java.lang.System#currentTimeMillis }
	 * @param deviceId
	 *            identifiant de la source d'évènement
	 * @param distanceMM
	 *            distance détectée par le sonar en millimètres
	 */
	public SonarDetectionEvent(long timestamp, int deviceId, int distanceMM)
	{
		_timestamp = timestamp;
		_deviceId = deviceId;
		_distanceMM = distanceMM;
	}

	public long getTimestamp()
	{
		return _timestamp;
	}

	public int getDeviceId()
	{
		return _deviceId;
	}

	public int getDistanceMM()
	{
		return _distanceMM;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("@0x");
		sb.append(Integer.toHexString(hashCode()));
		sb.append("[timestamp=");
		sb.append(_timestamp);
		sb.append(", deviceId=");
		sb.append(_deviceId);
		sb.append(", _distanceMM=");
		sb.append(_distanceMM);
		sb.append("]");
		return sb.toString();
	}

}
