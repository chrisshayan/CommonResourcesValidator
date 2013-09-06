package com.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

/**
 * @author Chris Shayan
 */
public class CommonResourcesValidateMojoTest extends AbstractMojoTestCase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testError1() throws Exception {
        final File pom = new File(getBasedir(), "src/test/resources/com/maven/plugins/commonResourcesValidateMojoGoal-plugin-config-error1.xml");
        CommonResourcesValidateMojo mojo = (CommonResourcesValidateMojo) lookupMojo("commonResourcesValidateMojoGoal", pom);
        assertNotNull(mojo);

        try {
            mojo.execute();
        } catch (MojoExecutionException e) {
            assertNotNull(e);
            System.out.println(e.getMessage());
        }
    }
}
