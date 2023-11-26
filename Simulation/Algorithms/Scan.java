package Simulation.Algorithms;

import java.util.TreeSet;
import Simulation.DriveRequest;
import Simulation.Simulation;

public class Scan extends Algorithm
{
	TreeSet<DriveRequest> waitingRequests;
	boolean _reverse = false;

	public Scan()
	{
		waitingRequests = new TreeSet<>((o1, o2) -> Integer.compare(o1.position, o2.position));
	}

	@Override
	public String getName()
	{
		return "Scan";
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

		if (_reverse == false)
		{
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
				nextRequest = waitingRequests.last();
				simulation.moveHeadToEnd();
				_reverse = true;
			}
		} else
		{
			for (DriveRequest request : waitingRequests.descendingSet())
			{
				if (request.position < simulation.hdd.getHeadAddress())
				{
					nextRequest = request;
					break;
				}
			}

			if (nextRequest == null)
			{
				nextRequest = waitingRequests.first();
				simulation.moveHeadToStart();
				_reverse = false;
			}
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
