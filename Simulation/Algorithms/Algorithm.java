package Simulation.Algorithms;

import Simulation.Simulation;

public abstract class Algorithm
{
	public abstract String getName();

	public void executeOnSimulation(Simulation simulation)
	{
		while (simulation.hasAnyFutureRequests() || hasWaitingRequests())
		{
			if (hasWaitingRequests() == false)
			{
				simulation.waitForNewRequests();
				getNewRequests(simulation);
			}
			executeNextRequest(simulation);
			getNewRequests(simulation);
		}
	}

	abstract boolean hasWaitingRequests();

	abstract void executeNextRequest(Simulation simulation);

	abstract void getNewRequests(Simulation simulation);
}
