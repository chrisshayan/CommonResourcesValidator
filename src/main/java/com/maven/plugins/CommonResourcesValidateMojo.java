package com.maven.plugins;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.*;

/**
 * @author Chris Shayan
 */
@Mojo(name = "commonResourcesValidateMojoGoal", defaultPhase = LifecyclePhase.VALIDATE, threadSafe = false)
public class CommonResourcesValidateMojo extends AbstractMojo {

    @Parameter(property = "common.resource.directory", required = true)
    private File commonResourceDirectory;

    @Parameter(property = "environments.directory", required = true)
    private Map environmentsDirectory;

    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException {
        validate();
        final Map<String, List<String>> defectedEnvironments = executeMojoRule();
        errorDecorator(defectedEnvironments);
    }

    /**
     * Decorator
     *
     * @param defectedEnvironments defected Environments
     * @throws MojoExecutionException
     */
    private void errorDecorator(Map<String, List<String>> defectedEnvironments) throws MojoExecutionException {
        if (defectedEnvironments.size() > 0) {
            String errorMessage = "Following environments are duplicating a file in common environment.";
            final Set<String> keys = defectedEnvironments.keySet();
            for (String commonFileName : keys) {
                errorMessage = String.format("%sCommon resource name [%s] is duplicated in: [%s]", errorMessage, commonFileName, defectedEnvironments.get(commonFileName));
            }
            throw new MojoExecutionException(errorMessage);
        }
    }

    /**
     * Executes the mojo rule
     *
     * @return key common resource name / value list of environments which are duplicating the resouce
     * @throws MojoExecutionException
     */
    private Map<String, List<String>> executeMojoRule() throws MojoExecutionException {
        final Map<String, List<String>> defectedEnvironments = new HashMap<String, List<String>>();
        CollectionUtils.forAllDo(FileUtils.getFileNames(commonResourceDirectory), new Closure() {
            @Override
            public void execute(Object input) {
                final String commonFileName = (String) input;
                final Set<String> environmentKeys = environmentsDirectory.keySet();
                for (final String envName : environmentKeys) {
                    if (FileUtils.isFileNameExist(commonFileName, (File) environmentsDirectory.get(envName))) {
                        if (defectedEnvironments.containsKey(commonFileName)) {
                            defectedEnvironments.get(commonFileName).add(envName);
                        } else {
                            List<String> environments = new ArrayList<String>();
                            environments.add(envName);
                            defectedEnvironments.put(commonFileName, environments);
                        }
                    }
                }
            }
        });
        return defectedEnvironments;
    }

    /**
     * This methods run the validation before any action to be taken in this mojo
     */
    @SuppressWarnings("unchecked")
    private void validate() throws MojoExecutionException {
        if (!commonResourceDirectory.exists()) {
            throw new MojoExecutionException("Common Resource Directory should already exist.");
        }

        if (environmentsDirectory.size() == 0) {
            throw new MojoExecutionException("No environment resource directory has been set.");
        }

        final Set<String> environmentKeys = environmentsDirectory.keySet();
        for (final String key : environmentKeys) {
            environmentsDirectory.put(key, new File(environmentsDirectory.get(key).toString()));
            if (!((File)environmentsDirectory.get(key)).exists()) {
                throw new MojoExecutionException(String.format("%s Resource Directory does not exist.", key));
            }
        }
    }

}
