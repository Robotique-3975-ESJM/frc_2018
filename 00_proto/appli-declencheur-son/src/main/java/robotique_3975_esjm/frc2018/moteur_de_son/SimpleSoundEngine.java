package robotique_3975_esjm.frc2018.moteur_de_son;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SimpleSoundEngine implements ISoundEngine
{
	private volatile Thread _thread;

	public SimpleSoundEngine()
	{
		// ...
	}

	public void OnStart()
	{
	}

	@Override
	public void playSound(File file) throws IOException
	{
		if (!file.exists())
			throw new IOException("Incapable de trouver le fichier " + file);

		stopPlayback();
		
		_thread = new Thread(new PlaySoundTask(file), "");
		_thread.setDaemon(true);
		_thread.start();
	}

	@Override
	public void stopPlayback()
	{
		if(_thread != null)
		{
			Thread t = _thread;
			_thread = null;
			t.interrupt();
		}
	}

	/////////////////////////////////////////////////////////////////////

	private class PlaySoundTask implements Runnable
	{
		private File _file;

		public PlaySoundTask(File file)
		{
			_file = file;
		}

		@Override
		public void run()
		{
			try
			{
				play(_file);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void play(File file) throws Exception
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

			AudioFormat format = audioInputStream.getFormat();
			AudioInputStream playbackInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);

			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			if (!AudioSystem.isLineSupported(info))
			{
				throw new RuntimeException("Line matching " + info + " not supported.");
			}

			SourceDataLine line;
			final int bufSize = 16384;

			try
			{
				line = (SourceDataLine) AudioSystem.getLine(info);
				line.open(format, bufSize);
			}
			catch (LineUnavailableException ex)
			{
				throw new RuntimeException("Unable to open the line: " + ex);
			}

			int frameSizeInBytes = format.getFrameSize();
			int bufferLengthInFrames = line.getBufferSize() / 8;
			int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
			byte[] data = new byte[bufferLengthInBytes];
			int numBytesRead = 0;

			// start the source data line
			line.start();

			while (!Thread.interrupted())
			{
				try
				{
					if ((numBytesRead = playbackInputStream.read(data)) == -1)
					{
						break;
					}
					int numBytesRemaining = numBytesRead;
					while (numBytesRemaining > 0)
					{
						numBytesRemaining -= line.write(data, 0, numBytesRemaining);
					}
				}
				catch (Exception e)
				{
					throw new RuntimeException("Error during playback: ", e);
				}
			}
			
			if(!Thread.interrupted())
			{
				// we reached the end of the stream. let the data play out, then
				// stop and close the line.
				line.drain();
			}
			line.stop();
			line.close();
		}

	}

}
