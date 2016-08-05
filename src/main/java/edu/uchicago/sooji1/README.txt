Written by Sooji Yi

1/ Ensure that your balls stay in bounds. If they hit a boundary, they should reflect.

    This is done. When the ball hits the walls of the fixed frame, they will reflect.

2/ When one balls collides with another, both balls must interact from the collision in a way
that is consistent with the physical rules described above, using their masses, velocities, and angle of incidence.

    This is done. The angle of incidence is automatically factored in by using the x and y coordinates
    of the velocity, which, using the 2D elastic collision formula, we can determine the angle/direction
    the balls will move afterwards, given their respective masses.

3/ Start the bouncing ball simulation with 4 balls (with random radii, random deltaX, and random deltaY).

    This is also done.

4/ There are two factors that will stress your application: (1) greater number of balls in the system and (2) reduced
time between move/draw calls. Add two UI controls, perhaps sliders or buttons along the bottom of the application to
allow users control these two factors. As you add more balls and/or reduce the sleep time between moves/draws and you
will notice that the application drags. Attempt to optimize this by delegating certain functions like boundary checking
and collision detection to the Ball (runnable) class which may be delegated to available threads in your exec-thread-
pool.

    This is done.

    Adding balls - This is done using a button, with a clear label to indicate to the user what the function is.
    Adjusting time between move/draw calls - I have added a slider that allows up to 100 ms of sleep time for the thread
    that draws (and moves) the balls.

    I have attempted to optimize this by assigning a Runnable thread per ball. The boundary checking is done by each ball.
    The collision detection is also done by each ball, but to make this efficient, I have made sure to remove checking if a
    ball collides with itself (which is not possible) and if ball A collides with ball B, I do not again check if ball B has
    collided with ball A in that instance, since that is redundant. This was done using iteration over the List.

These were the requirements for the project, and I was able to complete all of them in time! It was fun to work on.