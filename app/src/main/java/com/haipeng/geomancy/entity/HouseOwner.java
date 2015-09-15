package com.haipeng.geomancy.entity;

/**
 * Created by Sunyiyan on 2015/1/31.
 */
public class HouseOwner {
    long id;//身份ID
    String gender;//性别
    String birthday;//生日
    String birthPlace;//出生地

    Relative relative;//所拥有的亲戚
    Boolean IsRelative;//是否是他的亲戚


    int relationNumbers;//当前亲戚的数量,后台获得
    House house;//所拥有的房屋,
    Boolean IsPaid;//该房主的该房屋是否付过费用

    public void setId(long id)
    {
        this.id = id;
    }
    public Long getId()
    {
        return this.id;
    }
    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }
    public String getBirthday(){
        return this.birthday;
    }

    public void setIsRelation(Boolean isRelative) {
        IsRelative = isRelative;
    }

    public Boolean getIsRelative() {
        return IsRelative;
    }

    public void setRelative(Relative relative) {
        this.relative = relative;
    }

    public int getRelationNumbers() {
        return relationNumbers;
    }

    public void setRelationNumbers(int relationNumbers) {
        this.relationNumbers = relationNumbers;
    }

    public Relative getRelative() {
        return relative;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public House getHouse() {
        return house;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setIsPaid(Boolean isPaid) {
        IsPaid = isPaid;
    }

    public Boolean getIsPaid() {
        return IsPaid;
    }

    public void setIsRelative(Boolean isRelative) {
        IsRelative = isRelative;
    }

}
