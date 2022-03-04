package frc.robot.vision;

import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;

import java.util.ArrayList;

public class PixyHandler {
    private Pixy2 pixy;

    public PixyHandler(Pixy2 pixy) {
        this.pixy = pixy;

        pixy.init();
    }

    // signature 1 - red
    // signature 2 - blue
    public boolean getBallAvailable(int signature) {
        Pixy2CCC.Block bl = getBiggestBlock();
        if (bl == null)
            return false;

        return bl.getSignature() == signature;
    }

    public Pixy2CCC.Block getBiggestBlock() {
        // Gets the number of "blocks", identified targets, that match signature 1 on the Pixy2,
        // does not wait for new data if none is available,
        // and limits the number of returned blocks to 25, for a slight increase in efficiency
        int blockCount = pixy.getCCC().getBlocks(false, Pixy2CCC.CCC_SIG1, 25);
        if (blockCount <= 0) {
            return null; // If blocks were not found, stop processing
        }
        ArrayList<Pixy2CCC.Block> blocks = pixy.getCCC().getBlockCache(); // Gets a list of all blocks found by the Pixy2
        Pixy2CCC.Block largestBlock = null;
        for (Pixy2CCC.Block block : blocks) { // Loops through all blocks and finds the widest one
            if (largestBlock == null) {
                largestBlock = block;
            } else if (block.getWidth() > largestBlock.getWidth()) {
                largestBlock = block;
            }
        }
        return largestBlock;
    }

    public Pixy2 getPixy() {
        return pixy;
    }
}
