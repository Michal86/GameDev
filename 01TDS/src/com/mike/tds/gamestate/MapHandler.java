package com.mike.tds.gamestate;

import com.mike.tds.gameobject.*;
import com.mike.tds.main.Handler;
import com.mike.tds.sounds.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MapHandler extends GameState{

    private Handler handler;
    private Camera camera;
    private Player player;
    private MapEvents mapEvents;
    private Font txtFont;
    private volatile int nextEventTxt = 0;
    private volatile boolean mapIsFinished = false;
    //MAP + Enemy
    protected volatile int level;
    private SpriteSheet sprSheet;
    private BufferedImage mapImage = null;
    private BufferedImage spriteSheetImg = null;
    private BufferedImage floor = null;
    private BufferedImage road = null;
    private int enemiesLeft = 0;
    //----- Player -----
    private GameObject hero = null;
    private SpriteSheet sprPlayer = null, sprWizard = null;
    private BufferedImage playerImg = null, wizardImg=null;
    protected volatile int playersHp = 100;
    protected volatile int arrows = 60;
    private  volatile int MaxHP = 100;
    private AudioPlayer bgSound;

    public MapHandler(GameStateManager gsm) {
        super(gsm);
        //---
        txtFont = makeFont("Jet Set");
        txtFont = txtFont.deriveFont(Font.PLAIN,13);
        //================================
        handler = new Handler();
        camera = new Camera(0, 0);
        this.level = 0;
        mapEvents = new MapEvents(level, txtFont);
        //================================

        //==== GET MAP IMAGE ====
        BufferedImageLoader loader = new BufferedImageLoader();
        mapImage = loader.loadImage("/TDS_Maps.png");

        //==== SPRITES MAP/ENEMIES ====
        spriteSheetImg = loader.loadImage("/sprite_sheet.png");
        sprSheet = new SpriteSheet(spriteSheetImg);
        //wizard
        wizardImg = loader.loadImage("/wizard.png");
        sprWizard = new SpriteSheet(wizardImg);

        //==== HERO PLAYER ====
        playerImg = loader.loadImage("/sprite_player.png");
        sprPlayer = new SpriteSheet(playerImg);

        //==== PREPARE MAP ====
        mapLoader(level);

        //==== SOUNDS ====
        bgSound = new AudioPlayer();
    }//END_OF_constructor

    private synchronized void mapLoader(int mapLevel){
        //Set images according to Map level/stage
        if (level == 0) {
            //grass
            floor = sprSheet.getImage(1, 1, 32, 32);
            //road
            road = sprSheet.getImage(3, 1, 32, 32);
        }
        else if (level == 1){
            floor = sprSheet.getImage(7, 3, 32, 32);
            mapEvents.setMapLevel(level);
            mapEvents.setEventCounter(0);
            startEvent(true);
        }
        //=================
        loadLevel(mapImage, level);
        enemiesLeft = handler.getEnemiesToKill();
        setMapAsFinished(false);
    }

    //Loading map level method
    private void loadLevel(BufferedImage image, int level){
        int imgW = image.getWidth();
        int imgH = image.getHeight();
        int mapY;

        //First level(map) -> 0
        for (int x=0; x<imgW; x++) {
            mapY = 0;
            for (int y = (imgH / 2) * level; y < imgH / (2 - level); y++) {
                int pixel = image.getRGB(x, y);

                //Components will be in a range of 0..255
                //int alpha = (pixel >>>24) & 0xff000000; //>>> required due to sign bit
                int blue = pixel & 0xff;
                int green = (pixel >> 8) & 0xff;
                int red = (pixel >> 16) & 0xff;

                //MOSTLY LVL1
                //Wall
                if (red == 255 && green==0 && blue==0)
                    handler.addObject(new Block(x * 32, mapY * 32, ID.Block, sprSheet));
                //wall+grass
                if (red == 200 & green == 0)
                    handler.addObject(new WallGrass(x * 32, mapY * 32, ID.Block, sprSheet));

                //Red roof
                if ( (red == 160 || red == 155 || red == 150 || red == 145 || red == 140 || red == 135) &&(green==0 && blue==0) )
                    handler.addObject(new RoofRed(x * 32, mapY * 32, ID.Block, sprSheet, red));

                //stairs & doors
                if (red == 170 && blue==0) {
                    if (green == 100)
                        handler.addObject(new CastleEntry(x * 32, mapY * 32, ID.Stairs, sprSheet, green));
                    else if (green == 105)
                        handler.addObject(new CastleEntry(x * 32, mapY * 32, ID.Doors, sprSheet, green));
                        //sign
                    else if (green == 110 || green == 115)
                        handler.addObject(new CastleEntry(x * 32, mapY * 32, ID.Block, sprSheet, green));
                }
                //grass edge
                if (red == 240 & green == 0)
                    handler.addObject(new GrassEdge(x * 32, mapY * 32, ID.GrassEdge, sprSheet));

                //rock
                if (red == 245 & green == 0)
                    handler.addObject(new Rock(x * 32, mapY * 32, ID.Block, sprSheet));

                //Trees & root
                if (red == 150 && (green == 45 || green == 50 || green == 55 ))
                    handler.addObject(new TreeOne(x * 32, mapY * 32, ID.Block, sprSheet, green));

                //PLAYER
                if (blue == 255 && green == 0) {
                    hero = new Player(x * 32, mapY * 32, ID.Player, this, handler, sprPlayer);
                    handler.addObject(hero);
                }
                //Enemy Bear
                if (green == 255 && blue == 0)
                    handler.addObject(new Enemy(x * 32, mapY * 32, ID.Enemy, handler, sprSheet));

                //Enemy Wizard
                if (green == 255 && blue == 249)
                    handler.addObject(new Wizard(x * 32, mapY * 32, ID.Enemy, handler, sprWizard));


                Color crate = new Color(170, 70, 30);
                if (pixel == crate.getRGB())
                    handler.addObject(new Crate(x * 32, mapY * 32, ID.Crate, this, sprSheet));

                //MOSTLY LVL2 - wall+floor
                if (red == 255 && green==40 && blue==10)
                    handler.addObject(new Wall(x * 32, mapY * 32, ID.Block, sprSheet,red, green, blue));

                if (red == 180 && green==40 && blue==40)
                    handler.addObject(new Wall(x * 32, mapY * 32, ID.Block, sprSheet,red, green, blue));

                if (red == 160 && green==30 && blue==50)
                    handler.addObject(new Wall(x * 32, mapY * 32, ID.Block, sprSheet,red, green, blue));

                if (red == 104 && green==55 && blue==36)
                    handler.addObject(new Wall(x * 32, mapY * 32, ID.Block, sprSheet,red, green, blue));

                if (red == 132 && green==97 && blue==65)
                    handler.addObject(new Wall(x * 32, mapY * 32, ID.Block, sprSheet,red, green, blue));

                if (red == 105 && green==60 && blue==60)
                    handler.addObject(new Wall(x * 32, mapY * 32, ID.Block, sprSheet,red, green, blue));
                //--- healing pot ---
                if (red == 110 && green==00 && blue==200)
                    handler.addObject(new Potion(x * 32, mapY * 32, ID.Potion, this, sprSheet));


                mapY++;
            }
        }
    }//END_OF_loadLevel

    private void setMapLevel(){
        while(!handler.cleared()) {
            this.handler.clearOldObjects();
        }

        this.level++;
        //After clearing object from previous map set new
        mapLoader(level);
        //now set to false
        handler.setCleared();
    }
    //CHECK IF MAP IS FINISHED or EVENT OCCURED
    private boolean mapFinished(){
        return this.mapIsFinished;
    }

    public synchronized void setMapAsFinished(boolean done){
        this.mapIsFinished = done;
    }

    public synchronized void startEvent(boolean start){
        if (mapEvents.eventStarted() == start)
            return;

         mapEvents.setStartEvent(start);
    }
    public synchronized void setEventTick(int next){
        this.nextEventTxt = next;
    }
    //================================
    @Override
    public void tick() {
        //CHECK MAP STATE-> LOAD NEXT LVL
        if (mapFinished() && !mapEvents.eventStarted()) {
            setMapLevel();
            //System.out.println("Check");
        }
        else {
            //Connect camera to a Hero/Player
            for (int i = 0; i < handler.object.size(); i++) {
                if (handler.object.get(i).getId() == ID.Player) {
                    camera.tick(handler.object.get(i));
                }
            }

            handler.tick();
            //Check only when map is not finished -> -1 enemies
            if (enemiesLeft > 0) {
                enemiesLeft = handler.getEnemiesToKill();

                if (enemiesLeft == 0 && level==0)
                    startEvent(true);
                else if ((enemiesLeft == 0 && level==1))
                    gameOver(); //Ending txt
            }
            //Event started w8 for enter keys to continue
            if (mapEvents.eventStarted() && nextEventTxt != -1 && nextEventTxt!= -2) {
                mapEvents.tick(nextEventTxt);
                setEventTick(0);
            }
            else if (nextEventTxt == -2) {
                gameOver();
            }
        }
    }//END_OF_tick

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //==== HERE WE'RE DRAWING ====
        g2d.translate(-camera.getX(),-camera.getY());

        for (int x = 0; x < 32*64; x+=32){
            for (int y =0; y < 32*36; y+=32){

                g.drawImage(floor, x, y, null);

                //===== MAP 1 =====
                if (level == 0) {
                    if ((x == 1408 || x == 1440) && (y >= 224 && y <= 384))
                        g.drawImage(road, x, y, null);
                    else if ((x >= 1472 && x <= 2016) && (y == 352 || y == 384))
                        g.drawImage(road, x, y, null);
                }
            }
        }
        //=====
        camera.render(g);
        handler.render(g);

        g2d.translate(camera.getX(), camera.getY());
        //===================================
        //Display HP bar and amount of arrows
        drawPlayersStats(g);
        //Display Event text
        mapEvents.render(g);
    }//END_OF_render

    @Override
    public void init() {

    }
    //================ PLAYER ================
    public synchronized int getPlayersHp(){
        return this.playersHp;
    }

    public synchronized void drinkHpPot(int addHp){
        this.playersHp += addHp;
    }

    public synchronized void setPlayersHpAfterHit(int amount){
        this.playersHp += amount;
    }

    public void setArrows(int amount){
        this.arrows += amount;
    }

    public synchronized void gameOver(){
        if (level == 0 || level == 1 && getPlayersHp()<=0) {
            if (nextEventTxt != -1) {
                startEvent(true);
                nextEventTxt = -1;
                mapEvents.tick(nextEventTxt);
            }
            if (!mapEvents.eventStarted()) {
                gsm.statePop(false);
                bgSound.stop();
            }
        }
        else if (level==1) {
            if (nextEventTxt != -2) {
                startEvent(true);
                nextEventTxt = -2;
                mapEvents.tick(nextEventTxt);
            }
            //System.out.println("!mapEvents.eventStarted()? : " + !mapEvents.eventStarted());
            if (!mapEvents.eventStarted()) {
                gsm.statePop(false);
                bgSound.stop();
            }
        }
    }
    //===== PLAYERS STATR -> ARROWS/HP/ENEMIES
    private void drawPlayersStats(Graphics g){
        //HP bar
        Color tmp = new Color(45, 100, 45);
        g.setColor(Color.darkGray);
        g.fill3DRect(399,5, 202, 20,true);
        g.setColor(tmp);
        g.fillRect(400,6, playersHp*2, 18);
        g.setFont(txtFont);
        g.setColor(Color.white);
        g.drawString(playersHp +"/"+ MaxHP, 475,21);
        //Ammo
        tmp = new Color(40, 90, 165);
        g.setColor(tmp);
        g.fill3DRect(10,5, 105, 17,true);
        g.setColor(Color.white);
        g.drawString("Arrows: " + arrows, 15, 19);
        //Enemies
        tmp = new Color(160, 47, 60);
        g.setColor(tmp);
        g.fill3DRect(120,5, 100, 17,true);
        g.setColor(Color.white);
        g.drawString("Enemies: " + enemiesLeft, 125, 19);
    }//END_OF_drawPlayer


    //================ KEY AND MOUSE  EVENTS ================
    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ESCAPE)
            gsm.statePush(new PauseGame(gsm, (MainMenu) gsm.getState(0), bgSound) );

        if (mapEvents.eventStarted() && (key == KeyEvent.VK_ENTER)) {
            startEvent(false);
        }

        else if (!mapEvents.eventStarted()){
            for (GameObject gm : handler.object) {
                if (gm.getId() == ID.Player) {
                    if (key == KeyEvent.VK_W) handler.setUp(true);
                    if (key == KeyEvent.VK_S) handler.setDown(true);
                    if (key == KeyEvent.VK_A) handler.setLeft(true);
                    if (key == KeyEvent.VK_D) handler.setRight(true);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();

        for (GameObject gm : handler.object) {
            if (gm.getId() == ID.Player){
                if (key == KeyEvent.VK_W) handler.setUp(false);
                if (key == KeyEvent.VK_S) handler.setDown(false);
                if (key == KeyEvent.VK_A) handler.setLeft(false);
                if (key == KeyEvent.VK_D) handler.setRight(false);
            }
        }
    }

    //Mouse
    @Override
    public void mousePressed(MouseEvent e) {
        //Coordination the bullet is flying to
        int mX = (int) (e.getX() + camera.getX());
        int mY = (int) (e.getY() + camera.getY());

        for (int i=0; i<handler.object.size(); i++) {
            GameObject tempObj = handler.object.get(i);

            if (tempObj.getId() == ID.Player & this.arrows >= 1) {

                player = (Player) tempObj;
                if (!player.attackCoolDown()) {
                    player.setArrack(true);
                    player.setMouseX(mX);
                    player.setMouseY(mY);
                }
            }
        }
    }//END_OF_mousePressed

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
}
