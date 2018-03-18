package com.mike.tds.gamestate;

public class EventTexts {
    private String[][] txtMapHolder;

    public EventTexts(){
        this.txtMapHolder = new String[2][2];
        //map 1 -> All mobs killed
        this.txtMapHolder[0][0] =
            "After hunting down the creatures you can hear creaking door sound.\n"+
            "The front door opens showing a dim entrance to the mysterious mansion.\n"+
            "For a moment You had the feeling that someone is close by.\n"+
            "Well you have traveled far and your supplies are running low.\n"+
            "Since you feel tired, perhaps meeting another person will brighten your mood\n"+
            "and maybe owner of the mansion will welcome you?\n\n"+
            "Press Enter...";
        //map 1 -> Trap
        this.txtMapHolder[0][1] =
            "While you were walking up the stairs some mechanism triggered and you fell down!\n"+
            "You walked into a stairs with a trap.\n\n\n"+
            "Press Enter...";

        //map 2 -> Welcome
        this.txtMapHolder[1][0] =
            "A voice in a distance: \n\n"+
            "Hahaha ha\n"+
            "Welcome little stranger...\n"+
            "To my master's house.\n\n"+
            "Press Enter...";
    }

    public synchronized String getTxt(int mapLevel, int event){
        if (mapLevel<txtMapHolder.length) {
            if (event>= 0 && event < txtMapHolder[mapLevel].length)
                return txtMapHolder[mapLevel][event];
            else if (event == -1)
                return "\n\nGAME OVER \n\n\nPress Enter...";
            else if (event == -2)
                return "The voice: \n\n"+
                       "Not bad at all... \n"+
                       "Maybe.. if You survive we will meet someday!\n\n"+
                       "Press Enter...";
        }
        //---
        return null;
    }
}
