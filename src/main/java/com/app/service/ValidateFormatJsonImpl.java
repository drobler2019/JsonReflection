package com.app.service;

import com.app.interfaces.ValidateFormatJsonService;

import java.util.*;
import java.util.stream.Stream;

public class ValidateFormatJsonImpl implements ValidateFormatJsonService {

    @Override
    public Map<String, String> getMap(String json) {
        jsonValidate(json, "{", "}");
        return getJsonStringToMap(json);
    }

    @Override
    public List<Map<String, String>> getListMap(String json) {
        jsonValidate(json, "[", "]");
        return getArrayJsonToMap(json);
    }

    @Override
    public List<String> getArrayJsonToList(String json) {
        jsonValidate(json, "[", "]");
        return new ArrayList<>(List.of(json.substring(1, json.length() - 1).split(",")));
    }

    private HashMap<String, String> getJsonStringToMap(String json) {
        String content = json.substring(1, json.length() - 1);
        if(content.isEmpty()) {
            var hashMap = new HashMap<String, String>();
            hashMap.put("empty","{}");
            return hashMap;
        }
        String[] peers = content.split(",");
        List<String> peersList = new ArrayList<>(List.of(peers));
        for (int i = 0; i < peers.length; i++) {
            String peer = peers[i].substring(peers[i].indexOf(":") + 1).trim();
            if (peer.startsWith("{")) {
                var builder = new StringBuilder();
                int j = i;
                while(j < peersList.size()) {
                    if (!peers[j].endsWith("}")) {
                        builder.append(peers[j]).append(",");
                    } else {
                        builder.append(peers[j]);
                    }
                    if (peers[j].endsWith("}")) {
                        String result = builder.toString();
                        long keysOpen =  Arrays.stream(result.split("")).filter(r -> r.equals("{")).count();
                        long keysClose =  Arrays.stream(result.split("")).filter(r -> r.equals("}")).count();
                        if (keysOpen != keysClose) {
                            builder.append(",");
                            j++;
                            continue;
                        }
                        peersList.subList(i, j + 1).clear();
                        peersList.addFirst(builder.toString());
                        peers = peersList.toArray(String[]::new);
                        break;
                    }
                    j++;
                }
            } else if (peer.startsWith("[")) {
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
        var maps = new ArrayList<Map<String, String>>();
        var jsonValues = content.split("}");
        var mapList = this.getMapWithSubMap(content);

        if (!mapList.isEmpty()) {
            return mapList;
        }

        var stringBuilder = new StringBuilder();
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
                for (int i = 0; i < count; i++)
                    stringBuilder.append("}");
            } else {
                stringBuilder.append("}");
            }
            String jsonResult = stringBuilder.toString();
            maps.add(getJsonStringToMap(jsonResult));
            stringBuilder.delete(0, stringBuilder.length());
        }
        return maps;
    }

    private List<Map<String, String>> getMapWithSubMap(String json) {
        if((json.contains("[") || json.contains("]")) && json.startsWith("{") && json.endsWith("}")) {
            var split = json.split(",\\{");
            return Stream.of(split).map(j -> {
                var builder = new StringBuilder();
                if(!j.startsWith("{")) {
                    builder.append("{").append(j);
                }
                if(!j.endsWith("}")) {
                    builder.append(j).append("}");
                }
                String result = builder.toString();
                return result.isEmpty() ? j : result;
            }).map(this::getMap).toList();
        }
        return Collections.emptyList();
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

    private void jsonValidate(String json, String open, String close) {
        nullOrEmpty(json);
        if (!(json.startsWith(open) && json.endsWith(close))) {
            throw new IllegalArgumentException("El JSON debe empezar con '" + open + "' y terminar con '" + close + "'");
        }
    }

    private void nullOrEmpty(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("El JSON proporcionado es nulo o vacío");
        }
    }

}
