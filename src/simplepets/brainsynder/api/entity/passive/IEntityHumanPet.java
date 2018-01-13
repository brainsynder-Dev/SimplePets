package simplepets.brainsynder.api.entity.passive;


import simplepets.brainsynder.api.entity.IEntityPacketPet;

public interface IEntityHumanPet extends IEntityPacketPet {
    void setSkinFlags(byte flags);

    String getSkinName();

    void setSkinName(String name);
}
