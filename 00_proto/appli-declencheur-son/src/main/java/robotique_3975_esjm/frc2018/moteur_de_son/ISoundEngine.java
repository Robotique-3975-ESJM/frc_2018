package robotique_3975_esjm.frc2018.moteur_de_son;

import java.io.File;
import java.io.IOException;

/**
 * Interface générique d'un moteur de son.
 */
public interface ISoundEngine
{
	/**
	 * Jouer un fichier de son, par exemple, un fichier WAVE (*.wav).
	 * 
	 * @param file
	 *            référence non-null à un fichier de son
	 */
	void playSound(File file) throws IOException;
	
	/**
	 * Arrêter de jouer du son, s'il y a du son qui joue présentement.
	 */
	void stopPlayback();
	
}
