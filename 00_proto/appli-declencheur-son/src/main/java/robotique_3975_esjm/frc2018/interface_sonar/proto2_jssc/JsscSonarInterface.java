package robotique_3975_esjm.frc2018.interface_sonar.proto2_jssc;

import java.util.LinkedList;

import ca.smchan.sif.core.IInitializable;
import ca.smchan.sif.core.SifPropertyDef;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import robotique_3975_esjm.frc2018.interface_sonar.ISonarDetectionEventListener;
import robotique_3975_esjm.frc2018.interface_sonar.ISonarDetectionEventObservable;
import robotique_3975_esjm.frc2018.interface_sonar.SonarDetectionEvent;

public class JsscSonarInterface implements IInitializable, SerialPortEventListener, ISonarDetectionEventObservable
{
    private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager
            .getLogger(JsscSonarInterface.class);

    private String _comPort = "COM3";
    private int _baudRate = 9600;
    private SerialPort _serialPort;
    private byte[] _buffer = new byte[1024];
    private int _bufferIndex;
    private final LinkedList<ISonarDetectionEventListener> _listeners = new LinkedList<>();

    private int _deviceId;

    public JsscSonarInterface()
    {
        // ...
    }

    @SifPropertyDef(name = "com_port", isMandatory = false)
    public void setComPort(String port)
    {
        _comPort = port;
    }

    @SifPropertyDef(name = "baud_rate", isMandatory = false)
    public void setBaudRate(int baudRate)
    {
        _baudRate = baudRate;
    }

    @SifPropertyDef(name = "add_listener")
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
    public void onInitialize()
    {
        _serialPort = new SerialPort(_comPort);
        try
        {
            _serialPort.openPort();

            _serialPort.setParams(_baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            _serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to initialize serial port " + _comPort, e);
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event)
    {
        if (event.isRXCHAR() && event.getEventValue() > 0)
        {
            try
            {
                byte[] bytes = _serialPort.readBytes();

                for (int i = 0; i < bytes.length; i++)
                    processByte(bytes[i]);

            }
            catch (SerialPortException ex)
            {
                System.out.println("Error in receiving string from COM-port: " + ex);
            }
        }
    }

    private void processByte(byte b)
    {
        switch (b)
        {
            case 0xd:
            case 0xa:
            case 0:
                processBufferContent();
                break;
            default:
                _buffer[_bufferIndex++] = b;
                break;
        }
    }

    private void processBufferContent()
    {
        if (_bufferIndex == 0)
            return;

        int cm = Integer.parseInt(new String(_buffer, 0, _bufferIndex));
        _bufferIndex = 0;
        int distanceMM = cm * 10;
        LOGGER.debug("distanceMM=" + distanceMM);
        notifyListeners(new SonarDetectionEvent(System.currentTimeMillis(), _deviceId, distanceMM));
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
                LOGGER.warn("Unhandled exception", e);
            }
        }
    }

}
