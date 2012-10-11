// Iterative map with a graphing class
// to look at different behaviors of f(x,R)

float R0, R, X0;

int c = 0;

int nIter = 2000;
float xmin = 0;
float xmax = 1;
float rmin = 2.5;
float rmax = 4;
float rStep;

int nKeep=200;

// square window and graph
// for the iterative map
int W = 1280;
int H = 768;

float[] RR = new float[W];
float[] XX = new float[nKeep];


// iterative map function
float f( float x, float r ) {
  return r * x * (1-x);
  // return r * .25 * x * (1-x) * (2-x) * (3-x)
}

float[] iterf( float x0, float r, int nIter, int keep) {
  float[] ret = new float[keep];
  float x = x0;
  for (int i = 0; i<nIter; i++) {
    x = f(x, r);
    if ((nIter-i) < keep) {
      // println("" + x);
      ret[keep + i - nIter] = x;
    }
  }
  
  return ret;
}

void setup() {
  
  size(W, H);
  background(0);
  
  X0 = .2;
  
  rStep = (rmax - rmin) / W;
  R = rmin;
  
  background(0);
  stroke(127, 127, 187);
  stroke(255,255,255);
  
  for (int i = 0; i<W; i++) {
    float[] results = iterf(X0, R, nIter, nKeep);
    // println(results[0]);
    for (int j=0; j<nKeep; j++) point(i,(1 - results[j]) * H);
    
    R += rStep;
  }
  
  
}

void draw() {
  
  

}
