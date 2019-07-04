package com.newfly.pojo;


import java.util.HashMap;
import java.util.Map;

/*
 * player战斗属性
 * */
public class Combat
{
    private static Map<String, Combat> monsters;

    private int id;   // 角色ID
    private String name;

    private int hp; // 血量
    private int sp; // 蓝

    private int attack;    // 物攻
    private int defense;    // 护甲

    private int crit;   // 暴击
    private int tou;    // 坚韧

    private int hit;    // 命中
    private int agl;    // 躲闪

    void initMonstaer() {
        monsters = new HashMap<>();
    }

    public static Combat getMonster(String id) {
        return monsters.get(id);
    }


    public int getId() {
        return id;
    }

    public static Map<String, Combat> getMonsters() {
        return monsters;
    }

    public int getHp() {
        return hp;
    }

    public int getSp() {
        return sp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getCrit() {
        return crit;
    }

    public int getTou() {
        return tou;
    }

    public int getHit() {
        return hit;
    }

    public int getAgl() {
        return agl;
    }


    public void setId(int id) {
        this.id = id;
    }

    public static void setMonsters(Map<String, Combat> monsters) {
        Combat.monsters = monsters;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setCrit(int crit) {
        this.crit = crit;
    }

    public void setTou(int tou) {
        this.tou = tou;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public void setAgl(int agl) {
        this.agl = agl;
    }
}
