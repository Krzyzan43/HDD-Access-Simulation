package Simulation.Algorithms;

import java.util.LinkedList;
import java.util.Queue;
import Simulation.DriveRequest;
import Simulation.Simulation;

public class FIFO extends Algorithm
{
	Queue<DriveRequest> waitingRequests = new LinkedList<>();

	@Override
	public String getName()
	{
		return "FIFO";
	}

	@Override
	boolean hasWaitingRequests()
	{
		return !waitingRequests.isEmpty();
	}

	@Override
	void executeNextRequest(Simulation simulation)
	{
		simulation.executeRequest(waitingRequests.remove());
	}

	@Override
	void getNewRequests(Simulation simulation)
	{
		for (var request : simulation.getNewRequests())
		{
			waitingRequests.add(request);
		}
	}
}
