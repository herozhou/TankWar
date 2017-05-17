package TankWar2;
/**
 * 代表方向的枚举类
 * 共九个方向: L-Left LU-LeftUp U-Up RU-RightUp
 *           R-Right RD-RightDown D-Down LD-LeftDown
 *           STOP-Stop
 * 这个类可以用于给Tank或Missile指定方向
 * 
 *
 */
public enum Dir {
	LEFT, UPLEFT, UP, UPRIGHT, RIGHT, DOWNRIGHT, DOWN, DOWNLEFT, STOP
}
