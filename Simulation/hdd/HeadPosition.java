package Simulation.hdd;

public class HeadPosition
{
    public int sector;
    public int platterNum;
    public int platterPosition;

    HeadPosition(int sector, int platterNum, int platterPosition)
    {
        this.sector = sector;
        this.platterNum = platterNum;
        this.platterPosition = platterPosition;
    }

    HeadPosition copy()
    {
        return new HeadPosition(sector, platterNum, platterPosition);
    }
}
