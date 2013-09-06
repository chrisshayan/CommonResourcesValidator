package com.maven.plugins;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.EqualPredicate;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author Chris Shayan
 */
public class FileUtils {
    /**
     * This method will collects the file names of a specific folder
     *
     * @param folder Folder to iterate
     * @return List of file names within folder
     */
    @SuppressWarnings("unchecked")
    public static List<String> getFileNames(final File folder) throws MojoExecutionException {
        if (folder.isDirectory()) {
            return Arrays.asList(folder.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return !name.startsWith(".");
                }
            }));
        } else {
            throw new MojoExecutionException(String.format("%s Resource is not a Directory.", folder.getName()));
        }
    }

    /**
     * Will check that does this fileName exists in targetFolder or not.
     *
     * @param fileName     the name that we are looking for
     * @param targetFolder target to search for
     * @return true means there is a file with specific name
     */
    public static boolean isFileNameExist(final String fileName, final File targetFolder) {
        return CollectionUtils.exists(Arrays.asList(targetFolder.list()), new EqualPredicate(fileName));
    }
}
