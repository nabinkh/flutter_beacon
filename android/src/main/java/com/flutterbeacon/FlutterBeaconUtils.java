package com.flutterbeacon;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class FlutterBeaconUtils {
  static String parseState(int state) {
    return state == MonitorNotifier.INSIDE ? "INSIDE" : state == MonitorNotifier.OUTSIDE ? "OUTSIDE" : "UNKNOWN";
  }

  static List<Map<String, Object>> beaconsToArray(List<Beacon> beacons) {
    if (beacons == null) {
      return new ArrayList<>();
    }
    List<Map<String, Object>> list = new ArrayList<>();
    for (Beacon beacon : beacons) {
      Map<String, Object> map = beaconToMap(beacon);
      list.add(map);
    }

    return list;
  }

  private static Map<String, Object> beaconToMap(Beacon beacon) {
    Map<String, Object> map = new HashMap<>();

    map.put("proximityUUID", beacon.getId1().toString().toUpperCase());
    map.put("major", beacon.getId2().toInt());
    map.put("minor", beacon.getId3().toInt());
    map.put("rssi", beacon.getRssi());
    map.put("rawData",beacon.getRawData());
    map.put("txPower", beacon.getTxPower());
    map.put("accuracy", String.format(Locale.US, "%.2f", beacon.getDistance()));
    map.put("macAddress", beacon.getBluetoothAddress());

    return map;
  }

  static Map<String, Object> regionToMap(Region region) {
    Map<String, Object> map = new HashMap<>();

    map.put("identifier", region.getUniqueId());
    if (region.getId1() != null) {
      map.put("proximityUUID", region.getId1().toString());
    }
    if (region.getId2() != null) {
      map.put("major", region.getId2().toInt());
    }
    if (region.getId3() != null) {
      map.put("minor", region.getId3().toInt());
    }

    return map;
  }

  static Region regionFromMap(Map map) {
    String identifier = "";
    List<Identifier> identifiers = new ArrayList<>();

    Object objectIdentifier = map.get("identifier");
    if (objectIdentifier instanceof String) {
      identifier = objectIdentifier.toString();
    }

    Object proximityUUID = map.get("proximityUUID");

    if (proximityUUID instanceof String) {
      identifiers.add(Identifier.parse((String) proximityUUID));
    }

    Object major = map.get("major");
    if (major instanceof Integer) {
      identifiers.add(Identifier.fromInt((Integer) major));
    }
    Object minor = map.get("minor");
    if (minor instanceof Integer) {
      identifiers.add(Identifier.fromInt((Integer) minor));
    }

    return new Region(identifier, identifiers);
  }
}
