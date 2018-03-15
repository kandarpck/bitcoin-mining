package cs441641.miners;

import cs441641.blockchain.Block;
import cs441641.blockchain.NetworkStatistics;
import cs441641.miners.BaseMiner;
import cs441641.miners.Miner;

public class SelfishMiner extends BaseMiner implements Miner {

    private Block currentHead, localHead;

    public SelfishMiner(String id, int hashRate, int connectivity) {
        super(id, hashRate, connectivity);

    }

    @Override
    public Block currentlyMiningAt() {
        return localHead;
    }

    @Override
    public Block currentHead() {
        return currentHead;
    }

    @Override
    public void blockMined(Block block, boolean isMinerMe) {
        if (isMinerMe) {
            if (localHead == null) // Checks if my local version of Blockchain is initialized
            {
                this.localHead = block; // Block mined by me becomes the part of my local version but I'm not putting it to public chain
            } else if (block.getHeight() > localHead.getHeight()) // if the block.height is greater than my local chain height, I add to my chain.
            {
                this.localHead = block;
            }
        } else {
            if (localHead == null) // if block is not mined by me, I am just adding to my local chain.
            {
                this.localHead = block;
            } else if ((localHead.getHeight() >= block.getHeight()) && (localHead.getHeight() - block.getHeight() <= 1)) {
                this.currentHead = localHead; // I am checking the difference between the height my local chain and the new mined block and I then fork the public chain.
            } else if (localHead.getHeight() < block.getHeight()) // if my local chain is shorter than public one. I just add the block to my chain and also to the public chain.
            {
                this.currentHead = block;
                this.localHead = block;
            }

        }
    }

    @Override
    public void initialize(Block genesis, NetworkStatistics networkStatistics) {
        this.localHead = genesis;
    }

    @Override
    public void networkUpdate(NetworkStatistics statistics) {

    }
}