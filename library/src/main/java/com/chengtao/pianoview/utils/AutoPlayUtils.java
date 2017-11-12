package com.chengtao.pianoview.utils;

import android.text.TextUtils;
import android.util.Log;
import com.chengtao.pianoview.entity.AutoPlayEntity;
import com.chengtao.pianoview.entity.Piano;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Author : ChengTao(chengtaolearn@163.com)
 * Date : 11/3/17
 * Time : 10:17 AM
 * Description :
 */

@SuppressWarnings("unchecked") public class AutoPlayUtils {
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
            entity.setType(Piano.PianoKeyType.BLACK);
          } else if (key.getType() == PianoConvertUtils.PianoKey.WHITE_KEY) {
            entity.setType(Piano.PianoKeyType.WHITE);
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
