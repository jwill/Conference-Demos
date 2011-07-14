import motej.*
import motej.event.*

def listener = [moteFound:{Mote mote ->
                        System.out.println("Found mote: " + mote.getBluetoothAddress())
                        mote.setPlayerLeds([false, true, false, true] as boolean[])
                        mote.rumble(2000l)
                        Thread.sleep(10000l)
                        mote.disconnect()
                }
        ] as MoteFinderListener        
                
MoteFinder finder = MoteFinder.getMoteFinder()
finder.addMoteFinderListener(listener)
                
finder.startDiscovery()
Thread.sleep(30000l)
finder.stopDiscovery()