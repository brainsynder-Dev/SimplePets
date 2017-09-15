package simplepets.brainsynder.nms.pathfinders.aStar;

import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class PathFindingQueue {
    public List<Material> AllowedPathBlocks;
    public Boolean LookOneBlockDown;
    public Boolean OpensGates;
    public Boolean OpensWoodDoors;
    public Boolean OpensMetalDoors;

    public int range;
    public int start_X, start_Y, start_Z;
    public int end_X, end_Y, end_Z;
    public World world;

    public PathingResult pathFindingResult;
}