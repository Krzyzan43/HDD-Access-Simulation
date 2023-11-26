package Simulation.Algorithms;

import java.util.TreeSet;
import Simulation.DriveRequest;
import Simulation.Simulation;

public class CScan extends Algorithm
{
	TreeSet<DriveRequest> waitingRequests;

	public CScan()
	{
		waitingRequests = new TreeSet<>((o1, o2) -> Integer.compare(o1.position, o2.position));
	}

	@Override
	public String getName()
	{
		return "CScan";
	}

	@Override
	boolean hasWaitingRequests()
	{
		return waitingRequests.size() > 0;
	}

	@Override
	void executeNextRequest(Simulation simulation)
	{
		DriveRequest nextRequest = null;

		for (DriveRequest request : waitingRequests)
		{
			if (request.position > simulation.hdd.getHeadAddress())
			{
				nextRequest = request;
				break;
			}
		}

		if (nextRequest == null)
		{
			simulation.moveHeadToStart();
			simulation.moveHeadToEnd();
			nextRequest = waitingRequests.first();
		}

		simulation.executeRequest(nextRequest);
		waitingRequests.remove(nextRequest);
	}

	@Override
	void getNewRequests(Simulation simulation)
	{
		waitingRequests.addAll(simulation.getNewRequests());
	}
}
