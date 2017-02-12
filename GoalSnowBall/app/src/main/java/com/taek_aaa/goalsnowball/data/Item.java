package com.taek_aaa.goalsnowball.data;

/**
 * Created by taek_aaa on 2017. 2. 10..
 */

public class Item {
    public String bulbImageItem;
    public String goalItem;
    public String dateItem;
    public String goldItem;
    public int goldContent;
    public String whatdatetype;
    public int currentAmountItem;
    public int goalAmounItem;

    public Item(String bulb, String goal, String date, String gold, int goldAmount, String wdt, int current, int goalAmount){
        bulbImageItem = bulb;
        goalItem = goal;
        dateItem = date;
        goldItem = gold;
        goldContent = goldAmount;
        whatdatetype = wdt;
        currentAmountItem=current;
        goalAmounItem=goalAmount;
    }

}
