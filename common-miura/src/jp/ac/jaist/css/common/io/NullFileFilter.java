package jp.ac.jaist.css.common.io;

import java.io.File;

/**
 * �w�肵������̊g���q�����t�@�C���̂݁CJFileChooser�ɕ\�����邽�߂̃N���X
 * @author miuramo
 *
 */
class NullFileFilter extends javax.swing.filechooser.FileFilter implements java.io.FilenameFilter {
  String suffix;
  NullFileFilter(String s){
    suffix = s;
  }
  public boolean accept(File dir){
    String name = dir.getName();
    if (name.endsWith(suffix) || dir.isDirectory()){
      return true;
    } else {
      return false;
    }
  }
  public boolean accept(File dir, String name){
    if (name.endsWith(suffix)){
      return true;
    } else {
      return false;
    }
  }
  public String getDescription(){
    return suffix;
  }
}

