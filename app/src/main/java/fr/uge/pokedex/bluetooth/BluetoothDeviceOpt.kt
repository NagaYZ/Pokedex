package fr.uge.pokedex.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import fr.uge.pokedex.theme.Purple200
import fr.uge.pokedex.theme.Purple500


class BluetoothDeviceOpt{

    @Composable
    fun enableBluetooth(activity: Activity): Boolean {
        val context = LocalContext.current
        val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        var bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADMIN)
                    != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                            arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN), BluetoothAdapter.STATE_TURNING_ON)
                } else {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    if (!bluetoothAdapter.isEnabled) {
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        activity.startActivityForResult(enableBtIntent, BluetoothAdapter.STATE_TURNING_ON)
                    }
                }
                return false
            }
            activity.startActivityForResult(enableBtIntent, BluetoothAdapter.STATE_ON)
        }
        return true
    }

    @SuppressLint("MissingPermission")
    @Composable
    fun findPairDevice() : Set<BluetoothDevice>{
        val context = LocalContext.current
        val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
        val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
        return pairedDevices
    }


    @SuppressLint("MissingPermission")
    @Composable
    fun bluetoothDialog(
        pairedDevice:  List<BluetoothDevice>,
        device: (BluetoothDevice) -> Unit
    ) {
        Dialog(
            onDismissRequest = {}, properties = DialogProperties(
                dismissOnBackPress = true
            )
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Purple500)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(25.dp)
                        .background(Purple500)
                ) {
                    Spacer(Modifier.padding(10.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(items = pairedDevice) {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .height(70.dp)
                                    .fillMaxWidth()
                                    .background(Purple200, RoundedCornerShape(8.dp))
                                    .clickable {
                                               device(it)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(it.name, fontSize = 20.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
    }
}