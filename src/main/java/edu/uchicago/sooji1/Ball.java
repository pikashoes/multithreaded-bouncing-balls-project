package edu.uchicago.sooji1;

 import java.awt.*;
 import java.awt.geom.Dimension2D;
 import java.awt.geom.Ellipse2D;
 import java.awt.geom.Point2D;
 import java.util.Random;

/**
 * This is the Ball class. It implements Runnable and also contains methods used for collision detection.
 */
public class Ball implements Runnable, Movable
{
    /**
     * Instance variables
     */
    private Ellipse2D.Double ball; // Ball objects shape attributes
    private Point2D.Double ballCoords; // Store location coords of a Ball
    private Point2D.Double ballVelocity; // Store x and y velocity of Ball
    private final Color COLOR_OF_BALL; // Store randomized color of ball
    private double mass;
    private double angle;

    private double radiux;
    public static Random sRandom = new Random();

    /**
     * Constructor
     * @param ballColor
     * @param xPos
     * @param yPos
     */
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

    /**
     * Moves the ball. This function also contains the boundary setting, sot hat the ball bounces off the "walls".
     */
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

    /**
     * Draws the screen
     * @param g2d
     */
    public void draw(Graphics2D g2d)
    {
        g2d.setPaint( this.getBallColor() );
        g2d.fillOval((int) this.getBallCoords().getX(), (int) this.getBallCoords().getY(), (int) this.getRadiux()*2, (int) this.getRadiux()*2 );
    }

    /**
     * Collision checker
     * @param ball
     * @return
     */
    public boolean checkCollisions(Ball ball)
    {
        if (ball != this) // It cannot collide with itself
        {
            // Checks if the rectangular bounds of each ball are overlapping, which indicates they are within range.
            if (ballCoords.getX() + radiux + ball.radiux > ball.ballCoords.getX()
                    && ballCoords.getX() < ball.ballCoords.getX() + radiux + ball.radiux
                    && ballCoords.getY() + radiux + ball.radiux > ball.ballCoords.getY()
                    && ballCoords.getY() < ball.ballCoords.getY() + radiux + ball.radiux)
            {
                if (distanceBtwn(this, ball) < (radiux + ball.radiux) * (radiux + ball.radiux))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks the distance between the center of two spheres with different radii
     * @param ballA
     * @param ballB
     * @return
     */
    private double distanceBtwn(Ball ballA, Ball ballB)
    {
        double aX = ballA.ballCoords.getX();
        double aY = ballA.ballCoords.getY();
        double bX = ballB.ballCoords.getX();
        double bY = ballB.ballCoords.getY();

        double xDist = aX - bX;
        double yDist = aY - bY;

        double distanceSq = (xDist * xDist) + (yDist * yDist); // To avoid using square root
        return distanceSq;
    }

    /**
     * Performs the collision.
     * @param ball
     */
    public void doCollision(Ball ball)
    {
//        System.out.println("Collided!");
        getNewVelocities(this, ball); //Calculate new velocities
    }

    /**
     * New velocites after two balls bounce off each other
     * @param ballA
     * @param ballB
     */
    private void getNewVelocities(Ball ballA, Ball ballB)
    {
        double aMass = ballA.mass;
        double bMass = ballB.mass;
        double aVx = ballA.ballVelocity.getX();
        double bVx = ballB.ballVelocity.getX();
        double aVy = ballA.ballVelocity.getY();
        double bVy = ballB.ballVelocity.getY();
        double aX = ballA.ballCoords.getX();
        double aY = ballA.ballCoords.getY();
        double bX = ballB.ballCoords.getX();
        double bY = ballB.ballCoords.getY();

        double xDist = aX - bX;
        double yDist = aY - bY;

        double xVel = bVx - aVx;
        double yVel = bVy - aVy;
        double dotProd = (xDist * xVel) + (yDist * yVel);

        // Check if objects are moving towards one another
        // Inspiration/source from: http://gamedev.stackexchange.com/questions/20516/ball-collisions-sticking-together
        if (dotProd > 0)
        {
            double collScale = dotProd / distanceBtwn(ballA, ballB);
            double xColl = xDist * collScale;
            double yColl = yDist * collScale;
            double totalMass = ballA.mass + ballB.mass;
            double collWeightA = (2 * ballB.mass) / totalMass;
            double collWeightB = (2 * ballA.mass) / totalMass;

            double newaVx = ballA.getBallVelocityX() + (collWeightA * xColl);
            double newaVy = ballA.getBallVelocityY() + (collWeightA * yColl);
            double newbVx = ballB.getBallVelocityX() - (collWeightB * xColl);
            double newbVy = ballB.getBallVelocityY() - (collWeightB * yColl);

            ballA.setBallVelocity(newaVx, newaVy);
            ballB.setBallVelocity(newbVx, newbVy);

            ballA.ballCoords.setLocation((ballA.ballCoords.getX() + newaVx), (ballA.ballCoords.getY() + newaVy));
            ballB.ballCoords.setLocation((ballB.ballCoords.getX() + newbVx), (ballB.ballCoords.getY() + newbVy));

        }
        // Calculate new velocities based on elastic collision formula: https://en.wikipedia.org/wiki/Elastic_collision
        // See the two-dimensional section


    }

} // end BouncingBallsContainer class
