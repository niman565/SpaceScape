import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SpaceScape extends PApplet {


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

public void setup(){
  
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

public void draw(){
  PFont font = createFont("CourierNewPSMT-48.vlw",30);
  textFont(font);
  background(0);
  if(!contin){
  text("Welcome to SUPER'SCAPE", 250, 250);
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
      if(p.angVel < 0.2f){
        p.angVel += 0.01f * 20/ p.radius;
      }
    }
    else{
      if(p.angVel > 0){
        p.angVel /= 2;
      }
    }
  
    if(isLeft){
      if(p.angVel > -0.2f){
        p.angVel -= 0.01f * 20/ p.radius;
      }
    }
    else{
      if(p.angVel < 0){
        p.angVel /= 2;
      }
    }
  
    if(isUp){
      if(p.rVel < 3){
        p.rVel += 0.1f;
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

public void keyPressed(){
  if(!endState){
    setMove(keyCode, true);
  }
}

public void keyReleased(){
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

public boolean setMove(int k, boolean b) {
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
class Explosion
{
  float diameter = 5;
  float speed = 1.5f;
  Explosion(){}
  
  public void draw()
  {
    stroke(0);
    fill(234, 160, 21, 200);
    diameter += speed;
    ellipse(0, 0, diameter, diameter);
  }
  
}
class Level{
  int numCircles;
  int numHoles;
  Circle[] walls;
  
  Level(int _numCircles){
    numCircles = _numCircles;
    walls = new Circle[numCircles];
    numHoles = (int)random(3, 8);
    for(int i = 0; i < numCircles; ++i){
      walls[i] = new Circle(i+1, numHoles, random(0, TWO_PI/((float)numHoles)), random(-0.02f / (i+1),0.02f / (i+1)), 20);
    }
  }
  
  public void draw(float difficulty){
    stroke(255);
    for(int i = l.numCircles - 1; i >= 0; --i){
      walls[i].draw(difficulty);
    }
  }
};


class Player{
  AudioPlayer wallHit;
  
  int numPoints;
  int initRadius;
  float radius;
  float baseAngle;
  
  float angVel;
  float rVel;
  
  //PImage sprite;
  
  Player(int _numPoints, AudioPlayer _wallHit){
    wallHit = _wallHit;
    numPoints = _numPoints;
    initRadius = 20;
    radius = initRadius;
    baseAngle = 0;
    angVel = 0;
    rVel = 0;
    //sprite = _sprite;
  }
  
  public void draw(){
    stroke(255);
    noFill();
    baseAngle += angVel;
    baseAngle = baseAngle % (TWO_PI / ((float)numPoints));
    if(baseAngle < 0){
      baseAngle += (TWO_PI / ((float)numPoints));
    }
    radius += rVel;
    if(p.handleWallCollision(l)){
      wallHit.rewind();
      wallHit.setVolume(0.25f);
      wallHit.play();
    }
    for(int i = 0; i < numPoints; ++i){
      ellipse(radius*cos(baseAngle + i*(TWO_PI / ((float)numPoints) ) ), radius*sin(baseAngle + i*(TWO_PI / ((float)numPoints) ) ), 5, 5);
      //image(sprite, radius*cos(baseAngle + i*(TWO_PI / ((float)numPoints) ) ), radius*sin(baseAngle + i*(TWO_PI / ((float)numPoints) ) ));
    }
  }
  
  public boolean _handleWallCollision(Circle c){
    if( abs(baseAngle - c.rotAngle) % (TWO_PI / ((float) c.numHoles)) < asin(c.holeWidth / c.diameter) 
      || abs(baseAngle - c.rotAngle) % (TWO_PI / ((float) c.numHoles)) > TWO_PI / ((float) c.numHoles) - asin(c.holeWidth / c.diameter)
    ){ return false;}
    else if(radius < c.diameter / 2 + 5 && radius > c.diameter / 2 - 5){
      if(radius < c.baseDiameter / 2 + 5){
        radius = initRadius;
      }
      else{
        radius -= 3 * c.baseDiameter / 4;
      }
      return true;
    }
    return false;
  }

public boolean handleWallCollision(Level l){
  boolean buff = false;
  for(int i = 0; i < l.numCircles; ++i){
    buff = buff || _handleWallCollision(l.walls[i]);
  }
  return buff;
}
  
};
class Circle {
  float baseDiameter;
  float diameter;
  int numHoles;
  float rotAngle;
  float rotSpeed;
  float holeWidth;
  
  Circle(int _scale, int _numHoles, float _rotAngle, float _rotSpeed, float _holeWidth){
    baseDiameter = 85;
    diameter = _scale * baseDiameter;
    numHoles = _numHoles;
    rotAngle = _rotAngle;
    rotSpeed = _rotSpeed;
    holeWidth = _holeWidth;
  }
  
  public void draw(float difficulty){
    noFill();
    rotAngle += difficulty*rotSpeed;
    for(int i = 0; i < numHoles; ++i){
      arc(
        0,
        0,
        diameter,
        diameter,
        i * TWO_PI/((float)numHoles) + asin(holeWidth/(diameter)) + rotAngle,
        (i+1)* TWO_PI/((float)numHoles) - asin(holeWidth/(diameter)) + rotAngle
      );
    }
  }
};
  public void settings() {  size(1000,800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "SpaceScape" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
