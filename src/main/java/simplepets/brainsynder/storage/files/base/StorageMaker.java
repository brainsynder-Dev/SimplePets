package simplepets.brainsynder.storage.files.base;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import simple.brainsynder.nbt.CompressedStreamTools;
import simple.brainsynder.nbt.StorageTagCompound;
import simple.brainsynder.utils.Base64Wrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageMaker extends StorageTagCompound {
    private File file;

    public StorageMaker (File file) {
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {}
        }

        this.file = file;
    }


    public void save () {
        try {
            if (!file.exists()) file.createNewFile();
            CompressedStreamTools.writeCompressed(this, new FileOutputStream(file));
        }catch (Exception ignored){}
    }

    public void setJSONArray (String key, JSONArray array) {
        setString(key, Base64Wrapper.encodeString(array.toJSONString()));
    }

    public void setJSONObject (String key, JSONObject json) {
        setString(key, Base64Wrapper.encodeString(json.toJSONString()));
    }

    public JSONArray getJSONArray (String key) {
        if (hasKey(key)) {
            try {
                return (JSONArray) JSONValue.parseWithException(Base64Wrapper.decodeString(getString(key)));
            } catch (ParseException ignored) {}
        }

        return new JSONArray();
    }

    public JSONObject getJSONObject (String key) {
        if (hasKey(key)) {
            try {
                return (JSONObject) JSONValue.parseWithException(Base64Wrapper.decodeString(getString(key)));
            } catch (ParseException ignored) {}
        }

        return new JSONObject();
    }
}
