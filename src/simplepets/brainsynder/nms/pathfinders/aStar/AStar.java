/*
 * By @Adamki11s
 */

package simplepets.brainsynder.nms.pathfinders.aStar;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.Openable;

import java.util.*;

public class AStar {

    private final int sx, sy, sz, ex, ey, ez;
    private final World w;
    private final int range;
    private final String endUID;
    boolean checkOnce = false;
    private PathingResult result;
    private HashMap<String, Tile> open = new HashMap<>();
    private HashMap<String, Tile> closed = new HashMap<>();
    public AStar(Location start, Location end, int range) throws InvalidPathException {

        boolean s = this.isLocationWalkable(start), e = this.isLocationWalkable(end);

        if ((!s) || (!e)) {
            throw new InvalidPathException(s, e);
        }
        PathFindingQueue oQueueItem = new PathFindingQueue();
        oQueueItem.world = start.getWorld();
        oQueueItem.start_X = start.getBlockX();
        oQueueItem.start_Y = start.getBlockY();
        oQueueItem.start_Z = start.getBlockZ();
        oQueueItem.end_X = end.getBlockX();
        oQueueItem.end_Y = end.getBlockY();
        oQueueItem.end_Z = end.getBlockZ();
        oQueueItem.range = range;

        //  1/2 slab checks - V1.19
        //Check if the start location is a 1/2 slab
        if (start.getBlock().getRelative(0, 1, 0).getType() == Material.STONE_SLAB2 || start.getBlock().getRelative(0, 1, 0).getType() == Material.WOOD_STEP || start.getBlock().getRelative(0, 1, 0).getType() == Material.STEP) {
            if (start.getBlock().getRelative(0, 1, 0).getData() < 8 && !start.getBlock().getRelative(0, 2, 0).getType().isSolid() && !start.getBlock().getRelative(0, 3, 0).getType().isSolid()) {
                oQueueItem.start_Y++;
            }
        }

        //Check if the end location is a 1/2 slab
        if (end.getBlock().getRelative(0, 1, 0).getType() == Material.STONE_SLAB2 || end.getBlock().getRelative(0, 1, 0).getType() == Material.WOOD_STEP || end.getBlock().getRelative(0, 1, 0).getType() == Material.STEP) {
            if (end.getBlock().getRelative(0, 1, 0).getData() < 8 && !end.getBlock().getRelative(0, 2, 0).getType().isSolid() && !end.getBlock().getRelative(0, 3, 0).getType().isSolid()) {
                oQueueItem.end_Y++;
            }
        }
        this.w = start.getWorld();
        this.sx = start.getBlockX();
        this.sy = start.getBlockY();
        this.sz = start.getBlockZ();
        this.ex = end.getBlockX();
        this.ey = end.getBlockY();
        this.ez = end.getBlockZ();

        this.range = range;

        short sh = 0;
        Tile t = new Tile(sh, sh, sh, null);
        t.calculateBoth(oQueueItem.start_X, oQueueItem.start_Y, oQueueItem.start_Z, oQueueItem.end_X, oQueueItem.end_Y, oQueueItem.end_Z, true);
        open.put(t.getUID(), t);
        this.processAdjacentTiles(t);

        this.endUID = String.valueOf(ex - sx) + (ey - sy) + (ez - sz);
    }

    private void addToOpenList(Tile t, boolean modify) {
        if (open.containsKey(t.getUID())) {
            if (modify) {
                open.put(t.getUID(), t);
            }
        } else {
            open.put(t.getUID(), t);
        }
    }

    private void addToClosedList(Tile t) {
        if (!closed.containsKey(t.getUID())) {
            closed.put(t.getUID(), t);
        }
    }

    public Location getEndLocation() {
        return new Location(w, ex, ey, ez);
    }

    public PathingResult getPathingResult() {
        return this.result;
    }

    private int abs(int i) {
        return (i < 0 ? -i : i);
    }

    public ArrayList<Tile> iterate() {

        if (!checkOnce) {
            // invert the boolean flag
            checkOnce ^= true;
            if ((abs(sx - ex) > range) || (abs(sy - ey) > range) || (abs(sz - ez) > range)) {
                this.result = PathingResult.NO_PATH;
                return null;//jump out
            }
        }
        // while not at end
        Tile current = null;

        while (canContinue()) {

            // get lowest F cost square on open list
            current = this.getLowestFTile();

            // process tiles
            this.processAdjacentTiles(current);
        }

        if (this.result != PathingResult.SUCCESS) {
            return null;
        } else {
            // path found
            LinkedList<Tile> routeTrace = new LinkedList<>();
            Tile parent;

            routeTrace.add(current);

            while ((parent = current.getParent()) != null) {
                routeTrace.add(parent);
                current = parent;
            }

            Collections.reverse(routeTrace);

            return new ArrayList<>(routeTrace);
        }
    }

    private boolean canContinue() {
        // check if open list is empty, if it is no path has been found
        if (open.size() == 0) {
            this.result = PathingResult.NO_PATH;
            return false;
        } else {
            if (closed.containsKey(this.endUID)) {
                this.result = PathingResult.SUCCESS;
                return false;
            } else {
                return true;
            }
        }
    }

    private Tile getLowestFTile() {
        double f = 0;
        Tile drop = null;

        // get lowest F cost square
        for (Tile t : open.values()) {
            if (f == 0) {
                t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
                f = t.getF();
                drop = t;
            } else {
                t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
                double posF = t.getF();
                if (posF < f) {
                    f = posF;
                    drop = t;
                }
            }
        }

        // drop from open list and add to closed

        this.open.remove(drop.getUID());
        this.addToClosedList(drop);

        return drop;
    }

    private boolean isOnClosedList(Tile t) {
        return closed.containsKey(t.getUID());
    }

    // pass in the current tile as the parent
    private void processAdjacentTiles(Tile current) {

        // set of possible walk to locations adjacent to current tile
        HashSet<Tile> possible = new HashSet<>(26);

        for (byte x = -1; x <= 1; x++) {
            for (byte y = -1; y <= 1; y++) {
                for (byte z = -1; z <= 1; z++) {

                    if (x == 0 && y == 0 && z == 0) {
                        continue;// don't check current square
                    }

                    Tile t = new Tile((short) (current.getX() + x), (short) (current.getY() + y), (short) (current.getZ() + z), current);

                    if (!t.isInRange(this.range)) {
                        // if block is out of bounds continue
                        continue;
                    }

                    if (x != 0 && z != 0 && (y == 0 || y == 1)) {
                        // check to stop jumping through diagonal blocks
                        Tile xOff = new Tile((short) (current.getX() + x), (short) (current.getY() + y), current.getZ(), current), zOff = new Tile(current.getX(),
                                (short) (current.getY() + y), (short) (current.getZ() + z), current);
                        if (!this.isTileWalkable(xOff) && !this.isTileWalkable(zOff)) {
                            continue;
                        }
                    }

                    if (this.isOnClosedList(t)) {
                        // ignore tile
                        continue;
                    }

                    // only process the tile if it can be walked on
                    if (this.isTileWalkable(t)) {
                        t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
                        possible.add(t);
                    }

                }
            }
        }

        for (Tile t : possible) {
            // get the reference of the object in the array
            Tile openRef = null;
            if ((openRef = this.isOnOpenList(t)) == null) {
                // not on open list, so add
                this.addToOpenList(t, false);
            } else {
                // is on open list, check if path to that square is better using
                // G cost
                if (t.getG() < openRef.getG()) {
                    // if current path is better, change parent
                    openRef.setParent(current);
                    // force updates of F, G and H values.
                    openRef.calculateBoth(sx, sy, sz, ex, ey, ez, true);
                }

            }
        }

    }

    private Tile isOnOpenList(Tile t) {
        return (open.containsKey(t.getUID()) ? open.get(t.getUID()) : null);
        /*
		 * for (Tile o : open) { if (o.equals(t)) { return o; } } return null;
		 */
    }

    @SuppressWarnings("deprecation")
    public boolean isTileWalkable(Tile t) {
        Location l = new Location(w, (sx + t.getX()), (sy + t.getY()), (sz + t.getZ()));
        Block b = l.getBlock();
        int i = b.getTypeId();

        // lava, fire, wheat and ladders cannot be walked on, and of course air
        // 85, 107 and 113 stops npcs climbing fences and fence gates
        //if (i != 10 && i != 11 && i != 51 && i != 59 && i != 65 && i != 0 && i != 85 && i != 107 && i != 113 && !canBlockBeWalkedThrough(i))
        if (b.getRelative(0, 1, 0).getType() == Material.STONE_SLAB2 || b.getRelative(0, 1, 0).getType() == Material.WOOD_STEP || b.getRelative(0, 1, 0).getType() == Material.STEP) {
            return !(b.getRelative(0, 1, 0).getData() < 8 && !b.getRelative(0, 2, 0).getType().isSolid() && !b.getRelative(0, 3, 0).getType().isSolid());
        }

        if (i != 10 && i != 11 && i != 51 && i != 59 && i != 65 && i != 0 && i != 85 && i != 107 && i != 113 && b.getType().isSolid()) {
            if (b.getRelative(0, 1, 0).getState().getData() instanceof Openable) {
                org.bukkit.block.BlockState oBlockState = b.getRelative(0, 1, 0).getState();
                switch (b.getRelative(0, 1, 0).getType()) {
                    case BIRCH_FENCE_GATE:
                    case ACACIA_FENCE_GATE:
                    case DARK_OAK_FENCE_GATE:
                    case FENCE_GATE:
                    case SPRUCE_FENCE_GATE:
                        Openable Openable = (Openable) oBlockState.getData();
                        return (Openable.isOpen() && (b.getRelative(0, 2, 0).getTypeId() == 0));

                    case WOODEN_DOOR:
                    case WOOD_DOOR:
                    case ACACIA_DOOR:
                    case BIRCH_DOOR:
                    case DARK_OAK_DOOR:
                    case SPRUCE_DOOR:
                    case JUNGLE_DOOR:
                    case TRAP_DOOR:
                        Openable oOpenable = (Openable) oBlockState.getData();
                        return (oOpenable.isOpen() && (b.getRelative(0, 2, 0).getTypeId() == 0));

                    case IRON_DOOR:
                    case IRON_DOOR_BLOCK:
                    case IRON_TRAPDOOR:
                        Openable openable = (Openable) oBlockState.getData();
                        return (openable.isOpen() && (b.getRelative(0, 2, 0).getTypeId() == 0));

                    default:
                        break;

                }
            }
            if (!b.getRelative(0, 1, 0).isEmpty() & !b.getRelative(0, 2, 0).isEmpty())
                return false;
            //return (canBlockBeWalkedThrough(b.getRelative(0, 1, 0).getTypeId()) && b.getRelative(0, 2, 0).getTypeId() == 0);
            return (!b.getRelative(0, 1, 0).getType().isSolid() && b.getRelative(0, 2, 0).getType() == Material.AIR);
        } else {
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    public boolean isLocationWalkable(Location l) {
        Block b = l.getBlock();

        //if (i != 10 && i != 11 && i != 51 && i != 59 && i != 65 && i != 0 && !canBlockBeWalkedThrough(i)) {
        if (b.getType().isSolid()) {
            // make sure the blocks above are air or can be walked through
            //return (canBlockBeWalkedThrough(b.getRelative(0, 1, 0).getTypeId()) && b.getRelative(0, 2, 0).getTypeId() == 0);

            if (b.getRelative(0, 1, 0).getType() == Material.STONE_SLAB2 || b.getRelative(0, 1, 0).getType() == Material.WOOD_STEP || b.getRelative(0, 1, 0).getType() == Material.STEP) {
                return b.getRelative(0, 1, 0).getData() < 8 && !b.getRelative(0, 2, 0).getType().isSolid() && !b.getRelative(0, 3, 0).getType().isSolid();
            }

            return (!b.getRelative(0, 1, 0).getType().isSolid() && b.getRelative(0, 2, 0).getType() == Material.AIR);
        } else {
            return false;
        }
    }

    @SuppressWarnings("serial")
    public class InvalidPathException extends Exception {

        private final boolean s, e;

        public InvalidPathException(boolean s, boolean e) {
            this.s = s;
            this.e = e;
        }

        public String getErrorReason() {
            StringBuilder sb = new StringBuilder();
            if (!s) {
                sb.append("Start Location was air. ");
            }
            if (!e) {
                sb.append("End Location was air.");
            }
            return sb.toString();
        }

        public boolean isStartNotSolid() {
            return (!s);
        }

        public boolean isEndNotSolid() {
            return (!e);
        }
    }

}
