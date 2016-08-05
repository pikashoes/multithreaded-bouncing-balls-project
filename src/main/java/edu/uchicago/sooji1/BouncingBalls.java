package edu.uchicago.sooji1;

/* Filename:        BouncingBalls.java
 * Last Modified:   24 Mar 2014
 * Author:          Todd Parker
 * Email:           todd.i.parker@maine.edu
 * Course:          CIS314 - Advanced Java
 *
 * BouncingBalls.java is comprises of two classes: BouncingBalls, which hosts
 * main(), creates the GUI, creates a BallPanel object, and enters into an
 * infinite loop for repainting moving ball objects; and, BallPanel, which
 * uses a MouseEvent listener to create a ball object (method createBall()) on
 * each mouse click, store it in a Linked List, and overrides paintComponent to
 * paint the balls and a signature block. Method moveBall() is called from
 * main() for the purpose of repainting BallPanel. Each Ball object is executed
 * via ExecutorService in its own thread.
 */

import javax.swing.*;
import java.awt.*;

//original source from here: https://github.com/moldypeach/Multithreaded-Bouncing-Balls-GUI
//I already refactored this code, though you are welcome to borrow anything you want from the above project.
public class BouncingBalls
{
    public static void main(String[] args)
    {
        BallPanel ballPanel = new BallPanel(); // Create BallPanel - JPanel Object
        JFrame jFrame = new JFrame("Bouncing Balls");
        jFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        jFrame.add( ballPanel );
        jFrame.setBackground(Color.LIGHT_GRAY);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setResizable(false);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        JLabel valueSlider = new JLabel();

        JLabel label = new JLabel("Time between move/draw calls");
        label.setForeground(Color.WHITE);

        ballPanel.add(label);
        ballPanel.add(slider);
        ballPanel.add(valueSlider);

        while(true)
        {
            jFrame.repaint(); // Repaint JFrame's black background
            ballPanel.repaint(); // Move each Ball object
            try
            {
                Thread.sleep(slider.getValue());
                valueSlider.setText("Slider value: " + Integer.toString((slider.getValue())));
            }
            catch(Exception event){}
        }
    } // End main

} // End BouncingBalls class