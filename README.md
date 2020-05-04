# WIF3003.assignment
Assignment Project Sem 2 19/20

You are required to build a simple game using multithreading. The game (your program) accepts three arguments, n, t and m, where n >> t. After receiving the arguments, the program will sequentially create n random points. The points are floating point (floats or doubles) coordinates in a 1000 x 1000 region. NO two points should overlap. Each point is represented as an object containing the coordinates.

After that, your program will launch t threads. Each thread will randomly pick a pair of points to add an edge between them. Use an appropriate data structure to store the pair of connected points. However, NO point should be connected with more than one other point, i.e. each point could be used to form at most one edge (locking needed when forming an edge). The program will run for m seconds, or when any one thread has failed to form a single edge after 20 attempts. Display the number of edges each thread was successfully created.
