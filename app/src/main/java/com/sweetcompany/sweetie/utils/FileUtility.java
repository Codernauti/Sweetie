package com.sweetcompany.sweetie.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Eduard on 27-Sep-17.
 */

public class FileUtility {

    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;


    public static File from(Context context, Uri uri) throws IOException {
        // stream source file
        InputStream sourceFileStream = context.getContentResolver().openInputStream(uri);

        // get info of source file
        String fileName = getFileName(context, uri);
        String[] splitName = splitFileName(fileName);

        // create a new file in temporary directory with name (0) + extension (1)
        File tempFile = File.createTempFile(splitName[0], splitName[1]);

        // set settings for temporary file
        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();    // delete whe app is killed

        // create a stream for the new file
        FileOutputStream tempFileStream = null;
        try {
            // bind tempFile with output stream
            tempFileStream = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (sourceFileStream != null) {
            // copy the byte from inputStream (source file) to outputStream (temporary file)
            copy(sourceFileStream, tempFileStream);
            sourceFileStream.close();
        }

        if (tempFileStream != null) {
            tempFileStream.close();
        }

        return tempFile;
    }

    private static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    private static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private static File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old " + newName + " file");
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    private static long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0;
        int n;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

}
