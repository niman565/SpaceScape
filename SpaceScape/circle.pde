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
  
  void draw(float difficulty){
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