package cs441641.miners;

import cs441641.blockchain.Block;
import cs441641.blockchain.NetworkStatistics;

public class MajorityMiner extends BaseMiner implements Miner {

    private Block currentHead;
    private double totalHashRate, localHashRate;

    public MajorityMiner(String id, int hashRate, int connectivity) {
        super(id, hashRate, connectivity);
        this.localHashRate = hashRate; // The hash rate of the Majority miner
    }

    @Override
    public Block currentlyMiningAt() {
        return currentHead;
    }

    @Override
    public Block currentHead() { // The last block of the public chain
        return currentHead;
    }

    @Override
    public void blockMined(Block block, boolean isMinerMe) {
        if (isMinerMe) {
            if (block.getHeight() > currentHead.getHeight()) {
                this.currentHead = block;
            }
        } else {
            if (block == null) {
                return;
            }
            if (currentHead == null) {
                this.currentHead = block;
            } else if (block.getHeight() > currentHead.getHeight() &&
                    totalHashRate > 0 && (localHashRate / totalHashRate) < 0.51) {
                this.currentHead = block;
            }
        }
    }

    @Override
    public void initialize(Block genesis, NetworkStatistics networkStatistics) {
        this.currentHead = genesis;
        this.totalHashRate = networkStatistics.getTotalHashRate();
    }

    @Override
    public void networkUpdate(NetworkStatistics statistics) {
        this.totalHashRate = statistics.getTotalHashRate();
    }
}
