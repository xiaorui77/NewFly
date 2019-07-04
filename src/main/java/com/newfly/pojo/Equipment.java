package com.newfly.pojo;


// 装备
public class Equipment extends Item
{
    private int combatPower;   // 战力
    private short enhanceLevel; // 强化等级
    private short bind; // 绑定

    private int value1; // 属性一
    private int value2; // 属性二
    private int value3; // 属性三


    public Equipment() {
        super();
    }

    // 通过字符串构造
    public Equipment(String item) {
        String[] strings = item.split(":");
        id = Long.parseLong(strings[0]);
        kind = Integer.parseInt(strings[1]);
        backpackIndex = Integer.parseInt(strings[2]);

        combatPower = Integer.parseInt(strings[3]);
        enhanceLevel = Short.parseShort(strings[4]);
        bind = Short.parseShort(strings[5]);
        value1 = Integer.parseInt(strings[6]);
        value2 = Integer.parseInt(strings[7]);
        value3 = Integer.parseInt(strings[8]);
    }

    // 转换为返回的结果
    @Override
    public String toResultContent() {
        return id + ":" + kind + ":" + backpackIndex + ":" + combatPower + ":" + enhanceLevel + ":" + bind + ":" + value1 + ":" + value2 + ":" + value3;
    }


    public int getCombatPower() {
        return combatPower;
    }

    public short getEnhanceLevel() {
        return enhanceLevel;
    }

    public short getBind() {
        return bind;
    }

    public int getValue1() {
        return value1;
    }

    public int getValue2() {
        return value2;
    }

    public int getValue3() {
        return value3;
    }

    public void setCombatPower(int combatPower) {
        this.combatPower = combatPower;
    }

    public void setEnhanceLevel(short enhanceLevel) {
        this.enhanceLevel = enhanceLevel;
    }

    public void setBind(short bind) {
        this.bind = bind;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }

}
