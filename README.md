# WIF3003.assignment
Assignment Project Sem 2 19/20

You are required to build a simple game using multithreading. The game (your program) accepts three arguments, n, t and m, where n >> t. After receiving the arguments, the program will sequentially create n random points. The points are floating point (floats or doubles) coordinates in a 1000 x 1000 region. NO two points should overlap. Each point is represented as an object containing the coordinates.

After that, your program will launch t threads. Each thread will randomly pick a pair of points to add an edge between them. Use an appropriate data structure to store the pair of connected points. However, NO point should be connected with more than one other point, i.e. each point could be used to form at most one edge (locking needed when forming an edge). The program will run for m seconds, or when any one thread has failed to form a single edge after 20 attempts. Display the number of edges each thread was successfully created.

## Run the program
Execute the `wif3003.jar` file.
The output file will be at your current directory where you execute the jar file with the name of `wif3003_yyyy-mm-dd_HH:MM:SS_z`.

### Parameters
There are four parameters:
* `Number of points` : number of points on grid
* `Number of threads` : number of threads will pick and link the points
* `Max time (seconds)` : maximum time for program to run, program will stop either reach maximum time or reach maximum fail counts, which is 20
* `Sleep (50ms)` : boolean value, if toggled, then the threads will sleep for 50 ms after linking two points before choosing next point
