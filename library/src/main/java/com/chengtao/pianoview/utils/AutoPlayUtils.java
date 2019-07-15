package com.chengtao.pianoview.utils;

import android.text.TextUtils;
import android.util.Log;
import com.chengtao.pianoview.entity.AutoPlayEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*
 * 自动播放工具类
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
 */

@SuppressWarnings("unchecked")
public class AutoPlayUtils {
  public static final Gson gson = new GsonBuilder().create();

  public static ArrayList<AutoPlayEntity> getAutoPlayEntityListByJsonString(
      String configJsonString) {
    if (!TextUtils.isEmpty(configJsonString)) {
      try {
        return gson.fromJson(configJsonString, new TypeToken<List<AutoPlayEntity>>() {
        }.getType());
      } catch (Exception e) {
        Log.e("TAG", "AutoPlayUtils-->" + e.getMessage());
      }
    }
    return null;
  }

  public static ArrayList<AutoPlayEntity> getAutoPlayEntityListJsonStream(
      InputStream configJsonStream) {
    if (configJsonStream != null) {
      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(configJsonStream));
        ArrayList<AutoPlayEntity> list =
            gson.fromJson(reader, new TypeToken<ArrayList<AutoPlayEntity>>() {
            }.getType());
        reader.close();
        return list;
      } catch (Exception e) {
        Log.e("TAG", "AutoPlayUtils-->" + e.getMessage());
      }
    }
    return null;
  }

  public static ArrayList<AutoPlayEntity> getAutoPlayEntityListByCustomConfigString(
      String customConfigString) {
    try {
      Object[] result = PianoConvertUtils.convertByConfigString(customConfigString);
      return convertToAutoPlayEntityList((List<PianoConvertUtils.PianoKey>) result[2]);
    } catch (Throwable throwable) {
      Log.e("TAG", throwable.getMessage());
    }
    return null;
  }

  public static ArrayList<AutoPlayEntity> getAutoPlayEntityListByCustomConfigInputStream(
      InputStream customConfigInputStream) {
    try {
      Object[] result = PianoConvertUtils.convertByInputStream(customConfigInputStream);
      return convertToAutoPlayEntityList((List<PianoConvertUtils.PianoKey>) result[2]);
    } catch (Throwable throwable) {
      Log.e("TAG", throwable.getMessage());
    }
    return null;
  }

  public static ArrayList<AutoPlayEntity> convertToAutoPlayEntityList(
      List<PianoConvertUtils.PianoKey> keyList) {
    if (keyList != null && keyList.size() > 0) {
      ArrayList<AutoPlayEntity> list = new ArrayList<>();
      for (PianoConvertUtils.PianoKey key : keyList) {
        if (key != null) {
          AutoPlayEntity entity = new AutoPlayEntity();
          if (key.getType() == PianoConvertUtils.PianoKey.BLACK_KEY) {
            entity.setIsBlackKey(true);
          } else {
            entity.setIsBlackKey(false);
          }
          entity.setCurrentBreakTime(key.getFrequency());
          entity.setGroup(key.getGroup());
          entity.setPosition(key.getPosition());
          list.add(entity);
        }
      }
      return list;
    }
    return null;
  }
}
