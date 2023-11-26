# HDD Access Simulation
This project simulates different strategies for accessing data on hard drive. In order to test how a strategy would work, several requests to read the disc are generated. Algorithm selects which request to run at any given time and when it finishes the results are printed out to the console.

# HDD structure and measuring time
In this simulation HDD consists of several platters, each of which can rotate in order to read data. For each platter there is a head that reads data from the disc. Platters are divided into circles called 'sectors'. In order to read a single bit, adequate head and sector must be selected, and cylinder needs to be rotated a certain angle. Switching head time is neglected. Rotation time and sector change time can be specified for the simulation. Each requests starts and ends at some place in the disk, and read time for request is calculated as (time to move to the start byte + time to move from start to end byte). 

# Algorithms
There are several algorithms tested by this program:
  * FIFO (First In First Out)
  * Scan - Goes from start of disc memory to the end, executing all requests that it meets in the meanwhile. After its done, it goes to the start of memory, executing nothing
  * CScan - Same as Scan, but when it goes back to start it executes all requests meet
  * SSTF (Shortest Seek Time First) - Finds and executes request that it can reach the fastests
  * EDF (Earliest Deadline First) - Tries to execute request, that has earliest deadline. If deadline has already passed, request gets ignored
  * FDScan (Feasible Deadline Scan) - Same as Scan, but decides scan direction based on where the earliest feasible deadline is

# Results
Results for the set of parameters specified in App.java are as follows:
```
Algorytm: FIFO
Całkowity czas: 1342ms 
Procent udanych deadline`ow: 6.8181818181818175% 

Algorytm: SSTF
Całkowity czas: 659ms 
Procent udanych deadline`ow: 12.698412698412698% 

Algorytm: Scan
Całkowity czas: 803ms 
Procent udanych deadline`ow: 16.666666666666664% 

Algorytm: CScan
Całkowity czas: 851ms 
Procent udanych deadline`ow: 15.66265060240964% 

Algorytm: EDF
Całkowity czas: 1335ms 
Procent udanych deadline`ow: 100.0% 

Algorytm: FDScan
Całkowity czas: 815ms 
Procent udanych deadline`ow: 36.486486486486484%
```
