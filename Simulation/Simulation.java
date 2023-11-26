package Simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import Simulation.hdd.HDD;

public class Simulation
{
	int time = 0;
	Queue<DriveRequest> _futureRequests;
	Queue<DriveRequest> _doneRequests = new LinkedList<>();

	public HDD hdd;

	public Simulation(HDD hdd, ArrayList<DriveRequest> requests)
	{
		_futureRequests = new LinkedList<>(requests);
		this.hdd = hdd;
		hdd.moveHeadToStart();
	}

	public void executeRequest(DriveRequest request)
	{
		request.actualStartTime = time;
		time += hdd.executeRequest(request);
		request.endTime = time;
		_doneRequests.add(request);
	}

	public void moveHeadToEnd()
	{
		time += hdd.moveHeadToEnd();
	}

	public void moveHeadToStart()
	{
		time += hdd.moveHeadToStart();
	}

	public ArrayList<DriveRequest> getNewRequests()
	{
		ArrayList<DriveRequest> newRequests = new ArrayList<>();
		while (_futureRequests.isEmpty() == false)
		{
			if (_futureRequests.peek().startTime > time)
				break;
			newRequests.add(_futureRequests.remove());
		}
		return newRequests;
	}

	boolean hasNewRequests()
	{
		if (_futureRequests.isEmpty())
			return false;
		return _futureRequests.peek().startTime < time;
	}

	public boolean hasAnyFutureRequests()
	{
		return _futureRequests.size() > 0;
	}

	public void waitForNewRequests()
	{
		time = _futureRequests.peek().startTime;
	}

	public AlgResult getFinalResult(String algName)
	{
		return new AlgResult(new ArrayList<>(_doneRequests), time, algName);
	}

	public int getTime()
	{
		return time;
	}
}
