package org.example;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.example.Constants.rules;
class Pose {
    public final int x, y;
    public final Direction dir;

    public Pose(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }
}
public class CalculatePath {

    public static Deque<PathEnvelope> calculatePathEnvelopes(Carrier carrier, int targetX, int targetY, boolean vertDest) {
        Deque<PathEnvelope> q = new ArrayDeque<>();

        Pose pose = new Pose(carrier.x, carrier.y, carrier.direction);

        if (vertDest) {
            // sideways -> rotate(to DOWN) -> vertical
            pose = goSideWays(pose.x, pose.y, targetX - 2, q);
            if (pose.dir != Direction.down) {
                pose = rotateCarrier(pose.x, pose.y, pose.dir, Direction.down, q);
            }
            pose = goVertically(pose.x, pose.y, targetY, q);

        } else {
            // vertical -> rotate(to RIGHT) -> sideways
            pose = goVertically(pose.x, pose.y, targetY - 2, q);
            if (pose.dir != Direction.right) {
                pose = rotateCarrier(pose.x, pose.y, pose.dir, Direction.right, q);
            }
            pose = goSideWays(pose.x, pose.y, targetX, q);
        }

        return q;
    }

    /** ROTATE met offsets zoals Carrier.rotate:
     *  DOWN->RIGHT:  x -= 2; y += 2
     *  RIGHT->DOWN:  x += 2; y -= 2
     */
    public static Pose rotateCarrier(int x, int y, Direction current, Direction target, Deque<PathEnvelope> q) {
        if (!rules.canRotate(x, y, current)) {
            System.out.println("cannot rotate here");
            return new Pose(x, y, current);
        }

        if (current == Direction.down && target == Direction.right) {
            x -= 2; y += 2;              // jouw offset
            q.addLast(new PathEnvelope(x, y, Direction.right));
            return new Pose(x, y, Direction.right);

        } else if (current == Direction.right && target == Direction.down) {
            x += 2; y -= 2;              // jouw offset
            q.addLast(new PathEnvelope(x, y, Direction.down));
            return new Pose(x, y, Direction.down);
        }

        // Als current al gelijk is aan target, gewoon dezelfde pose + logische tick
        q.addLast(new PathEnvelope(x, y, target));
        return new Pose(x, y, target);
    }

    /** SIDEWAYS: elke stap 1 naar links/rechts, direction=RIGHT */
    public static Pose goSideWays(int x, int y, int targetX, Deque<PathEnvelope> q) {
        while (x != targetX) {
            x += (x < targetX) ? 1 : -1;
            q.addLast(new PathEnvelope(x, y, Direction.right));
        }
        return new Pose(x, y, Direction.right);
    }

    /** VERTICAL: elke stap 1 omhoog/omlaag, direction=DOWN (jouw as: y kleiner is omlaag) */
    public static Pose goVertically(int x, int y, int targetY, Deque<PathEnvelope> q) {
        while (y != targetY) {
            y += (y < targetY) ? 1 : -1;  // y++ is omhoog, y-- is omlaag in jouw stelsel
            q.addLast(new PathEnvelope(x, y, Direction.down));
        }
        return new Pose(x, y, Direction.down);
    }
}
