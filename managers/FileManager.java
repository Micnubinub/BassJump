package tbs.jumpsnew.managers;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by root on 31/07/14.
 */

public class FileManager {
    //    private static String currentDirectory = Environment.getExternalStorageDirectory().getPath();
//    private static boolean isInFileManagerMode = false;
//    private static ArrayList<File> currentTree;
    private static ArrayList<File> music;
    private static ArrayList<File> directories;


//    public static boolean isIsInFileManagerMode() {
//        return isInFileManagerMode;
//    }
//
//    private static void setIsInFileManagerMode(boolean isInFileManagerMode) {
//        FileManager.isInFileManagerMode = isInFileManagerMode;
//    }
//
//    public static String getCurrentDirectory() {
//        if (!(new File(currentDirectory).isDirectory()) || currentDirectory.length() < 1) {
//            return Environment.getExternalStorageDirectory().toString();
//        }
//        return currentDirectory;
//    }
//
//    private static void setCurrentDirectory(String currentDirectory) {
//        FileManager.currentDirectory = currentDirectory;
//    }
//
//    private static void openFile(Context context, String path) {
//        try {
//            Intent share = new Intent(Intent.ACTION_SEND);
//            share.setType("*/*");
//            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
//            context.startActivity(Intent.createChooser(share, "Share File"));
//        } catch (Exception e) {
//            print("Failed to open " + (new File(path).getName()));
//            Commands.seperator();
//        }
//    }
//
//    private static void openFile(File file) {
//        openFile(MainActivity.context, file.getPath());
//    }
//
//    public static void showTree(File dir) {
//        currentDirectory = dir.getPath();
//
//        int i = 0;
//        try {
//            if (dir.listFiles() != null) {
//                if (!(dir.listFiles().length < 1)) {
//                    if (currentTree != null)
//                        currentTree.clear();
//                    else
//                        currentTree = new ArrayList<File>();
//
//                    Collections.addAll(currentTree, dir.listFiles());
//
//                    sort(currentTree);
//                    print(" File hierarchy " + dir.getAbsolutePath() + " >");
//                    Commands.seperator();
//                    for (File file : currentTree) {
//                        ++i;
//                        if (file.isDirectory())
//                            print("   " + i + ".  /" + file.getName() + "/");
//                        else
//                            print("   " + i + ".  " + file.getName());
//                    }
//                    Commands.seperator();
//                } else {
//                    print("   Specified folder is empty.");
//                    Commands.seperator();
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            print("Failed to show :" + dir.getPath());
//            Commands.seperator();
//        }
//    }
//
//    private static void showTree(String path) {
//        showTree(new File(path));
//    }
//
//    public static void openFolder(String path) {
//        try {
//            openFolder(new File(path));
//        } catch (Exception e) {
//            if (path != null)
//                print("   Failed to open :" + path);
//            else
//                print("   Failed to open folder");
//            Commands.seperator();
//        }
//    }
//
//    private static void openFolder(File file) {
//        setCurrentDirectory(file.getPath());
//        showTree(file);
//    }
//
//    public static void open(int path) {
//        if (currentTree.get(path - 1).isDirectory())
//            openFolder(currentTree.get(path - 1));
//        else
//            openFile(currentTree.get(path - 1));
//    }
//
//    public static void open(String path) {
//        open(new File(path));
//    }
//
//    private static void open(File file) {
//
//        if (file.isDirectory())
//            openFolder(file);
//        else
//            openFile(file);
//    }
//
//    private static void showFileDetails(File file) {
//        try {
//
//            print("   Name : " + file.getName());
//            print("   Size : " + Tools.fileSize(file.length()));
//
//
//            print("   Modified : " + Tools.getDate(file.lastModified()));
//
//            Commands.seperator();
//        } catch (Exception e) {
//            print("   Failed to show details of " + file.getPath());
//            Commands.seperator();
//        }
//    }
//
//    public static void showFileDetails(String path) {
//        showFileDetails(new File(path));
//    }
//
//    public static void showFileDetails(int path) {
//        showFileDetails(currentTree.get(path));
//    }
//
//    public static void createFolder(String path) {
//        createFolder(new File(path));
//    }
//
//    public static void createFolder(File file) {
//        print("creating : " + file.getPath());
//        try {
//            // file.createNewFile();
//            file.mkdirs();
//            showTree(getCurrentDirectory());
//        } catch (Exception e) {
//        }
//    }
//
//    public static void search(String name) {
//
//
//    }
//
//    public static void delete(int file) {
//        delete(currentTree.get(file - 1));
//    }
//
//    public static void delete(String file) {
//        delete(new File(getCurrentDirectory() + "/" + file));
//    }
//
//    private static void delete(File file) {
//
//        file.delete();
//        print(file.getName() + " deleted");
//        Commands.seperator();
//        showTree(currentDirectory);
//    }
//
//    public static void pop(Context context) {
//    }
//
//
//    public static long getFreeSpace(String path) {
//        return (new File(path)).getFreeSpace();
//    }
//
//    public static long getTotalSpace(String path) {
//        return (new File(path)).getTotalSpace();
//    }
//
//    public static long getSizeInBytes(String file) {
//
//        return (new File(file)).length();
//    }
//
//    public static long getSizeInBytes(File file) {
//        return file.length();
//    }
//
//
//    public static void sort(ArrayList<File> list) {
//        Collections.sort(list, new Comparator<File>() {
//            @Override
//            public int compare(File file, File file2) {
//                return file.getName().compareToIgnoreCase(file2.getName());
//            }
//        });
//    }

    public static ArrayList<File> scanForMusic() {
        if (directories != null)
            directories.clear();
        else
            directories = new ArrayList<>();

        if (music != null)
            music.clear();
        else
            music = new ArrayList<>();

        final File file = Environment.getExternalStorageDirectory();
        for (File file1 : file.listFiles()) {
            if (file1.isDirectory())
                directories.add(file1);
        }
        while (directories.size() > 0) {
            getMusicFromDirectory(directories.get(0));
        }
        return music;
    }

    private static void getMusicFromDirectory(File root) {
        if (!root.isDirectory())
            return;

        final File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory())
                directories.add(file);
            else {
                if (getFileExtension(file).contains("mp3") || getFileExtension(file).contains("wav"))
                    music.add(file);
            }
        }
        directories.remove(root);
    }

    private static String getFileExtension(File file) {
        final String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }


//    public static ArrayList<File> getCurrentTree() {
//        return currentTree;
//    }
}
