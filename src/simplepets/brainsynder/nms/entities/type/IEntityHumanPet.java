package simplepets.brainsynder.nms.entities.type;


import simplepets.brainsynder.nms.entities.type.main.IEntityPacketPet;

public interface IEntityHumanPet extends IEntityPacketPet {
    void setSkinFlags(byte flags);

    String getSkinName();

    void setSkinName(String name);
}
