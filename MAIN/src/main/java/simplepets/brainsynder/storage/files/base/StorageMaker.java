package simplepets.brainsynder.storage.files.base;

import lib.brainsynder.files.StorageFile;
import lib.brainsynder.utils.Base64Wrapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.File;

public class StorageMaker extends StorageFile {
    public StorageMaker(File file) {
        super(file);
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
