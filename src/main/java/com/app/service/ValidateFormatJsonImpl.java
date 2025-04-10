package com.app.service;

import com.app.interfaces.ValidateFormatJsonService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ValidateFormatJsonImpl implements ValidateFormatJsonService {

    @Override
    public Map<String, String> getMap(String json) {
        jsonValidate(json);
        return getJsonStringToMap(json);
    }

    @Override
    public List<Map<String, String>> getListMap(String json) {
        return getArrayJsonToMap(json);
    }

    private HashMap<String, String> getJsonStringToMap(String json) {
        String content = json.substring(1, json.length() - 1);
        String[] peers = content.split(",");
        List<String> peersList = new ArrayList<>(List.of(peers));
        for (int i = 0; i < peers.length; i++) {
            if (peers[i].contains("{")) {
                var builder = new StringBuilder();
                for (int j = i; j < peersList.size(); j++) {
                    if (!peers[j].endsWith("}")) {
                        builder.append(peers[j]).append(",");
                    } else {
                        builder.append(peers[j]);
                    }
                    if (peers[j].endsWith("}")) {
                        peersList.subList(i, j + 1).clear();
                        peersList.add(builder.toString());
                        peers = peersList.toArray(String[]::new);
                        break;
                    }
                }
            } else if (peers[i].contains("[")) {
                var builder = new StringBuilder();
                for (int j = i; j < peersList.size(); j++) {
                    if (!peers[j].endsWith("]")) {
                        builder.append(peers[j]).append(",");
                    } else {
                        builder.append(peers[j]);
                    }
                    if (peers[j].endsWith("]")) {
                        peersList.subList(i, j + 1).clear();
                        peersList.add(i, builder.toString());
                        peers = peersList.toArray(String[]::new);
                        break;
                    }
                }
            }
        }
        return getJsonStringToMap(peers);
    }

    private List<Map<String, String>> getArrayJsonToMap(String json) {
        var content = json.substring(1, json.length() - 1);
        var jsonValues = content.split("}");
        var stringBuilder = new StringBuilder();
        var maps = new ArrayList<Map<String, String>>();
        for (String jsonValue : jsonValues) {
            if (jsonValue.isEmpty()) {
                continue;
            }
            stringBuilder.append(jsonValue);
            if (stringBuilder.toString().startsWith(",")) {
                stringBuilder.deleteCharAt(0);
            }
            long count = Stream.of(jsonValue.split("")).filter(s -> s.equals("{")).count();
            if (count > 1) {
                stringBuilder.append("}").append("}");
            } else {
                stringBuilder.append("}");
            }
            String jsonResult = stringBuilder.toString();
            maps.add(getJsonStringToMap(jsonResult));
            stringBuilder.delete(0, stringBuilder.length());
        }
        return maps;
    }

    private HashMap<String, String> getJsonStringToMap(String[] peers) {
        var map = new HashMap<String, String>();
        for (String peer : peers) {
            var keyValue = peer.split(":");
            String key = keyValue[0].replace("\"", "").trim();
            if (peer.contains("{")) {
                String value = peer.substring(peer.indexOf(":") + 1).trim();
                map.put(key, value);
                continue;
            }
            String value = keyValue[1].replace("\"", "").trim();
            map.put(key, value);
        }
        return map;
    }

    private void jsonValidate(String json) {
        nullOrEmpty(json);
        if (!(json.startsWith("{") && json.endsWith("}"))) {
            throw new IllegalArgumentException("El JSON debe empezar con '{' y terminar con '}'");
        }
    }

    private void nullOrEmpty(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("El JSON proporcionado es nulo o vacío");
        }
    }

}
