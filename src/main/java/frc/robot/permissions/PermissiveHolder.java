package frc.robot.permissions;

/**
 * Allows for the permissive control of an object. Useful for dictating what system
 * is controlling what motors, etc.
 */
public class PermissiveHolder<T> {
    private T t;
    private int permID;
    private int defaultID;

    public PermissiveHolder(T t, int defaultID) {
        this.defaultID = defaultID;
        this.t = t;
        permID = defaultID;
    }

    public boolean hasPermission(int permID) {
        return this.permID == permID;
    }

    public void setPermission(int permID) {
        this.permID = permID;
    }

    public void setDefaultPermission() {
        permID = defaultID;
    }

    /**
     * @param permID Permission ID of the acessor
     * @return value if permID matches, null if not
     */
    public T getVal(int permID) {
        return this.permID == permID ? t : null;
    }
}
