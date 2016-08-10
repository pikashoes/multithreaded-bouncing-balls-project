package edu.uchicago.sooji1;

/**
 * Created by pikashoes on 8/3/16.
 * The Ball Panel
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class BallPanel extends JPanel
{
    public java.util.List<Ball> balls;
    private final Random colorGenerator = new Random();
    private ExecutorService threadExecutor;
    private Color ballColor;

    private JSlider slider;
    private JButton button;

    public static Dimension DIM = new Dimension(1100, 600);
    public static Random sRandom = new Random();

    @Override
    public Dimension getPreferredSize()
    {
        return DIM;
    } // End Overloaded JPanel Dimension

    /**
     * Constructor
     */
    public BallPanel()
    {
        balls = new ArrayList<>(100); //make it large to avoid capacity issues.
        threadExecutor = Executors.newCachedThreadPool();

        // Button to add new balls
        button = new JButton("Add Balls");
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ballColor = new Color( colorGenerator.nextInt(255),colorGenerator.nextInt(255),colorGenerator.nextInt(255) );
                // Add new ball object to a LinkedList
                Ball ball = new Ball(ballColor, sRandom.nextInt(1031) + 35, sRandom.nextInt(566) + 35); // random.nextInt(max - min + 1) + min, 35 is largest radius possible
                balls.add(ball);
                // Executes new thread of created ball object
                threadExecutor.execute(ball);
            } // end actionPerformed
        }); // end ActionListener

        int initBalls = 4; // Start with 4 balls
        for (int i = 0; i < initBalls; i++)
        {
            Color initColor = new Color(colorGenerator.nextInt(255),colorGenerator.nextInt(255),colorGenerator.nextInt(255) );
            Ball startBalls = new Ball(initColor, sRandom.nextInt(800), sRandom.nextInt(400));
            balls.add(startBalls);
            threadExecutor.execute(startBalls);
        }

        add(button);

    } // End BouncingBalls constructor


    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;
        for( Movable ball : balls)
        {
            ball.draw(g2d);
        }

        // Collision detector
        // This will skip redundant checks
        // Inspiration from: http://gamedev.stackexchange.com/questions/20516/ball-collisions-sticking-together
        for (int i = 0; i < balls.size(); i++)
        {
            for (int j = i + 1; j < balls.size(); j++)
            {
                if (balls.get(i).checkCollisions(balls.get(j)))
                {
                    balls.get(i).doCollision(balls.get(j));
                }
            }
        }
    }

} // end MyPanel class
