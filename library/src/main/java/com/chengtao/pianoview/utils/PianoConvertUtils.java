package com.chengtao.pianoview.utils;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Author : ChengTao(chengtaolearn@163.com)
 * Date : 11/3/17
 * Time : 11:43 PM
 * Description :
 */

public class PianoConvertUtils {
  private static final int STANDARD_DO_GROUP = 3;
  private static final int STANDARD_DO_POSITION = 0;
  private static final long STANDARD_FREQUENCY = 240;
  private static final String NUMBER_REGEX = "^\\d+$";
  private static final String MUSIC_NUMBER_REGEX = "^[0-7](\\*(0\\.25|0\\.5|2|4|6|8))?$";
  private static final String MUSIC_HML_NUMBER_REGEX = "^[H,M,L][0-7](\\*(0\\.25|0\\.5|2|4|6|8))?$";
  private static final String MUSIC_HO_NUMBER_LO_NUMBER_REGEX =
      "^HO[0-7](\\*(0\\.25|0\\.5|2|4))?$|^LO[0-7](\\*(0\\.25|0\\.5|2|4|6|8))?$";
  private static final String MUSIC_HO_HML_NUMBER_LO_HML_NUMBER_REGEX =
      "^HO[H,M,L][0-7](\\*(0\\.25|0\\.5|2|4))?$|^LO[H,M,L][0-7](\\*(0\\.25|0\\.5|2|4|6|8))?$";
  private static final HashSet<Integer> HIGH_BLACK = new HashSet<>(Arrays.asList(1, 2, 4, 5, 6));
  private static final HashSet<Integer> LOW_BLACK = new HashSet<>(Arrays.asList(7, 4, 5, 3, 2));

  public static final class Error {
    public static final String FILE_NOT_EXIT = "file not exist";
    public static final String READ_FILE_EXCEPTION = "read file exception";
    public static final String CONFIG_FILE_WRONG = "config file wrong";
    public static final String TUNE_LENGTH_NOT_ONE = "tune length is not 1";
    public static final String TUNE_NOT_IN_RANGE = "tune not in range [A-G]";
    public static final String FREQUENCY_NOT_NUMBER = "frequency is not number";
    public static final String FREQUENCY_NOT_IN_RANGE = "frequency not int range [60,4000]";
    public static final String NO_MUSIC_NAME = "no music name";
    public static final String MUSIC_NOTE_CONFIG_WRONG = "music config wrong";
  }

  public static final class PianoKey {
    public static final int BLACK_KEY = 0;
    public static final int WHITE_KEY = 1;
    public static final int NULL_KEY = -1;
    private int group;
    private int position;
    private int type;
    private long frequency;

    public PianoKey() {
      type = NULL_KEY;
    }

    public int getGroup() {
      return group;
    }

    public void setGroup(int group) {
      this.group = group;
    }

    public int getPosition() {
      return position;
    }

    public void setPosition(int position) {
      this.position = position;
    }

    public int getType() {
      return type;
    }

    public void setType(int type) {
      this.type = type;
    }

    public long getFrequency() {
      return frequency;
    }

    public void setFrequency(long frequency) {
      this.frequency = frequency;
    }

    @Override public String toString() {
      return "PianoKey [group="
          + group
          + ", position="
          + position
          + ", type="
          + type
          + ", frequency="
          + frequency
          + "]";
    }
  }

  public static Object[] convertByFilePath(String configFilePath) throws Throwable {
    File file = new File(configFilePath);
    if (file.exists()) {
      try {
        FileInputStream fis = new FileInputStream(file);
        return convertByInputStream(fis);
      } catch (FileNotFoundException e) {
        throw new Exception(Error.FILE_NOT_EXIT);
      }
    } else {
      throw new Exception(Error.FILE_NOT_EXIT);
    }
  }

  public static Object[] convertByInputStream(InputStream is) throws Throwable {
    if (is != null) {
      StringBuilder stringBuilder = new StringBuilder();
      String line;
      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        while ((line = reader.readLine()) != null) {
          stringBuilder.append(line);
        }
        convertByConfigString(stringBuilder.toString());
      } catch (IOException e) {
        throw new Exception(Error.READ_FILE_EXCEPTION);
      }
      return convertByConfigString(stringBuilder.toString());
    } else {
      throw new Exception(Error.READ_FILE_EXCEPTION);
    }
  }

  public static Object[] convertByConfigString(String configString) throws Throwable {
    if (configString != null
        && !configString.equals("")
        && configString.indexOf("{") == 0
        && configString.contains("}")) {
      StringBuilder stringBuilder = new StringBuilder();
      boolean nameStart = false;
      boolean nameEnd = false;
      for (char c : configString.toCharArray()) {
        if (!nameStart || nameEnd) {//读取名称没有开始或者已经结束
          if (!Character.isWhitespace(c)) {//去掉所有空白符
            stringBuilder.append(c);
          }
        } else {
          stringBuilder.append(c);
        }
        if (!nameStart) {
          if (c == ':') {
            int length = stringBuilder.length();
            if (length >= 5) {
              String name = stringBuilder.substring(length - 5, length - 1);
              if (name.equals("name")) {
                nameStart = true;
              }
            }
          }
        }
        if (nameStart && (c == ';' || c == '}')) {
          nameEnd = true;
        }
      }
      return convert(stringBuilder.toString());
    } else {
      throw new Exception(Error.CONFIG_FILE_WRONG);
    }
  }

  private static Object[] convert(String configString) throws Throwable {
    Object[] result = new Object[3];
    int currentDoGroup = STANDARD_DO_GROUP;
    int currentDoPosition = STANDARD_DO_POSITION;
    long currentFrequency = STANDARD_FREQUENCY;
    String name = null;
    StringBuilder baseConfigBuilder = new StringBuilder();
    int musicNoteConfigStartIndex = 0;
    for (char c : configString.toCharArray()) {
      baseConfigBuilder.append(c);
      musicNoteConfigStartIndex++;
      if (c == '}') {
        break;
      }
    }
    // 基本配置
    String baseConfigString = baseConfigBuilder.substring(1, baseConfigBuilder.length() - 1);
    for (String baseConfig : baseConfigString.split(";")) {
      if (!baseConfig.equals("")) {
        if (baseConfig.contains("tune:")) {
          String tune = baseConfig.replace("tune:", "");
          if (tune.length() != 1) {
            throw new Exception(Error.TUNE_LENGTH_NOT_ONE);
          }
          char charTune = tune.toUpperCase().charAt(0);
          if (charTune < 'A' || charTune > 'G') {
            throw new Exception(Error.TUNE_NOT_IN_RANGE);
          }
          if (charTune == 'A') {
            currentDoGroup--;
            currentDoPosition = 5;
          } else if (charTune == 'B') {
            currentDoGroup--;
            currentDoPosition = 6;
          } else {
            currentDoPosition += ((int) charTune - (int) ('C'));
          }
        } else if (baseConfig.contains("frequency:")) {
          String frequency = baseConfig.replace("frequency:", "");
          if (!frequency.matches(NUMBER_REGEX)) {
            throw new Exception(Error.FREQUENCY_NOT_NUMBER);
          }
          currentFrequency = Long.valueOf(frequency);
          if (currentFrequency < 60 || currentFrequency > 4000) {
            throw new Exception(Error.FREQUENCY_NOT_IN_RANGE);
          }
        } else if (baseConfig.contains("name:")) {
          name = baseConfig.replace("name:", "");
        }
      }
    }
    if (TextUtils.isEmpty(name)) {
      throw new Exception(Error.NO_MUSIC_NAME);
    }
    result[0] = name;
    result[1] = configString;
    Log.e("TAG", "convert(PianoConvertUtils.java:" + Thread.currentThread()
        .getStackTrace()[2].getLineNumber() + ")" + "configString:" + configString);
    // 音符配置
    String musicConfigString = configString.substring(musicNoteConfigStartIndex);
    HashSet<Integer> highSet = new HashSet<>();
    HashSet<Integer> lowSet = new HashSet<>();
    List<PianoKey> pianoKeyList = new ArrayList<>();
    for (String musicNotePart : musicConfigString.split("\\|")) {
      for (String musicNote : musicNotePart.split(",")) {
        if (!musicNote.equals("")) {
          if (musicNote.matches(MUSIC_NUMBER_REGEX)) {
            addNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet, lowSet,
                pianoKeyList, musicNote, false, false);
          } else if (musicNote.matches(MUSIC_HML_NUMBER_REGEX)) {
            addHighLowNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet,
                lowSet, pianoKeyList, musicNote, false, false);
          } else if (musicNote.matches(MUSIC_HO_NUMBER_LO_NUMBER_REGEX)) {
            String number = musicNote.substring(2);
            if (musicNote.contains("HO")) {
              addNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet, lowSet,
                  pianoKeyList, number, true, false);
            } else if (musicNote.contains("LO")) {
              addNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet, lowSet,
                  pianoKeyList, number, false, true);
            }
          } else if (musicNote.matches(MUSIC_HO_HML_NUMBER_LO_HML_NUMBER_REGEX)) {
            String remainString = musicNote.substring(2);
            if (musicNote.contains("HO")) {
              addHighLowNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet,
                  lowSet, pianoKeyList, remainString, true, false);
            } else if (musicNote.contains("LO")) {
              addHighLowNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet,
                  lowSet, pianoKeyList, remainString, false, true);
            }
          } else {
            throw new Exception(Error.MUSIC_NOTE_CONFIG_WRONG + ":" + musicNote);
          }
        }
      }
      highSet.clear();
      lowSet.clear();
    }
    result[2] = pianoKeyList;
    return result;
  }

  /**
   * 添加有高音或者低音的钢琴键
   *
   * @param currentDoGroup 当前do所在的组数
   * @param currentDoPosition 当前do所在组数下的位置
   * @param currentFrequency 当前的默认频率
   * @param highSet 高音集合
   * @param lowSet 低音集合
   * @param pianoKeyList 钢琴键列表
   * @param musicNote 当前的数字
   * @param highTune 是否高八度
   * @param lowTune 是否低八度
   */
  private static void addHighLowNumberKey(int currentDoGroup, int currentDoPosition,
      long currentFrequency, HashSet<Integer> highSet, HashSet<Integer> lowSet,
      List<PianoKey> pianoKeyList, String musicNote, boolean highTune, boolean lowTune) {
    char status = musicNote.charAt(0);
    int number = Integer.valueOf(musicNote.charAt(1) + "");
    switch (status) {
      case 'H':
        highSet.add(number);
        lowSet.remove(number);
        break;
      case 'L':
        lowSet.add(number);
        highSet.remove(number);
        break;
      case 'M':
        highSet.remove(number);
        lowSet.remove(number);
        break;
      default:
        break;
    }
    addNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet, lowSet, pianoKeyList,
        musicNote.substring(1), highTune, lowTune);
  }

  /**
   * 添加只有数字(数字 + 频率)的钢琴键
   *
   * @param currentDoGroup 当前do所在的组数
   * @param currentDoPosition 当前do所在组数下的位置
   * @param currentFrequency 当前的默认频率
   * @param highSet 高音集合
   * @param lowSet 低音集合
   * @param pianoKeyList 钢琴键列表
   * @param highTune 是否高八度
   * @param lowTune 是否低八度
   * @param musicNote 当前的数字
   */
  private static void addNumberKey(int currentDoGroup, int currentDoPosition, long currentFrequency,
      HashSet<Integer> highSet, HashSet<Integer> lowSet, List<PianoKey> pianoKeyList,
      String musicNote, boolean highTune, boolean lowTune) {
    if (musicNote.length() == 1) {// 只有数字
      pianoKeyList.add(obtainPianoKey(currentDoGroup, currentDoPosition, currentFrequency,
          Integer.valueOf(musicNote), highSet, lowSet, highTune, lowTune));
    } else {// 添加了频率
      int number = Integer.valueOf(musicNote.charAt(0) + "");
      Float times = Float.valueOf(musicNote.substring(2));
      pianoKeyList.add(
          obtainPianoKey(currentDoGroup, currentDoPosition, (long) (currentFrequency * times),
              number, highSet, lowSet, highTune, lowTune));
    }
  }

  /**
   * 获取钢琴键实体
   *
   * @param currentDoGroup 当前do所在的组数
   * @param currentDoPosition 当前do所在组数的位置
   * @param frequency 该音符的频率
   * @param musicNoteNumber 该音符对应简谱的数组
   * @param highSet 改小节高音集合
   * @param lowSet 该小节的低音集合
   * @param highTune 是否高八度
   * @param lowTune 是否低八度
   * @return 钢琴键实体
   */
  private static PianoKey obtainPianoKey(int currentDoGroup, int currentDoPosition, long frequency,
      int musicNoteNumber, HashSet<Integer> highSet, HashSet<Integer> lowSet, Boolean highTune,
      Boolean lowTune) {
    PianoKey key = new PianoKey();
    if (musicNoteNumber == 0) {
      key.setType(PianoKey.NULL_KEY);
    } else {
      int group = currentDoGroup;
      int position = currentDoPosition + musicNoteNumber - 1;
      if (position > 6) {
        group++;
        position -= 7;
      }
      if (highTune) {
        group++;
      } else if (lowTune) {
        group--;
      }
      if (highSet.contains(musicNoteNumber)) {
        if (!HIGH_BLACK.contains(musicNoteNumber)) {// 还是白建
          position++;
          if (position > 6) {
            group++;
            position -= 7;
          }
          key.setType(PianoKey.WHITE_KEY);
        } else {// 黑键
          if (position > 1) {
            position--;
          }
          key.setType(PianoKey.BLACK_KEY);
        }
      } else if (lowSet.contains(musicNoteNumber)) {
        if (!LOW_BLACK.contains(musicNoteNumber)) {// 还是白建
          position--;
          if (position < 0) {
            group--;
            if (group != 0) {
              position += 7;
            } else {
              position += 2;
            }
          }
          key.setType(PianoKey.WHITE_KEY);
        } else {// 黑键
          if (position <= 2) {
            position--;
          } else {
            position -= 2;
          }
          key.setType(PianoKey.BLACK_KEY);
        }
      } else {
        key.setType(PianoKey.WHITE_KEY);
      }
      key.setGroup(group);
      key.setPosition(position);
    }
    key.setFrequency(frequency);
    return key;
  }
}