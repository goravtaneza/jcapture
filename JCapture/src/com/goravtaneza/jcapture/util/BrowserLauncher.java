package com.goravtaneza.jcapture.util;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**

 * BrowserLauncher is a class that provides one static method, openURL, which opens the default
 * web browser for the current user of the system to the given URL.  It may support other
 * protocols depending on the system -- mailto, ftp, etc. -- but that has not been rigorously
 * tested and is not guaranteed to work.
 * <p>
 * Yes, this is platform-specific code, and yes, it may rely on classes on certain platforms
 * that are not part of the standard JDK.  What we're trying to do, though, is to take something
 * that's frequently desirable but inherently platform-specific -- opening a default browser --
 * and allow programmers (you, for example) to do so without worrying about dropping into native
 * code or doing anything else similarly evil.
 * <p>
 * Anyway, this code is completely in Java and will run on all JDK 1.1-compliant systems without
 * modification or a need for additional libraries.  All classes that are required on certain
 * platforms to allow this to run are dynamically loaded at runtime via reflection and, if not
 * found, will not cause this to do anything other than returning an error when opening the
 * browser.
 * <p>
 * There are certain system requirements for this class, as it's running through Runtime.exec(),
 * which is Java's way of making a native system call.  Currently, this requires that a Macintosh
 * have a Finder which supports the GURL event, which is true for Mac OS 8.0 and 8.1 systems that
 * have the Internet Scripting AppleScript dictionary installed in the Scripting Additions folder
 * in the Extensions folder (which is installed by default as far as I know under Mac OS 8.0 and
 * 8.1), and for all Mac OS 8.5 and later systems.  On Windows, it only runs under Win32 systems
 * (Windows 95, 98, and NT 4.0, as well as later versions of all).  On other systems, this drops
 * back from the inherently platform-sensitive concept of a default browser and simply attempts
 * to launch Netscape via a shell command.
 * <p>
 * This code is Copyright 1999-2001 by Eric Albert (ejalbert@cs.stanford.edu) and may be
 * redistributed or modified in any form without restrictions as long as the portion of this
 * comment from this paragraph through the end of the comment is not removed.  The author
 * requests that he be notified of any application, applet, or other binary that makes use of
 * this code, but that's more out of curiosity than anything and is not required.  This software
 * includes no warranty.  The author is not repsonsible for any loss of data or functionality
 * or any adverse or unexpected effects of using this software.
 * <p>
 * Credits:
 * <br>Steven Spencer, JavaWorld magazine (<a href="http://www.javaworld.com/javaworld/javatips/jw-javatip66.html">Java Tip 66</a>)
 * <br>Thanks also to Ron B. Yeh, Eric Shapiro, Ben Engber, Paul Teitlebaum, Andrea Cantatore,
 * Larry Barowski, Trevor Bedzek, Frank Miedrich, and Ron Rabakukk
 *
 * @author Eric Albert (<a href="mailto:ejalbert@cs.stanford.edu">ejalbert@cs.stanford.edu</a>)
 * @version 1.4b1 (Released June 20, 2001)
 */
public class BrowserLauncher
{
    /**
     * The Java virtual machine that we are running on.  Actually, in most cases we only care
     *  about the operating system, but some operating systems require us to switch on the VM. */
    private static int jvm;

    /** The browser for the system */
    private static Object browser;

    /**
     * Caches whether any classes, methods, and fields that are not part of the JDK and need to
     *  be dynamically loaded at runtime loaded successfully.
     *  <p>
     *  Note that if this is <code>false</code>, <code>openURL()</code> will always return an
     * IOException.
     */
    private static boolean loadedWithoutErrors;

    /** The linkage object required for JDirect 3 on Mac OS X. */
    private static Object linkage;

    /** JVM constant for MRJ 2.2 or later (Classic Mac OS) */
    private static final int MRJ_2_2 = 1;

    /** JVM constant for MRJ 3.0 (Mac OS X) */
    private static final int MRJ_3_0 = 3;

    /** JVM constant for MRJ 3.1 or later (Mac OS X) */
    private static final int MRJ_3_1 = 4;

    /** JVM constant for any Windows NT JVM */
    private static final int WINDOWS_NT = 5;

    /** JVM constant for any Windows 9x JVM */
    private static final int WINDOWS_9x = 6;

    /** JVM constant for any other platform */
    private static final int OTHER = -1;

    /**
     * The first parameter that needs to be passed into Runtime.exec() to open the default web
     * browser on Windows.
     */
    private static final String FIRST_WINDOWS_PARAMETER = "/c";

    /** The second parameter for Runtime.exec() on Windows. */
    private static final String SECOND_WINDOWS_PARAMETER = "start";

    /**
     * The third parameter for Runtime.exec() on Windows.  This is a "title"
     * parameter that the command line expects.  Setting this parameter allows
     * URLs containing spaces to work.
     */
    private static final String THIRD_WINDOWS_PARAMETER = "\"\"";

    /**
     * The shell parameters for Netscape that opens a given URL in an already-open copy of Netscape
     * on many command-line systems.
     */
    private static final String NETSCAPE_REMOTE_PARAMETER = "-remote";
    private static final String NETSCAPE_OPEN_PARAMETER_START = "'openURL(";
    private static final String NETSCAPE_OPEN_PARAMETER_END_1 = ")'";
    private static final String NETSCAPE_OPEN_PARAMETER_END_2 = ", new-window)"; // MMA - 2001.04.12 - changed the parameters to open
                                                                                 //                    the page in a new netscape window
                                                                                 //                    if netscape is already running.

    /**
     * The message from any exception thrown throughout the initialization process.
     */
    private static String errorMessage;

    /**
     * An initialization block that determines the operating system and loads the necessary
     * runtime data.
     */
    static
    {
        loadedWithoutErrors = true;
        String osName = System.getProperty("os.name");

        if (osName.startsWith("Mac OS"))
        {
            String mrjVersion = System.getProperty("mrj.version");
            String majorMRJVersion = "3.1";
            if(mrjVersion!=null){ 
            	 majorMRJVersion = mrjVersion.substring(0, 3);
            }else{
            	
            }
            try
            {
                double version = Double.valueOf(majorMRJVersion).doubleValue();
                if (version >= 2.2)
                {
                    if (version >= 2.2 && version < 3)
                    {
                        jvm = MRJ_2_2;
                    }
                    else if (version == 3.0)
                    {
                        jvm = MRJ_3_0;
                    }
                    else if (version >= 3.1)
                    {
                        // Assume that all 3.1 and later versions of MRJ work the same.
                        jvm = MRJ_3_1;
                    }
                }
                else
                {
                    loadedWithoutErrors = false;
                    errorMessage = "Unsupported MRJ version: " + version;
                }
            }
            catch (NumberFormatException nfe)
            {
                loadedWithoutErrors = false;
                errorMessage = "Invalid MRJ version: " + mrjVersion;
            }
        }
        else if (osName.startsWith("Windows"))
        {
            if (osName.indexOf("9") != -1 || osName.toLowerCase().indexOf("me") != -1)
            {
                jvm = WINDOWS_9x;
            }
            else
            {
                jvm = WINDOWS_NT;
            }
        }
        else
        {
            jvm = OTHER;
        }

        // if we haven't hit any errors yet
        if (loadedWithoutErrors)
        {
            loadedWithoutErrors = loadClasses();
        }
    }

    /**
     * This class should be never be instantiated; this just ensures so.
     */
    private BrowserLauncher() { }

    /**
     * Called by a static initializer to load any classes, fields, and methods required at runtime
     *  to locate the user's web browser.
     *  @return <code>true</code> if all intialization succeeded
	 *                                             			<code>false</code> if any portion of the initialization failed
     */
    private static boolean loadClasses()
    {
        switch (jvm)
        {
            case MRJ_3_0:
                try
                {
                    @SuppressWarnings("rawtypes")
					Class linker = Class.forName("com.apple.mrj.jdirect.Linker");
                    @SuppressWarnings({ "unchecked", "rawtypes" })
					Constructor constructor = linker.getConstructor(new Class[]{ Class.class });
                    linkage = constructor.newInstance(new Object[] { BrowserLauncher.class });
                }
                catch (ClassNotFoundException cnfe)
                {
                    errorMessage = cnfe.getMessage();
                    return false;
                }
                catch (NoSuchMethodException nsme)
                {
                    errorMessage = nsme.getMessage();
                    return false;
                }
                catch (InvocationTargetException ite)
                {
                    errorMessage = ite.getMessage();
                    return false;
                }
                catch (InstantiationException ie)
                {
                    errorMessage = ie.getMessage();
                    return false;
                }
                catch (IllegalAccessException iae)
                {
                    errorMessage = iae.getMessage();
                    return false;
                }
                catch (Throwable t)
                {
                    errorMessage = t.getMessage();
                    return false;
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * Attempts to locate the default web browser on the local system.  Caches results so it
     *  only locates the browser once for each use of this class per JVM instance.
     *  @return The browser for the system.  Note that this may not be what you would consider
	 *                                             			to be a standard web browser; instead, it's the application that gets called to
	 *                                             			open the default web browser.  In some cases, this will be a non-String object
	 *                                             			that provides the means of calling the default browser.
     */
    private static Object locateBrowser()
    {
        if (browser != null)
        {
            return browser;
        }

        switch (jvm)
        {
            case MRJ_2_2:
                browser = "MRJFileUtils.openURL()"; // Return something non-null
                break;
            case MRJ_3_0:
                browser = "JDirect Invocation"; // Return something non-null
                break;
            case MRJ_3_1:
                browser = "MRJFileUtils.openURL()"; // Return something non-null
                break;
            case WINDOWS_NT:
                browser = "cmd.exe";
                break;
            case WINDOWS_9x:
                browser = "command.com";
                break;
            case OTHER:
            default:
                browser = "netscape";
                break;
        }

        return browser;
    }

    /**
     * Attempts to open the default web browser to the given URL.
     *  @param url The URL to open
     *  @throws IOException If the web browser could not be located or does not run
     */
    public static void openURL(String url) throws IOException
    {
        //
        // if we are on Mac OS X, escape the spaces in the URL with %20's
        //
        if ("Mac OS X".equals(System.getProperty("os.name")))
        {
            url = escapeSpaces(url);
        }

        //System.err.println("\nBrowserLauncher.openURL(" + url + ")\n");

        if (!loadedWithoutErrors)
        {
            throw new IOException("Exception in finding browser: " + errorMessage);
        }

        Object browser = locateBrowser();
        if (browser == null)
        {
            throw new IOException("Unable to locate browser: " + errorMessage);
        }

        openURL(url, browser);
    }

    private static String escapeSpaces(String originalURL)
    {
        String newURL = originalURL;
        java.util.StringTokenizer st = new java.util.StringTokenizer(originalURL);

        if (st.hasMoreTokens())
        {
            newURL = st.nextToken();

            while (st.hasMoreTokens())
            {
                newURL += "%20" + st.nextToken();
            }
        }

        return newURL;
    }

    private static void openURL(String url, Object browser) throws IOException
    {
        switch (jvm)
        {
            case MRJ_3_1:
            case MRJ_2_2:
                try
                {
                    @SuppressWarnings("rawtypes")
					Class mrjUtilsClass = Class.forName("com.apple.mrj.MRJFileUtils");
                    @SuppressWarnings("unchecked")
					Method method = mrjUtilsClass.getMethod("openURL", new Class[] {String.class});
                    if (method != null)
                    {
                        method.invoke(null, new Object[] {url});
                    }
                    //com.apple.mrj.MRJFileUtils.openURL(url);
                }
                catch (Exception e)
                {
                    throw new IOException("Unable to launch default browser using: " + browser);
                }
                break;

            case MRJ_3_0:
                int[] instance = new int[1];
                int result = ICStart(instance, 0);
                if (result == 0)
                {
                    int[] selectionStart = new int[] { 0 };
                    byte[] urlBytes = url.getBytes();
                    int[] selectionEnd = new int[] { urlBytes.length };
                    result = ICLaunchURL(instance[0], new byte[] { 0 }, urlBytes,
                                            urlBytes.length, selectionStart,
                                            selectionEnd);
                    if (result == 0)
                    {
                        // Ignore the return value; the URL was launched successfully
                        // regardless of what happens here.
                        ICStop(instance);
                    }
                    else
                    {
                        throw new IOException("Unable to launch URL: " + result);
                    }
                }
                else
                {
                    throw new IOException("Unable to create an Internet Config instance: " + result);
                }
                break;
            case WINDOWS_NT:
            case WINDOWS_9x:
                //
                // Add quotes around the URL to allow ampersands and other special
                // characters to work.
                //
                if (!url.startsWith("\""))
                {
                    url = '\"' + url + '\"';
                }

                String[] winArgs = null;
                if (jvm == WINDOWS_NT)
                {
                    winArgs = new String[] { (String) browser,
                                                    FIRST_WINDOWS_PARAMETER,
                                                    SECOND_WINDOWS_PARAMETER,
                                                    THIRD_WINDOWS_PARAMETER,
                                                    url};
                }
                else
                {
                    winArgs = new String[] { SECOND_WINDOWS_PARAMETER,
                                                    url};
                    /*winArgs = new String[] { (String) browser,
                                                 FIRST_WINDOWS_PARAMETER,
                                                    SECOND_WINDOWS_PARAMETER,
                                                    url};*/
                }

                Runtime.getRuntime().exec(winArgs);
                break;
            case OTHER:
                //
                // Assume that we're on Unix and that Netscape is installed
                //
                try
                {
                    //
                    // First, attempt to open the URL in a currently running session of Netscape
                    //
                    //Process process = Runtime.getRuntime().exec(new String[] { (String) browser,
                    //                           NETSCAPE_REMOTE_PARAMETER,
                    //                          NETSCAPE_OPEN_PARAMETER_START + url + NETSCAPE_OPEN_PARAMETER_END_2 });
                    Runtime.getRuntime().exec(new String[]{"sh","-c"," netscape " + url + " || mozilla " + url + " || firefox " + url});                    
                    /*
                    int exitCode = process.waitFor();                   
                    if (exitCode != 0)
                    {
                        process = Runtime.getRuntime().exec(new String[] { (String) browser,
                                                            NETSCAPE_REMOTE_PARAMETER,
                                                            NETSCAPE_OPEN_PARAMETER_START + url + NETSCAPE_OPEN_PARAMETER_END_1 });
                        exitCode = process.waitFor();
                        //
                        // if neither of those worked, maybe netscape/mozilla was not already open.
                        //
                        if (exitCode != 0)
                        {
                            process = Runtime.getRuntime().exec(new String[] { (String) browser, url });
                        }
                    }*/
                }             
                catch (IOException ioe)
                {
                    //
                    // if nothing worked and we have not yet tried mozilla.
                    //
                    if (browser != null && !browser.equals("mozilla"))
                    {
                        System.err.println("Failure opening netscape, trying: mozilla");
                        openURL(url, "mozilla");
                    }
                    else
                    {
                        throw ioe;
                    }
                }
                break;
            default:
                //
                // This should never occur, but if it does, we'll try the simplest thing possible
                //
                Runtime.getRuntime().exec(new String[] { (String) browser, url });
                break;
        }
    }

    /**
     * Methods required for Mac OS X.  The presence of native methods does not cause
     * any problems on other platforms.
     */
    private native static int ICStart(int[] instance, int signature);
    private native static int ICStop(int[] instance);
    private native static int ICLaunchURL(int instance, byte[] hint, byte[] data, int len,
                                            int[] selectionStart, int[] selectionEnd);
}