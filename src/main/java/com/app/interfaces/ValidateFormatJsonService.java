package com.app.interfaces;

import java.util.List;
import java.util.Map;

public interface ValidateFormatJsonService {

    Map<String, String> getMap(String json);

    List<Map<String, String>> getListMap(String json);

    List<String> getArrayJsonToList(String json);

}
