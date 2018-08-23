package cn.jzvd;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class JZDataSource {
    public Object currentDataSource;//正在播放的当前url或uri

    public LinkedHashMap urlsMap;
    public HashMap headerMap;
    public boolean looping = false;
    public Object[] objects;
    
    /**
     * 第一个是url的map
     * 第二个是loop
     * 第三个是header
     * 第四个是context
     */
    public Object[] dataSourceObjects;//包含了地址的map（多分辨率用），context，loop，header等
}
