// IterativeMap1
// Explorations in plotting the iterative map


float R, X, X0;

int nIter = 1000;
float xmin = 0;
float xmax = 1;
float rmin = 0;
float rmax = 4;

// square window and graph
// for the iterative map
int windowsize = 500;
int graphsize = 400;
int I, J;

int gmin = windowsize/2 - graphsize/2;
int gmax = windowsize/2 + graphsize/2;

float f( float x, float r ) {
  return r * x * (1-x);
}

int ftoGraph(float x) {
   
  return gmin + int(graphsize * x / (xmax - xmin));
  
}

void setup() {
   
  R = 3;
  X0 = .99;
 
  size(windowsize, windowsize);

  background(0);  
  stroke(255,255,255);
  rectMode(CENTER);
  ellipseMode(CENTER);
  noFill();
  rect(windowsize/2, windowsize/2, graphsize, graphsize);
  
  text("1", gmin - 15, gmin + 5);
  text("f(x)", gmin - 25, windowsize/2);
  
  text("0", gmin - 10, gmax + 15);
  
  text("x", windowsize/2, gmax + 25);
  text("1", gmax, gmax + 15);
  
  for (int i = 0; i<graphsize; i++) {
    stroke(0,255,0);
    X = float(i) / graphsize;
    I = i + gmin;
    J = windowsize - ftoGraph(f(X,R));
    point(I,J); 
    println("x: " + X + "\tf(x): " + f(X, R));
    println("I: " + I + "\tJ: " + J + "\n");
    }  
  
    stroke(0,0,255);
    line(gmin,windowsize - gmin, gmax,windowsize - gmax);
    
    stroke(255,0,0);
    fill(255,0,0);
    X = X0;
 
    for (int n=0; n<nIter; n++) {
      ellipse(ftoGraph(X), windowsize - ftoGraph(f(X,R)), 4,4);
      X = f(X,R);
      println("n: " + n);
    }
}

void draw() {

  
  
  

}
