class Level{
  int numCircles;
  int numHoles;
  Circle[] walls;
  
  Level(int _numCircles){
    numCircles = _numCircles;
    walls = new Circle[numCircles];
    numHoles = (int)random(3, 8);
    for(int i = 0; i < numCircles; ++i){
      walls[i] = new Circle(i+1, numHoles, random(0, TWO_PI/((float)numHoles)), random(-0.02 / (i+1),0.02 / (i+1)), 20);
    }
  }
  
  void draw(float difficulty){
    stroke(255);
    for(int i = l.numCircles - 1; i >= 0; --i){
      walls[i].draw(difficulty);
    }
  }
};