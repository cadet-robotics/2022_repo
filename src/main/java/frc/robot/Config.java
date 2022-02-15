package frc.robot;

import edu.wpi.first.wpilibj.Filesystem;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Manages the Config system, handling button mapping, electronics mapping, etc.
 *
 * @author Matt Robinson
 */
public class Config {
    static Config instance;
    static {
        try {
            instance = new Config(Constants.CONFIG_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Config getInstance() { return instance; }

    private JSONObject controls,
                       motors,
                       pneumatics;

    public JSONObject getControls() { return controls; }
    public JSONObject getMotors() { return motors; }
    public JSONObject getPneumatics() { return pneumatics; }

    /**
     * Constructs configuration object from config file name
     *
     * @param fileName File name in src/main/deploy
     * @throws FileNotFoundException If fileName cannot be found
     */
    public Config(String fileName) throws FileNotFoundException {
        File[] dir = Filesystem.getDeployDirectory().listFiles();

        for (File f : dir) {
            if (f.getName().equals(fileName)) {
                JSONObject json = new JSONObject(new JSONTokener(new FileReader(f)));

                controls = json.getJSONObject("controls");
                motors = json.getJSONObject("motors");
                pneumatics = json.getJSONObject("pneumatics");

                return;
            }
        }

        throw new FileNotFoundException("Failed to find specified config file.");
    }
}
