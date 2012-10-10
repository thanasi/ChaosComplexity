// Iterative map with a graphing class
// to look at different behaviors of f(x,R)

TGrapher graph1, graph2;

float R, R0, X, X0;

int nIterX = 1000;
int nR = 1000;
float xmin = 0;
float xmax = 1;
float rmin = 1;
float rmax = 3;

// square window and graph
// for the iterative map
int W = 1000;
int H = 700;

// iterative map function
float f( float x, float r ) {
  return r * x * (1-x);
}

void setup() {

  R0 = 1;
  X0 = .99;

  size(W, H);
  background(0);


  //graph1 = new TGrapher("graph1");
  graph2 = new TGrapher("graph2");

  /*
  graph1.setBounds(xmin, xmax, xmin, xmax);
  graph1.setWindow(25, 50, graphsize, graphsize);
  graph1.drawFrame();
  */
  
  graph2.setBounds(0, 4, 0, 1);
  graph2.setWindow(0,0, 1000,800);
  graph2.setpSize(1);
  graph2.drawFrame();

  /*
  // plot background curves
  graph1.setpSize(1);
  for (int n=0; n<nR; n++) {
    R = rmin + n * rmax / nR;
    for (int i=0; i<graphsize; i++) {
      X = float(i) / graphsize;
      stroke(0, 255, 0);
      fill(0, 255, 0);
      // println("" + X + "\t" + f(X,R));
      graph1.plotPoint(X, f(X, R));
      fill(0, 0, 255);
      stroke(0, 0, 255);
      graph1.plotPoint(X, X);
    }
  }

  graph1.setpSize(4);
  fill(255, 0, 0);
  stroke(255, 0, 0);
  */
  
  compute();
}

void draw() {
}

void compute() {

  int rStep = int((rmax - rmin) / nR);

  for (int n=0; n<nR; n++) {
    R = rmin + n * rmax/nR;
    X = X0;
    for (int i=0; i<nIterX; i++) {
      // graph1.plotPoint(X, f(X, R));
      X = f(X, R);
    }  

    graph2.plotPoint(R, X);
  }
}


// TGrapher
// Thanasi graphing class
public class TGrapher {

  // plotting bounds
  float xmin, xmax, ymin, ymax;

  // width and height of the graph
  int w, h;

  // location in main window
  int w0, h0;

  // point size for plotting
  int pS;

  String name;

  // class constructor
  public TGrapher(String name_) {
    name = name_;
    xmin = 0;
    xmax = 1;
    ymin = 0;
    ymax = 1;

    w0 = 100;
    h0 = 100;

    pS = 2;
  }


  ////////////////////////////////////////  
  // class parameter setting methods
  ////////////////////////////////////////

  public void setBounds(float x1, float x2, float y1, float y2) {
    xmin = x1;
    xmax = x2;
    ymin = y1;
    ymax = y2;
  }

  public void setWindow(int w0_, int h0_, int w_, int h_) {
    w = w_;
    h = h_;
    w0 = w0_;
    h0 = h0_;
  }


  private void setpSize( int p ) {
    if (p == 1) pS = 1;
    else if (p % 2 == 1) pS = p+1;
    else pS = p;
  }   


  ////////////////////////////////////////  
  // drawing methods
  ////////////////////////////////////////  

  public void drawFrame() {
    noFill();
    stroke(255, 255, 255);
    rect(w0, h0, w, h);
  }


  private void plotPoint(float x, float y) {

    if (x > xmax) System.out.println(name + ": x value too high");
    else if (x < xmin) System.out.println(name + ": x value too low");    
    else if (y > ymax) System.out.println(name + ": y value too high");
    else if (y < ymin) System.out.println(name + ": y value too low");
    else {

      int xx = int( x * w / (xmax-xmin) );
      int yy = int( y * h / (ymax-ymin) );
      if (pS == 1) point(xx+w0, (h0+h)-yy);
      else {      
        ellipseMode(CENTER);
        ellipse(xx + w0, (h0+h) - yy, pS, pS);
      }
    }
  }

  private void plotPoints(float [] x, float [] y, int N) {

    for (int n = 0; n<N; n++) {
      plotPoint(x[n], y[n]);
    }
  }
}

