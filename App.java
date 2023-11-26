import Simulation.RequestGenerator;
import Simulation.Simulation;
import Simulation.Algorithms.Algorithm;
import Simulation.Algorithms.CScan;
import Simulation.Algorithms.EDF;
import Simulation.Algorithms.FDScan;
import Simulation.Algorithms.FIFO;
import Simulation.Algorithms.SSTF;
import Simulation.Algorithms.Scan;
import Simulation.hdd.HDD;

public class App
{
    public static void main(String[] args) throws Exception
    {
        HDD hdd = new HDD(4, 50, 1000);
        hdd.setParameters(20, 1);

        RequestGenerator generator = new RequestGenerator();
        generator.initialRequestsAmount = 50;
        generator.addedRequestsAmount = 1500;
        generator.minSize = 300;
        generator.maxSize = 500;
        generator.minDeadline = 200 * 100;
        generator.maxDeadline = 500 * 100;
        generator.fractionWithDeadline = 0.05;
        generator.maxAddress = hdd.maxAddress;

        Algorithm[] algs =
                {new FIFO(), new SSTF(), new Scan(), new CScan(), new EDF(), new FDScan(1.05)};
        for (Algorithm algorithm : algs)
        {
            generator.generate();
            Simulation sim = new Simulation(hdd, generator.getDriveRequestsCopy());
            algorithm.executeOnSimulation(sim);

            System.out.println(sim.getFinalResult(algorithm.getName()));
        }
    }
}
