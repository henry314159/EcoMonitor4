# Import modules for time, access to sensor and bluetooth
import time
from smbus import SMBus
from bme280 import BME280
import bluetooth

# Setup for bluetooth
server_sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
server_sock.bind(("", bluetooth.PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee" # default uuid

bluetooth.advertise_service(server_sock, "SampleServer", service_id=uuid,
                            service_classes=[uuid, bluetooth.SERIAL_PORT_CLASS],
                            profiles=[bluetooth.SERIAL_PORT_PROFILE],
                            )

# Initialise the BME280
bus = SMBus(1)
bme280 = BME280(i2c_dev=bus,i2c_addr=0x77)

# Get data and discard to avoid garbage first reading
temperature = bme280.get_temperature()
pressure = bme280.get_pressure()
humidity = bme280.get_humidity()
time.sleep(1)

while True:
    print("Waiting for connection on RFCOMM channel %d" % port)
    client_sock, client_info = server_sock.accept()
    print("Accepted connection from ", client_info)
    
    try:
        data = client_sock.recv(1024)
        if len(data) == 0:
            break
        print("received %s" % data)

        temperature = bme280.get_temperature()
        pressure = bme280.get_pressure()
        humidity = bme280.get_humidity()
        client_sock.send('{:05.2f}*C {:05.2f}hPa {:05.2f}%!'.format(temperature, pressure, humidity))
        print("sending %s" % '{:05.2f}°C {:05.2f}hPa {:05.2f}%'.format(temperature, pressure, humidity))

    except IOError:
        pass

    except KeyboardInterrupt:

        print("disconnected")

        client_sock.close()
        server_sock.close()
        print("all done")
        break

    time.sleep(1)
    
