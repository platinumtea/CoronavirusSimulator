# CoronavirusSimulator
Simulator of Covid-19 spread written in Java.
This simulator simulates the spread of viruses in various scenarios, using a circle to represent a human. 

# Simulation
The simulation begins with one person infected on each side, and the first infected begins infecting others. From being infected, a person can go to the hospital or recover on their own. Those hospitalized have a higher chance of dying, and the hospital only has a certain capacity before no more people can enter. The simulation on the right side has a fraction of the population practicing social distancing, meaning they will stay in place.
## Colors
* **Green** - healthy
* **Red** - infected
* **Pink** - hospitalized
* **Blue** - recovered
* **Gray** - dead
## Constants
* **Delay** - Delay on timer (aka tickrate).
* **People** - Number of people per simulation.
* **Record rate** - Graph will record data every time this number of ticks pass.
* **Graph scaling** - Changes how the graph is scaled vertically so they fit on-screen.
* **Immobile** - Number of people who are practicing social distancing in the distanced simulation.
* **Capacity** - Hospital capacity per simulation- it is guaranteed that no more than this many people will be hospitalized.
* **WIDTH** - Width of each simulation, screen width is a total of WIDTH * 2.
* **HEIGHT** - Height of screen, without graph. Total height will be HEIGHT + (number of people / graph scaling)
