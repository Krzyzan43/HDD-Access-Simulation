package Simulation;

public class DriveRequest
{
	public int position = 0;
	public int size = 0;
	public int deadline = 0;
	public int startTime = 0;

	public int actualStartTime = 0;
	public int endTime = 0;

	public DriveRequest(int position, int size, int deadline, int startTime, int actualStartTime,
			int endTime)
	{
		this.position = position;
		this.size = size;
		this.deadline = deadline;
		this.startTime = startTime;
		this.actualStartTime = actualStartTime;
		this.endTime = endTime;
	}

	public DriveRequest(int position, int size, int deadline, int startTime)
	{
		this.position = position;
		this.size = size;
		this.deadline = deadline;
		this.startTime = startTime;
	}

	public DriveRequest copy()
	{
		return new DriveRequest(position, size, deadline, startTime, actualStartTime, endTime);
	}
}
