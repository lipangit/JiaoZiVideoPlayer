package cn.jzvd;

import java.util.LinkedList;

/**
 * Put JZVideoPlayer into layout
 * From a JZVideoPlayer to another JZVideoPlayer
 * Created by Nathen on 16/7/26.
 */
public class JzvdMgr {

    public static Jzvd currentJzvd;
    //每次移动jzvd的时候都要把body存起来
    public static LinkedList jzvdBodys = new LinkedList();


    //    public static Jzvd FIRST_FLOOR_JZVD;
//    public static Jzvd SECOND_FLOOR_JZVD;
//
//    public static Jzvd getFirstFloor() {
//        return FIRST_FLOOR_JZVD;
//    }
//
//    public static void setFirstFloor(Jzvd jzvd) {
//        FIRST_FLOOR_JZVD = jzvd;
//    }
//
//    public static Jzvd getSecondFloor() {
//        return SECOND_FLOOR_JZVD;
//    }
//
//    public static void setSecondFloor(Jzvd jzvd) {
//        SECOND_FLOOR_JZVD = jzvd;
//    }
//
    public static Jzvd getCurrentJzvd() {
        return currentJzvd;
    }

    public static void completeAll() {
        if (currentJzvd != null)
            currentJzvd.onCompletion();
        jzvdBodys.clear();
    }
//complete currentjzvd, 清空jzvdBodys
//
//    public static void completeAll() {
//        if (SECOND_FLOOR_JZVD != null) {
//            SECOND_FLOOR_JZVD.onCompletion();
//            SECOND_FLOOR_JZVD = null;
//        }
//        if (FIRST_FLOOR_JZVD != null) {
//            FIRST_FLOOR_JZVD.onCompletion();
//            FIRST_FLOOR_JZVD = null;
//        }
//    }
}
