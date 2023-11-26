package Simulation.Algorithms;

import java.util.PriorityQueue;
import java.util.Queue;
import Simulation.DriveRequest;
import Simulation.Simulation;

public class EDF extends Algorithm
{
	Queue<DriveRequest> waitingRequests = new PriorityQueue<>();

	public EDF()
	{
		waitingRequests = new PriorityQueue<>((r1, r2) -> {
			if (r1.deadline == 0 && r2.deadline == 0)
			{
				return 0;
			} else if (r1.deadline == 0 && r2.deadline != 0)
			{
				return 1;
			} else if (r1.deadline != 0 && r2.deadline == 0)
			{
				return -1;
			} else
			{
				return Integer.compare(r1.deadline, r2.deadline);
			}
		});
	}

	@Override
	public String getName()
	{
		return "EDF";
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
		waitingRequests.addAll(simulation.getNewRequests());
	}
}
