package com.newfly.pojo;


// 武器
public class Weapon extends Equipment
{
    int classify;   // 装备类型
    boolean binded;

    int ability; // 战力
    String image;


    public boolean isBinded() {
        return binded;
    }



    public void setBinded(boolean binded) {
        this.binded = binded;
    }
}
