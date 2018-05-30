package cn.jzvd;

import java.util.LinkedHashMap;
import java.util.Map;

public class JZDataSource {

    private LinkedHashMap<String, Object> map;
    private boolean loop;
    private Map<String, String> headers;

    /**
     * @param map The (text, url) or (text, assetFileDescriptor) that will be played.
     */
    public JZDataSource(LinkedHashMap<String, Object> map) {
        this.map = map;
    }

    public LinkedHashMap<String, Object> getMap() {
        return map;
    }

    public boolean isLooping() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @param headers The HTTP request headers.
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public boolean containsUri(Object path) {
        LinkedHashMap<String, Object> map = getMap();
        if (map != null && path != null) {
            return map.containsValue(path);
        }
        return false;
    }

    public String getKey(int index) {
        LinkedHashMap<String, Object> map = getMap();
        int currentIndex = 0;
        for (String key : map.keySet()) {
            if (currentIndex == index) {
                return key;
            }
            currentIndex++;
        }
        return null;
    }

    public Object getCurrentPath(int index) {
        LinkedHashMap<String, Object> map = getMap();
        if (map != null && map.size() > 0) {
            return getValueFromLinkedMap(map, index);
        }
        return null;
    }

    private Object getValueFromLinkedMap(LinkedHashMap<String, Object> map, int index) {
        int currentIndex = 0;
        for (String key : map.keySet()) {
            if (currentIndex == index) {
                return map.get(key);
            }
            currentIndex++;
        }
        return null;
    }
}
