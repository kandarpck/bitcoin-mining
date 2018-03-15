package cs441641.blockchain;

import cs441641.miners.Miner;

import java.util.Collection;


public interface ChurnFunction {
    static ChurnFunction NO_CHURN = (orphanRate, miners) -> {
        int totalHashRate = miners.stream().mapToInt(Miner::getHashRate).sum();
        int totalConnectivity = miners.stream().mapToInt(Miner::getConnectivity).sum();
        return new NetworkStatistics(orphanRate, totalHashRate, totalConnectivity);
    };

    NetworkStatistics churnNetwork(double orphanRate, Collection<Miner> miners);


}
