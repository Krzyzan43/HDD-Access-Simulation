package Simulation;

import java.util.ArrayList;
import java.util.Random;

public class RequestGenerator
{
	ArrayList<DriveRequest> _generatedRequests = new ArrayList<>();
	Random _rand = new Random(2);

	public int addedRequestsAmount = 1500;
	public int initialRequestsAmount = 50;
	public int maxSize = 500;
	public int minSize = 300;
	public int maxAddress;
	public int maxDeadline = 500 * 100;
	public int minDeadline = 200 * 100;
	public double fractionWithDeadline = 0.1;

	public void generate()
	{
		_generatedRequests.clear();
		int totalTime = 0;

		for (var i = 0; i < initialRequestsAmount; i++)
		{
			int size = _rand.nextInt(maxSize - minSize + 1) + minSize;
			int address = _rand.nextInt(maxAddress + 1 - size);
			totalTime += size;
			int deadline = 0;
			if (_rand.nextDouble() < fractionWithDeadline)
			{
				deadline = _rand.nextInt(maxDeadline - minDeadline) + minDeadline;
			}
			_generatedRequests.add(new DriveRequest(address, size, deadline, 0));
		}
		for (var i = 0; i < addedRequestsAmount; i++)
		{
			int size = _rand.nextInt(maxSize - minSize + 1) + minSize;
			totalTime += size;
			int address = _rand.nextInt(maxAddress + 1 - size);
			int startTime = _rand.nextInt(totalTime);
			int deadline = 0;
			if (_rand.nextDouble() < fractionWithDeadline)
			{
				deadline = _rand.nextInt(maxDeadline - minDeadline) + minDeadline + startTime;
			}
			_generatedRequests.add(new DriveRequest(address, size, deadline, startTime));
		}
		_generatedRequests.sort((e1, e2) -> Integer.compare(e1.startTime, e2.startTime));
	}

	public ArrayList<DriveRequest> getDriveRequestsCopy()
	{
		ArrayList<DriveRequest> copy = new ArrayList<>();
		for (var request : _generatedRequests)
		{
			copy.add(request.copy());
		}
		return copy;
	}
}
