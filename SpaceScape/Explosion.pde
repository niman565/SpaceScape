class Explosion
{
  float diameter = 5;
  float speed = 1.5;
  Explosion(){}
  
  void draw()
  {
    stroke(0);
    fill(234, 160, 21, 200);
    diameter += speed;
    ellipse(0, 0, diameter, diameter);
  }
  
}