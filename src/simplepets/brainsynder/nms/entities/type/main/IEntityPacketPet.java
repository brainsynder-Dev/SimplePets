package simplepets.brainsynder.nms.entities.type.main;

/**
 * This will not be in effect until FULLY Tested and will be VERY buggy, So I will not be adding this fully yet.
 */
public interface IEntityPacketPet extends IEntityPet {
    void updatePackets(boolean navigating);

    void updateAI();
}
