package Simulation.hdd;

import Simulation.DriveRequest;

class RequestExecutionInfo
{
  HeadPosition start;
  HeadPosition end;
  int moveTime;
  int executionTime;
  int totalTime;

  RequestExecutionInfo(HeadPosition start, HeadPosition end, int moveTime, int executionTime,
      int totalTime)
  {
    this.start = start;
    this.end = end;
    this.moveTime = moveTime;
    this.executionTime = executionTime;
    this.totalTime = totalTime;
  }
}


public class HDD
{

  private HeadPosition _currentPosition;
  private int _headMoveTime = 0;
  private int _platterMoveTime = 0;

  int bitsPerRotation = 360;
  int platterCount = 4;
  int sectorCount = 50;
  public int maxAddress;

  public HDD(int platterCount, int sectorCount, int bitsPerRotation)
  {
    this.platterCount = platterCount;
    this.sectorCount = sectorCount;
    this.bitsPerRotation = bitsPerRotation;
    _currentPosition = new HeadPosition(0, 0, 0);
    maxAddress = bitsPerRotation * platterCount * sectorCount - 1;
  }

  public void setParameters(int headMoveTime, int platterMoveTime)
  {
    this._headMoveTime = headMoveTime;
    this._platterMoveTime = platterMoveTime;
  }

  public int calculateSeekTime(DriveRequest request)
  {
    return calculateExecution(request).moveTime;
  }

  public int calculateExecutionTime(DriveRequest request)
  {
    return calculateExecution(request).executionTime;
  }

  public int executeRequest(DriveRequest request)
  {
    var info = calculateExecution(request);
    _currentPosition = info.end;
    return info.totalTime;
  }

  public int moveHeadToStart()
  {
    return _moveHeadToAddress(0);
  }

  public int moveHeadToEnd()
  {
    return _moveHeadToAddress(maxAddress);
  }

  public int getHeadAddress()
  {
    return _getHeadAddress(_currentPosition);
  }

  public HDD copy()
  {
    HDD hdd = new HDD(platterCount, sectorCount, bitsPerRotation);
    hdd.setParameters(_headMoveTime, _platterMoveTime);
    hdd._moveHeadToAddress(getHeadAddress());
    return hdd;
  }

  int _calculateMoveTimeAB(HeadPosition a, HeadPosition b)
  {
    int spinForwardTime = Math.abs(a.platterPosition - b.platterPosition);
    int spinBackwardTime = bitsPerRotation - spinForwardTime;
    int spinTime = Math.min(spinBackwardTime, spinForwardTime);

    return Math.abs(a.sector - b.sector) * _headMoveTime + spinTime * _platterMoveTime;
  }

  int _moveHeadToAddress(int address)
  {
    return _moveHeadToPosition(_getHeadPosition(address));
  }

  int _moveHeadToPosition(HeadPosition target)
  {
    int timeSpent = _calculateMoveTimeAB(_currentPosition, target);
    _currentPosition = target;
    return timeSpent;
  }

  HeadPosition _getHeadPosition(int dataAddress)
  {
    if (dataAddress > maxAddress)
    {
      throw new IllegalArgumentException("Data address too big");
    }
    if (dataAddress == 0)
    {
      return new HeadPosition(0, 0, 0);
    }

    int dataPerSector = bitsPerRotation * platterCount;
    int sector = (int) Math.floor(1.0 * dataAddress / dataPerSector);
    int platterNum =
        ((int) Math.floor(1.0 * dataAddress / bitsPerRotation)) - sector * platterCount;
    int platterPos = dataAddress - sector * dataPerSector - bitsPerRotation * platterNum;
    return new HeadPosition(sector, platterNum, platterPos);
  }

  int _getHeadAddress(HeadPosition position)
  {
    int address = 0;
    int dataPerSector = bitsPerRotation * platterCount;
    address += position.sector * dataPerSector;
    address += position.platterNum * bitsPerRotation;
    address += position.platterPosition;
    return address;
  }

  private RequestExecutionInfo calculateExecution(DriveRequest request)
  {
    HeadPosition a = _getHeadPosition(request.position);
    HeadPosition b = _getHeadPosition(request.position + request.size);

    int aMoveTime = _calculateMoveTimeAB(_currentPosition, a);
    int bMoveTime = _calculateMoveTimeAB(_currentPosition, b);

    int moveTime;
    HeadPosition start, end;
    if (aMoveTime < bMoveTime)
    {
      start = a;
      end = b;
      moveTime = aMoveTime;
    } else
    {
      start = b;
      end = a;
      moveTime = bMoveTime;
    }

    int platterReadTime = (request.size - 1) * _platterMoveTime;
    int headReadTime = Math.abs(start.sector - end.sector) * _headMoveTime;
    int totalMoveTime = moveTime + platterReadTime + headReadTime;

    return new RequestExecutionInfo(start, end, moveTime, platterReadTime + headReadTime,
        totalMoveTime);
  }
}
