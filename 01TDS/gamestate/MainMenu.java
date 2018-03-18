package com.mike.tds.gamestate;

import com.mike.tds.gameobject.BufferedImageLoader;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainMenu extends GameState{

    private String[] btnNames = {"play", "info", "quit", "back"};
    private BufferedImage menuImage = null, credits = null;
    private Map<String, Button> buttons;
    private volatile ArrayList<Button> activeButtons;

    public Font nameFont, txtFont;
    private String infoString;
    String lrString = "Lone Ranger";
    private volatile boolean info = false;

    public MainMenu(GameStateManager gsm){
        super(gsm);

        BufferedImageLoader loader = new BufferedImageLoader();
        menuImage = loader.loadImage("/Menu_start.png");
        credits = loader.loadImage("/credits.png");

        buttons = new HashMap<>();
        addButtons(loader, btnNames.length, 15);

        activeButtons = new ArrayList<>(this.buttons.values());
        setInfo(false);

        nameFont = makeFont("8bitlim");
        txtFont = makeFont("AtariST8x16SystemFont");
        txtFont = txtFont.deriveFont(Font.PLAIN,15);
        if (nameFont == null) nameFont = new Font("Verdena", Font.BOLD, 40);
        else if (txtFont == null) txtFont = new Font("Verdena", Font.BOLD, 13);

        ////===== SHORT INFO =====
        infoString = "Mini TDS by Michal Radzewicz\n\n" +
            "It's my second attempt to write something bigger that previous project -\n" +
            "game called \"mini tennis\".\n"+
            "Feel free to play/comment/report bugs or copy the code.\n" +
            "My e-mail: radzewicz.michal@gmail.com\n\n"+
            "If You are looking for a very nice graphics for the project visit https://itch.io \n"+
            "Graphics I used are under a CC-BY-3.0 License - \n" +
            "http://creativecommons.org/licenses/by/3.0 \n\n"+
            "I would like to give credits to people who made some assets,\n"+
            "that I did modify to suit my project:\n" +
            "Artwork created by Luis Zuno and Demo Music by Pascal Belisle,\n"+
            "Ivan Voirol from opengameart.org/content/basic-map-32x32-by-silver-iv\n\n"+
            "\nMany Thanks!";
    }

    //===== ACTIVATE/DEACTIVATE INFO DISPLAY =====
    public void setInfo(boolean b){
        this.info = b;
    }

    private synchronized void activeInfo(){
        if (info)
            return;

        changeButtonActivation();
        setInfo(true);
    }

    private synchronized void deactivateInfo(){
        if (!info)
            return;

        changeButtonActivation();
        setInfo(false);
    }
    //==========
    @Override
    public void render(Graphics g) {
        if (info){
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0,0,1000,563);
            //==========

            nameFont = nameFont.deriveFont(Font.BOLD,50);
            g.setFont(nameFont);
            g.setColor(Color.RED);
            g.drawString(lrString, 1000/2-g.getFontMetrics().stringWidth(lrString)/2,50);

            g.setFont(txtFont);
            g.setColor(Color.BLACK);
            drawString(g, infoString, 10,55);

            g.drawImage(credits, 0,0,null);
            g.drawImage(buttons.get("back").getButtonImage(),
                    buttons.get("back").getBtnX(), buttons.get("back").getBtnY(), null);
        }
        else {
            g.drawImage(menuImage, 0, 0, null);
            drawButtonsExcept(g,"back");
        }

    }


    //===== CHANGE STATE/DISPLAY INFO =====
    private synchronized void ifButtonPressed(Button button){
        if(button.getName().equals("play")) {
            gsm.statePush(new MapHandler(gsm));
            //System.out.println("PLAY");
           // System.out.println(button.toString());
        }
        else if (button.getName().equals("info")){
            activeInfo();
        }
        else if (button.getName().equals("quit")){
            System.exit(0);
        }
        else if (button.getName().equals("back")){
            deactivateInfo();
        }

    }

    //===== ACTIVATE BUTTONS ACCORDING TO STATE =====
    private void changeButtonActivation(){
        for (Button currentBtn : activeButtons) {
            //reverse state
            boolean reverse = !currentBtn.getIsActive();
            currentBtn.setIsActive(reverse);
        }
    }

    //===== ADD BUTTONS =====
    private void addButtons(BufferedImageLoader image, int amountToAdd, int spaceBetween){
        String tmpName;
        Button currentBtn;
        BufferedImage tmpImg;
        int tmpX,tmpY, space = 0;

        for(int i=0; i<amountToAdd; i++){
            tmpName = "/" +btnNames[i]+ "_button.png";
            tmpImg = image.loadImage(tmpName);
            tmpX =  1000/2 - tmpImg.getWidth()/2;

            if (!tmpName.contains("back")) {
                tmpY = 563 / 3 - 10 - spaceBetween;

                currentBtn = new Button(tmpImg, tmpX, tmpY+space,btnNames[i]);
                currentBtn.setIsActive(true);
            }else{
                space = 0;
                tmpY = (int) (563 * 0.85f);

                currentBtn = new Button(tmpImg, tmpX, tmpY+space,btnNames[i]);
                currentBtn.setIsActive(false);
            }

            this.buttons.put(btnNames[i], currentBtn);
            space += tmpImg.getHeight()+spaceBetween;
        }
    }

    protected Button getButton(String buttonName){
        Button newBtn = null;
        for (String btn : buttons.keySet()) {
            if (btn.equals(buttonName)) {
                newBtn = new Button(buttons.get(buttonName));
            }
        }
        return newBtn;
    }

    private void drawButtonsExcept(Graphics g, String except){
        for (String btnName : buttons.keySet()) {
            if (!btnName.contains(except))
                g.drawImage(buttons.get(btnName).getButtonImage(),
                        buttons.get(btnName).getBtnX(), buttons.get(btnName).getBtnY(), null);
        }
    }

    //===== GET MY FONTS =====
    private Font makeFont(String name) {
        Font myFont = null;
        try {
            myFont = Font.createFont(
                    Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/font/"+name+".ttf")
            );
            if(myFont == null)
                myFont = Font.createFont(
                        Font.PLAIN, this.getClass().getResourceAsStream("/font/"+name+".ttf")
                );

            return myFont;
        }
        catch (FontFormatException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void drawString(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight()+2);
    }

    //===== MOUSE EVENT =====
    @Override
    public void mousePressed(MouseEvent e) {
        int mX = (int) e.getX();
        int mY = (int) e.getY();

        //System.out.println("x="+ mX +" y="+mY);

        for (Button currentBtn : activeButtons) {
            if (currentBtn.getIsActive()) {
                int btX = currentBtn.getBtnX();
                int btY = currentBtn.getBtnY();

                if (mX > btX && mX < (btX + currentBtn.getBtnW()) &&
                        mY > btY && mY < (btY + currentBtn.getBtnH())) {
                    ifButtonPressed(currentBtn);
                }

            }
        }//END_OF_FOR

    }

    @Override
    public void init() {
        activeInfo();
        //System.out.println("Aktywacja info - " + info);
    }

    @Override
    public void tick() {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e){

    }
}
