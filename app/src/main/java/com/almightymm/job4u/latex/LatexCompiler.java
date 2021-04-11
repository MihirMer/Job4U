package com.almightymm.job4u.latex;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.almightymm.job4u.fragment.ProfileFragment;
import com.almightymm.job4u.utils.ZipUtils;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alberto on 14/02/16.
 */
public class LatexCompiler{

    private Context ctx;
    private LatexNetClient netClient;
    File zip;

    public LatexCompiler(Context ctx) {
        this.ctx = ctx;
        this.netClient = new LatexNetClient(ctx);
    }

    /**
     * Routine that searches for the images used in the current document, zips them with the file
     * and sends it to the server.
     * It will show the response pdf or log.
     */
    public void generatePDF(
            String editorText,
            File imagesFolder,
            File outputFolder,
            Document document,
            FileAsyncHttpResponseHandler handler) {

        // Searches for the images
        final LinkedList<String> imagesFilenames = new LinkedList<>();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
//        String editorText = editor.getTextString();
        Pattern pattern = Pattern.compile("(?<=\\\\includegraphics).*\\{.*\\}", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(editorText);
        while (matcher.find()) {
            if (matcher.group().length() > 0) {
                String group = matcher.group();
                group = group.substring(group.indexOf("{") + 1, group.indexOf("}"));
                if (group.contains(".")) {
                    group = group.substring(0, group.indexOf("."));
                }
                imagesFilenames.add(group);
            }
        }

        FilenameFilter compareWithoutExtension = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                String filenameNoExt = filename;
                if (filename.contains(".")) {
                    filenameNoExt = filename.substring(0, filename.lastIndexOf("."));
                }
                return imagesFilenames.contains(filenameNoExt);
            }
        };

        File[] images = imagesFolder.listFiles(compareWithoutExtension);
        LinkedList<File> files = new LinkedList<>();
        Collections.addAll(files, images);

        files.add(document);
        // Zips the files

        zip = ZipUtils.newZipFile(outputFolder.getPath() + document.getName(), files);
        zip.deleteOnExit();

        RequestParams params = new RequestParams();
        try {
            String compiler = sharedPref.getString(ProfileFragment.EXE, "");
            params.put("zip_file", zip, "application/zip");
            params.put("compiler", compiler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        netClient.post("latex", params, handler);

        Temp temp = new Temp();
        temp.start();
    }


    class Temp extends Thread {
        public void run(){
            try{
                Thread.sleep(5000);
            }catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            zip.delete();
        }
    }
}
