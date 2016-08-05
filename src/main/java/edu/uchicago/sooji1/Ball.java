package edu.uchicago.sooji1;

 import java.awt.*;
 import java.awt.geom.Dimension2D;
 import java.awt.geom.Ellipse2D;
 import java.awt.geom.Point2D;
 import java.util.Random;


public class Ball implements Runnable, Movable
{
    private Ellipse2D.Double ball; // Ball objects shape attributes
    private Point2D.Double ballCoords; // Store location coords of a Ball
    private Point2D.Double ballVelocity; // Store x and y velocity of Ball
    private final Color COLOR_OF_BALL; // Store randomized color of ball
    private double mass;
    private double angle;

    private double radiux;
    public static Random sRandom = new Random();

    // Constructor
    public Ball(Color ballColor, double xPos, double yPos)
    {
        ballCoords = new Point2D.Double(xPos, yPos);
        radiux = sRandom.nextDouble()*20 + 15;
        angle = sRandom.nextInt(361) * (180 / Math.PI);

        switch (sRandom.nextInt(4))
        {
            case 0:
                ballVelocity = new Point2D.Double(sRandom.nextInt(5) + 1, sRandom.nextInt(5) + 1);
                break;

            case 1:
                ballVelocity = new Point2D.Double(-sRandom.nextInt(5) + 1, sRandom.nextInt(5) + 1);
                break;

            case 2:
                ballVelocity = new Point2D.Double(sRandom.nextInt(5) + 1, -sRandom.nextInt(5) + 1);
                break;

            case 3:
                ballVelocity = new Point2D.Double(-sRandom.nextInt(5) + 1, -sRandom.nextInt(5) + 1);
                break;
        }

        mass = radiux * radiux * radiux;
            // 4/3*pi is a constant. So the only thing that affects volume is the radius.
            // And since everything has the same density, volume and mass are directly correlated.
            // Hence, for simplicity, I set mass = r^3.
        COLOR_OF_BALL = ballColor;
        setBallPosition(ballCoords, radiux);
    } // End Ball constructor

    // Update ball position

    public void run()
    {
        while(true)
        {
            move();

            try
            {
                Thread.sleep(20);
            }
            catch(Exception e){}
        }
    } // End run() method

    // private method to set initial ball object
    private void setBallPosition(Point2D loc, double radiux)
    {
        ball = new Ellipse2D.Double( loc.getX(), loc.getY(), radiux, radiux);
    } // End setBallPosition() mutator

    // private overloaded method to set new ball values
    private void setBallPosition(Point2D loc)
    {
        ball.setFrame(loc, new Dimension2D() {
            @Override
            public double getWidth() {
                return radiux * 2;
            }

            @Override
            public double getHeight() {
                return radiux * 2;
            }

            @Override
            public void setSize(double width, double height) {

            }
        });
    }

    // public method to return coordinates of ball object
    public Point2D getBallCoords()
    {
        return ballCoords;
    } // End getBallCoords() accessor

    // public method to return color of ball object
    public Color getBallColor()
    {
        return COLOR_OF_BALL;
    } // End getBallColor() accessor

    // Getters and setters for radius
    public double getRadiux()
    {
        return radiux;
    }

    public void setRadiux(double radiux)
    {
        this.radiux = radiux;
    }

    public double getBallVelocityX()
    {
        return ballVelocity.getX();
    }

    public double getBallVelocityY()
    {
        return ballVelocity.getY();
    }


    public void setBallVelocity(double x, double y)
    {
        ballVelocity = new Point2D.Double(x, y);
    }

    public void move()
    {
        double newX = ballCoords.getX() + ballVelocity.getX();
        double newY = ballCoords.getY() + ballVelocity.getY();

        // If the ball is too far to the right and moving to the right
        if (newX >= BallPanel.DIM.getWidth() - 2 * radiux && ballVelocity.getX() > 0)
        {
            setBallVelocity(-ballVelocity.getX(), ballVelocity.getY());
        }
        // If the ball is too far to the left and moving to the left
        else if (newX <= 0 && ballVelocity.getX() < 0)
        {
            setBallVelocity(-ballVelocity.getX(), ballVelocity.getY());
        }
        // If the ball is too low and moving downward
        else if (newY >= BallPanel.DIM.getHeight() - 2 * radiux && ballVelocity.getY() > 0)
        {
            setBallVelocity(ballVelocity.getX(), -ballVelocity.getY());
        }
        // If the ball is too high and moving upward
        else if (newY <= 0 && ballVelocity.getY() < 0)
        {
            setBallVelocity(ballVelocity.getX(), -ballVelocity.getY());
        }
        ballCoords.setLocation(ballCoords.getX() + ballVelocity.getX(), ballCoords.getY() + ballVelocity.getY());
        // Set new coordinates of ball
        setBallPosition(ballCoords);
    }

    public void draw(Graphics2D g2d)
    {
        g2d.setPaint( this.getBallColor() );
        g2d.fillOval((int) this.getBallCoords().getX(), (int) this.getBallCoords().getY(), (int) this.getRadiux()*2, (int) this.getRadiux()*2 );
    }

    public boolean checkCollisions(Ball ball)
    {
        if (ball != this) // It cannot collide with itself
        {
            if (ballCoords.getX() + radiux + ball.radiux > ball.ballCoords.getX()
                    && ballCoords.getX() < ball.ballCoords.getX() + radiux + ball.radiux
                    && ballCoords.getY() + radiux + ball.radiux > ball.ballCoords.getY()
                    && ballCoords.getY() < ball.ballCoords.getY() + radiux + ball.radiux)
            {
                if (distanceBtwn(this, ball) < radiux + ball.radiux)
                {
                    return true;
                }
            }
        }

        return false;
    }

    private double distanceBtwn(Ball ballA, Ball ballB)
    {
        double aX = ballA.ballCoords.getX();
        double aY = ballA.ballCoords.getY();
        double bX = ballB.ballCoords.getX();
        double bY = ballB.ballCoords.getY();

        double distance = Math.sqrt(((aX - bX) * (aX - bX)) + ((aY - bY) * (aY - bY)));
        // This is using pythagorean theorem to calculate distance between center of two spheres with diff radii
        if (distance < 0)
        {
            distance = -distance;
        }
        return distance;
    }

    public void doCollision(Ball ball)
    {
//        System.out.println("Collided!");
//        drawCollision(this, ball); // Special effects with the collision, if desired
        getNewVelocities(this, ball); //Calculate new velocities

    }

    /**
     * If I want to do something when it collides.
     * @param ballA
     * @param ballB
     */
    public void drawCollision(Ball ballA, Ball ballB)
    {
        double aX = ballA.ballCoords.getX();
        double aY = ballA.ballCoords.getY();
        double bX = ballB.ballCoords.getX();
        double bY = ballB.ballCoords.getY();
        double aR = ballA.radiux;
        double bR = ballB.radiux;

        Point2D.Double collisionPoint = new Point2D.Double(
                ((aX * bR) + (bX * aR)) / (aR + bR),
                ((aY * bR) + (bY * aR)) / (aR + bR));

        // Draw a circle at the point of collision?
    }

    private void getNewVelocities(Ball ballA, Ball ballB)
    {
        double aMass = ballA.mass;
        double bMass = ballB.mass;
        double aVx = ballA.ballVelocity.getX();
        double bVx = ballB.ballVelocity.getX();
        double aVy = ballA.ballVelocity.getY();
        double bVy = ballB.ballVelocity.getY();

        // Calculate new velocities based on elastic collision formula: https://en.wikipedia.org/wiki/Elastic_collision
        // See the two-dimensional section
        double newaVx = ( (aVx * (aMass - bMass)) + (2 * bMass * bVx) ) / (aMass + bMass);
        double newbVx = ( (bVx * (bMass - aMass)) + (2 * aMass * aVx) ) / (aMass + bMass);
        double newaVy = ( (aVy * (aMass - bMass)) + (2 * bMass * bVy) ) / (aMass + bMass);
        double newbVy = ( (bVy * (bMass - aMass)) + (2 * aMass * aVy) ) / (aMass + bMass);

        ballA.setBallVelocity(newaVx, newaVy);
        ballB.setBallVelocity(newbVx, newbVy);

        ballA.ballCoords.setLocation((ballA.ballCoords.getX() + newaVx), (ballA.ballCoords.getY() + newaVy));
        ballB.ballCoords.setLocation((ballB.ballCoords.getX() + newbVx), (ballB.ballCoords.getY() + newbVy));
    }

} // end BouncingBallsContainer class
