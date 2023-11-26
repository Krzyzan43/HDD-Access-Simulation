package Simulation.Algorithms;

import java.util.ArrayList;
import java.util.TreeSet;
import Simulation.DriveRequest;
import Simulation.Simulation;
import Simulation.hdd.HDD;

public class FDScan extends Algorithm
{
	double estimateTolerance = 1;
	TreeSet<DriveRequest> waitingRequests, deadlineRequests;
	boolean _reverse = true;

	public FDScan(double estimateTolerance)
	{
		this.estimateTolerance = estimateTolerance;
		waitingRequests = new TreeSet<>((o1, o2) -> Integer.compare(o1.position, o2.position));
		deadlineRequests = new TreeSet<>((r1, r2) -> Integer.compare(r1.deadline, r2.deadline));
	}

	@Override
	public String getName()
	{
		return "FDScan";
	}

	@Override
	boolean hasWaitingRequests()
	{
		return waitingRequests.size() > 0;
	}

	@Override
	void executeNextRequest(Simulation simulation)
	{
		decideScanDirection(simulation);

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
		deadlineRequests.remove(nextRequest);
		waitingRequests.remove(nextRequest);
	}

	void decideScanDirection(Simulation simulation)
	{
		ArrayList<DriveRequest> lateRequests = new ArrayList<>();

		for (DriveRequest request : deadlineRequests)
		{
			if (isDeadlineFeasible(request, simulation))
			{
				_reverse = request.position < simulation.hdd.getHeadAddress();
				break;
			} else
			{
				lateRequests.add(request);
			}
		}

		deadlineRequests.removeAll(lateRequests);
	}

	private boolean isDeadlineFeasible(DriveRequest targetRequest, Simulation simulation)
	{
		HDD hdd = simulation.hdd;
		int targetAddress = targetRequest.position;
		int headAddress = hdd.getHeadAddress();

		boolean reverse = targetAddress < headAddress;
		ArrayList<DriveRequest> inbetweenRequests = new ArrayList<>();
		for (DriveRequest request : waitingRequests)
		{
			if (reverse)
			{
				if (targetAddress <= request.position && request.position <= headAddress)
					inbetweenRequests.add(request);
			} else
			{
				if (headAddress <= request.position && request.position <= targetAddress)
					inbetweenRequests.add(request);
			}
		}

		int totalExecutionTime = 0;
		for (DriveRequest driveRequest : inbetweenRequests)
		{
			totalExecutionTime += hdd.calculateExecutionTime(driveRequest);
		}
		totalExecutionTime += hdd.calculateSeekTime(targetRequest);

		int remainingDeadlineTime = targetRequest.deadline - simulation.getTime();

		return remainingDeadlineTime - totalExecutionTime * estimateTolerance > 0;

	}

	@Override
	void getNewRequests(Simulation simulation)
	{
		var newRequests = simulation.getNewRequests();
		waitingRequests.addAll(newRequests);
		for (DriveRequest request : newRequests)
		{
			if (request.deadline != 0)
			{
				deadlineRequests.add(request);
			}
		}
	}
}
