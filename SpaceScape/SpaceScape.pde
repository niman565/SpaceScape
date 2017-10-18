import ddf.minim.*;
Minim m;
AudioPlayer mainArp;
AudioPlayer addDrums;
AudioPlayer addQuickArp;
AudioPlayer addKeys;

AudioPlayer wallHit;
AudioPlayer exploHit;
AudioPlayer winLevel;

Level l;
Player p;
Explosion e;

int time = 0;
int level = 1;
int lives = 10;
float difficulty = 0;

boolean win = false;
boolean isLeft, isRight, isUp;
boolean contin = false, isEasy = false, isMedium = false, isHard = false, firstTime = true;
boolean endState = false;

void setup(){
  size(1000,800);
  background(200);
  
  m = new Minim(this);
  mainArp = m.loadFile("mainArp.mp3");
  addDrums = m.loadFile("drums.mp3");
  addQuickArp = m.loadFile("quickArp.mp3");
  addKeys = m.loadFile("keys.mp3");
  wallHit = m.loadFile("wallHit.mp3");
  exploHit = m.loadFile("explosion.mp3");
  winLevel = m.loadFile("winLevel.mp3");
  mainArp.loop();
  l = new Level(level);
  p = new Player(l.numHoles, wallHit);
  e = new Explosion();
}

void draw(){
  PFont font = createFont("CourierNewPSMT-48.vlw",40);
  textFont(font);
  background(0);
  if(!contin){
  text("Welcome to SPACESCAPE", 250, 250);
  text("Press ENTER to start!", 250, 300);
  }
  else{
  clear();
  text("Please choose your difficulty!", 250,250); 
  text("Press 'H' for Hard, 'M' for Medium, and 'E' for Easy", 70,300);
  if(key == 'e' || key == 'E'){
    difficulty = 0;
    isEasy = true;
    firstTime = false;
  }
  else if(key == 'm' || key == 'M'){
    difficulty = 1;
    isMedium = true;
    firstTime = false;
  }
  else if(key == 'h' || key == 'H'){
    difficulty = 3;
    isHard = true;
    firstTime = false;
  }
  if(!firstTime){
  clear();
  translate(width/2, height/2);
  background(0);
  textSize(64);
  fill(255,255,255);
  text("Lives: " + ((endState && !win) ? lives - 1 : lives), -450, -300);
  
  if(p.radius > l.walls[l.numCircles - 1].diameter / 2 + 5){
    win = true;
    text("that was a neat level...\nhit enter to do another one", -400, 0);
    winLevel.play();
    endState = true;
    isLeft = isRight = isUp = false;
  }
  else if(e.diameter >= p.radius * 2){
    text("press enter to git good", -350, 0);
    exploHit.play();
    endState = true;
    isLeft = isRight = isUp = false;
  }
  else {
    if(isRight){
      if(p.angVel < 0.2){
        p.angVel += 0.01 * 20/ p.radius;
      }
    }
    else{
      if(p.angVel > 0){
        p.angVel /= 2;
      }
    }
  
    if(isLeft){
      if(p.angVel > -0.2){
        p.angVel -= 0.01 * 20/ p.radius;
      }
    }
    else{
      if(p.angVel < 0){
        p.angVel /= 2;
      }
    }
  
    if(isUp){
      if(p.rVel < 3){
        p.rVel += 0.1;
      }
    }
    else{
      if(p.rVel > 0){
        p.rVel /= 2;
      }
    }
    l.draw(difficulty);
    p.draw();
    if(time > 250){
      e.draw();
    }
    time++;
  }
}
  }
}

void keyPressed(){
  if(!endState){
    setMove(keyCode, true);
  }
}

void keyReleased(){
  if(!endState){
    setMove(keyCode, false);
  }
  else{
    if(keyCode == ENTER){
      if(win)
      ++level;
      else lives--;
      if(lives == 0)
      {
        level = 1;
        lives = 10;
      }
      if(level == 1 && lives == 10){
        mainArp.pause();
        addDrums.pause();
        addQuickArp.pause();
        addKeys.pause();
        mainArp.rewind();
        mainArp.loop();
        
      }
      if(level == 3 && win){
        addDrums.loop();
        addDrums.skip(mainArp.position());
        mainArp.pause();
      }
      if(level == 6 && win){
        addQuickArp.loop();
        addQuickArp.skip(addDrums.position());
        addDrums.pause();
      }
      
      if(level == 8 && win){
        addKeys.loop();
        addKeys.skip(addQuickArp.position());
        addQuickArp.pause();
      }
      exploHit.pause();
      exploHit.rewind();
      winLevel.pause();
      winLevel.rewind();
      l = new Level(level);
      p = new Player(l.numHoles, wallHit);
      e = new Explosion();
      time = 0;
      win = false;
      endState = false;
    }
  }
}

boolean setMove(int k, boolean b) {
  switch (k) {
  case UP:
    return isUp = b;
 
  case LEFT:
    return isLeft = b;
 
  case RIGHT:
    return isRight = b;
 
  case ENTER:
    return contin = true;
  default:
    return b;
  }
}