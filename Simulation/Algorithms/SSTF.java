package Simulation.Algorithms;

import java.util.ArrayList;
import Simulation.DriveRequest;
import Simulation.Simulation;
import Simulation.hdd.HDD;

public class SSTF extends Algorithm
{
	final ArrayList<DriveRequest> _waitingRequests = new ArrayList<>();

	public String getName()
	{
		return "SSTF";
	}

	boolean hasWaitingRequests()
	{
		return !_waitingRequests.isEmpty();
	}

	void executeNextRequest(Simulation simulation)
	{
		HDD hdd = simulation.hdd;
		int shortestTime = hdd.calculateSeekTime(_waitingRequests.get(0));
		DriveRequest shortestRequest = _waitingRequests.get(0);

		for (var request : _waitingRequests)
		{
			int moveTime = hdd.calculateSeekTime(request);
			if (moveTime < shortestTime)
			{
				shortestRequest = request;
				shortestTime = moveTime;
			}
		}

		simulation.executeRequest(shortestRequest);
		_waitingRequests.remove(shortestRequest);
	}

	void getNewRequests(Simulation simulation)
	{
		_waitingRequests.addAll(simulation.getNewRequests());
	}
}
