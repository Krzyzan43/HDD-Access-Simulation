package Simulation;

import java.util.ArrayList;

public class AlgResult
{
	String algName;
	ArrayList<DriveRequest> requestList;
	int totalTime;
	double deadLinePercentageMet = 0;
	double avgDeadlineDelay = 0;
	double avgWaitingTime = 0;

	AlgResult(ArrayList<DriveRequest> requestList, int totalTime, String algName)
	{
		this.algName = algName;
		this.requestList = requestList;
		this.totalTime = totalTime;

		int deadlinesMet = 0;
		int totalDeadlines = 0;
		for (var request : requestList)
		{
			avgWaitingTime += (request.actualStartTime - request.startTime);

			if (request.deadline != 0)
			{
				totalDeadlines++;
				if (request.endTime <= request.deadline)
					deadlinesMet++;
				else
					avgDeadlineDelay += request.endTime - request.deadline;
			}
		}
		avgWaitingTime /= requestList.size();
		deadLinePercentageMet = 1.0 * deadlinesMet / totalDeadlines;
		avgDeadlineDelay /= totalDeadlines;
	}

	@Override
	public String toString()
	{
		String returnStr = "Algorytm: " + algName + '\n';
		returnStr += "Całkowity czas: " + totalTime / 1000 + "ms \n";
		returnStr += "Procent udanych deadline`ow: " + (deadLinePercentageMet * 100) + "% \n";
		return returnStr;
	}
}
