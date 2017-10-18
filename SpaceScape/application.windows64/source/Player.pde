

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
  
  void draw(){
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
      wallHit.setVolume(0.25);
      wallHit.play();
    }
    for(int i = 0; i < numPoints; ++i){
      ellipse(radius*cos(baseAngle + i*(TWO_PI / ((float)numPoints) ) ), radius*sin(baseAngle + i*(TWO_PI / ((float)numPoints) ) ), 5, 5);
      //image(sprite, radius*cos(baseAngle + i*(TWO_PI / ((float)numPoints) ) ), radius*sin(baseAngle + i*(TWO_PI / ((float)numPoints) ) ));
    }
  }
  
  boolean _handleWallCollision(Circle c){
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

boolean handleWallCollision(Level l){
  boolean buff = false;
  for(int i = 0; i < l.numCircles; ++i){
    buff = buff || _handleWallCollision(l.walls[i]);
  }
  return buff;
}
  
};