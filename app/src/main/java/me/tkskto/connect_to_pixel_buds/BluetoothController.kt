package me.tkskto.connect_to_pixel_buds

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import java.io.IOException
import java.util.*

class BluetoothController {
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    val SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    var connectionThread: ConnectionThread? = null

    fun scan () {
        if (bluetoothAdapter == null) {
            Log.w("warn:", "your device not supports Bluetooth.")
        }

        if (!bluetoothAdapter.isEnabled) {
            val bluetoothEnableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

            // NEED: Consider launch dialog for not enabled device.
        } else {
            val pairedDevice = bluetoothAdapter.bondedDevices

            pairedDevice.forEach { device ->
                val deviceName = device.name

                if (deviceName == "Pixel Buds") {
                    connectionThread = ConnectionThread(device, SPP_UUID)
                }
            }
        }

        if (connectionThread != null) {
            bluetoothAdapter.cancelDiscovery();
            connectionThread!!.start();

            try {
                connectionThread!!.join()
            } catch (err: InterruptedException) {
                err.printStackTrace()
            }

            Log.d("device state:", "connected")
        }
    }
}

class ConnectionThread(bluetoothdevice: BluetoothDevice, uuid: UUID) : Thread() {
    var socket: BluetoothSocket? = null

    init {
        var tmp: BluetoothSocket? = null
        try {
            tmp = bluetoothdevice.createRfcommSocketToServiceRecord(uuid)
        } catch (err: IOException) {
            err.printStackTrace()
        }
        socket = tmp
    }

    public override fun run() {
        super.run()

        if (socket == null) {
            return
        }

        try {
            Log.d("isconnected:" , socket!!.isConnected.toString())
            if (!socket!!.isConnected) {
                socket!!.connect()
            } else {
                socket!!.close()
            }
        } catch (err: IOException) {
            err.printStackTrace()
            socket!!.close()
        }
    }

    // Closes the client socket and causes the thread to finish.
    fun cancel() {
        try {
            socket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the client socket", e)
        }
    }
}
