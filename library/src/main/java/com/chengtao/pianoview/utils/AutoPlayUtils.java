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

/**
 * Author : ChengTao(chengtaolearn@163.com)
 * Date : 11/3/17
 * Time : 10:17 AM
 * Description :
 */

public class AutoPlayUtils {
  public static final Gson gson = new GsonBuilder().create();

  public static ArrayList<AutoPlayEntity> getAutoPlayEntityList(String configJsonString) {
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

  public static ArrayList<AutoPlayEntity> getAutoPlayEntityList(InputStream configJsonStream) {
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
}
