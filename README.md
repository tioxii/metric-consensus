# The Metric Consensus Process
The metric consensus process simulation software.
This software is part of my bachelor's thesis and it's purpose is to help simulating the metric consensus process, to find the complexity of the process and to check how robosust certain adaptations are.
In the metric consensus process there are a fixed number of participants, where each participant has an initial opinion. 
Each opinion represents a point in space where the node is located. 
The goal now is to reach a consensus in a decentralized manner, so that all honest nodes in the end have the same opinion.
The process itself is simulated in rounds which can either be synchronous or asynchronous.
In each round all nodes apply the same procedure, which is referred to as dynamic.

## Dynamics
There are 4 dynamics implemented in this project:
- ClosestNodeDynamics
- (1 + beta) - ClosestNodeDynamics
- VoterDynamics
- MeanValueDynamics
- ClosestToMeanDynamics

## Termination
There are three termination criteria implemented in this project:
- ConsensusTermination
- EpsilonTermination
- FiftyPercentTermination

## Starting Configurations
There are a bunch of starting positions implemented in this project:
- RandomPositions
- Circle
- TwoChoices
- Byzantine
