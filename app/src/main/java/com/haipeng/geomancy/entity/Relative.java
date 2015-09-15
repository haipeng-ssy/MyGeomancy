package com.haipeng.geomancy.entity;

/**
 * Created by Sunyiyan on 2015/1/31.
 */
public class Relative {
    String RelativeId;//身份ID
    String RelativeBirthday;//生日
    String RelativeGender;//性别
    String RelativeBirthPalce;//出生地
    String RelativeCall;//称呼
    
    public void setRelativeBirthday(String RelativeBirthday) {
        RelativeBirthday = RelativeBirthday;
    }

    public String getRelativeBirthday() {
        return RelativeBirthday;
    }

    public void setRelativeId(String RelativeId) {
        RelativeId = RelativeId;
    }

    public String getRelativeId() {
        return RelativeId;
    }

    public void setRelativeGender(String relativeGender) {
        RelativeGender = relativeGender;
    }

    public String getRelativeGender() {
        return RelativeGender;
    }

    public void setRelativeBirthPalce(String relativeBirthPalce) {
        RelativeBirthPalce = relativeBirthPalce;
    }

    public String getRelativeBirthPalce() {
        return RelativeBirthPalce;
    }

    public void setRelativeCall(String relativeCall) {
        RelativeCall = relativeCall;
    }

    public String getRelativeCall() {
        return RelativeCall;
    }
}
