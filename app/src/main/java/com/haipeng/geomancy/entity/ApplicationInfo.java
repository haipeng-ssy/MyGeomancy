package com.haipeng.geomancy.entity;

/**
 * Created by Tyler on 2015/1/31.
 */
public class ApplicationInfo {
    long intalled_time;//什么时间安装的
    long uninstalled_time;//什么时间卸载的
    String out_activity;//什么页面退出的
    HouseOwner houseOwner;//什么用户操作的
    public void setIntalled_time(long intalled_time) {
        this.intalled_time = intalled_time;
    }

    public long getIntalled_time() {
        return intalled_time;
    }

    public void setUninstalled_time(long uninstalled_time) {
        this.uninstalled_time = uninstalled_time;
    }

    public long getUninstalled_time() {
        return uninstalled_time;
    }

    public void setOut_activity(String out_activity) {
        this.out_activity = out_activity;
    }

    public String getOut_activity() {
        return out_activity;
    }

    public void setHouseOwner(HouseOwner houseOwner) {
        this.houseOwner = houseOwner;
    }

    public HouseOwner getHouseOwner() {
        return houseOwner;
    }
}
