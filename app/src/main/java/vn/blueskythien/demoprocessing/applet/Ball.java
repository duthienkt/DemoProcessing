package vn.blueskythien.demoprocessing.applet;

/**
 * Created by DuThien on 09/05/2017.
 */

public class Ball {
    double lx, ly;
    double x;
    double y;
    double vx;
    double vy;
    double gX ;
    double gY;

    double m = 1;
    float R, G, B;
    Game p;

    public Ball(Game p, double x, double y) {
        this.p = p;
        this.x = x;
        this.y = y;
        R = (int) p.random(0, 255);
        G = (int) p.random(0, 255);
        B = (int) p.random(0, 255);
        gX = p.gx*20;
        gY = p.gy*20;

    }

    boolean isHover(float x, float y) {
        return MathUtils.distance(x, y, (float) this.x, (float) this.y) <= 50;
    }

    boolean isCatch = false;
    double cx, cy;
    double cvx, cvy;

    void onCatch(float x, float y) {
        isCatch = true;
        cx  =x;
        cy = y;
        cvx = 0;
        cvy = 0;
    }

    void onMove(float x, float y) {
        cx  =x;
        cy = y;
    }

    void onRelease() {
        isCatch = false;
        vx+= cvx;
        vy+= cvy;


    }


    public void update(double dt) {
        lx = x;
        ly = y;
        gX = p.gx*20;
        gY = p.gy*20;
        if (isCatch) {
            x  = cx*0.1+ x*0.9;
            y  = cy*0.1+ y*0.9;
            cvx = (x-lx)/dt;
            cvy = (y-ly)/dt;
        } else {
            vy += gY * dt;
            vx += gX * dt;
            vy*= 1-p.gz/28*dt;
            vx*= 1-p.gz/28*dt;

            y += vy * dt;
            x += vx * dt;
            if (y > p.height - 50) {
                vy = -Math.abs(vy) * 0.99;
                y = p.height - 50;
            }

            if (y<50){
                vy = Math.abs(vy) * 0.99;
                y = 50;
            }

            if (x<50)
            {
                vx = Math.abs(vx) * 0.99;
                x = 50;
            }

            if (x+50> p.width){
                x = p.width-50;
                vx = -Math.abs(vx) * 0.99;
            }
        }
    }

    public void draw() {
        p.noStroke();
        p.fill(R, G, B);
        p.ellipse((float) x, (float) y, 100, 100);
    }

    public double v() {
        return Math.sqrt(vx * vx + vy * vy);
    }


    public double power() {
        double res = 1 / 2 * m * v();
        if (gX>0)
            res+= (p.width-x)*gX;
        else
            res+= -x*gX;
        if (gY>0)
            res+= (p.height-y)*gY;
        else
            res+= -y*gY;

        return res;
    }


    public boolean isAlive() {
        return power() > 1;
    }
}
